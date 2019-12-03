package app.smp;

import android.os.Parcel;
import android.os.Parcelable;

public class CardData implements Parcelable{
    int student_id;
    String first_name;
    String last_name;
    String gender;
    String course_study;
    int age;
    String address;
    public CardData(int id,String fn,String ln,String gen,String cs,int ag,String add){
        this.student_id = id;
        this.first_name = fn;
        this.last_name = ln;
        this.gender = gen;
        this.course_study = cs;
        this.age = ag;
        this.address = add;
    }
    protected CardData(Parcel in){
        this.student_id = in.readInt();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.gender = in.readString();
        this.course_study = in.readString();
        this.age = in.readInt();
        this.address = in.readString();
    }
    public int getId(){
        return student_id;
    }
    public String getFirstName(){
        return first_name;
    }
    public String getLastName(){
        return last_name;
    }
    public String getGender(){
        return gender;
    }
    public String getCourse(){
        return course_study;
    }
    public int getAge(){
        return age;
    }
    public String getAddress(){
        return address;
    }

    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out,int flags){
        out.writeInt(student_id);
        out.writeString(first_name);
        out.writeString(last_name);
        out.writeString(gender);
        out.writeString(course_study);
        out.writeInt(age);
        out.writeString(address);
    }

    public static final Parcelable.Creator<CardData> CREATOR = new Parcelable.Creator<CardData>(){
        public CardData createFromParcel(Parcel in){
            return new CardData(in);
        }

        @Override
        public CardData[] newArray(int i) {
            return new CardData[i];
        }
    };
}

