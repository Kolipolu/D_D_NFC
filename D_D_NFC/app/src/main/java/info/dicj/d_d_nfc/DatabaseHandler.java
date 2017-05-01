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

    public static boolean initaliser = false;
    private static int _idPNJAdd = 0;
    private static int _idQuestAdd = 0;
    private static int _idPNJQuestAdd = 0;
    private static int _idMonsterAdd = 0;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DDNFC";

    private static final String TABLE_PNJ = "tblPNJ";

    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_AGE = "age";
    private static final String KEY_CLASSE = "classe";
    private static final String KEY_RACE = "race";
    private static final String KEY_DESC = "descrip";

    private static final String TABLE_QUEST = "tblQuest";

    private static final String KEY_IDQ = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCQ = "descrip";
    private static final String KEY_EXPQ = "experience";
    private static final String KEY_GOLDQ = "gold";

    private static final String TABLE_MONSTER = "tblMonster";

    private static final String KEY_IDM = "id";
    private static final String KEY_HEALTH = "health";
    private static final String KEY_ATTACK = "attack";
    private static final String KEY_DEFENCE = "defence";
    private static final String KEY_EXPM = "experience";
    private static final String KEY_GOLDM = "gold";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCM = "descrip";
    private static final String KEY_LOOT = "loot";

    private static final String TABLE_PNJQUEST = "tblPNJQuest";

    private static final String KEY_IDPNJQUEST = "idPNJQuest";
    private static final String KEY_IDPNJ = "idPNJ";
    private static final String KEY_IDQUEST = "idQuest";


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
                + KEY_DESC + " TEXT)";
        db.execSQL(CREATE_PNJ_TABLE);

        String CREATE_QUEST_TABLE = "CREATE TABLE " + TABLE_QUEST + "("
                + KEY_IDQ + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCQ + " TEXT,"
                + KEY_EXPQ + " INTEGER,"
                + KEY_GOLDQ + " INTEGER)";
        db.execSQL(CREATE_QUEST_TABLE);

        String CREATE_MONSTER_TABLE = "CREATE TABLE " + TABLE_MONSTER + "("
                + KEY_IDM + " INTEGER PRIMARY KEY,"
                + KEY_HEALTH + " INTEGER,"
                + KEY_ATTACK + " INTEGER,"
                + KEY_DEFENCE + " INTEGER,"
                + KEY_EXPM + " INTEGER,"
                + KEY_GOLDM + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCM + " TEXT,"
                + KEY_LOOT + " TEXT)";
        db.execSQL(CREATE_MONSTER_TABLE);

        String CREATE_PNJQUEST_TABLE = "CREATE TABLE " + TABLE_PNJQUEST + "("
                + KEY_IDPNJQUEST + " INTEGER PRIMARY KEY,"
                + KEY_IDPNJ + " INTEGER,"
                + KEY_IDQUEST + " INTEGER,"
                + "FOREIGN KEY (" + KEY_IDPNJ + ") REFERENCES " + TABLE_PNJ + " (" + KEY_ID + "),"
                + "FOREIGN KEY (" + KEY_IDQUEST + ") REFERENCES " + TABLE_QUEST + " (" + KEY_IDQ + "))";
        db.execSQL(CREATE_PNJQUEST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PNJ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONSTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PNJQUEST);
        onCreate(db);
    }

    //
    //ADD
    //

    public void addPNJ(PNJ pnj){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, pnj.get_nom());
        values.put(KEY_PRENOM, pnj.get_prenom());
        values.put(KEY_AGE, pnj.get_age());
        values.put(KEY_CLASSE, pnj.get_classe());
        values.put(KEY_RACE, pnj.get_race());
        values.put(KEY_DESC, pnj.get_descrip());

        db.insert(TABLE_PNJ, null, values);
        db.close();
    }

    public void addQuest(Quest quest){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDQ, quest.get_id());
        values.put(KEY_TITLE, quest.get_title());
        values.put(KEY_DESCQ, quest.get_descrip());
        values.put(KEY_EXPQ, quest.get_exp());
        values.put(KEY_GOLDQ, quest.get_gold());

        db.insert(TABLE_QUEST, null, values);
        db.close();
    }

    public void addMonster(Monster monster){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDM, monster.get_id());
        values.put(KEY_HEALTH, monster.get_health());
        values.put(KEY_ATTACK, monster.get_attack());
        values.put(KEY_DESCM, monster.get_def());
        values.put(KEY_EXPM, monster.get_exp());
        values.put(KEY_GOLDM, monster.get_gold());
        values.put(KEY_NAME, monster.get_name());
        values.put(KEY_DESCM, monster.get_descrip());

        String Loot = "";
        for(String loot : monster.get_loot()){
            Loot += loot + ",";
        }
        values.put(KEY_LOOT, Loot);

        db.insert(TABLE_MONSTER, null, values);
        db.close();
    }

    public void addIdPQ(PnjQuest pnjQuest){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDPNJQUEST, pnjQuest.get_id());
        values.put(KEY_IDPNJ, pnjQuest.get_idPNJ());
        values.put(KEY_IDQUEST, pnjQuest.get_idQuest());
        db.insert(TABLE_PNJQUEST, null, values);
        db.close();
    }

    //
    //GET
    //

    public PNJ getPNJ(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PNJ, new String[]{KEY_ID, KEY_NOM, KEY_PRENOM, KEY_AGE, KEY_CLASSE, KEY_RACE, KEY_DESC}, KEY_ID + " = ?", new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        PNJ pnj = new PNJ(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(3)), cursor.getString(1),cursor.getString(2),cursor.getString(4), cursor.getString(5), cursor.getString(6));
        cursor.close();
        return pnj;
    }

    public Quest getQuest(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUEST, new String[]{KEY_IDQ, KEY_TITLE, KEY_DESCQ, KEY_EXPQ, KEY_EXPQ}, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Quest quest = new Quest(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
        cursor.close();
        return quest;
    }

    public Monster getMonster(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MONSTER, new String[]{KEY_IDM, KEY_HEALTH, KEY_ATTACK, KEY_DEFENCE, KEY_EXPM, KEY_GOLDM, KEY_NAME, KEY_DESCM, KEY_LOOT}, KEY_ID + " = " + id, new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        String[] loot = null;

        try{
        loot = cursor.getString(8).split(",");
        }catch(Exception e){}
        Monster monster = new Monster(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), cursor.getString(6), cursor.getString(7), loot);
        cursor.close();
        return monster;
    }

    public PnjQuest getPnjQuest(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PNJQUEST, new String[]{KEY_IDPNJQUEST, KEY_IDPNJ, KEY_IDQUEST},KEY_IDPNJQUEST + " = " + id, null, null, null, null, null);


        if(cursor != null){
            cursor.moveToFirst();
        }

        PnjQuest pnjQuest = new PnjQuest(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)));
        cursor.close();
        return pnjQuest;
    }

    //
    //GET ALL
    //

    public List<PNJ> getAllPNJ(){
        List<PNJ> pnjList = new ArrayList<>();

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
                pnjList.add(pnj);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return pnjList;
    }

    public List<Quest> getAllQuest(){
        List<Quest> questList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_QUEST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Quest quest = new Quest();
                quest.set_id(Integer.parseInt(cursor.getString(0)));
                quest.set_title(cursor.getString(1));
                quest.set_descrip(cursor.getString(2));
                quest.set_exp(Integer.parseInt(cursor.getString(3)));
                quest.set_gold(Integer.parseInt(cursor.getString(4)));
                questList.add(quest);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return questList;
    }

    public List<Monster> getAllMonster(){
        List<Monster> monsterList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MONSTER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Monster monster = new Monster();
                monster.set_id(Integer.parseInt(cursor.getString(0)));
                monster.set_health(Integer.parseInt(cursor.getString(1)));
                monster.set_attack(Integer.parseInt(cursor.getString(2)));
                monster.set_def(Integer.parseInt(cursor.getString(3)));
                monster.set_exp(Integer.parseInt(cursor.getString(4)));
                monster.set_gold(Integer.parseInt(cursor.getString(5)));
                monster.set_name(cursor.getString(6));
                monster.set_descrip(cursor.getString(7));
                String[] loot = cursor.getString(8).split(",");
                monster.set_loot(loot);
                monsterList.add(monster);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return monsterList;
    }

    public List<PnjQuest> getAllPnjQuest(){
        List<PnjQuest> pnjQuestList = new ArrayList<>();

        String selectString = "SELECT * FROM " + TABLE_PNJQUEST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectString, null);

        if(cursor.moveToFirst()){
            do {
                PnjQuest pnjQuest = new PnjQuest();
                pnjQuest.set_id(Integer.parseInt(cursor.getString(0)));
                pnjQuest.set_idPNJ(Integer.parseInt(cursor.getString(1)));
                pnjQuest.set_idQuest(Integer.parseInt(cursor.getString(2)));
                pnjQuestList.add(pnjQuest);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return pnjQuestList;
    }

    //
    //GET COUNT
    //

    public int getPNJCount(){
        String selectQuery = "SELECT * FROM " + TABLE_PNJ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int getQuestCount(){
        String selectQuery = "SELECT * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int getMonsterCount(){
        String selectQuery = "SELECT * FROM "+ TABLE_MONSTER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int getPnjQuestCount(){
        String selectQuery = "SELECT * FROM " + TABLE_PNJQUEST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    //
    //UPDATE
    //

    public int updatePNJ(PNJ pnj){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, pnj.get_nom());
        values.put(KEY_PRENOM, pnj.get_prenom());
        values.put(KEY_AGE, pnj.get_age());
        values.put(KEY_CLASSE, pnj.get_classe());
        values.put(KEY_RACE, pnj.get_race());
        values.put(KEY_DESC, pnj.get_descrip());

        return db.update(TABLE_PNJ, values, KEY_ID + " = ?", new String[]{String.valueOf(pnj.get_id())});
    }

    public int updateQuest(Quest quest){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDQ, quest.get_id());
        values.put(KEY_TITLE, quest.get_title());
        values.put(KEY_DESCQ, quest.get_descrip());
        values.put(KEY_EXPQ, quest.get_exp());
        values.put(KEY_GOLDQ, quest.get_gold());

        return db.update(TABLE_QUEST, values, KEY_IDQ + " = ?", new String[]{String.valueOf(quest.get_id())});
    }

    public int updateMonster(Monster monster){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDM, monster.get_id());
        values.put(KEY_HEALTH, monster.get_health());
        values.put(KEY_ATTACK, monster.get_attack());
        values.put(KEY_DEFENCE, monster.get_def());
        values.put(KEY_EXPM, monster.get_exp());
        values.put(KEY_GOLDM, monster.get_gold());
        values.put(KEY_NAME, monster.get_name());
        values.put(KEY_DESCM, monster.get_descrip());

        String Loot = "";
        for(String loot : monster.get_loot()){
            Loot += loot + ",";
        }
        values.put(KEY_LOOT, Loot);

        return db.update(TABLE_MONSTER, values, KEY_IDM + " = ?", new String[]{String.valueOf(monster.get_id())});
    }

    public int updatePnjQuest(PnjQuest pnjQuest){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDPNJQUEST, pnjQuest.get_id());
        values.put(KEY_IDPNJ, pnjQuest.get_idPNJ());
        values.put(KEY_IDQUEST, pnjQuest.get_idQuest());

        return db.update(TABLE_PNJQUEST, values, KEY_IDPNJQUEST + " = ?", new String[]{String.valueOf(pnjQuest.get_id())});
    }

    //
    //DELETE
    //

    public void deletePNJ(PNJ pnj){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PNJ, KEY_ID + " = ?", new String[]{String.valueOf(pnj.get_id())});
        db.close();
    }

    public void deleteQuest(Quest quest){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUEST, KEY_IDQ + " = ?", new String[]{String.valueOf(quest.get_id())});
        db.close();
    }

    public void deleteMonster(Monster monster){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MONSTER, KEY_IDM + " = ?", new String[]{String.valueOf(monster.get_id())});
        db.close();
    }

    public void deletePnjQuest(PnjQuest pnjQuest){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PNJQUEST, KEY_IDPNJQUEST + " = ?", new String[]{String.valueOf(pnjQuest.get_id())});
        db.close();
    }

    //
    //INSERT DE BASE
    //

    public void initialisation(){
        addPNJ(new PNJ(_idPNJAdd++, 18, "McGee", "Bobby", "Gunslinger", "Human", "Little more than a kid, Bobby had to grow up fast, yet holds onto his penchant for the theater."));
        addQuest(new Quest(_idQuestAdd++, "The delivery Boy", "Save Bobby McGee from a deadly fall.",10,100));
        addIdPQ(new PnjQuest(_idPNJQuestAdd++, _idPNJAdd, _idQuestAdd));
        initaliser =  true;
    }
}
