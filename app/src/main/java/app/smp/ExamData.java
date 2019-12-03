package app.smp;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamData implements Parcelable {
    int student_id;
    String exam_name;
    String exam_date;
    String exam_location;
    //static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyy");
    public ExamData(int i,String ename,String edate,String elocation){
        this.student_id = i;
        this.exam_name = ename;
        this.exam_date = edate;
        this.exam_location = elocation;
    }
    protected ExamData(Parcel in){
        this.student_id = in.readInt();
        this.exam_name = in.readString();
        this.exam_date = in.readString();
        this.exam_location = in.readString();
    }
    int getSid(){return this.student_id;}
    String getName(){return this.exam_name;}
    String getDate(){return this.exam_date.toString();}
    String getLocation(){return this.exam_location;}
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out,int flags){
        out.writeInt(student_id);
        out.writeString(exam_name);
        out.writeString(exam_date);
        out.writeString(exam_location);
    }

    public static final Parcelable.Creator<ExamData> CREATOR = new Parcelable.Creator<ExamData>(){
        public ExamData createFromParcel(Parcel in){
                return new ExamData(in);
        }

        @Override
        public ExamData[] newArray(int i) {
            return new ExamData[i];
        }
    };
}
