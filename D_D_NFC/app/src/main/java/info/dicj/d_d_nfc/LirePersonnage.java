package info.dicj.d_d_nfc;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;


public class LirePersonnage extends Activity implements INFC, INFCLecture {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lire_personnage);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public void disableForegroundDispatchSystem() {

    }

    @Override
    public void enableForegroundDispatchSystem() {

    }

    @Override
    public void formatTag(Tag tag, NdefMessage ndefMessage) {

    }



    @Override
    public void readTextFromMessage(NdefMessage ndefMessage) {

    }

    @Override
    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        return null;
    }
}
