package kr.prev.ndnd;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.prev.ndnd.data.CommitResult;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.utils.DateUtil;
import kr.prev.ndnd.utils.NdUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * proj. ndnd
 * AddForm Activity
 *
 * @author Prev (prevdev@gmail.com)
 */


public class AddingFormActivity extends AppCompatActivity implements Button.OnClickListener, GoogleApiClient.ConnectionCallbacks {

	private int selectedNote = -1;
	private String selectedNoteString;

	EditText fNameText;
	EditText fAmountText;
	ArrayList<Button> fNoteBtns = new ArrayList<Button>();

	TextView fDateText;
	EditText fLocationText;
	Button fRegsiterButton;


	GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adding_form);

		fNameText = (EditText) findViewById(R.id.fNameText);
		fAmountText = (EditText) findViewById(R.id.fAmountText);

		fNoteBtns.add((Button) findViewById(R.id.fNoteLunchBtn));
		fNoteBtns.add((Button) findViewById(R.id.fNoteDinnerBtn));
		fNoteBtns.add((Button) findViewById(R.id.fNoteCoffeeBtn));
		fNoteBtns.add((Button) findViewById(R.id.fNoteCustomBtn));

		fDateText = (TextView) findViewById(R.id.fDateText);
		fLocationText = (EditText) findViewById(R.id.fLocationText);

		fRegsiterButton = (Button) findViewById(R.id.fRegsiterButton);


		fNameText.setText("");
		fDateText.setText(DateUtil.parseAsMDH(new Date()));


		for (int i = 0; i < fNoteBtns.size(); i++)
			fNoteBtns.get(i).setOnClickListener(this);

		fRegsiterButton.setOnClickListener(this);

		GraphRequest request = GraphRequest.newMyFriendsRequest(
				AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONArrayCallback() {
					@Override
					public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
						Log.d("graph_api", jsonArray.toString());
					}
				}
		);

		Bundle parameters = new Bundle();
		parameters.putString("locale", "ko");
		parameters.putString("fields", "id,name,link");
		request.setParameters(parameters);
		request.executeAsync();


		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addApi(LocationServices.API)
				.build();
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
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fRegsiterButton:
				doUpload();
				break;


			default:
				if (fNoteBtns.contains(v)) {
					int idx = fNoteBtns.indexOf(v);

					if (selectedNote != -1)
						fNoteBtns.get(selectedNote).getBackground().clearColorFilter();

					fNoteBtns.get(idx).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
					selectedNoteString = fNoteBtns.get(idx).getText().toString();

					selectedNote = idx;

				}
				break;
		}


	}

	private boolean checkBeforeUpload() {
		if (fNameText.getText().length() == 0) {
			Toast.makeText(this, "이름을 입력 해 주세요", Toast.LENGTH_LONG).show();
			return false;
		}

		if (fAmountText.getText().length() == 0 || fAmountText.getText().toString().equals("0")) {
			Toast.makeText(this, "금액을 입력 해 주세요", Toast.LENGTH_LONG).show();
			return false;
		}

		if (selectedNote == -1) {
			Toast.makeText(this, "노트를 선택 해 주세요", Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}


	private void doUpload() {
		if (checkBeforeUpload()) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("type", "0");
			data.put("target_social_uid", fNameText.getText().toString());
			data.put("amount", fAmountText.getText().toString());
			data.put("note", selectedNoteString);
			data.put("date", fDateText.getText().toString());
			data.put("location", fLocationText.getText().toString());

			NdAPI.insertRecordData(data, new Callback<CommitResult>() {
				@Override
				public void onResponse(Call<CommitResult> call, Response<CommitResult> response) {
					if (response.body().success)
						finish();
					else
						Toast.makeText(AddingFormActivity.this, "오류가 발생했습니다", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onFailure(Call<CommitResult> call, Throwable t) {
					Toast.makeText(AddingFormActivity.this, "서버 전송에 오류가 발생했습니다", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(this, "권한이 필요합니다", Toast.LENGTH_SHORT).show();
			return;
		}
		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			Log.d("loc_lat", "" + mLastLocation.getLatitude() );
			Log.d("loc_lng", "" + mLastLocation.getLongitude() );
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}
}
