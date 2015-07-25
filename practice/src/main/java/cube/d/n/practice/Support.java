package cube.d.n.practice;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Support extends FullAct  {

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            Log.d("onServiceDisconnected","mService is now null");
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            Log.d("onServiceConnected","return null");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_support);


        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, this.BIND_AUTO_CREATE);







        TextView tv = (TextView)findViewById(R.id.problem_title_text);
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        tv.setTypeface(dj);
        tv.setText("Support");


        ListView listView = (ListView) findViewById(R.id.problem_listView);

        final TwoLineArrayAdaptor adapter = new TwoLineArrayAdaptor(this,getProblems(),listView);

        listView.setAdapter(adapter);
        final Activity that = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> unused,View v, int position,long arg3){
                if (adapter.getRow(position) instanceof DonateRow) {
                    ((DonateRow) adapter.getRow(position)).go(mService, that);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    final String token = jo.getString("purchaseToken");
                    Log.d("","You have bought the " + sku);

                    Thread thread = new Thread(){
                        @Override
                        public void run(){
                            try {
                                int response = mService.consumePurchase(3, getPackageName(), token);
                                Mathilda.getMathilda().supporter();
                            }catch (RemoteException re){
                                Log.e("consumePurchase",re.toString());
                            }
                        }

                    };
                    thread.start();


                }
                catch (JSONException e) {
                    Log.d("","Failed to parse purchase data.");
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        if (mService != null) {
            unbindService(mServiceConn);
        }
        super.onDestroy();
    }

    private ArrayList<Row> getProblems() {
        ArrayList<Row> res = new ArrayList<>();
        res.add(new AboutRow("Donate to help support Mathilda continued development. "));
        res.add(new DonateRow("One Dollar","$1"));
        res.add(new DonateRow("Five Dollars","$5"));
        res.add(new DonateRow("Ten Dollars","$10"));
        res.add(new DonateRow("Twenty Dollars","$20"));
        res.add(new DonateRow("Fifty Dollars","$50"));
        return res;
    }
}
