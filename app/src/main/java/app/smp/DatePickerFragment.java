package app.smp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private static final String ARGUMENT_DAY = "ARGUMENT_DAY";
    private static final String ARGUMENT_MONTH = "ARGUMENT_MONTH";
    private static final String ARGUMENT_YEAR = "ARGUMENT_YEAR";
    private DatePickerDialog.OnDateSetListener listener;

    private int day;
    private int month;
    private int year;

    public static DatePickerFragment newInstance(final int d, final int m, final int y) {
        final DatePickerFragment df = new DatePickerFragment();
        final Bundle args = new Bundle();
        args.putInt(ARGUMENT_DAY, d);
        args.putInt(ARGUMENT_MONTH, m);
        args.putInt(ARGUMENT_YEAR, y);
        df.setArguments(args);
        return df;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            day = args.getInt(ARGUMENT_DAY);
            month = args.getInt(ARGUMENT_MONTH);
            year = args.getInt(ARGUMENT_YEAR);
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return new DatePickerDialog(getContext(), this.listener, this.year, this.month,this.day);
    }

    public void setListener(final DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
}