package com.panos.helepolis.mainFragments;

import android.content.Context;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panos.helepolis.R;
import com.panos.helepolis.exploits.MainExploitController;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by takhs on 14/6/2015.
 */
public class FragmentRouter extends Fragment {
	@InjectView(R.id.gatewayText)
	TextView gatewayText;
	private Boolean runned = false;
	@InjectView(R.id.providerText)
	TextView providerText;
	private ArrayList<AsyncTask> tasks = new ArrayList<>();
	@InjectView(R.id.ipText)
	TextView ipText;
	MainExploitController controller;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.router_fragment_layout, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	private void addTextChangeListener(TextView object, final String id) {
		object.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
			                              int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				switch (id) {
					case "ip":
						if (new InetAddressValidator().isValidInet4Address(s.toString())) {
							tasks.add(new routerScoutGetProvider().execute(s.toString()));
							addTextChangeListener(providerText, "provider");
						}
						break;
					case "provider":
						tasks.add(new routerScoutGetGateway().execute());
						addTextChangeListener(gatewayText, "gateway");
						break;
					case "gateway":
						tasks.add(new MainExploitController(getActivity(), MainExploitController.RUNLEVEL_CHECK).execute());
						break;
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!runned) {
			tasks.add(new routerScoutGetIP().execute());
			addTextChangeListener(ipText, "ip");
			runned = true;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
		for (AsyncTask task : tasks) {
			if (task.getStatus().equals(AsyncTask.Status.RUNNING)) {
				task.cancel(true);
			}
		}
	}

	protected String doConnect(String url) {
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private class routerScoutGetIP extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... url) {

			return doConnect(getActivity().getString(R.string.get_ip_provider));
		}

		protected void onPostExecute(String result) {
			ipText.setText(result);
			ipText.setTextColor(Color.GREEN);
		}
	}

	private class routerScoutGetProvider extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... ip) {
			String result = "";
			String requestResult = doConnect(getActivity().getString(R.string.get_ip_details_ripe) + ip[0]);
			try {
				JSONObject jObj = new JSONObject(requestResult);
				JSONArray test = jObj.getJSONObject("data").getJSONArray("assignments");
				result = test.getJSONObject(0).getString("asn_name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		protected void onPostExecute(String result) {
			providerText.setText(result);
			providerText.setTextColor(Color.GREEN);
		}
	}

	private class routerScoutGetGateway extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... ip) {
			DhcpInfo dhcpInfo = ((WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE)).getDhcpInfo();
			return intToIp(dhcpInfo.gateway);
		}

		protected void onPostExecute(String result) {
			gatewayText.setText(result);
			gatewayText.setTextColor(Color.GREEN);
		}

		public String intToIp(int i) {

			return (i & 0xFF)+"."+((i >> 8) & 0xFF)+"."+((i >> 16) & 0xFF)+"."+((i>>24) & 0xFF);
		}
	}
}

