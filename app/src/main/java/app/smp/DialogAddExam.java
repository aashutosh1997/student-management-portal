package app.smp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;



public class DialogAddExam
        extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{
    private EditText name;
    private TextView date;
    private EditText location;
    private Button btn1;
    private Button btn2;
    private TextView time;
    private Button date_button;
    private Button time_button;
    private int id;
    private Date d;
    private Time t;
    DatabaseManager databaseManager;
    public DialogAddExam() {
    }

    public static DialogAddExam newInstance(int sid) {
        DialogAddExam fragment = new DialogAddExam();
        Bundle args = new Bundle();
        args.putInt("id",sid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
            this.id = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addexam, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        name = view.findViewById(R.id.add_exam_name);
        location = view.findViewById(R.id.add_exam_location);
        date = view.findViewById(R.id.exam_date);
        time = view.findViewById(R.id.exam_time);
        date_button = view.findViewById(R.id.add_exam_date);
        time_button = view.findViewById(R.id.add_exam_time);
        btn1 = view.findViewById(R.id.add_exam_btn1);
        btn2 = view.findViewById(R.id.add_exam_btn2);
        databaseManager = new DatabaseManager(view.getContext(),"exams");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseManager.addExam(id,
                        name.getText().toString(),
                        d.toString()+" "+t.toString(),
                        location.getText().toString());
                closeDialog("add_exam");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog("add_exam");
            }
        });

        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final boolean is24Hours = DateFormat.is24HourFormat(getContext());
                final TimePickerFragment timePicker = TimePickerFragment.newInstance(
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        is24Hours);
                timePicker.setListener(DialogAddExam.this);
                timePicker.showNow(getChildFragmentManager(), null);
            }
        });

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final DatePickerFragment datePicker = DatePickerFragment.newInstance(
                        c.get(Calendar.DAY_OF_MONTH),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.YEAR));
                datePicker.setListener(DialogAddExam.this);
                datePicker.showNow(getChildFragmentManager(), null);
            }
        });

    }

    public void closeDialog(String tag){
        Fragment prev = getFragmentManager().findFragmentByTag(tag);
        if(prev != null){
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        d = new Date(year-1900,month,dayOfMonth);
        date.setText(date.getText()+d.toString());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        t =  new Time(hourOfDay,minute,0);
        time.setText(time.getText()+t.toString());
    }
}
