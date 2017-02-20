package info.dicj.d_d_nfc;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class CreationPersonnage extends Activity implements INFCEcriture, INFC{

    NfcAdapter nfcAdapter;
    Boolean ecriture = false;
    String Sexe = "";
    ToggleButton tglSexe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_personnage);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(ecriture == true){
            if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
                Toast.makeText(this, "Écriture en cours...", Toast.LENGTH_LONG).show();


                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                if(tglSexe.isChecked()) {
                    Sexe = "M";
                }else {
                    Sexe = "F";
                }

                NdefMessage ndefMessage = createNdefMessage(creationContentTag());

                writeNdefMessage(tag, ndefMessage);
            }

            ecriture = false;
        }
    }

    @Override
    public void disableForegroundDispatchSystem() {

        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void enableForegroundDispatchSystem() {

        Intent intent = new Intent(CreationPersonnage.this, CreationPersonnage.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(CreationPersonnage.this, pendingIntent, intentFilters, null);
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
    public void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try{
            if(tag == null){
                Toast.makeText(this, "Tag cannot be null", Toast.LENGTH_LONG).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if(ndef == null){
                formatTag(tag, ndefMessage);
            }else{
                ndef.connect();

                if(!ndef.isWritable()){
                    Toast.makeText(this, "Tag not writable", Toast.LENGTH_LONG).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Personnage sauvegardé!", Toast.LENGTH_LONG).show();

            }


        }catch (Exception e){
            Log.e("WrireTag", e.getMessage());
        }
    }

    @Override
    public NdefRecord createTextRecord(String content) {
        try{
            byte[] language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte)(languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        }catch (UnsupportedEncodingException e){
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    @Override
    public NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ ndefRecord });

        return ndefMessage;
    }


    public void btnSavePersoOnClick(View v) {
        ecriture = true;
        Toast.makeText(this, "Enregistrer le personnage sur la puce!", Toast.LENGTH_LONG).show();
    }

    private String creationContentTag(){
        String content;
        EditText edTxtNom, edTxtAge, edTxtTaille, edTxtPoids, edTxtForce, edTxtIntelligence, edTxtPV, edTxtDex, edTxtSag, edTxtCon, edTxtCha;
        EditText edTxtAttN1, edTxtAttA1, edTxtAttD1, edTxtAttM1,edTxtAttN2, edTxtAttA2, edTxtAttD2, edTxtAttM2,edTxtAttN3, edTxtAttA3, edTxtAttD3, edTxtAttM3,edTxtAttN4, edTxtAttA4, edTxtAttD4, edTxtAttM4, edTxtAttN5, edTxtAttA5, edTxtAttD5, edTxtAttM5;
        TextView txtRaceClasse;

        edTxtNom = (EditText)findViewById(R.id.edTextNom);
        content = edTxtNom.getText() + ",";
        edTxtAge = (EditText)findViewById(R.id.edTxtAge);
        content += edTxtAge.getText() + ",";
        edTxtTaille = (EditText)findViewById(R.id.edTxtTaille);
        content += edTxtTaille.getText() + ",";
        edTxtPoids = (EditText)findViewById(R.id.edTxtPoids);
        content += edTxtPoids.getText() + ",";
        txtRaceClasse = (TextView)findViewById(R.id.edTxtRaceClasse);
        content += txtRaceClasse.getText() + ",";
        tglSexe = (ToggleButton)findViewById(R.id.tglSexe);
        content += tglSexe.getText() + ",";
        edTxtForce = (EditText)findViewById(R.id.edTxtForce);
        content += edTxtForce.getText() + ",";
        edTxtIntelligence = (EditText)findViewById(R.id.edTxtIntelligence);
        content += edTxtIntelligence.getText() + ",";
        edTxtPV = (EditText)findViewById(R.id.edTxtPV);
        content += edTxtPV.getText() + ",";
        edTxtDex = (EditText)findViewById(R.id.edTxtDexterite);
        content += edTxtDex.getText() + ",";
        edTxtSag = (EditText)findViewById(R.id.edTxtSagesse);
        content += edTxtSag.getText() + ",";
        edTxtCon = (EditText)findViewById(R.id.edTxtConstitution);
        content += edTxtCon.getText() + ",";
        edTxtCha = (EditText)findViewById(R.id.edTxtCharisme);
        content += edTxtCha.getText()+ ",";

        edTxtAttN1 = (EditText)findViewById(R.id.edTxtAttNom1);
        edTxtAttN2 = (EditText)findViewById(R.id.edTxtAttNom2);
        edTxtAttN3 = (EditText)findViewById(R.id.edTxtAttNom3);
        edTxtAttN4 = (EditText)findViewById(R.id.edTxtAttNom4);
        edTxtAttN5 = (EditText)findViewById(R.id.edTxtAttNom5);

        edTxtAttA1 = (EditText)findViewById(R.id.edTxtAttAtt1);
        edTxtAttA2 = (EditText)findViewById(R.id.edTxtAttAtt2);
        edTxtAttA3 = (EditText)findViewById(R.id.edTxtAttAtt3);
        edTxtAttA4 = (EditText)findViewById(R.id.edTxtAttAtt4);
        edTxtAttA5 = (EditText)findViewById(R.id.edTxtAttAtt5);

        edTxtAttD1 = (EditText)findViewById(R.id.edTxtAttDom1);
        edTxtAttD2 = (EditText)findViewById(R.id.edTxtAttDom2);
        edTxtAttD3 = (EditText)findViewById(R.id.edTxtAttDom3);
        edTxtAttD4 = (EditText)findViewById(R.id.edTxtAttDom4);
        edTxtAttD5 = (EditText)findViewById(R.id.edTxtAttDom5);

        edTxtAttM1 = (EditText)findViewById(R.id.edTxtAttMun1);
        edTxtAttM2 = (EditText)findViewById(R.id.edTxtAttMun2);
        edTxtAttM3 = (EditText)findViewById(R.id.edTxtAttMun3);
        edTxtAttM4 = (EditText)findViewById(R.id.edTxtAttMun4);
        edTxtAttM5 = (EditText)findViewById(R.id.edTxtAttMun5);


        return content;
    }
}
