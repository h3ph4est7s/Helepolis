package com.panos.helepolis.mainFragments;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panos.helepolis.R;


public class FragmentSettings extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_fragment_settings, container, false);
		return view;
	}



	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
