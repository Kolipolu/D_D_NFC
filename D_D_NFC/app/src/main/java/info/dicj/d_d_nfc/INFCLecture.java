package info.dicj.d_d_nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

/**
 * Created by utilisateur on 2017-01-31.
 */
public interface INFCLecture {
    void readTextFromMessage(NdefMessage ndefMessage);
    String getTextFromNdefRecord(NdefRecord ndefRecord);
}
