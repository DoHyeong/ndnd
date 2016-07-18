package kr.prev.ndnd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

	CallbackManager fbCallbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());


		setContentView(R.layout.activity_login);

		LoginButton fbLoginBtn = (LoginButton) findViewById(R.id.fb_login_button);
		fbLoginBtn.setReadPermissions("email,user_friends");


		//----------------------------
		// Facebook Callback
		//----------------------------

		fbCallbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				//Toast.makeText(LoginActivity.this, loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();

				openMainActivity( loginResult.getAccessToken().getToken() );
			}

			@Override
			public void onCancel() {
				//Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(FacebookException exception) {
				Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
				Log.e("fb", exception.toString());
			}
		});

		if (AccessToken.getCurrentAccessToken() != null) {
			openMainActivity( AccessToken.getCurrentAccessToken().getToken() );
		}
	}


	private void openMainActivity(String accessToken) {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		intent.putExtra("accessToken", accessToken);
		finish();
		startActivity(intent);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fbCallbackManager.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// NOT USED
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// NOT USED
		return super.onOptionsItemSelected(item);
	}


}
