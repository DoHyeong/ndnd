package kr.prev.ndnd.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.util.Date;

public class DialogUtil {

	public interface Callback<T> {
		public void onData(T data);
	}

	public static void openDialogWithEditText(Context context, String title, String message, final Callback<String> callback) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setMessage(message);

		final EditText input = new EditText(context);
		dialog.setView(input);

		dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				callback.onData( input.getText().toString() );
			}
		});
		/*dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				callback.onData( null );
			}
		});*/

		dialog.show();
	}

	public static void openDateDialog(FragmentManager fragmentManager, String title, Date date, final Callback<Date> callback) {

		SlideDateTimeListener listener = new SlideDateTimeListener() {
			@Override
			public void onDateTimeSet(Date date) {
				callback.onData(date);
			}
		};

		new SlideDateTimePicker.Builder(fragmentManager)
				.setListener(listener)
				.setInitialDate(date)
				.build()
				.show();
	}
}
