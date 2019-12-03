package app.smp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import static android.view.View.GONE;

public class TodoDialog extends DialogFragment {
    private EditText title;
    private EditText location;
    private Switch status;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    DatabaseManager databaseManager;
    int tid;
    String tit;
    String loc;
    String sta;
    boolean new_flag;
    boolean edit_flag;
    public TodoDialog() {
    }

    public static TodoDialog newInstance(int i, String t, String l,String s,Boolean edit_flag) {
        TodoDialog fragment = new TodoDialog();
        Bundle args = new Bundle();
        args.putInt("id",i);
        args.putString("title",t);
        args.putString("location",l);
        args.putString("status",s);
        args.putBoolean("new_flag",false);
        args.putBoolean("edit_flag",edit_flag);
        fragment.setArguments(args);
        return fragment;
    }
    public static TodoDialog newInstance(){
        TodoDialog fragment = new TodoDialog();
        Bundle args = new Bundle();
        args.putString("title","");
        args.putString("location","");
        args.putString("status","");
        args.putBoolean("new_flag",true);
        args.putBoolean("edit_flag",false);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tid = getArguments().getInt("id",-1);
            tit = getArguments().getString("title");
            loc = getArguments().getString("location");
            sta = getArguments().getString("status");
            new_flag = getArguments().getBoolean("new_flag");
            edit_flag = getArguments().getBoolean("edit_flag");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        title = view.findViewById(R.id.todo_title);
        location = view.findViewById(R.id.todo_location);
        status = view.findViewById(R.id.todo_status);
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (status.isChecked())
                                                      databaseManager.editStatus(tid,"Yes");
                                                  else
                                                      databaseManager.editStatus(tid,"No");
                                              }
                                          }
        );
        btn1 = view.findViewById(R.id.todo_btn1);
        btn2 = view.findViewById(R.id.todo_btn2);
        btn3 = view.findViewById(R.id.todo_btn3);
        databaseManager = new DatabaseManager(view.getContext(),"todo");
        if(new_flag&&!edit_flag){
            btn2.setVisibility(GONE);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String stat;
                    if (status.isChecked())
                        stat = "Yes";
                    else
                        stat = "No";
                    databaseManager.addRow(title.getText().toString(),
                                            location.getText().toString(),
                                            stat);
                    closeDialog("todo_dialog");
                }
            });
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog("todo_dialog");
                }
            });
        }else if(!new_flag&&edit_flag){
            btn1.setText("Save");
            title.setText(tit);
            location.setText(loc);
            if (sta.equals("Yes"))
                status.setChecked(true);
            else
                status.setChecked(false);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String stat;
                    if (status.isChecked())
                        stat = "Yes";
                    else
                        stat = "No";
                    databaseManager.editRow(tid,
                                            title.getText().toString(),
                                            location.getText().toString(),
                                            stat);
                    closeDialog("edit_dialog");
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseManager.deleteRow(tid);
                    closeDialog("edit_dialog");
                }
            });
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog("edit_dialog");
                }
            });
        }else{
            btn1.setText("Edit");
            title.setVisibility(GONE);
            location.setVisibility(GONE);
            TextView textTitle = view.findViewById(R.id.todo_tt);
            TextView locationTitle = view.findViewById(R.id.todo_tl);
            textTitle.setText(textTitle.getText()+tit);
            locationTitle.setText(locationTitle.getText()+loc);
            if (sta.equals("Yes"))
                status.setChecked(true);
            else
                status.setChecked(false);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TodoDialog currTodo = TodoDialog.this;
                    TodoDialog newTodo = TodoDialog.newInstance(tid,tit,loc,sta,true);
                    newTodo.show(getActivity().getSupportFragmentManager().beginTransaction(),"edit_dialog");
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                    newTodo.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            currTodo.closeDialog("todo_dialog");
                        }
                    });
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseManager.deleteRow(tid);
                    closeDialog("todo_dialog");
                }
            });
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog("todo_dialog");
                }
            });
        }

    }

    public void closeDialog(String tag){
        Fragment prev = getFragmentManager().findFragmentByTag(tag);
        if(prev != null){
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
}
