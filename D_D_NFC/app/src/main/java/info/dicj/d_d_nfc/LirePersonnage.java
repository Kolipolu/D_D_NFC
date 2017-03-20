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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;


public class LirePersonnage extends Activity implements INFC, INFCLecture {

    NfcAdapter nfcAdapter;
    Boolean lecture = true;

    TextView txtNom, txtTaille, txtPoids, txtSexe, txtRaceClasse, txtFor, txtInt, txtPV, txtDex, txtSag, txtCon, txtCha;
    TextView txtNomAtt1, txtNomAtt2, txtNomAtt3, txtNomAtt4, txtNomAtt5;
    TextView txtAttAttPerso1, txtAttAttPerso2, txtAttAttPerso3, txtAttAttPerso4, txtAttAttPerso5;
    TextView txtDomAtt1, txtDomAtt2, txtDomAtt3, txtDomAtt4, txtDomAtt5;
    TextView txtMunAtt1, txtMunAtt2, txtMunAtt3, txtMunAtt4, txtMunAtt5;
    TextView edTxtAge, txtLvl;
    String[] tblTagContent;

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

    public void btnModifier_onClick(View v){
        Intent intent = new Intent(LirePersonnage.this, CreationPersonnage.class);
        intent.putExtra("tableContent", tblTagContent);
        startActivity(intent);
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
        edTxtAge = (TextView) findViewById(R.id.txtAgePerso);
        txtTaille = (TextView)findViewById(R.id.txtTaillePerso);
        txtPoids = (TextView)findViewById(R.id.txtPoidsPerso);
        txtSexe = (TextView)findViewById(R.id.txtSexePerso);
        txtRaceClasse = (TextView)findViewById(R.id.txtRaceClassePerso);
        txtFor = (TextView)findViewById(R.id.txtForcePerso);
        txtInt = (TextView)findViewById(R.id.txtIntPerso);
        txtPV = (TextView)findViewById(R.id.txtPVPerso);
        txtDex = (TextView)findViewById(R.id.txtDexPerso);
        txtSag = (TextView)findViewById(R.id.txtSagPerso);
        txtCon = (TextView)findViewById(R.id.txtConPerso);
        txtCha = (TextView)findViewById(R.id.txtChaPerso);

        txtNomAtt1 = (TextView)findViewById(R.id.txtNomAttPerso1);
        txtNomAtt2 = (TextView)findViewById(R.id.txtNomAttPerso2);
        txtNomAtt3 = (TextView)findViewById(R.id.txtNomAttPerso3);
        txtNomAtt4 = (TextView)findViewById(R.id.txtNomAttPerso4);
        txtNomAtt5 = (TextView)findViewById(R.id.txtNomAttPerso5);

        txtAttAttPerso1 = (TextView)findViewById(R.id.txtAttAttPerso1);
        txtAttAttPerso2 = (TextView)findViewById(R.id.txtAttAttPerso2);
        txtAttAttPerso3 = (TextView)findViewById(R.id.txtAttAttPerso3);
        txtAttAttPerso4 = (TextView)findViewById(R.id.txtAttAttPerso4);
        txtAttAttPerso5 = (TextView)findViewById(R.id.txtAttAttPerso5);

        txtDomAtt1 = (TextView)findViewById(R.id.txtDomAttPerso1);
        txtDomAtt2 = (TextView)findViewById(R.id.txtDomAttPerso2);
        txtDomAtt3 = (TextView)findViewById(R.id.txtDomAttPerso3);
        txtDomAtt4 = (TextView)findViewById(R.id.txtDomAttPerso4);
        txtDomAtt5 = (TextView)findViewById(R.id.txtDomAttPerso5);

        txtMunAtt1 = (TextView)findViewById(R.id.txtMunAttPerso1);
        txtMunAtt2 = (TextView)findViewById(R.id.txtMunAttPerso2);
        txtMunAtt3 = (TextView)findViewById(R.id.txtMunAttPerso3);
        txtMunAtt4 = (TextView)findViewById(R.id.txtMunAttPerso4);
        txtMunAtt5 = (TextView)findViewById(R.id.txtMunAttPerso5);

        txtLvl = (TextView)findViewById(R.id.txtLvlPerso);
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
    public void readTextFromMessage(NdefMessage ndefMessage) {

        Boolean persoTrouver = false;

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            try{
                tblTagContent = tagContent.split(",");

                txtNom.setText("Nom : " + tblTagContent[NumTag.Nom.ordinal()]);
                persoTrouver = true;
                edTxtAge.setText(" " + tblTagContent[NumTag.Age.ordinal()]);
                String taille = tblTagContent[NumTag.Taille.ordinal()];
                txtTaille.setText("Taille : " + taille.charAt(0) + "'" + taille.charAt(1) + "''");
                txtPoids.setText("Poids : " + tblTagContent[NumTag.Poids.ordinal()]);
                txtSexe.setText("Sexe : " + tblTagContent[NumTag.Sexe.ordinal()]);
                txtRaceClasse.setText("Race/Classe : " + tblTagContent[NumTag.RaceClasse.ordinal()]);
                txtFor.setText("For : " + tblTagContent[NumTag.For.ordinal()]);
                txtInt.setText("Int : " + tblTagContent[NumTag.Int.ordinal()]);
                txtPV.setText("Int : " + tblTagContent[NumTag.PV.ordinal()]);
                txtDex.setText("Dex : " + tblTagContent[NumTag.Dex.ordinal()]);
                txtSag.setText("Sag : " + tblTagContent[NumTag.Sag.ordinal()]);
                txtCon.setText("Con : " + tblTagContent[NumTag.Con.ordinal()]);
                txtCha.setText("Cha : " + tblTagContent[NumTag.Cha.ordinal()]);

                txtNomAtt1.setText(tblTagContent[NumTag.NomAtt1.ordinal()]);
                txtNomAtt2.setText(tblTagContent[NumTag.NomAtt2.ordinal()]);
                txtNomAtt3.setText(tblTagContent[NumTag.NomAtt3.ordinal()]);
                txtNomAtt4.setText(tblTagContent[NumTag.NomAtt4.ordinal()]);
                txtNomAtt5.setText(tblTagContent[NumTag.NomAtt5.ordinal()]);

                txtAttAttPerso1.setText(tblTagContent[NumTag.Att1.ordinal()]);
                txtAttAttPerso2.setText(tblTagContent[NumTag.Att2.ordinal()]);
                txtAttAttPerso3.setText(tblTagContent[NumTag.Att3.ordinal()]);
                txtAttAttPerso4.setText(tblTagContent[NumTag.Att4.ordinal()]);
                txtAttAttPerso5.setText(tblTagContent[NumTag.Att5.ordinal()]);

                txtDomAtt1.setText(tblTagContent[NumTag.DomAtt1.ordinal()]);
                txtDomAtt2.setText(tblTagContent[NumTag.DomAtt2.ordinal()]);
                txtDomAtt3.setText(tblTagContent[NumTag.DomAtt3.ordinal()]);
                txtDomAtt4.setText(tblTagContent[NumTag.DomAtt4.ordinal()]);
                txtDomAtt5.setText(tblTagContent[NumTag.DomAtt5.ordinal()]);

                txtMunAtt1.setText(tblTagContent[NumTag.MunAtt1.ordinal()]);
                txtMunAtt2.setText(tblTagContent[NumTag.MunAtt2.ordinal()]);
                txtMunAtt3.setText(tblTagContent[NumTag.MunAtt3.ordinal()]);
                txtMunAtt4.setText(tblTagContent[NumTag.MunAtt4.ordinal()]);
                txtMunAtt5.setText(tblTagContent[NumTag.MunAtt5.ordinal()]);

                txtLvl.setText("Lvl : " + tblTagContent[NumTag.Lvl.ordinal()]);

            }catch(Exception e){
                if(persoTrouver == true){
                    Toast.makeText(this, "Il n'y a pas de personnage dans la puce.", Toast.LENGTH_LONG).show();
                }
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
