package app.smp;

import android.os.Parcel;
import android.os.Parcelable;

public class TodoData implements Parcelable{
    private int id;
    private String title;
    private String location;
    private String status;
    public TodoData(int i,String t,String l,String s){
        this.id = i;
        this.title = t;
        this.location = l;
        this.status = s;
    }
    protected TodoData(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.location = in.readString();
        this.status = in.readString();
    }
    public int getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getLocation(){
        return this.location;
    }
    public String getStatus() {
        return this.status;
    }
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out,int flags){
        out.writeInt(id);
        out.writeString(title);
        out.writeString(location);
        out.writeString(status);
    }

    public static final Parcelable.Creator<TodoData> CREATOR = new Parcelable.Creator<TodoData>(){
        public TodoData createFromParcel(Parcel in){
            return new TodoData(in);
        }

        @Override
        public TodoData[] newArray(int i) {
            return new TodoData[i];
        }
    };
}
