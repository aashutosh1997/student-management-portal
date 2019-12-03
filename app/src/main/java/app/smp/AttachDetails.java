package app.smp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;


class AttachDetails
        extends RecyclerView.Adapter<AttachDetails.AddViewHolder> {
    CardData cd;
    Context parContext;
    public static class AddViewHolder extends
            RecyclerView.ViewHolder{
        TextView textTitle;
        TextView textContent;
        CardView cardView;
        Button examsButton;
        Button editButton;
        Button deleteButton;
        public AddViewHolder(View itemView){
            super(itemView);
            this.textTitle = itemView.findViewById(R.id.detail_card_title);
            this.textContent = itemView.findViewById(R.id.detail_card_content);
            this.cardView = itemView.findViewById((R.id.details_card_view));
            this.examsButton = itemView.findViewById(R.id.button_exams);
            this.editButton = itemView.findViewById(R.id.button_edit);
            this.deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
    public AttachDetails(CardData d,Context c) {
        this.cd = d;
        this.parContext = c;
    }
    @Override
    public AttachDetails.AddViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_details,parent,false);
        return (new AttachDetails.AddViewHolder(view));
    }

    @Override
    public void onBindViewHolder(final AttachDetails.AddViewHolder holder, final int listPosition){
        TextView tv = holder.textTitle;
        TextView cv = holder.textContent;
        Button b;
        switch (listPosition) {
            case 0: tv.setText("Student ID : ");
                cv.setText(""+cd.getId());
                break;
            case 1: tv.setText("First Name : ");
                    cv.setText(cd.getFirstName());
                    break;
            case 2: tv.setText("Last Name : ");
                    cv.setText(cd.getLastName());
                    break;
            case 3: tv.setText("Gender : ");
                    cv.setText(cd.getGender());
                    break;
            case 4: tv.setText("Course : ");
                    cv.setText(cd.getCourse());
                    break;
            case 5: tv.setText("Age : ");
                    cv.setText(""+cd.getAge());
                    break;
            case 6: tv.setText("Address : ");
                    cv.setText(cd.getAddress());
                    break;
            case 7: tv.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);
                    b = holder.examsButton;
                    b.setVisibility(View.VISIBLE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseManager dbexam = new DatabaseManager(v.getContext(),"exams");
                            ArrayList<ExamData> examData = dbexam.retrieveExams(cd.getId());
                            final FragmentManager fm = ((FragmentActivity)parContext).getSupportFragmentManager();
                            ExamDialog dialog = ExamDialog.newInstance(examData,cd);
                            dialog.show(fm,"exam_dialog");
                        }
                    });
                    break;
            case 8: tv.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);
                    b = holder.editButton;
                    b.setVisibility(View.VISIBLE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(parContext,detailsDisplay.class);
                            in.putExtra("new_data",false);
                            in.putExtra("edit_data",true);
                            in.putExtra("details",cd);
                            parContext.startActivity(in);
                        }
                    });
                    break;
            case 9: tv.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);
                    b = holder.deleteButton;
                    b.setVisibility(View.VISIBLE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseManager dm = new DatabaseManager(parContext,"students");
                            dm.deleteRow(cd.getId());
                            Intent in = new Intent(v.getContext(), Students.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            v.getContext().startActivity(in);
                        }
                    });
                    break;
        }
    }
    @Override
    public int getItemCount(){
        return 10;
    }
}
