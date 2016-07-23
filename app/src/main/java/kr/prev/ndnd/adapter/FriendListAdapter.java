package kr.prev.ndnd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.prev.ndnd.R;
import kr.prev.ndnd.data.UserData;
import kr.prev.ndnd.util.CircularImageUtil;


public class FriendListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<UserData> list = new ArrayList<UserData>();

	public FriendListAdapter(Context context) {
		this.context = context;
	}

	public ArrayList<UserData> getList() { return list; }

	@Override
	public int getCount() { return list.size(); }

	@Override
	public Object getItem(int position) { return list.get(position); }

	@Override
	public long getItemId(int position) { return position; }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.friend_list_item, parent, false);
		}

		UserData data = list.get(position);

		TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
		CircleImageView profilePicture = (CircleImageView) convertView.findViewById(R.id.profilePicture);

		CircularImageUtil.setImage(profilePicture, "https://graph.facebook.com/" + data.socialUid + "/picture?width=200&height=200");

		nameText.setText( data.userName );

		return convertView;
	}
}
