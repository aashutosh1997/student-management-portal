package app.smp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class Todo extends Layout {
    View parView;
    FragmentManager fm;
    DatabaseManager databaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parView = getLayoutInflater().inflate(R.layout.activity_todo,fl);
        FloatingActionButton fab = findViewById(R.id.todo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodo();
            }
        });
        DatabaseManager databaseManager = new DatabaseManager(this,"todo");
        fm = getSupportFragmentManager();
        displayTodo(databaseManager.retrieveTodos());
    }

    private void displayTodo(ArrayList<TodoData> todoData) {
        RecyclerView rv = (RecyclerView)parView.findViewById(R.id.todo_recycler);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.Adapter radapter = new AttachTodo(databaseManager,fm,todoData,this);
        rv.setAdapter(radapter);
    }


    private void addTodo() {
        TodoDialog dialog = TodoDialog.newInstance();
        dialog.show(fm,"todo_dialog");
        fm.executePendingTransactions();
        dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                recreate();
            }
        });
    }
}
