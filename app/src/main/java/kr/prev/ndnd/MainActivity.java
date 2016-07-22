package kr.prev.ndnd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import kr.prev.ndnd.data.InitialData;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.utils.NdUtil;
import kr.prev.ndnd.utils.RecordListAdapter;
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

    ProgressDialog progressDialog;

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

		//--------------------------------------------------
		// UI Object Control
		//---------------------------------------------------
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

        loadRecords();
	}

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog = ProgressDialog.show(this, "", "로딩 중입니다", true);
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
    private void loadRecords() {
        NdAPI.loadInitialData(new Callback<InitialData>() {

            /**
             * On Response Callback
             */
            @Override
            public void onResponse(Call<InitialData> call, Response<InitialData> response) {
                InitialData initData = response.body();

                if (response.body() == null) {
                    Toast.makeText(MainActivity.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
                    return;
                }

                // Set user data
                ((TextView) findViewById(R.id.drawerMameText)).setText(initData.user.userName);
                ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.drawerProfilePicture);
                profilePicture.setProfileId( initData.user.socialUid );

                // Set summary data
                ((TextView) findViewById(R.id.sumLendText)).setText(NdUtil.getFormattedAmount(initData.summary.lend));
                ((TextView) findViewById(R.id.sumLoanText)).setText(NdUtil.getFormattedAmount(initData.summary.loan));

                // Set Listview by recordData
                RecordListAdapter adapter = new RecordListAdapter();
                ListView listView = (ListView) findViewById(R.id.recordListView);
                listView.setAdapter(adapter);

                for (RecordData rd : initData.data)
                    adapter.getList().add(rd);

                progressDialog.hide();

            }

            /**
             * On Failure Callback
             */
            @Override
            public void onFailure(Call<InitialData> call, Throwable t) {
                progressDialog.hide();

                Toast.makeText(MainActivity.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
                Log.e("callback", "fail");
            }

        });
    }
}





