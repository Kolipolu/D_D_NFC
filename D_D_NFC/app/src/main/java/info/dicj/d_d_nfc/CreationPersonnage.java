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
    EditText edTxtNom, edTxtAge, edTxtTaille, edTxtPoids, edTxtForce, edTxtIntelligence, edTxtPV, edTxtDex, edTxtSag, edTxtCon, edTxtCha, edTxtLvl;
    EditText edTxtAttN1, edTxtAttA1, edTxtAttD1, edTxtAttM1,edTxtAttN2, edTxtAttA2, edTxtAttD2, edTxtAttM2,edTxtAttN3, edTxtAttA3, edTxtAttD3, edTxtAttM3,edTxtAttN4, edTxtAttA4, edTxtAttD4, edTxtAttM4, edTxtAttN5, edTxtAttA5, edTxtAttD5, edTxtAttM5;
    TextView txtRaceClasse;
    TextView txtTitle;
    Intent intentLire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_personnage);

        initialiserVar();
        intentLire = getIntent();

        try{
            if(intentLire.hasExtra("tableContent")){
                txtTitle.setText("Modifier le personnage");
                modifPerso(intentLire.getStringArrayExtra("tableContent"));
            }
        }catch(Exception e){}

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tglSexe = (ToggleButton)findViewById(R.id.tglSexe);
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
                Toast.makeText(this, "Écriture en cours...", Toast.LENGTH_SHORT).show();


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
                Toast.makeText(this, "La puce ne peut être écrite.", Toast.LENGTH_LONG).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "La puce n'a pas été écrit.", Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Log.e("FormatTag", e.getMessage());
        }
    }

    @Override
    public void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try{
            if(tag == null){
                Toast.makeText(this, "La puce ne peut être nulle.", Toast.LENGTH_LONG).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if(ndef == null){
                formatTag(tag, ndefMessage);
            }else{
                ndef.connect();

                if(!ndef.isWritable()){
                    Toast.makeText(this, "La puce ne peut être écrite.", Toast.LENGTH_LONG).show();
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

    private void initialiserVar(){
        edTxtNom = (EditText)findViewById(R.id.edTextNom);
        edTxtAge = (EditText)findViewById(R.id.edTxtAge);
        edTxtTaille = (EditText)findViewById(R.id.edTxtTaille);
        edTxtPoids = (EditText)findViewById(R.id.edTxtPoids);
        txtRaceClasse = (TextView)findViewById(R.id.edTxtRaceClasse);
        edTxtForce = (EditText)findViewById(R.id.edTxtForce);
        edTxtIntelligence = (EditText)findViewById(R.id.edTxtIntelligence);
        edTxtPV = (EditText)findViewById(R.id.edTxtPV);
        edTxtDex = (EditText)findViewById(R.id.edTxtDexterite);
        edTxtSag = (EditText)findViewById(R.id.edTxtSagesse);
        edTxtCon = (EditText)findViewById(R.id.edTxtConstitution);
        edTxtCha = (EditText)findViewById(R.id.edTxtCharisme);


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

        txtTitle = (TextView)findViewById(R.id.txtTitle);
        edTxtLvl = (EditText)findViewById(R.id.txtLvl);
    }

    private void modifPerso(String[] tblContent){
        edTxtNom.setText(tblContent[NumTag.Nom.ordinal()]);
        edTxtAge.setText(tblContent[NumTag.Age.ordinal()]);
        edTxtTaille.setText(tblContent[NumTag.Taille.ordinal()]);
        edTxtPoids.setText(tblContent[NumTag.Poids.ordinal()]);
        tglSexe.setText(tblContent[NumTag.Sexe.ordinal()]);
        txtRaceClasse.setText(tblContent[NumTag.RaceClasse.ordinal()]);
        edTxtForce.setText(tblContent[NumTag.For.ordinal()]);
        edTxtIntelligence.setText(tblContent[NumTag.Int.ordinal()]);
        edTxtPV.setText(tblContent[NumTag.PV.ordinal()]);
        edTxtDex.setText(tblContent[NumTag.Dex.ordinal()]);
        edTxtSag.setText(tblContent[NumTag.Sag.ordinal()]);
        edTxtCon.setText(tblContent[NumTag.Con.ordinal()]);
        edTxtCha.setText(tblContent[NumTag.Cha.ordinal()]);

        edTxtAttN1.setText(tblContent[NumTag.NomAtt1.ordinal()]);
        edTxtAttN2.setText(tblContent[NumTag.NomAtt2.ordinal()]);
        edTxtAttN3.setText(tblContent[NumTag.NomAtt3.ordinal()]);
        edTxtAttN4.setText(tblContent[NumTag.NomAtt4.ordinal()]);
        edTxtAttN5.setText(tblContent[NumTag.NomAtt5.ordinal()]);

        edTxtAttA1.setText(tblContent[NumTag.Att1.ordinal()]);
        edTxtAttA2.setText(tblContent[NumTag.Att2.ordinal()]);
        edTxtAttA3.setText(tblContent[NumTag.Att3.ordinal()]);
        edTxtAttA4.setText(tblContent[NumTag.Att4.ordinal()]);
        edTxtAttA5.setText(tblContent[NumTag.Att5.ordinal()]);

        edTxtAttD1.setText(tblContent[NumTag.DomAtt1.ordinal()]);
        edTxtAttD2.setText(tblContent[NumTag.DomAtt2.ordinal()]);
        edTxtAttD3.setText(tblContent[NumTag.DomAtt3.ordinal()]);
        edTxtAttD4.setText(tblContent[NumTag.DomAtt4.ordinal()]);
        edTxtAttD5.setText(tblContent[NumTag.DomAtt5.ordinal()]);

        edTxtAttM1.setText(tblContent[NumTag.MunAtt1.ordinal()]);
        edTxtAttM2.setText(tblContent[NumTag.MunAtt2.ordinal()]);
        edTxtAttM3.setText(tblContent[NumTag.MunAtt3.ordinal()]);
        edTxtAttM4.setText(tblContent[NumTag.MunAtt4.ordinal()]);
        edTxtAttM5.setText(tblContent[NumTag.MunAtt5.ordinal()]);

        edTxtLvl.setText(tblContent[NumTag.Lvl.ordinal()]);
    }

    private String creationContentTag(){
        String content;

        content = edTxtNom.getText() + ",";
        content += edTxtAge.getText() + ",";
        content += edTxtTaille.getText() + ",";
        content += edTxtPoids.getText() + ",";
        content += tglSexe.getText() + ",";
        content += txtRaceClasse.getText() + ",";
        content += edTxtForce.getText() + ",";
        content += edTxtIntelligence.getText() + ",";
        content += edTxtPV.getText() + ",";
        content += edTxtDex.getText() + ",";
        content += edTxtSag.getText() + ",";
        content += edTxtCon.getText() + ",";
        content += edTxtCha.getText()+ ",";

        content += edTxtAttN1.getText() + ",";
        content += edTxtAttN2.getText() + ",";
        content += edTxtAttN3.getText() + ",";
        content += edTxtAttN4.getText() + ",";
        content += edTxtAttN5.getText() + ",";

        content += edTxtAttA1.getText() + ",";
        content += edTxtAttA2.getText() + ",";
        content += edTxtAttA3.getText() + ",";
        content += edTxtAttA4.getText() + ",";
        content += edTxtAttA5.getText() + ",";

        content += edTxtAttD1.getText() + ",";
        content += edTxtAttD2.getText() + ",";
        content += edTxtAttD3.getText() + ",";
        content += edTxtAttD4.getText() + ",";
        content += edTxtAttD5.getText() + ",";

        content += edTxtAttM1.getText() + ",";
        content += edTxtAttM2.getText() + ",";
        content += edTxtAttM3.getText() + ",";
        content += edTxtAttM4.getText() + ",";
        content += edTxtAttM5.getText() + ",";

        content += edTxtLvl.getText();


        return content;
    }
    private enum NumTag{
        Nom,
        Age,
        Taille,
        Poids,
        Sexe,
        RaceClasse,
        For,
        Int,
        PV,
        Dex,
        Sag,
        Con,
        Cha,
        NomAtt1, NomAtt2, NomAtt3, NomAtt4, NomAtt5,
        Att1, Att2, Att3, Att4, Att5,
        DomAtt1, DomAtt2, DomAtt3, DomAtt4, DomAtt5,
        MunAtt1, MunAtt2, MunAtt3, MunAtt4, MunAtt5,
        Lvl
    }
}
