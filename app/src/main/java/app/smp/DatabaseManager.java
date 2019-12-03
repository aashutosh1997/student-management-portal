package app.smp;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

public class DatabaseManager{
    public static final String DB_NAME = "studentsdb";
    public static String DB_TABLE;
    public static final int DB_VERSION = 1;
    private static String CREATE_TABLE;
    private SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public DatabaseManager(Context c,String table) {
        this.context = c;
        this.DB_TABLE = table;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }
    public DatabaseManager openReadable() throws android.database.SQLException {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }
    public SQLiteDatabase getDb(){
        return (this.db);
    }

    public void close() {
        helper.close();
    }

    public int addRow(String fn,String ln,String gen,String course, int age, String add) {
        synchronized(this.db) {

            ContentValues newStudent = new ContentValues();
            newStudent.put("first_name", fn);
            newStudent.put("last_name", ln);
            newStudent.put("gender",gen);
            newStudent.put("course_study",course);
            newStudent.put("age",age);
            newStudent.put("address",add);
            try {
                db.insertOrThrow(DB_TABLE, null, newStudent);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return -1;
            }
            //db.close();
            Cursor c = db.query(DB_TABLE,new String[]{"last_insert_rowid()"},null,null,null,null,null,"");
            c.moveToFirst();
            return c.getInt(0);
        }
    }

    public void addRow(String title, String location, String status) {
        synchronized(this.db) {

            ContentValues todoAct = new ContentValues();
            todoAct.put("title",title);
            todoAct.put("location",location);
            todoAct.put("status",status);
            try {
                db.insertOrThrow(DB_TABLE, null, todoAct);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
            }
            //db.close();
        }
    }

    public void addExam(int student_id,String name,String date,String location){
        synchronized (this.db){
            ContentValues exam = new ContentValues();
            exam.put("student_id",student_id);
            exam.put("name",name);
            exam.put("date",date);
            exam.put("location",location);
            try{
                db.insertOrThrow(DB_TABLE,null,exam);
            }catch (Exception e){}
        }
    }

    public ArrayList<CardData> retrieveRows() {
        ArrayList<CardData> studentRows = new ArrayList<CardData>();
        String[] columns = new String[] {"id","first_name","last_name","gender","course_study","age","address"};
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            studentRows.add(new CardData(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return(studentRows);
    }

    public ArrayList<TodoData> retrieveTodos(){
        ArrayList<TodoData> todoRows = new ArrayList<TodoData>();
        String[] columns = new String[] {"id", "title", "location", "status"};
        Cursor cursor = db.query(DB_TABLE,columns,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            todoRows.add(new TodoData(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)));
            cursor.moveToNext();
        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return(todoRows);
    }
    public ArrayList<ExamData> retrieveExams(int id){
        ArrayList<ExamData> examRows = new ArrayList<>();
        String[] columns = new String[] {"student_id","name","date","location"};
        Cursor cursor = db.query(DB_TABLE,columns,"student_id=?",new String[]{""+id},null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            examRows.add(new ExamData(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return(examRows);
    }

    public void clearRecords()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
    }

    public int editRow(int sid, String fn, String ln, String gen, String cou, int age, String add) {
        ContentValues newStudent = new ContentValues();
        newStudent.put("first_name", fn);
        newStudent.put("last_name", ln);
        newStudent.put("gender",gen);
        newStudent.put("course_study",cou);
        newStudent.put("age",age);
        newStudent.put("address",add);
        //String[] columns = new String[] {"first_name","last_name","gender","course_study","age","address"};
        return(db.update(DB_TABLE,newStudent,"id=?",new String[]{""+sid}));
    }

    public int editRow(int id, String tit, String loc, String stat){
        ContentValues newTodo = new ContentValues();
        newTodo.put("title",tit);
        newTodo.put("location",loc);
        newTodo.put("status",stat);
        return(db.update(DB_TABLE,newTodo,"id=?",new String[]{""+id}));
    }

    public int editStatus(int id,String stat){
        ContentValues newTodo = new ContentValues();
        newTodo.put("status",stat);
        return(db.update(DB_TABLE,newTodo,"id=?",new String[]{""+id}));
    }

    public int editExam(int id, String name,String newName,String date,String location){
        ContentValues exam = new ContentValues();
        exam.put("name",newName);
        exam.put("date",date);
        exam.put("location",location);
        return(db.update(DB_TABLE,exam,"student_id=? and name=?",new String[]{""+id,name}));
    }

    public void deleteRow(int id) {
        db.delete(DB_TABLE,"id=?",new String[]{""+id});
    }
    public void deteleExam(int id,String name){db.delete(DB_TABLE,"student_id=? and name=?",new String[]{""+id,name});}

    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            CREATE_TABLE = "CREATE TABLE students (id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT, last_name TEXT, gender TEXT, course_study TEXT, age INT, address TEXT)";
            db.execSQL(CREATE_TABLE);
            CREATE_TABLE = "CREATE TABLE todo (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,location TEXT,status TEXT)";
            db.execSQL(CREATE_TABLE);
            CREATE_TABLE = "CREATE TABLE exams (student_id INTEGER, name TEXT, date DATETIME, location TEXT, FOREIGN KEY(student_id) REFERENCES students(id))";
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Products table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

}
