package kr.prev.ndnd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.prev.ndnd.R;
import kr.prev.ndnd.adapter.FriendListAdapter;
import kr.prev.ndnd.data.UserData;
import kr.prev.ndnd.util.DialogUtil;

public class FriendSelectActivity extends AppCompatActivity {

	FriendListAdapter adapter;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_select);

		adapter = new FriendListAdapter(this);

		Bundle parameters = new Bundle();
		parameters.putString("locale", "ko");
		parameters.putString("fields", "id,name");

		GraphRequest request = GraphRequest.newMyFriendsRequest(
				AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONArrayCallback() {
					@Override
					public void onCompleted(JSONArray jsonArray, GraphResponse response) {
						for (int i = 0; i < jsonArray.length(); i++) {
							try {
								JSONObject obj = jsonArray.getJSONObject(i);
								UserData user = new UserData();
								user.type = "facebook";
								user.userName = (String) obj.get("name");
								user.socialUid = (String) obj.get("id");

								adapter.getList().add(user);

							}catch (JSONException e) {
								e.printStackTrace();
							}
						}
						adapter.notifyDataSetChanged();
					}
				}
		);
		request.setParameters(parameters);
		request.executeAsync();



		listView = (ListView) findViewById(R.id.friendListView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UserData user = adapter.getList().get(position);
				sendDataAndFinish(user);
			}
		});

		Button directInputButton = (Button) findViewById(R.id.directInputButton);
		directInputButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtil.openDialogWithEditText(FriendSelectActivity.this, "이름 입력", "상대방의 이름을 입력 해 주세요", "", new DialogUtil.Callback<String>() {
					@Override
					public void onData(String data) {
						if (data != null) {
							UserData user = new UserData();
							user.userName = data;

							sendDataAndFinish(user);
						}
					}
				});
			}
		});

	}

	private void sendDataAndFinish(UserData user) {
		Intent intent = new Intent();

		if (user.socialUid == null)
			user.socialUid = "";

		intent.putExtra("social_uid", user.socialUid);
		intent.putExtra("user_name", user.userName);

		setResult(AddingFormActivity.RESULT_FRIEND_SELECTED, intent);
		finish();
	}

}
