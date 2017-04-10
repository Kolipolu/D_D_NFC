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
}
