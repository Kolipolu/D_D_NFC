package info.dicj.d_d_nfc;

/**
 * Created by utilisateur on 27/03/2017.
 */
public class PNJ {

    private int _id, _age;
    private String _nom, _prenom, _classe, _race, _descrip;

    public PNJ(){}

    public PNJ(int id, int age, String nom, String prenom, String classe, String race, String descrip){
        this._id = id;
        this._age = age;
        this._nom = nom;
        this._prenom = prenom;
        this._classe = classe;
        this._race = race;
        this._descrip = descrip;
    }

    public PNJ(int age, String nom, String prenom, String classe, String race, String descrip){
        this._age = age;
        this._nom = nom;
        this._prenom = prenom;
        this._classe = classe;
        this._race = race;
        this._descrip = descrip;
    }

    //ID

    public int get_id(){
        return this._id;
    }

    public void set_id(int id){
        this._id = id;
    }

    //Age

    public int get_age(){
        return this._age;
    }

    public void set_age(int age){
        this._age = age;
    }

    //Nom

    public String get_nom(){
        return this._nom;
    }

    public void set_nom(String nom){
        this._nom = nom;
    }

    //Prenom

    public String get_prenom() {
        return this._prenom;
    }

    public void set_prenom(String prenom){
        this._prenom = prenom;
    }

    //Classe

    public String get_classe(){
        return this._classe;
    }

    public void set_classe(String classe){
        this._classe = classe;
    }

    //Race

    public String get_race(){
        return this._race;
    }

    public void set_race(String race){
        this._race = race;
    }

    //Descrip

    public String get_descrip(){
        return this._descrip;
    }

    public void set_descrip(String descrip){
        this._descrip = descrip;
    }

}
