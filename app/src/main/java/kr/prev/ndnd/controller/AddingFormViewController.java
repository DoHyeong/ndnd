package kr.prev.ndnd.controller;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import kr.prev.ndnd.R;
import kr.prev.ndnd.activity.FriendSelectActivity;
import kr.prev.ndnd.data.CommitResult;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.data.UserData;
import kr.prev.ndnd.net.NdAPI;
import kr.prev.ndnd.util.DateUtil;
import kr.prev.ndnd.util.DialogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddingFormViewController implements IViewController {

	Activity activity;
	RecordData model;

	Button typeBtn1, typeBtn2, registerBtn;
	TextView nameText, dateText;
	EditText amountText, locationText;
	ArrayList<Button> noteBtns = new ArrayList<Button>();

	int selectedNote = -1;
	Date selectedDate = new Date();

	ProgressDialog loadingProgressDialog;

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.fTypeLendBtn:
				case R.id.fTypeLoanBtn:
					model.type = (v == typeBtn1) ? 0 : 1;
					toggleTypeButton(typeBtn1, model.type == 0);
					toggleTypeButton(typeBtn2, model.type == 1);
					break;

				case R.id.fNameText:
					activity.startActivityForResult( new Intent(activity, FriendSelectActivity.class), 0 );
					break;

				case R.id.fDateText:
					DialogUtil.openDateDialog(((FragmentActivity) activity).getSupportFragmentManager(), "날짜 선택", selectedDate, new DialogUtil.Callback<Date>() {
						@Override
						public void onData(Date date) {
							selectedDate = date;
							dateText.setText( DateUtil.parseAsMDH(date) );
						}
					});
					break;

				case R.id.fRegsiterButton:
					upload();
					break;

				case R.id.fNoteLunchBtn:
				case R.id.fNoteDinnerBtn:
				case R.id.fNoteCoffeeBtn:
					int idx = noteBtns.indexOf(v);

					for (Button b : noteBtns) b.getBackground().clearColorFilter();

					noteBtns.get(idx).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
					model.note = noteBtns.get(idx).getText().toString();

					selectedNote = idx;
					break;

				case R.id.fNoteCustomBtn:
					String inputValue = (selectedNote == 3) ? model.note : "";

					DialogUtil.openDialogWithEditText(activity, "노트 입력", "간단한 설명을 입력 해 주세요", inputValue, new DialogUtil.Callback<String>() {
						@Override
						public void onData(String data) {
							if (data != null) {
								for (Button b : noteBtns) b.getBackground().clearColorFilter();
								noteBtns.get(3).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
								model.note = data;

								selectedNote = 3;
							}
						}
					});

					break;
			}
		}
	};



	public AddingFormViewController(Activity activity) {
		this.activity = activity;
		this.model = new RecordData();
		this.model.targetUser = new UserData();
	}

	public void init() {
		bindViews();
		bindEvents();
		initViews();
		initModel();
	}

	@Override
	public void update() {
		if (model.targetUser.userName != null) {
			nameText.setText(model.targetUser.userName);

			if (model.targetUser.socialUid.length() != 0)
				nameText.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
			else
				nameText.getBackground().clearColorFilter();
		}
	}



	protected void bindViews() {
		typeBtn1 = (Button) activity.findViewById(R.id.fTypeLendBtn);
		typeBtn2 = (Button) activity.findViewById(R.id.fTypeLoanBtn);

		nameText = (TextView) activity.findViewById(R.id.fNameText);
		amountText = (EditText) activity.findViewById(R.id.fAmountText);

		noteBtns = new ArrayList<Button>() {{
				add( (Button) activity.findViewById(R.id.fNoteLunchBtn) );
				add( (Button) activity.findViewById(R.id.fNoteDinnerBtn) );
				add ((Button) activity.findViewById(R.id.fNoteCoffeeBtn) );
				add( (Button) activity.findViewById(R.id.fNoteCustomBtn) );
		}};

		dateText = (TextView) activity.findViewById(R.id.fDateText);
		locationText = (EditText) activity.findViewById(R.id.fLocationText);

		registerBtn = (Button) activity.findViewById(R.id.fRegsiterButton);
	}

	protected void bindEvents() {
		typeBtn1.setOnClickListener(clickListener);
		typeBtn2.setOnClickListener(clickListener);

		nameText.setOnClickListener(clickListener);

		for (Button b : noteBtns)
			b.setOnClickListener(clickListener);

		dateText.setOnClickListener(clickListener);
		registerBtn.setOnClickListener(clickListener);
	}

	protected void initViews() {
		selectedDate = new Date();
		dateText.setText( DateUtil.parseAsMDH(selectedDate) );
	}

	private void toggleTypeButton(Button btn, boolean value) {
		if (value) btn.setBackgroundColor(Color.rgb(255, 255, 255));
		else 	btn.setBackgroundColor(Color.rgb(170, 170, 170));
	}



	public RecordData getModel() {
		return model;
	}

	public void initModel() {
		model.type = 0;
		model.state = 0;
	}

	public void updateModel() {
		//model.targetUser.userName = nameText.getText().toString();
		if (amountText.getText().length() == 0)	 model.amount = 0;
		else										 model.amount = Integer.parseInt( amountText.getText().toString() );

		model.date = DateUtil.parseAsYMDHIS(selectedDate);
		model.location = locationText.getText().toString();
	}

	public String validateModel() {
		if (model.targetUser.userName == null || model.targetUser.userName.length() == 0)
			return "이름을 입력 해 주세요";

		if (model.amount == 0)
			return "올바른 금액을 입력 해 주세요";

		if (model.note == null)
			return "노트를 선택 해 주세요";

		return null;
	}


	public void upload() {
		updateModel();
		String result = validateModel();

		if (result != null) {
			Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
			return;
		}

		if (loadingProgressDialog != null)
			loadingProgressDialog.show();


		NdAPI.createService()
				.insertRecordData(
						model.type,
						model.targetUser.socialUid,
						model.targetUser.userName,
						model.amount,
						model.note,
						model.date,
						model.location,
						NdAPI.getBaseParams()
				)
				.enqueue(new Callback<CommitResult>() {
					@Override
					public void onResponse(Call<CommitResult> call, Response<CommitResult> response) {
						if (loadingProgressDialog != null)  loadingProgressDialog.hide();

						if (response.body().success)
							activity.finish();
						else
							Toast.makeText(activity, "오류가 발생했습니다", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(Call<CommitResult> call, Throwable t) {
						if (loadingProgressDialog != null)  loadingProgressDialog.hide();

						Toast.makeText(activity, "서버 전송에 오류가 발생했습니다", Toast.LENGTH_LONG).show();
					}
				});
	}

	public void setLoadingProgressDialog(ProgressDialog dialog) {
		this.loadingProgressDialog = dialog;
	}
}
