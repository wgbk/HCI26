package tutorials.com.hci;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by khanh-bk on 11/19/2016.
 */
public class IncomingCall extends BroadcastReceiver {
    private Context contextCall;
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            contextCall = context;
            TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            tmgr.listen(PhoneListener, PhoneListener.LISTEN_CALL_STATE);
        }catch (Exception e){

        }
    }
    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == 1) {
                String msg = "New Phone Call Event. Incomming Number : "+incomingNumber;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(contextCall, msg, duration);
                toast.show();
            }
        }
    }
}
