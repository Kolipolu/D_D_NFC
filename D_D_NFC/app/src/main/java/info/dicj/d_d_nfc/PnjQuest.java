package info.dicj.d_d_nfc;


public class PnjQuest {
    private int _idQuest, _idPNJ, _idPNJQuest;

    public PnjQuest(){}

    public PnjQuest(int id, int idPNJ,int idQuest){
        this._idPNJQuest = id;
        this._idPNJ = idPNJ;
        this._idQuest = idQuest;
    }

    public PnjQuest(int idPNJ, int idQuest){
        this._idPNJ = idPNJ;
        this._idQuest = idQuest;
    }

    //ID
    public int get_id(){return this._idPNJQuest;}
    public void set_id(int id){this._idPNJQuest = id;}

    //ID Quest
    public int get_idQuest(){return this._idQuest;}
    public  void set_idQuest(int idQ){this._idQuest = idQ;}

    //ID PNJ
    public int get_idPNJ(){return this._idPNJ;}
    public void set_idPNJ(int idP){this._idPNJ = idP;}
}
