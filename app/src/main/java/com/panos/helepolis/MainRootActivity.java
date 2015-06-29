package com.panos.helepolis;

import android.content.Intent;
import android.os.Bundle;
import com.panos.helepolis.mainFragments.FragmentIndex;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainRootActivity extends MaterialNavigationDrawer {

	@Override
	public void init(Bundle savedInstanceState) {
		FragmentIndex home = new FragmentIndex();
		MaterialSection homeSection = newSection(getString(R.string.home_section_text), R.drawable.ic_menu_home, home);
		MaterialSection settingsSection = newSection(getString(R.string.settings_section_text), R.drawable.ic_gear, home);
		Intent intent = getIntent();
		String username = intent.getStringExtra(MainLoginActivity.EXTRA_USERNAME);
		Boolean firstRun = intent.getBooleanExtra(MainLoginActivity.EXTRA_FIRSTRUN, true);
		if(!firstRun){
			this.disableLearningPattern();
		}
		setDrawerHeaderImage(R.drawable.mat2);
		setUsername(username);
		//setUserEmail("My version build");
		this.addSection(homeSection);
		//this.addSection(newSection("Section 2",new FragmentIndex()));
		//this.addSection(newSection("Section 3",R.drawable.ic_mic_white_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
		//this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));
		// create bottom section
		this.addBottomSection(settingsSection);
	}
	@Override
	protected void onStart() {
		super.onStart();

		// set the indicator for child fragments
		// N.B. call this method AFTER the init() to leave the time to instantiate the ActionBarDrawerToggle
		this.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
	}


}