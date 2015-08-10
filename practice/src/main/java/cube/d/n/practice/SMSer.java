package cube.d.n.practice;

import android.telephony.SmsManager;

/**
 * Created by Colin_000 on 8/9/2015.
 */
public class SMSer {

    public static void send(String target, String text){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(target,null,text,null,null);
    }
}
