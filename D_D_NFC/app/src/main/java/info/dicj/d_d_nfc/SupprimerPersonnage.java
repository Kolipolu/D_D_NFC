package info.dicj.d_d_nfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class SupprimerPersonnage extends Activity implements INFCEcriture {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supprimer_perssonnage);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    public void btnSupprimerPersoOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Suppression");
        builder.setMessage("Êtes-vous sur de vouloir supprimer ce personnage?");

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                enableForegroundDispatchSystem();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "Suppression en cours...", Toast.LENGTH_LONG).show();


            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            NdefMessage ndefMessage = createNdefMessage("X");

            writeNdefMessage(tag, ndefMessage);


        }
    }

    @Override
    public void disableForegroundDispatchSystem() {

        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void enableForegroundDispatchSystem() {

        Intent intent = new Intent(SupprimerPersonnage.this, SupprimerPersonnage.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(SupprimerPersonnage.this, pendingIntent, intentFilters, null);

        Toast.makeText(this, "Veuillez poser le téléphone sur la puce!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Tag ne peut êre null", Toast.LENGTH_LONG).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if(ndef == null){
                formatTag(tag, ndefMessage);
            }else{
                ndef.connect();

                if(!ndef.isWritable()){
                    Toast.makeText(this, "Le tag ne peut être écrit", Toast.LENGTH_LONG).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Personnage supprimé!", Toast.LENGTH_LONG).show();

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
}