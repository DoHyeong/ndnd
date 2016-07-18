package kr.prev.ndnd;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.prev.ndnd.data.InitialData;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.utils.NdUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

	String accessToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		accessToken = getIntent().getStringExtra("accessToken");
		//( (TextView) findViewById(R.id.text1) ).setText(accessToken);

		Log.d("accessToken", accessToken);

		NdAPI.getInstance().load(accessToken, new Callback<InitialData>() {
			@Override
			public void onResponse(Call<InitialData> call, Response<InitialData> response) {
				InitialData tempData = response.body();

				Log.d("user", tempData.user.toString());

				RecordListAdapter adapter = new RecordListAdapter();
				ListView listView = (ListView) findViewById(R.id.recordListView);
				listView.setAdapter(adapter);

				for (RecordData rd : tempData.data) {
					//Log.d("record", rd.toString());

					adapter.getList().add(rd);
					//Log.d("data", "------------------");
					//Log.d("data", rd.id + " / " + rd.amout + " / " + rd.note);
				}
			}

			@Override
			public void onFailure(Call<InitialData> call, Throwable t) {
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





class RecordListAdapter extends BaseAdapter {

	private ArrayList<RecordData> list;

	public RecordListAdapter() {
		list = new ArrayList<RecordData>();
	}

	public ArrayList<RecordData> getList() {
		return list;
	}

	@Override
	public int getCount() { return list.size(); }

	@Override
	public Object getItem(int position) { return list.get(position); }

	@Override
	public long getItemId(int position) { return position; }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, parent, false);
		}

		RecordData data = list.get(position);

		( (TextView) convertView.findViewById(R.id.nameText) ).setText( "" + data.target_user_id );
		( (TextView) convertView.findViewById(R.id.noteText) ).setText( data.note );
		( (TextView) convertView.findViewById(R.id.amoutText) ).setText( NdUtil.getFormattedAmount(data.amount) );
		( (TextView) convertView.findViewById(R.id.additionalText) ).setText( NdUtil.getRelativeDate(data.date) + " " + data.location );

		return convertView;
	}
}