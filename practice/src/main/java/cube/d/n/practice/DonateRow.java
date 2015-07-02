package cube.d.n.practice;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

/**
 * Created by Colin_000 on 7/1/2015.
 */
public class DonateRow extends ProblemRow {

    public  final String sku="donate_one";

    public DonateRow(String name, String circleText) {
        super(name, circleText);
    }


    public void go(IInAppBillingService mService,Activity that) {

        String type="inapp";
        String developerPayload="";
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3,Mathilda.getMathilda().getPackageName(),sku,type,developerPayload);

            Log.d("RESPONSE_CODE", ""+buyIntentBundle.getInt("RESPONSE_CODE"));

            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

            that.startIntentSenderForResult(pendingIntent.getIntentSender(),
                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
