package com.panos.helepolis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainLoginActivity extends Activity {
	private final static String PREFERENCE_FIRST_RUN = "first_run";
	public final static String EXTRA_USERNAME = "com.panos.helepolis.USERNAME";
	public final static String EXTRA_FIRSTRUN = "com.panos.helepolis.FIRSTRUN";
	private boolean firstRun;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		firstRun = p.getBoolean(PREFERENCE_FIRST_RUN, true);
		p.edit().putBoolean(PREFERENCE_FIRST_RUN, false).commit();
		setContentView(R.layout.activity_main_login);
		ImageView img = (ImageView) findViewById( R.id.circleLogo );
		moveViewToScreenTop(img);
	}

	public void clickLogin(View v){
		EditText usernameField = (EditText) findViewById(R.id.username);
		EditText passwordField = (EditText) findViewById(R.id.password);
		String username = usernameField.getText().toString();
		String password = passwordField.getText().toString();
		if(username.isEmpty() || password.isEmpty()){
			Toast.makeText(this,getString(R.string.usr_pwd_check_text),Toast.LENGTH_LONG).show();
			return;
		}
		if(username.equals(getString(R.string.username)) && strToSha512(password).toUpperCase().equals(getString(R.string.password_hash))){
			Intent intent = new Intent(this,MainRootActivity.class);
			intent.putExtra(EXTRA_USERNAME, username);
			intent.putExtra(EXTRA_FIRSTRUN,firstRun);
			startActivity(intent);
		}

	}
	private String strToSha512(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(str.getBytes());
		byte byteData[] = md.digest();
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[byteData.length * 2];
		for ( int j = 0; j < byteData.length; j++ ) {
			int v = byteData[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	private void moveViewToScreenTop( View view )
	{
		TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.RELATIVE_TO_PARENT,0.6f,Animation.ABSOLUTE,0);
		anim.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation anim)
			{
			}
			public void onAnimationRepeat(Animation anim)
			{
			}
			public void onAnimationEnd(Animation anim)
			{
				findViewById(R.id.loginTable).setVisibility(TableLayout.VISIBLE);
			}
		});
		anim.setDuration(1000);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}
}
