package kr.prev.ndnd.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.daimajia.swipe.util.Attributes;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import kr.prev.ndnd.R;
import kr.prev.ndnd.controller.IViewControllerManager;
import kr.prev.ndnd.controller.UserDataViewController;
import kr.prev.ndnd.data.InitialData;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.data.UserData;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.adapter.RecordListAdapter;
import kr.prev.ndnd.controller.SummaryDataViewController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * proj. ndnd
 * MainActivity
 *
 * @author Prev (prevdev@gmail.com)
 */

public class MainActivity extends AppCompatActivity implements IViewControllerManager {

	ProgressDialog loadingProgressDialog;
	InitialData initData;

	SummaryDataViewController summaryDataViewController;
	UserDataViewController userDataViewController;

	RecordListAdapter recordListAdapter;

	Callback<InitialData> loadInitialCallBack = new Callback<InitialData>() {
		@Override
		public void onResponse(Call<InitialData> call, Response<InitialData> response) {
			initData = response.body();

			if (response.body() == null) {
				Toast.makeText(MainActivity.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
				return;
			}

			userDataViewController.setData(initData.user);
			summaryDataViewController.setData(initData.summary);

			recordListAdapter.getList().clear();

			for (RecordData rd : initData.data)
				recordListAdapter.getList().add(rd);

			recordListAdapter.notifyDataSetChanged();



			MainActivity.this.updateAllViewControllers(false);
			loadingProgressDialog.hide();

			if (initData.data.size() == 0)
				Toast.makeText(MainActivity.this, "아직 기록이 없습니다!\n기록 등록하기 버튼을 눌러 추가해보세요", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onFailure(Call<InitialData> call, Throwable t) {
			loadingProgressDialog.hide();

			Toast.makeText(MainActivity.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
			Log.e("callback", "fail");
		}
	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!FacebookSdk.isInitialized())
			FacebookSdk.sdkInitialize(getApplicationContext());

		setContentView(R.layout.activity_main);


		// Drawer layout proccess
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
		drawer.setDrawerListener(toggle);
		toggle.syncState();


		// Instance initializing
		loadingProgressDialog = new ProgressDialog(this);
		loadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingProgressDialog.setMessage("로딩중입니다...");

		userDataViewController = new UserDataViewController(this);
		summaryDataViewController = new SummaryDataViewController(this);

		recordListAdapter = new RecordListAdapter(this);
		recordListAdapter.setViewControllerManager(this);
		recordListAdapter.setMode(Attributes.Mode.Single);

		ListView listView = (ListView) findViewById(R.id.recordListView);
		listView.setAdapter(recordListAdapter);

		// UI Object Control
		findViewById(R.id.addRecordDataBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity( new Intent(MainActivity.this, AddingFormActivity.class) );
			}
		});

		findViewById(R.id.drawerLogoutWrapper).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginManager.getInstance().logOut();
				startActivity( new Intent(MainActivity.this, LoginActivity.class) );
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadRecords();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Load initial data by connecting to server
	 */
	public void loadRecords() {
		if (!loadingProgressDialog.isShowing())
			loadingProgressDialog.show();

		NdAPI.createService()
				.load(NdAPI.getBaseParams())
				.enqueue(loadInitialCallBack);
	}

	@Override
	public void updateAllViewControllers() {
		updateAllViewControllers(true);
	}

	public void updateAllViewControllers(boolean renew) {
		if (renew)
			summaryDataViewController.renewData(initData.data);

		userDataViewController.update();
		summaryDataViewController.update();
	}
}





