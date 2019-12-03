package app.smp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.QuickContactBadge;

import java.io.Serializable;
import java.util.ArrayList;

public class Students extends Layout {
    DatabaseManager mydManager;
    View parView;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parView = getLayoutInflater().inflate(R.layout.activity_students,fl);
        mydManager = new DatabaseManager(Students.this,"students");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Students.this,detailsDisplay.class);
                in.putExtra("new_data",true);
                in.putExtra("edit_data",false);
                startActivityForResult(in,1);
            }
        });
        loadData(mydManager.retrieveRows());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.recreate();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    public void loadData(ArrayList<CardData> cdList){
            RecyclerView rv = (RecyclerView)parView.findViewById(R.id.students_recycler);
            rv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            rv.setLayoutManager(lm);
            rv.setItemAnimator(new DefaultItemAnimator());
            RecyclerView.Adapter radapter = new AttachAdapter(mydManager,cdList,this);
            rv.setAdapter(radapter);
    }
}
