package app.smp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends Layout{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home,fl);
        checkForPermissions();
        addGrid();
    }

    private void addGrid() {
        GridLayout gl = findViewById(R.id.home_grid);
        //To add Student List
        View butt = getLayoutInflater().inflate(R.layout.card_home_buttons,null);
        ImageView imv = butt.findViewById(R.id.button_image);
        imv.setImageResource(R.drawable.ic_home_student);
        TextView txt = butt.findViewById(R.id.button_text);
        txt.setText("Students");
        butt.setPadding(50,50,50,50);
        gl.addView(butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(),Students.class);
                startActivity(in);
            }
        });

        //To add Todo List icon
        butt = getLayoutInflater().inflate(R.layout.card_home_buttons,null);
        imv = butt.findViewById(R.id.button_image);
        imv.setImageResource(R.drawable.ic_home_todo);
        txt = butt.findViewById(R.id.button_text);
        txt.setText("Todo");
        butt.setPadding(50,50,50,50);
        gl.addView(butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(),Todo.class);
                startActivity(in);
            }
        });

        //To add Power Button
        butt = getLayoutInflater().inflate(R.layout.card_home_buttons,null);
        imv = butt.findViewById(R.id.button_image);
        imv.setImageResource(R.drawable.ic_power);
        txt = butt.findViewById(R.id.button_text);
        txt.setText("Exit");
        butt.setPadding(50,50,50,50);
        gl.addView(butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkForPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }
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
}
