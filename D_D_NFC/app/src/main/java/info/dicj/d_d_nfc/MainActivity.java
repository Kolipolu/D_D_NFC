package info.dicj.d_d_nfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(DatabaseHandler.initaliser == false){
            db.initialisation();
        }
    }

    public void btnCreerPersonnageOnClick(View v){

        Intent creationIntent = new Intent(MainActivity.this, CreationPersonnage.class);
        startActivity(creationIntent);
    }

    public void btnLirePerso(View v){

        Intent lireIntent = new Intent(MainActivity.this, LirePersonnage.class);
        startActivity(lireIntent);
    }

    public void btnSupprimerOnClick(View v){

        Intent SuppressionIntent = new Intent(MainActivity.this, SupprimerPersonnage.class);
        startActivity(SuppressionIntent);
    }

    public void btnLirePNJ(View v){

        Intent lirePNJ = new Intent(MainActivity.this, LirePNJ.class);
        startActivity(lirePNJ);
    }

    public void btnLireMonstre(View v){

        Intent lireMonster = new Intent(MainActivity.this, LireMonster.class);
        startActivity(lireMonster);
    }
}
