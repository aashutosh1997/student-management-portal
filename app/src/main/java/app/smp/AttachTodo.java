package app.smp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;

class AttachTodo
        extends RecyclerView.Adapter<AttachTodo.AddViewHolder> {

        private ArrayList<TodoData> data;
        public Context parContext;
        public DatabaseManager databaseManager;
        public FragmentManager fragmentManager;

    public static class AddViewHolder extends
                RecyclerView.ViewHolder{
            CheckedTextView textView;

            public AddViewHolder(View itemView){
                super(itemView);
                this.textView = (CheckedTextView) itemView.findViewById(R.id.todo_item);
            }
        }
        public AttachTodo(DatabaseManager mydManager, FragmentManager fm, ArrayList<TodoData> d, Context c) {
            this.data = d;
            this.parContext = c;
            this.databaseManager = mydManager;
            this.fragmentManager = fm;
        }
    @Override
    public AttachTodo.AddViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_todo,parent,false);
        return (new AttachTodo.AddViewHolder(view));
    }

    @Override
    public void onBindViewHolder(AddViewHolder holder, int listPosition){
        CheckedTextView ctv = holder.textView;
        final TodoData td = data.get(listPosition);
        ctv.setText(td.getTitle());
        if (td.getStatus().equals("Yes")){
            ctv.setChecked(true);
        }else{
            ctv.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoDialog dialog = TodoDialog.newInstance(td.getId(),td.getTitle(),td.getLocation(),td.getStatus(),false);
                dialog.show(fragmentManager,"todo_dialog");
                fragmentManager.executePendingTransactions();
                dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent in = new Intent(parContext,Todo.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        parContext.startActivity(in);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

}
