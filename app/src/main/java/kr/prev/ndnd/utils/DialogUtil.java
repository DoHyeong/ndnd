package kr.prev.ndnd.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Calendar;
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

    public static void openDateDialog(Context context, String title, Date date, final Callback<Date> callback) {
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Date selectedDate = new Date(year-1900, monthOfYear, dayOfMonth);
                //callback.onData( new Date(year-1900, monthOfYear, dayOfMonth) );

            }
        }, date.getYear() + 1900, date.getMonth(), date.getDate());

        dialog.setTitle(title);
        dialog.show();


        /*AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
        final NumberPicker np = new NumberPicker(context);
        np.setMinValue(1);
        np.setMaxValue(23);
        np.setValue(date.getHours());

        dialog2.setTitle(title);
        dialog2.setView(np);

        dialog2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("hour", ""+np.getValue());
            }
        });

        dialog2.show();*/
    }
}
