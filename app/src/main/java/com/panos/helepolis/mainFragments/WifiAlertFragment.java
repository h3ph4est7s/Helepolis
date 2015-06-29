package com.panos.helepolis.mainFragments;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.panos.helepolis.R;

/**
 * Created by takhs on 16/6/2015.
 */

public class WifiAlertFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				// Set Dialog Icon
				.setIcon(R.drawable.compatibility)
						// Set Dialog Title
				.setTitle("Wifi problem")
						// Set Dialog Message
				.setMessage("Active wifi connection not detected.")

						// Positive button
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do something else
					}
				}).create();

				/*		// Negative Button
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
						// Do something else
					}
				}).create();*/
	}
}