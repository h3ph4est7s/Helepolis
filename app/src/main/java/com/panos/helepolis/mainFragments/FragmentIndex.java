package com.panos.helepolis.mainFragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.panos.helepolis.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by takhs on 12/6/2015.
 */

public class FragmentIndex extends Fragment {


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.main_fragment_layout, null, false);
		ButterKnife.inject(this, view);
		return view;
	}

	@OnClick(R.id.routerPwnButton) public void click(View v){
		//this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
		ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (!mWifi.isConnected()) {
			// Do whatever
			WifiAlertFragment wifiAlertFragment = new WifiAlertFragment();
			// Show Alert DialogFragment
			wifiAlertFragment.show(getActivity().getSupportFragmentManager(), "Alert Dialog Fragment");
		}
		else {
			((MaterialNavigationDrawer) this.getActivity()).setFragmentChild(new FragmentRouter(), getActivity().getString(R.string.router_pwner_title));
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}
}

