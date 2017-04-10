package info.dicj.d_d_nfc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by utilisateur on 27/03/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DDNFC";

    private static final String TABLE_PNJ = "PNJ";

    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_AGE = "age";
    private static final String KEY_CLASSE = "classe";
    private static final String KEY_RACE = "race";
    private static final String KEY_DESC = "descrip";
    private static final String KEY_QUETE = "quete";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PNJ_TABLE = "CREATE TABLE " + TABLE_PNJ + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NOM + " TEXT,"
                + KEY_PRENOM + " TEXT,"
                + KEY_AGE + " INTEGER,"
                + KEY_CLASSE + " TEXT,"
                + KEY_RACE + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_QUETE + " TEXT)";
        db.execSQL(CREATE_PNJ_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PNJ);
        onCreate(db);
    }

    public void addPNJ(PNJ pnj){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, pnj.get_nom());
        values.put(KEY_PRENOM, pnj.get_prenom());
        values.put(KEY_AGE, pnj.get_age());
        values.put(KEY_CLASSE, pnj.get_classe());
        values.put(KEY_RACE, pnj.get_race());
        values.put(KEY_DESC, pnj.get_descrip());
        values.put(KEY_QUETE, pnj.get_quete());

        db.insert(TABLE_PNJ, null, values);
        db.close();
    }

    public PNJ getPNJ(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PNJ, new String[]{KEY_ID, KEY_NOM, KEY_PRENOM, KEY_AGE, KEY_CLASSE, KEY_RACE, KEY_DESC, KEY_QUETE}, KEY_ID + " = ?", new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        PNJ pnj = new PNJ(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(3)), cursor.getString(1),cursor.getString(2),cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        return pnj;
    }

    public List<PNJ> getAllPNJ(){
        List<PNJ> pnjList = new ArrayList<PNJ>();

        String selectQuery = "SELECT * FROM " + TABLE_PNJ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                PNJ pnj = new PNJ();
                pnj.set_id(Integer.parseInt(cursor.getString(0)));
                pnj.set_age(Integer.parseInt(cursor.getString(3)));
                pnj.set_nom(cursor.getString(1));
                pnj.set_prenom(cursor.getString(2));
                pnj.set_classe(cursor.getString(4));
                pnj.set_race(cursor.getString(5));
                pnj.set_descrip(cursor.getString(6));
                pnj.set_quete(cursor.getString(7));
                pnjList.add(pnj);
            }while(cursor.moveToNext());
        }

        return pnjList;
    }

    public int getContactCount(){
        String selectQuery = "SELECT * FROM " + TABLE_PNJ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateContact(PNJ pnj){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, pnj.get_nom());
        values.put(KEY_PRENOM, pnj.get_prenom());
        values.put(KEY_AGE, pnj.get_age());
        values.put(KEY_CLASSE, pnj.get_classe());
        values.put(KEY_RACE, pnj.get_race());
        values.put(KEY_DESC, pnj.get_descrip());
        values.put(KEY_QUETE, pnj.get_quete());

        return db.update(TABLE_PNJ, values, KEY_ID + " = ?", new String[]{String.valueOf(pnj.get_id())});
    }

    public void deleteContact(PNJ pnj){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PNJ, KEY_ID + " = ?", new String[]{String.valueOf(pnj.get_id())});
        db.close();
    }
}
