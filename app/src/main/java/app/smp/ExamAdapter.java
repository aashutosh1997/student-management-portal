package app.smp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class ExamAdapter
        extends RecyclerView.Adapter<ExamAdapter.AddViewHolder> {
    ArrayList<ExamData> data;
    CardData cardData;
    Context parContext;
    FragmentManager f;
    java.sql.Date d;
    Time t;

    public static class AddViewHolder extends
            RecyclerView.ViewHolder{
        TextView name_display;
        TextView date_display;
        TextView loca_display;
        TextView status_display;
        EditText name_edit;
        EditText loca_edit;
        DatePicker btn_date;
        TimePicker btn_time;
        Button btn1;
        Button btn2;
        Button btn_add;
        public AddViewHolder(View itemView) {
            super(itemView);
            this.name_display = itemView.findViewById(R.id.card_ename);
            this.date_display = itemView.findViewById(R.id.card_edate);
            this.loca_display = itemView.findViewById(R.id.card_elocation);
            this.name_edit = itemView.findViewById(R.id.card_ename_edit);
            this.loca_edit = itemView.findViewById(R.id.card_elocation_edit);
            this.status_display=itemView.findViewById(R.id.status_display);
            this.btn_date = itemView.findViewById(R.id.card_edate_button);
            this.btn_time = itemView.findViewById(R.id.card_etime_button);
            this.btn1 = itemView.findViewById(R.id.card_ebtn1);
            this.btn2 = itemView.findViewById(R.id.card_ebtn2);
            this.btn_add = itemView.findViewById(R.id.add_exam);
        }
    }
    public ExamAdapter(ArrayList<ExamData> d, CardData cd,FragmentManager fm,Context c) {
        this.data = d;
        this.cardData = cd;
        this.parContext = c;
        this.f=fm;
    }
    @Override
    public ExamAdapter.AddViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_exam,parent,false);
        return (new ExamAdapter.AddViewHolder(view));
    }
    @Override
    public void onBindViewHolder(final ExamAdapter.AddViewHolder holder, final int listPosition){
        final TextView dname = holder.name_display;
        final TextView ddate = holder.date_display;
        final TextView dloca = holder.loca_display;
        final TextView dstat = holder.status_display;
        final EditText ename = holder.name_edit;
        final DatePicker edate = holder.btn_date;
        final TimePicker etime = holder.btn_time;
        final EditText eloca = holder.loca_edit;
        final Button b1 = holder.btn1;
        final Button b2 = holder.btn2;
        final Button b3 = holder.btn_add;
        final DatabaseManager dbexam = new DatabaseManager(parContext,"exams");

        if (listPosition==data.size()){
            dname.setVisibility(View.GONE);
            ddate.setVisibility(View.GONE);
            dloca.setVisibility(View.GONE);
            dstat.setVisibility(View.GONE);
            b1.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);

            b3.setVisibility(View.VISIBLE);
            //Add Exam Button
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogAddExam diag = DialogAddExam.newInstance(cardData.getId());
                    f.beginTransaction().add(diag,"add_exam").addToBackStack(null).commit();
                    f.executePendingTransactions();
                    diag.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ArrayList<ExamData> examData = dbexam.retrieveExams(cardData.getId());
                            ExamDialog d = ExamDialog.newInstance(examData,cardData);
                            f.beginTransaction().remove(f.findFragmentByTag("exam_dialog")).commit();
                            f.beginTransaction().add(d,"exam_dialog").addToBackStack(null).commit();
                        }
                    });
                }
            });
            return;
        }
        final ExamData ed = data.get(listPosition);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d = new java.sql.Date(sdf.parse(ed.getDate()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date curDate = new java.sql.Date(new java.util.Date().getTime());
        final String[] splt = ed.getDate().split("\\s+");
        dname.setText(ed.getName());
        ddate.setText("Date : " + ed.getDate());
        dloca.setText("Location : " + ed.getLocation());
        if (curDate.after(d))
            dstat.setText(dstat.getText()+"Completed");
        else
            dstat.setText(dstat.getText()+"Coming Up");
        //Edit Button
        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dname.setVisibility(View.GONE);
                ddate.setVisibility(View.GONE);
                dloca.setVisibility(View.GONE);
                dstat.setVisibility(View.GONE);

                ename.setVisibility(View.VISIBLE);
                ename.setText(ed.getName());
                eloca.setVisibility(View.VISIBLE);
                eloca.setText(ed.getLocation());
                b1.setText("Save");
                b2.setText("Cancel");
                edate.setVisibility(View.VISIBLE);
                etime.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    edate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            d = new Date(year-1900,monthOfYear,dayOfMonth);
                        }
                    });
                }

                etime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        t = new Time(hourOfDay,minute,0);
                    }
                });

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExamData ed = data.get(listPosition);
                        String string;
                        if (t==null)
                            string = splt[1];
                        else
                            string = t.toString();
                        dbexam.editExam(ed.getSid(),
                                ed.getName(),
                                ename.getText().toString(),
                                d.toString()+" "+string,
                                eloca.getText().toString());

                        ArrayList<ExamData> examData = dbexam.retrieveExams(ed.getSid());
                        ExamDialog diag = ExamDialog.newInstance(examData,cardData);
                        f.beginTransaction().remove(f.findFragmentByTag("exam_dialog")).commit();
                        f.beginTransaction().add(diag,"exam_dialog").addToBackStack(null).commit();
                    }
                });

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ename.setVisibility(View.GONE);
                        edate.setVisibility(View.GONE);
                        eloca.setVisibility(View.GONE);
                        etime.setVisibility(View.GONE);
                        b1.setText("Edit");
                        b2.setText("Delete");
                        dname.setVisibility(View.VISIBLE);
                        dstat.setVisibility(View.VISIBLE);
                        ddate.setVisibility(View.VISIBLE);
                        dloca.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        //Delete Button
        holder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExamData ed = data.get(listPosition);
                DatabaseManager dbexam = new DatabaseManager(v.getContext(),"exams");
                dbexam.deteleExam(ed.getSid(),ed.getName());
                ArrayList<ExamData> examData = dbexam.retrieveExams(ed.getSid());
                ExamDialog diag = ExamDialog.newInstance(examData,cardData);
                f.beginTransaction().remove(f.findFragmentByTag("exam_dialog")).commit();
                f.beginTransaction().add(diag,"exam_dialog").addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount(){
        return (data.size()+1);
    }
}
