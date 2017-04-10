package info.dicj.d_d_nfc;

/**
 * Created by utilisateur on 2017-04-03.
 */
public class Quest {

    private int _id, _exp, _gold;
    private String _title, _descrip;

    public Quest(){}

    public Quest(int id, String title, String descrip, int exp, int gold){
        this._id = id;
        this._title = title;
        this._descrip = descrip;
        this._exp = exp;
        this._gold = gold;
    }

    public Quest(String title, String descrip, int exp, int gold){
        this._title = title;
        this._descrip = descrip;
        this._exp = exp;
        this._gold = gold;
    }

    //ID
    public int get_id(){return this._id;}
    public void set_id(int id){this._id = id;}

    //Title
    public String get_title(){return this._title;}
    public void set_title(String title){this._title = title;}

    //Description
    public String get_descrip(){return this._descrip;}
    public void set_descrip(String descrip){this._descrip = descrip;}

    //Expérience
    public int get_exp(){return this._exp;}
    public void set_exp(int exp){this._exp = exp;}

    //Gold
    public int get_gold(){return this._gold;}
    public void set_gold(int gold){this._gold = gold;}
}
