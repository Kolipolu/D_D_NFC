package info.dicj.d_d_nfc;

import android.nfc.NdefMessage;
import android.nfc.Tag;

/**
 * Created by utilisateur on 2017-02-06.
 */
public interface INFC {
    void formatTag(Tag tag, NdefMessage ndefMessage);
    void disableForegroundDispatchSystem();
    void enableForegroundDispatchSystem();
}
