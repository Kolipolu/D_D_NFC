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


public class LirePNJ extends Activity implements INFC, INFCLecture{

    NfcAdapter nfcAdapter;
    DatabaseHandler db = new DatabaseHandler(this);
    TextView txtPrenom, txtNom, txtAge, txtClasse, txtRace, txtDescrip, txtQuete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lire_pnj);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        db.addPNJ(new PNJ(1,21,"Sheehy","David","Mage","Husky","C'est un homme d'envergure", "Tuer 10 monstres"));
        txtPrenom = (TextView)findViewById(R.id.txtTestPrenom);
        txtNom = (TextView)findViewById(R.id.txtTestNom);
        txtAge = (TextView)findViewById(R.id.txtTestAge);
        txtClasse = (TextView)findViewById(R.id.txtTestClasse);
        txtRace = (TextView)findViewById(R.id.txtTestRace);
        txtDescrip = (TextView)findViewById(R.id.txtTestDescription);
        txtQuete = (TextView)findViewById(R.id.txtTestQuete);
    }

    @Override
    public void disableForegroundDispatchSystem() {

        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, LirePNJ.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

        Toast.makeText(this, "Veuillez lire la puce personnage!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "The NFC is empty.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            try{

                PNJ pnj;

                pnj = db.getPNJ(Integer.parseInt(tagContent));

                txtPrenom.setText(pnj.get_prenom());
                txtNom.setText(pnj.get_nom());
                txtAge.setText(String.valueOf(pnj.get_age()));
                txtClasse.setText(pnj.get_classe());
                txtRace.setText(pnj.get_race());
                txtDescrip.setText(pnj.get_descrip());
                txtQuete.setText(pnj.get_quete());

            }catch(Exception e){

            }

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
