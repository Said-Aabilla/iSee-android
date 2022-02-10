package com.example.iSee.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.iSee.Controllers.impl.SignupController;
import com.example.iSee.Models.User;

import java.util.ArrayList;

public class UserDbHelper extends SQLiteOpenHelper {
//Globals
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "iSee.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

//Constructor
public UserDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
}



//Création de la table user
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + UserContrat.UserTable.Table_Name + " (" +
                    UserContrat.UserTable._ID + " INTEGER PRIMARY KEY," +
                    UserContrat.UserTable.column_name_fullname + TEXT_TYPE + COMMA_SEP +
                    UserContrat.UserTable.column_name_email + TEXT_TYPE +COMMA_SEP+
                    UserContrat.UserTable.column_name_password +TEXT_TYPE+ COMMA_SEP+
                      UserContrat.UserTable.column_name_langage +TEXT_TYPE+COMMA_SEP+
                    UserContrat.UserTable.column_name_vision +TEXT_TYPE + " )";
//Suppression de la table user
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + UserContrat.UserTable.Table_Name;



    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
 //Insert user
    public void InsertUser(String fullname, String email,String password,String langage,String vision ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContrat.UserTable.column_name_fullname, fullname);
        values.put(UserContrat.UserTable.column_name_email, email);
        values.put(UserContrat.UserTable.column_name_password, password);
        values.put(UserContrat.UserTable.column_name_langage,langage);
        values.put(UserContrat.UserTable.column_name_vision,vision);

        long newRowId = db.insert
                (UserContrat.UserTable.Table_Name, null, values);
    }
//Update user
    public void UpdateUser( String id ) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues valeurs = new ContentValues();
        valeurs.put(UserContrat.UserTable.column_name_fullname,""); //MAJ
        bd.update(UserContrat.UserTable.Table_Name, valeurs, "_id=?", new String[] { id });
    }

//Delete user
    public void DeleteUser(String email ) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues valeurs = new ContentValues();
        bd.delete(UserContrat.UserTable.Table_Name,"email= ?",new String[]{email});
    }
//Get user
public User getUser(String Email) {
    SQLiteDatabase bd = this.getReadableDatabase();
   final User user = new User();
    // Colonnes de la Base de Données.
    String[] projection = {
            UserContrat.UserTable._ID,
            UserContrat.UserTable.column_name_fullname,
            UserContrat.UserTable.column_name_email,
            UserContrat.UserTable.column_name_password,
            UserContrat.UserTable.column_name_langage,
            UserContrat.UserTable.column_name_vision



    };
    String selection = UserContrat.UserTable.column_name_email + " = ?";
    String[] selectionArgs ={Email};


    Cursor cursor = bd.query(UserContrat.UserTable.Table_Name, //Table to query
            projection,                    //columns to return
            selection,                  //columns for the WHERE clause
            selectionArgs,              //The values for the WHERE clause
            null,                       //group the rows
            null,                       //filter by row groups
            null);
    cursor.moveToFirst();
if (cursor.moveToFirst()) {
    user.setFullname(cursor.getString(cursor.getColumnIndex(UserContrat.UserTable.column_name_fullname)));
    user.setEmail(cursor.getString(cursor.getColumnIndex(UserContrat.UserTable.column_name_email)));
    user.setPassword(cursor.getString(cursor.getColumnIndex(UserContrat.UserTable.column_name_password)));
    user.setLangage(cursor.getString(cursor.getColumnIndex(UserContrat.UserTable.column_name_langage)));
    user.setVision(cursor.getString(cursor.getColumnIndex(UserContrat.UserTable.column_name_vision)));



}
    return user;


}

//____
    @SuppressLint("Range")
    public ArrayList<String> getUsers( ) {
        SQLiteDatabase bd = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        // Colonnes de la Base de Données.
        String[] projection = {
                UserContrat.UserTable._ID,
                UserContrat.UserTable.column_name_fullname,
                UserContrat.UserTable.column_name_email,
                UserContrat.UserTable.column_name_password,
                UserContrat.UserTable.column_name_langage,
                UserContrat.UserTable.column_name_vision



        };

        Cursor c = bd.rawQuery("select * from "+UserContrat.UserTable.Table_Name,null);
        c.moveToFirst();

        while(c.isAfterLast() == false){
            array_list.add(c.getString(c.getColumnIndex(UserContrat.UserTable.column_name_email)));
            array_list.add(c.getString(c.getColumnIndex(UserContrat.UserTable.column_name_password)));
            array_list.add(c.getString(c.getColumnIndex(UserContrat.UserTable.column_name_vision)));
            array_list.add(c.getString(c.getColumnIndex(UserContrat.UserTable.column_name_langage)));
            array_list.add(c.getString(c.getColumnIndex(UserContrat.UserTable.column_name_fullname)));

            c.moveToNext();
        }
        return array_list;
    }
//Check user
public boolean checkUser(String email, String password) {
    // array of columns to fetch
    String[] columns = {
            UserContrat.UserTable._ID
    };
    SQLiteDatabase db = this.getReadableDatabase();
    // selection criteria
    String selection = UserContrat.UserTable.column_name_email + " = ?" + " AND " + UserContrat.UserTable.column_name_password + " = ?";
    // selection arguments
    String[] selectionArgs = {email, password};
    // query user table with conditions
    /**
     * Here query function is used to fetch records from user table this function works like we use sql query.
     * SQL query equivalent to this query function is
     * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
     */
    Cursor cursor = db.query(UserContrat.UserTable.Table_Name, //Table to query
            columns,                    //columns to return
            selection,                  //columns for the WHERE clause
            selectionArgs,              //The values for the WHERE clause
            null,                       //group the rows
            null,                       //filter by row groups
            null);                      //The sort order
    int cursorCount = cursor.getCount();
    cursor.close();
    db.close();
    if (cursorCount > 0) {
        return true;
    }
    return false;
}
}
