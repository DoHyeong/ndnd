package kr.prev.ndnd.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.login.widget.ProfilePictureView;
import java.util.ArrayList;
import kr.prev.ndnd.R;
import kr.prev.ndnd.data.RecordData;

public class RecordListAdapter extends BaseAdapter {

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

		ProfilePictureView profilePicture = (ProfilePictureView) convertView.findViewById(R.id.profilePicture);
		profilePicture.setProfileId(data.targetUser.socialUid);
		profilePicture.setCropped(true);

		ImageView recordTypeImage = (ImageView) convertView.findViewById(R.id.recordTypeImage);

		TextView amountText = (TextView) convertView.findViewById(R.id.amoutText);
		amountText.setText(NdUtil.getFormattedAmount(data.amount));

		( (TextView) convertView.findViewById(R.id.nameText)).setText(data.targetUser.userName);
		( (TextView) convertView.findViewById(R.id.noteText) ).setText(data.note);
		( (TextView) convertView.findViewById(R.id.additionalText) ).setText( DateUtil.getRelativeDate(data.date) + " " + data.location );


		switch (data.type) {
			case 0 :
				recordTypeImage.setImageResource(R.drawable.icon_lend);
				amountText.setTextColor(Color.BLUE);
				break;

			case 1:
				recordTypeImage.setImageResource(R.drawable.icon_loan);
				amountText.setTextColor(Color.RED);
				amountText.setText( "-" + amountText.getText() );
				break;
		}


		return convertView;
	}
}