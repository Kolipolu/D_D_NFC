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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;


public class LirePersonnage extends Activity implements INFC, INFCLecture {

    NfcAdapter nfcAdapter;
    Boolean lecture = true;

    TextView txtNom, txtTaille, txtPoids, txtSexe;
    EditText edTxtAge;

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (lecture == true){

            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(parcelables != null && parcelables.length > 0){

                readTextFromMessage((NdefMessage)parcelables[0]);
                lecture = false;
            }else{
                Toast.makeText(this, "The NFC is empty.", Toast.LENGTH_LONG).show();
            }
        }else{

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lire_personnage);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        txtNom = (TextView)findViewById(R.id.txtNomPerso);
        edTxtAge = (EditText)findViewById(R.id.txtAgePerso);
        txtTaille = (TextView)findViewById(R.id.txtTaillePerso);
        txtPoids = (TextView)findViewById(R.id.txtPoidsPerso);
        txtSexe = (TextView)findViewById(R.id.txtSexePerso);
    }

    @Override
    public void disableForegroundDispatchSystem() {

        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, LirePersonnage.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

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
                Toast.makeText(this, "tag not formatable", Toast.LENGTH_LONG).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag not writen", Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Log.e("FormatTag", e.getMessage());
        }
    }



    @Override
    public void readTextFromMessage(NdefMessage ndefMessage) {



        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            try{
                String[] tblTagContent = tagContent.split(",");


                txtNom.setText("Nom : " + tblTagContent[NumTag.Nom.ordinal()]);
                edTxtAge.setText(tblTagContent[NumTag.Age.ordinal()]);
                String taille = tblTagContent[NumTag.Taille.ordinal()];
                txtTaille.setText("Taille : " + taille.charAt(0) + "'" + taille.charAt(1) + "''");
                txtPoids.setText("Poids : " + tblTagContent[NumTag.Poids.ordinal()]);
                txtSexe.setText("Sexe : " + tblTagContent[NumTag.Sexe.ordinal()]);
            }catch(Exception e){
                Toast.makeText(this, "Il n'y a pas de personnage dans la puce.", Toast.LENGTH_LONG).show();

            }

        }else{
            Toast.makeText(this, "The NFC is empty.", Toast.LENGTH_LONG).show();
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

    public enum NumTag{
        Nom,
        Age,
        Taille,
        Poids,
        Sexe;
    }
}
