package info.dicj.d_d_nfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnCreerPersonnageOnClick(View v){

        Intent creationIntent = new Intent(MainActivity.this, CreationPersonnage.class);
        startActivity(creationIntent);
    }

    public void btnSupprimerOnClick(View v){

        Intent SuppressionIntent = new Intent(MainActivity.this, SupprimerPersonnage.class);
        startActivity(SuppressionIntent);
    }
}
