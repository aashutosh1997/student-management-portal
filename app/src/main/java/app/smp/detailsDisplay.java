package app.smp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class detailsDisplay
        extends CollapsingLayout {
    View parView;
    CardData cd;
    DatabaseManager databaseManager;
    private static int RESULT_LOAD_IMAGE = 535;
    int sid;
    boolean edit_flag;
    boolean new_flag;
    int age;
    NumberPicker age_picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parView = getLayoutInflater().inflate(R.layout.activity_details_display, fl);
        RecyclerView rv = parView.findViewById(R.id.details_recycler);
        TableLayout tl = parView.findViewById(R.id.edit_details);
        Intent in = getIntent();
        new_flag = in.getBooleanExtra("new_data",true);
        edit_flag = in.getBooleanExtra("edit_data",false);
        databaseManager = new DatabaseManager(this,"students");
        if (new_flag && !edit_flag) {
            rv.setVisibility(View.GONE);
            tl.setVisibility(View.VISIBLE);
            sid = databaseManager.addRow("","","","",0,"");
            age_picker = findViewById(R.id.table_age);
            age_picker.setMinValue(15);
            age_picker.setMaxValue(60);
            age_picker.setOnValueChangedListener(onValueChangedListener);
            addDetails(sid);
        } else if(!new_flag && edit_flag) {
            rv.setVisibility(View.GONE);
            tl.setVisibility(View.VISIBLE);
            cd = in.getParcelableExtra("details");
            sid = cd.getId();
            loadImage();
            EditText fn = findViewById(R.id.table_fn);
            fn.setText(cd.getFirstName());
            EditText ln = findViewById(R.id.table_ln);
            ln.setText(cd.getLastName());
            RadioGroup gen = findViewById(R.id.table_gen);
            if (cd.getGender().equals("Male")){
                RadioButton rb = gen.findViewById(R.id.RadioMale);
                rb.setChecked(true);
            }else if (cd.getGender().equals("Female")){
                RadioButton rb = gen.findViewById(R.id.RadioFemale);
                rb.setChecked(true);
            }else{
                RadioButton rb = gen.findViewById(R.id.RadioOthers);
                rb.setChecked(true);
            }
            EditText cou = findViewById(R.id.table_course);
            cou.setText(cd.getCourse());
            age = cd.getAge();
            age_picker = findViewById(R.id.table_age);
            age_picker.setMinValue(15);
            age_picker.setMaxValue(60);
            age_picker.setValue(age);
            age_picker.setOnValueChangedListener(onValueChangedListener);
            EditText add = findViewById(R.id.table_add);
            add.setText(cd.getAddress());
            addDetails(cd.getId());
        }else{
                cd = in.getParcelableExtra("details");
                sid = cd.getId();
                loadImage();
                rv.setVisibility(View.VISIBLE);
                tl.setVisibility(View.GONE);
                viewDetail();
            }
        }

    private void addDetails(final int sid) {
        Button sbtn = findViewById(R.id.table_submit);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView id = findViewById(R.id.table_id);
                id.setText(""+sid);
                EditText fn = findViewById(R.id.table_fn);
                EditText ln = findViewById(R.id.table_ln);
                RadioGroup gen = findViewById(R.id.table_gen);
                int sel_gen_id = gen.getCheckedRadioButtonId();
                RadioButton sel_gen = findViewById(sel_gen_id);
                EditText cou = findViewById(R.id.table_course);
                EditText add = findViewById(R.id.table_add);
                int r = databaseManager.editRow(
                        sid,
                        fn.getText().toString(),
                        ln.getText().toString(),
                        sel_gen.getText().toString(),
                        cou.getText().toString(),
                        age,
                        add.getText().toString());
                cd = new CardData(sid,
                        fn.getText().toString(),
                        ln.getText().toString(),
                        sel_gen.getText().toString(),
                        cou.getText().toString(),
                        age,
                        add.getText().toString());
                if(r>0){
                    id.setText("");
                    fn.setText("");
                    ln.setText("");
                    sel_gen.setSelected(false);
                    cou.setText("");
                    cou.setText("");
                    add.setText("");
                    ImageView imv = findViewById(R.id.htab_header);
                    imv.setBackgroundResource(R.drawable.img);
                    Intent in = new Intent(v.getContext(), detailsDisplay.class);
                    in.putExtra("data", cd);
                    in.putExtra("new_flag",false);
                    in.putExtra("edit_flag",false);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    v.getContext().startActivity(in);
                    new_flag = false;
                }
                Toast.makeText(detailsDisplay.this,r + " Records Added!",Toast.LENGTH_SHORT).show();

                //databaseManager.close();
            }
        });
        sbtn = findViewById(R.id.table_cancel);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_flag && new_flag)
                    databaseManager.deleteRow(sid);
                Intent in = new Intent(v.getContext(), Students.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                v.getContext().startActivity(in);
            }
        });
    }

    public void viewDetail() {
        RecyclerView rv = (RecyclerView) parView.findViewById(R.id.details_recycler);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.Adapter radapter = new AttachDetails(cd, this);
        rv.setAdapter(radapter);
    }

    NumberPicker.OnValueChangeListener onValueChangedListener = new NumberPicker.OnValueChangeListener(){
        @Override
        public void onValueChange(NumberPicker numberPicker, int i,int i1){
            age = numberPicker.getValue();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.edit_image:   Intent i = new Intent(
                                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                                    break;
            case android.R.id.home: if(!edit_flag && new_flag)
                                        databaseManager.deleteRow(sid);
                                    Intent in = new Intent(this, Students.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
            }
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                File folder = new File(this.getFilesDir(), sid + "/");
                folder.mkdirs();
                final File file = new File(folder, "display.jpg");
                getImage(new FileInputStream(picturePath).getChannel(),
                        new FileOutputStream(file).getChannel());
                loadImage();
            }catch (FileNotFoundException e){}
        }

    }

    private void loadImage() {
        File folder = new File(this.getFilesDir(), sid + "/");
        final File file = new File(folder, "display.jpg");
        if (file.exists()&&file.length()!=0) {
            ImageView imv = findViewById(R.id.htab_header);
            Bitmap bmp = BitmapFactory.decodeFile(file.toString());
            imv.setImageBitmap(bmp);
        }
    }

    public void getImage(FileChannel source,FileChannel destination){
        long count = 0;
        try {
            long size = source.size();
            while ((count += destination.transferFrom(source, count, size - count)) < size) ;
        }catch (IOException ex){}
    }

    @Override
    public void onBackPressed(){
        if (!edit_flag && new_flag)
            databaseManager.deleteRow(sid);
        Intent in = new Intent(this, Students.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
    }
}
