package kr.prev.ndnd.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
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
import org.json.JSONArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.prev.ndnd.R;
import kr.prev.ndnd.data.CommitResult;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.util.DateUtil;
import kr.prev.ndnd.util.DialogUtil;
import kr.prev.ndnd.util.GPSTracker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * proj. ndnd
 * AddForm Activity
 *
 * @author Prev (prevdev@gmail.com)
 */


public class AddingFormActivity extends AppCompatActivity implements View.OnClickListener {

	private int selectedType = 0;
	private int selectedNote = -1;
	private String selectedNoteString;
	private Date selectedDate;


	ProgressDialog loadingProgressDialog;

	Button fTypeLendBtn, fTypeLoanBtn;

	EditText fNameText;
	EditText fAmountText;
	ArrayList<Button> fNoteBtns = new ArrayList<Button>();

	TextView fDateText;
	EditText fLocationText;
	Button fRegsiterButton;

	GPSTracker gpsTracker;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adding_form);

		fTypeLendBtn = (Button) findViewById(R.id.fTypeLendBtn);
		fTypeLoanBtn = (Button) findViewById(R.id.fTypeLoanBtn);
		fTypeLendBtn.setOnClickListener(this);
		fTypeLoanBtn.setOnClickListener(this);


		fNameText = (EditText) findViewById(R.id.fNameText);
		fAmountText = (EditText) findViewById(R.id.fAmountText);


		// Buttons of note

		fNoteBtns.add((Button) findViewById(R.id.fNoteLunchBtn));
		fNoteBtns.add((Button) findViewById(R.id.fNoteDinnerBtn));
		fNoteBtns.add((Button) findViewById(R.id.fNoteCoffeeBtn));
		fNoteBtns.add((Button) findViewById(R.id.fNoteCustomBtn));

		for (int i = 0; i < fNoteBtns.size(); i++)
			fNoteBtns.get(i).setOnClickListener(this);


		selectedDate = new Date();
		fDateText = (TextView) findViewById(R.id.fDateText);
		fDateText.setText( DateUtil.parseAsMDH(selectedDate) );
		fDateText.setOnClickListener(this);

		fLocationText = (EditText) findViewById(R.id.fLocationText);

		fRegsiterButton = (Button) findViewById(R.id.fRegsiterButton);
		fRegsiterButton.setOnClickListener(this);


		loadingProgressDialog = new ProgressDialog(this);
		loadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingProgressDialog.setMessage("로딩중입니다...");

		// Get friends by facebook
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
					Toast.makeText(this, addresses.get(0).toString(), Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(this, "Location data not found", Toast.LENGTH_LONG).show();
				}

			} catch (IOException e) {
				Log.d("location", e.toString());
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fTypeLendBtn:
				selectedType = 0;
				toggleTypeButton(fTypeLendBtn, true);
				toggleTypeButton(fTypeLoanBtn, false);
				break;

			case R.id.fTypeLoanBtn:
				selectedType = 1;
				toggleTypeButton(fTypeLendBtn, false);
				toggleTypeButton(fTypeLoanBtn, true);
				break;

			case R.id.fDateText :
				DialogUtil.openDateDialog(getSupportFragmentManager(), "날짜 선택", selectedDate, new DialogUtil.Callback<Date>() {
					@Override
					public void onData(Date date) {
						selectedDate = date;
						fDateText.setText( DateUtil.parseAsMDH(date) );
					}
				});
				break;

			case R.id.fRegsiterButton:
				doUpload();
				break;


			case R.id.fNoteLunchBtn:
			case R.id.fNoteDinnerBtn:
			case R.id.fNoteCoffeeBtn:
			case R.id.fNoteCustomBtn:
				int idx = fNoteBtns.indexOf(v);

				if (selectedNote != -1)
					fNoteBtns.get(selectedNote).getBackground().clearColorFilter();

				fNoteBtns.get(idx).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
				selectedNoteString = fNoteBtns.get(idx).getText().toString();

				selectedNote = idx;

				if (idx == 3) {
					// 직접입력
					DialogUtil.openDialogWithEditText(this, "노트 입력", "간단한 설명을 입력 해 주세요", new DialogUtil.Callback<String>() {
						@Override
						public void onData(String data) {
							selectedNoteString = data;
						}
					});
				}
				break;
		}
	}

	private void toggleTypeButton(Button btn, boolean value) {
		if (value)
			btn.setBackgroundColor(Color.rgb(255, 255, 255));
		else
			btn.setBackgroundColor(Color.rgb(170, 170, 170));

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
			loadingProgressDialog.show();

			NdAPI.createService()
					.insertRecordData(
							selectedType,
							fNameText.getText().toString(),
							fAmountText.getText().toString(),
							selectedNoteString,
							DateUtil.parseAsYMDHIS(selectedDate),
							fLocationText.getText().toString(),
							NdAPI.getBaseParams()
					)
					.enqueue(new Callback<CommitResult>() {
						@Override
						public void onResponse(Call<CommitResult> call, Response<CommitResult> response) {
							loadingProgressDialog.hide();

							if (response.body().success)
								finish();
							else
								Toast.makeText(AddingFormActivity.this, "오류가 발생했습니다", Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Call<CommitResult> call, Throwable t) {
							loadingProgressDialog.hide();
							Toast.makeText(AddingFormActivity.this, "서버 전송에 오류가 발생했습니다", Toast.LENGTH_LONG).show();
						}
					});
		}
	}
}
