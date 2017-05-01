package info.dicj.d_d_nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/**
 * Created by utilisateur on 2017-04-10.
 */
public class LireMonster extends Activity implements INFC, INFCLecture{


    NfcAdapter nfcAdapter;
    TextView txtHealth, txtAttack, txtDefence, txtExp, txtGold, txtName, txtDescrip;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lire_monster);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        txtName = (TextView)findViewById(R.id.txtNameM);
        txtHealth = (TextView)findViewById(R.id.txtHealthM);
        txtAttack = (TextView)findViewById(R.id.txtAttackM);
        txtDefence = (TextView)findViewById(R.id.txtDefenseM);
        txtGold = (TextView)findViewById(R.id.txtGoldM);
        txtExp = (TextView)findViewById(R.id.txtExpM);
        txtDescrip = (TextView)findViewById(R.id.txtDescripM);
    }

    public void disableForegroundDispatchSystem() {

        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, LireMonster.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

        Toast.makeText(this, "Veuillez lire la puce monstre!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void formatTag(Tag tag, NdefMessage ndefMessage) {

        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable == null){
                Toast.makeText(this, "La puce ne peut être écrite.", Toast.LENGTH_LONG).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "La puce n'a pas été écrite.", Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Log.e("FormatTag", e.getMessage());
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if(parcelables != null && parcelables.length > 0){

            readTextFromMessage((NdefMessage)parcelables[0]);
        }else{
            Toast.makeText(this, "La puce est vide.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            try{
                Monster monster;

                monster = db.getMonster(Integer.parseInt(tagContent));

                txtName.setText("Nom : " + monster.get_name());
                txtHealth.setText("HP : " + monster.get_health());
                txtAttack.setText("Points d'attaque : " + monster.get_attack());
                txtDefence.setText("Points de defense : " + monster.get_def());
                txtExp.setText("Exp. : " + monster.get_exp());
                txtGold.setText("Pièces d'or : " + monster.get_gold());
                txtDescrip.setText("Description : " + monster.get_descrip());
            }catch(Exception e){}

        }else{
            Toast.makeText(this, "La puce est vide.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize +1,
                    payload.length - languageSize - 1, textEncoding);
        }catch (UnsupportedEncodingException e){
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }


    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }
}
