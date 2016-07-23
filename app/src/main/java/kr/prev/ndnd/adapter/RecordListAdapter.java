package kr.prev.ndnd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.facebook.login.widget.ProfilePictureView;
import java.util.ArrayList;
import java.util.Map;
import kr.prev.ndnd.R;
import kr.prev.ndnd.controller.IViewControllerManager;
import kr.prev.ndnd.data.CommitResult;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.utils.DateUtil;
import kr.prev.ndnd.utils.FormatUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordListAdapter extends BaseSwipeAdapter {
	private Context context;
	private IViewControllerManager viewControllerManager;
    private ArrayList<RecordData> list;


	public RecordListAdapter(Context context) {
		this.context = context;
		list = new ArrayList<RecordData>();
	}
	public ArrayList<RecordData> getList() { return list; }
	public void setList(ArrayList<RecordData> list) { this.list = list; }

    public void setViewControllerManager(IViewControllerManager viewControllerManager) { this.viewControllerManager = viewControllerManager; }


	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipe;
	}

	@Override
	public View generateView(final int position, ViewGroup parent) {
		View v = LayoutInflater.from(context).inflate(R.layout.list_item, null);
		SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

		swipeLayout.addSwipeListener(new SimpleSwipeListener() {
			@Override
			public void onOpen(SwipeLayout layout) {
				//layout.getCurrentBottomView()
			}
		});

		v.findViewById(R.id.completeButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RecordData data = list.get(position);
				data.state = 1;

                if (viewControllerManager != null)
                    viewControllerManager.updateAllViewControllers();

				Map<String, String> params = NdAPI.getBaseParams();
				params.put("state", Integer.toString(data.state));

				NdAPI.createService()
						.updateData(data.id, params)
						.enqueue(new Callback<CommitResult>() {
							@Override
							public void onResponse(Call<CommitResult> call, Response<CommitResult> response) {
								if (response.body() == null || response.body().success != true)
									Toast.makeText(context, "서버 전송에 오류가 발생했습니다", Toast.LENGTH_LONG).show();
							}

							@Override
							public void onFailure(Call<CommitResult> call, Throwable t) {
								Toast.makeText(context, "서버 전송에 오류가 발생했습니다", Toast.LENGTH_LONG).show();
							}
						});


				SwipeLayout swipeLayout = (SwipeLayout) view.getParent();
				LinearLayout convertedView = (LinearLayout) swipeLayout.findViewById(R.id.listViewContainer);

                fillValues(position, convertedView);
				swipeLayout.close(true);
			}
		});

		return v;
	}

	@Override
	public void fillValues(int position, View convertView) {
		RecordData data = list.get(position);

		ProfilePictureView profilePicture = (ProfilePictureView) convertView.findViewById(R.id.profilePicture);
		profilePicture.setProfileId(data.targetUser.socialUid);
		//profilePicture.setCropped(true);

		ImageView recordTypeImage = (ImageView) convertView.findViewById(R.id.recordTypeImage);

		TextView amountText = (TextView) convertView.findViewById(R.id.amoutText);
		amountText.setText(FormatUtil.getFormattedAmount(data.amount));

		( (TextView) convertView.findViewById(R.id.nameText)).setText(data.targetUser.userName);
		( (TextView) convertView.findViewById(R.id.noteText) ).setText(data.note);
		( (TextView) convertView.findViewById(R.id.additionalText) ).setText( DateUtil.getRelativeDate(data.date) + " " + data.location );


		switch (data.type) {
			case 0 :
				if (data.state == 0)    recordTypeImage.setImageResource(R.drawable.icon_lend);
                else                    recordTypeImage.setImageResource(R.drawable.icon_lend_complete);

				amountText.setTextColor(Color.rgb(0, 102, 204));
				break;

			case 1:
                if (data.state == 0)    recordTypeImage.setImageResource(R.drawable.icon_loan);
                else                    recordTypeImage.setImageResource(R.drawable.icon_loan_complete);

				amountText.setTextColor(Color.rgb(255, 51, 0));
				amountText.setText( "-" + amountText.getText() );
				break;
		}

		if (data.state == 1)
			convertView.setAlpha(0.5f);
		else
			convertView.setAlpha(1);

	}


	@Override
	public int getCount() { return list.size(); }
	@Override
	public Object getItem(int position) { return list.get(position); }
	@Override
	public long getItemId(int position) { return position; }
}