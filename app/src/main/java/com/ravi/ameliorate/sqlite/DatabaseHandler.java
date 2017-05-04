package com.ravi.ameliorate.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.ravi.ameliorate.model.Contact;

/**
 * Created by Ankit on 13-01-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "databaseRavi";

    // Contacts table name
    private static final String TABLE_CONTACTS = "dictionary";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "word";
    private static final String KEY_MEANING1 = "meaning1";
    private static final String KEY_MEANING2 = "meaning2";
    private static final String KEY_PRONUN = "pronun";
    private static final String KEY_SYNONYM = "synonyms";
    private static final String KEY_ANTONYM = "antonyms";
    private static final String KEY_SENTENCE = "sentence";

    public DatabaseHandler dbHelper;
    SQLiteDatabase db;
    Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public DatabaseHandler open() throws SQLException {
        dbHelper = new DatabaseHandler(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_MEANING1 + " TEXT," + KEY_MEANING2 + " TEXT," +KEY_PRONUN + " TEXT," +
                KEY_SYNONYM + " TEXT," + KEY_ANTONYM + " TEXT," + KEY_SENTENCE + " TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);



    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_MEANING1, contact.getMeaning()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new contact
    public void addContact2(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Dict Name
        values.put(KEY_MEANING1, contact.getMeaning()); // Dict Meaning
        values.put(KEY_MEANING2, contact.getMeaning2()); // Dict Meaning2
        values.put(KEY_PRONUN, contact.getPronun()); // Dict Pronun
        values.put(KEY_SYNONYM, contact.getSynonym()); // Dict Synonym
        values.put(KEY_ANTONYM, contact.getAntonym()); // Dict Antonym
        values.put(KEY_SENTENCE, contact.getSentence()); // Dict Sentence

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
    // Getting single word
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_MEANING1 }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting All words
    public List<Contact> getAllWord() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));//id
                contact.setName(cursor.getString(1));//word
                contact.setMeaning(cursor.getString(2));//meaning1
                contact.setMeaning2(cursor.getString(3));//meaning2
                contact.setPronun(cursor.getString(4));//pronun
                contact.setSynonym(cursor.getString(5));//synonym
                contact.setAntonym(cursor.getString(6));//antonym
                contact.setSentence(cursor.getString(7));//sentence
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting All words
    public List<Contact> getTenWords() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS +" ORDER BY " + KEY_ID + " DESC LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));//id
                contact.setName(cursor.getString(1));//word
                contact.setMeaning(cursor.getString(2));//meaning1
                contact.setMeaning2(cursor.getString(3));//meaning2
                contact.setPronun(cursor.getString(4));//pronun
                contact.setSynonym(cursor.getString(5));//synonym
                contact.setAntonym(cursor.getString(6));//antonym
                contact.setSentence(cursor.getString(7));//sentence
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting All Contacts
    public List<Contact> getAllWords(String input) {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        Log.e("ravi", "input " + input);
        Cursor mCursor = null;

        if(input.length()==0){

            mCursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                    KEY_NAME, KEY_MEANING1,KEY_MEANING1,KEY_PRONUN,
                    KEY_SYNONYM,KEY_ANTONYM,KEY_SENTENCE },null, null, null, null, null);

        }else{

            String selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + KEY_NAME +" like '%" + input + "%'";

            SQLiteDatabase db = this.getWritableDatabase();
             mCursor = db.rawQuery(selectQuery, null);

            /*mCursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                    KEY_NAME, KEY_MEANING1 },KEY_NAME+" like '%" + input + "%'",null, null, null, null);*/
            Log.e("ravi", "input 2");
        }

        if (mCursor.moveToFirst()) {
            do {
                Log.e("ravi", "input 3");
                Contact contact = new Contact();

                contact.setName(mCursor.getString(1));
                contact.setMeaning(mCursor.getString(2));//meaning1
                contact.setMeaning2(mCursor.getString(3));//meaning2
                contact.setPronun(mCursor.getString(4));//pronun
                contact.setSynonym(mCursor.getString(5));//synonym
                contact.setAntonym(mCursor.getString(6));//antonym
                contact.setSentence(mCursor.getString(7));//sentence
                Log.e("ravi", "input 4");
                // Adding contact to list
                contactList.add(contact);
            } while (mCursor.moveToNext());
        }else{

            try {
                //MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.voicepro);
               // mediaPlayer.start();
                Toast.makeText(context,"No word found in database.",Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_MEANING1, contact.getMeaning());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



    public Cursor fetchAllContacts(String input) throws SQLException{

        SQLiteDatabase db = null;
        Log.w("helper", input);
        Cursor mCursor = null;

        if(input.length()==0){

            mCursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                            KEY_NAME, KEY_MEANING1 },null, null, null, null, null);

        }else{

            mCursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                    KEY_NAME, KEY_MEANING1 },KEY_NAME+" like '%" + input + "%'",null, null, null, null);

        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public Cursor fetchAllCont() {

        Cursor mCursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_MEANING1 },null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchTenCont() {

        Cursor mCursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_MEANING1 },null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
