package app.smp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

class AttachAdapter extends
        RecyclerView.Adapter<AttachAdapter.AddViewHolder> {
    private ArrayList<CardData> data;
    public Context parContext;
    public DatabaseManager databaseManager;
    public static class AddViewHolder extends
            RecyclerView.ViewHolder{
        ImageView cardImageView;
        TextView cardTextView;
        ImageView cardEditButton;
        ImageView cardDeleteButton;

        public AddViewHolder(View itemView){
            super(itemView);
            this.cardImageView = (ImageView) itemView.findViewById(R.id.card_image);
            this.cardTextView = (TextView) itemView.findViewById(R.id.card_text);
            this.cardEditButton = (ImageView) itemView.findViewById(R.id.card_edit);
            this.cardDeleteButton= (ImageView) itemView.findViewById(R.id.card_delete);
        }
    }
    public AttachAdapter(DatabaseManager mydManager, ArrayList<CardData> d, Context c) {
        this.data = d;
        this.parContext = c;
        this.databaseManager = mydManager;
    }

    @Override
    public AddViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_student,parent,false);
        return (new AddViewHolder(view));
    }

    @Override
    public void onBindViewHolder(final AddViewHolder holder, final int listPosition){
        TextView tView = holder.cardTextView;
        ImageView imgView = holder.cardImageView;


        CardData cd = data.get(listPosition);
        String text = cd.getFirstName()
                +cd.getLastName()
                +"\nID: "
                +cd.getId()
                +"\nCourse: "
                +cd.getCourse();
        tView.setText(text);

        File folder = new File(parContext.getFilesDir(), cd.getId()+"/");
        folder.mkdirs();
        final File file = new File(folder, "display.jpg");
        if (file.exists()){
            Bitmap bmp = BitmapFactory.decodeFile(file.toString());
            imgView.setImageBitmap(bmp);
        }

        setListeners(holder,cd);
    }

    private void setListeners(AddViewHolder holder, final CardData cd) {

        View view = holder.itemView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(parContext,detailsDisplay.class);
                in.putExtra("new_data",false);
                in.putExtra("edit_data",false);
                in.putExtra("details",cd);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                parContext.startActivity(in);
            }
        });

        view = holder.cardEditButton;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(parContext,detailsDisplay.class);
                in.putExtra("new_data",false);
                in.putExtra("edit_data",true);
                in.putExtra("details",cd);
                parContext.startActivity(in);
            }
        });

        view = holder.cardDeleteButton;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager dm = new DatabaseManager(parContext,"students");
                dm.deleteRow(cd.getId());
                Intent in = new Intent(view.getContext(), Students.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                view.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

}
