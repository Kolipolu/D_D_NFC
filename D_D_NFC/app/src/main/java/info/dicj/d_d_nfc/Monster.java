package info.dicj.d_d_nfc;

/**
 * Created by utilisateur on 2017-04-03.
 */
public class Monster {

    private int _id, _health, _attack, _def, _exp, _gold;
    private String _name, _descrip;
    private String[] _loot;

    public Monster(){}

    public Monster(int id, int health, int attack, int def, int exp, int gold, String name, String descrip, String[] loot){
        this._id = id;
        this._health = health;
        this._attack = attack;
        this._def = def;
        this._exp = exp;
        this._gold = gold;
        this._name = name;
        this._descrip = descrip;
        this._loot = loot;
    }

    public Monster(int health, int attack, int def, int exp, int gold, String name, String descrip, String[] loot){
        this._health = health;
        this._attack = attack;
        this._def = def;
        this._exp = exp;
        this._gold = gold;
        this._name = name;
        this._descrip = descrip;
        this._loot = loot;
    }

    //ID
    public int get_id(){return this._id;}
    public void set_id(int id){this._id = id;}

    //Health
    public int get_health(){return this._health;}
    public void set_health(int health){this._health = health;}

    //Attack
    public int get_attack(){return this._attack;}
    public  void set_attack(int attack){this._attack = attack;}

    //Defence
    public int get_def(){return this._def;}
    public void set_def(int def){this._def = def;}

    //Exp.
    public int get_exp(){return this._exp;}
    public void set_exp(int exp){this._exp = exp;}

    //Gold
    public int get_gold() {return _gold;}
    public void set_gold(int gold){this._gold = gold;}

    //Name
    public String get_name(){return this._name;}
    public void set_name(String name){this._name = name;}

    //Descrip
    public String get_descrip(){return this._descrip;}
    public void set_descrip(String descrip){this._descrip = descrip;}

    //Loot
    public String[] get_loot(){return this._loot;}
    public void set_loot(String[] loot){this._loot = loot;}
}
