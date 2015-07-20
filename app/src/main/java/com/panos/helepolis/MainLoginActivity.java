package com.panos.helepolis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.LowercaseCharacterRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class MainLoginActivity extends Activity {
	protected static boolean toastProtection = false;
	private final static String PREFERENCE_FIRST_RUN = "first_run";
	private final static String PREFERENCE_USERNAME = "username";
	private final static String PREFERENCE_PASSWORD_HASH =  "password_hash";
	public final static String EXTRA_USERNAME = "com.panos.helepolis.USERNAME";
	public final static String EXTRA_FIRSTRUN = "com.panos.helepolis.FIRSTRUN";
	protected boolean firstRun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		firstRun = p.getBoolean(PREFERENCE_FIRST_RUN, true);
		if(firstRun){
			setContentView(R.layout.activity_add_user);
			ImageView img = (ImageView) findViewById(R.id.circleLogoAdd);
			moveViewToScreenTop(img);
			enterListener(((EditText) findViewById(R.id.passwordRepeat)), 2);
		}
		else {
			setContentView(R.layout.activity_main_login);
			ImageView img = (ImageView) findViewById(R.id.circleLogo);
			moveViewToScreenTop(img);
			enterListener(((EditText) findViewById(R.id.password)),1);
		}
	}
	private void enterListener(EditText view,final int state){
		view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL) {
					if (state == 1) {
						clickLogin(null);
					} else if (state == 2) {
						clickAddUser(null);
					}

					return true;
				}
				return false;
			}
		});
	}
	public void clickAddUser(View v){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String username = ((EditText) findViewById(R.id.usernameAdd)).getText().toString();
		String password = ((EditText) findViewById(R.id.passwordAdd)).getText().toString();
		String passwordRepeat = ((EditText) findViewById(R.id.passwordRepeat)).getText().toString();
		if(username.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()){
			Toast.makeText(this, getString(R.string.usr_pwd_rep_check_text),Toast.LENGTH_LONG).show();
			return;
		}
		if(password.equals(passwordRepeat)) {
			if (passwordComplexityCheck(password)) {
				prefs.edit().putString(PREFERENCE_USERNAME, username).apply();
				prefs.edit().putString(PREFERENCE_PASSWORD_HASH, strToSha512(password).toUpperCase()).apply();
				prefs.edit().putBoolean(PREFERENCE_FIRST_RUN, false).apply();
				Intent intent = new Intent(this, MainRootActivity.class);
				intent.putExtra(EXTRA_USERNAME, username);
				intent.putExtra(EXTRA_FIRSTRUN, firstRun);
				startActivity(intent);
			}
		}
		else {
			Toast.makeText(this, "Password does not match in both fields.",Toast.LENGTH_LONG).show();
		}
	}
	public void clickLogin(View v) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		EditText usernameField = (EditText) findViewById(R.id.username);
		EditText passwordField = (EditText) findViewById(R.id.password);
		String username = usernameField.getText().toString();
		String password = passwordField.getText().toString();
		if (username.isEmpty() || password.isEmpty()) {
			Toast.makeText(this, getString(R.string.usr_pwd_check_text), Toast.LENGTH_LONG).show();
			return;
		}
		if (username.equals(prefs.getString(PREFERENCE_USERNAME,"")) && strToSha512(password).toUpperCase().equals(prefs.getString(PREFERENCE_PASSWORD_HASH,""))) {
			Intent intent = new Intent(this, MainRootActivity.class);
			intent.putExtra(EXTRA_USERNAME, username);
			intent.putExtra(EXTRA_FIRSTRUN, firstRun);
			startActivity(intent);
		}
		else {
			if(!MainLoginActivity.toastProtection) {
				Toast.makeText(this, "Wrong credentials please try again.", Toast.LENGTH_LONG).show();
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							MainLoginActivity.toastProtection = true;
							Thread.sleep(TimeUnit.SECONDS.toMillis(Toast.LENGTH_LONG)+100);
							MainLoginActivity.toastProtection = false;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
			}
		}

	}

	private String strToSha512(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		assert md != null;
		md.update(str.getBytes());
		byte byteData[] = md.digest();
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[byteData.length * 2];
		for (int j = 0; j < byteData.length; j++) {
			int v = byteData[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private void moveViewToScreenTop(View view) {
		TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_PARENT, 0.6f, Animation.ABSOLUTE, 0);
		anim.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation anim) {
			}

			public void onAnimationRepeat(Animation anim) {
			}

			public void onAnimationEnd(Animation anim) {
				if(firstRun){
					findViewById(R.id.addUserTable).setVisibility(TableLayout.VISIBLE);
				}
				else{
					findViewById(R.id.loginTable).setVisibility(TableLayout.VISIBLE);
				}
			}
		});
		anim.setDuration(1000);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}
	private boolean passwordComplexityCheck(String password){

		PasswordValidator validator = new PasswordValidator(Arrays.asList(
				new LengthRule(8,20),
				new UppercaseCharacterRule(1),
				new LowercaseCharacterRule(1),
				new DigitCharacterRule(1),
				new SpecialCharacterRule(2),
				new WhitespaceRule()
		));
		RuleResult result = validator.validate(new PasswordData(password));
		if(result.isValid()){
			return true;
		}
		else{
			for (String msg : validator.getMessages(result)) {
				Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	}
}
