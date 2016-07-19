package kr.prev.ndnd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.prev.ndnd.data.InitialData;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.utils.NdUtil;
import kr.prev.ndnd.utils.RecordListAdapter;
import kr.prev.ndnd.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * proj. ndnd
 * MainActivity
 *
 * @author Prev (prevdev@gmail.com)
 */

public class MainActivity extends AppCompatActivity {

	String accessToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		// get facebook access token
		//accessToken = getIntent().getStringExtra("accessToken");
		accessToken = SessionManager.getInstance().getAccessToken();

		Log.d("accessToken", accessToken);


		//--------------------------------------------------
		// UI Object Control
		//---------------------------------------------------
		Button registerButton = (Button) findViewById(R.id.addRecordDataBtn);
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, AddFormActivity.class));
			}
		});




		//--------------------------------------------------
		// Load initial data by connecting to server
		//---------------------------------------------------
		NdAPI.loadInitialData(new Callback<InitialData>() {


			/**
			 * On Response Callback
			 * @param call: Call<InitialData>
			 * @param response: Response<InitialData>
			 */
			@Override
			public void onResponse(Call<InitialData> call, Response<InitialData> response) {
				InitialData initData = response.body();

				Log.d("user", initData.user.toString());


				//--------------------------------------
				// Set summary data
				//--------------------------------------
				((TextView) findViewById(R.id.sumLendText)).setText(NdUtil.getFormattedAmount(initData.summary.lend));
				((TextView) findViewById(R.id.sumLoanText)).setText(NdUtil.getFormattedAmount(initData.summary.loan));


				//--------------------------------------
				// Set Listview by recordData
				//--------------------------------------
				RecordListAdapter adapter = new RecordListAdapter();
				ListView listView = (ListView) findViewById(R.id.recordListView);
				listView.setAdapter(adapter);

				for (RecordData rd : initData.data)
					adapter.getList().add(rd);


			}


			/**
			 * On Failure Callback
			 * @param call: Call<InitialData>
			 * @param t: Throwable
			 */
			@Override
			public void onFailure(Call<InitialData> call, Throwable t) {
				Toast.makeText(MainActivity.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
				Log.e("callback", "fail");
			}

		});

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}





