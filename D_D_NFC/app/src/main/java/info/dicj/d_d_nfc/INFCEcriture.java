package info.dicj.d_d_nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;

/**
 * Created by utilisateur on 2017-01-31.
 */
public interface INFCEcriture {
    void disableForegroundDispatchSystem();
    void enableForegroundDispatchSystem();
    void formatTag(Tag tag, NdefMessage ndefMessage);
    void writeNdefMessage(Tag tag, NdefMessage ndefMessage);
    NdefRecord createTextRecord(String content);
    NdefMessage createNdefMessage(String content);
}
