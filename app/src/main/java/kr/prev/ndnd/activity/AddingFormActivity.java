package kr.prev.ndnd.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import kr.prev.ndnd.R;
import kr.prev.ndnd.controller.AddingFormViewController;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.util.GPSTracker;


/**
 * proj. ndnd
 * AddForm Activity
 *
 * @author Prev (prevdev@gmail.com)
 */


public class AddingFormActivity extends AppCompatActivity {

	static final int RESULT_FRIEND_SELECTED = 3;

	ProgressDialog loadingProgressDialog;
	GPSTracker gpsTracker;

	AddingFormViewController viewController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adding_form);

		viewController = new AddingFormViewController(this);
		viewController.init();

		loadingProgressDialog = new ProgressDialog(this);
		loadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingProgressDialog.setMessage("로딩중입니다...");

		gpsTracker = new GPSTracker(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();

		double lat, lng;

		if (gpsTracker.canGetLocation()) {
			lat = gpsTracker.getLatitude();
			lng = gpsTracker.getLongitude();

			//Toast.makeText(this, lat + "," + lng, Toast.LENGTH_LONG).show();
			Log.d("location", lat + "," + lng);

			Geocoder geocoder;
			List<Address> addresses;

			geocoder = new Geocoder(this, Locale.getDefault());

			try {
				addresses = geocoder.getFromLocation(lat, lng, 1);
				if (addresses.size() > 0) {
					Log.d("location", addresses.get(0).toString());
					//Toast.makeText(this, addresses.get(0).toString(), Toast.LENGTH_LONG).show();
				}else {
					Log.d("location", "Location data not found");
					//Toast.makeText(this, "Location data not found", Toast.LENGTH_LONG).show();
				}

			} catch (IOException e) {
				Log.d("location", e.toString());
				e.printStackTrace();
			}
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_FRIEND_SELECTED) {
			RecordData model = viewController.getModel();
			model.targetUser.socialUid = data.getStringExtra("social_uid");
			model.targetUser.userName = data.getStringExtra("user_name");

			viewController.update();
		}
	}
}
