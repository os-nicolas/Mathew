package cube.d.n.commoncore;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Colin on 8/15/2015.
 */
public class ErrorReporter implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler old;
    public ErrorReporter(Thread.UncaughtExceptionHandler old){
        this.old = old;
    }
    private static Queue<String> que = new LinkedList<String>();
    private static final int MAX_SIZE = 100;

    @Override
    public void uncaughtException(Thread thread, final Throwable ex){
        final String res = getDeviceName() + "\n"+BaseApp.getApp().about() + "\n\nTHROWABLE:\n\n"+ Log.getStackTraceString(ex)+"\n\nRECENT LOG:\n\n"+ recentLog();
        Log.e("got a err",res);
        Thread th = new Thread( new Runnable() {
            @Override
            public void run() {
                //SES.sendEmail("MathildaApp@gmail.com","crash!",res);
                Log.e("sent Email","done did it!");
            }
        });
        th.start();
        old.uncaughtException(thread,ex);
    }

    private static String recentLog() {
        String res = "";

        for (String s: que ){
            res +=s +'\n';
        }
        return res;
    }

    public  static void log(String tag,String message){
        Log.i(tag, message);
        que.add(tag + " : "+ message);
        if (que.size() > MAX_SIZE){
            que.remove(0);
        }
    }

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String androidVersion = Build.VERSION.SDK_INT + "";
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            // make sure "HTC" is fully capitalized.
            return "HTC " + model;
        }
        return "MANUFACTUER: "+capitalize(manufacturer) + "\n" + "MODEL: "  + model + "\n"+"PRODUCT: "+ product + "\n"+ "SDK_INT: "+ androidVersion ;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
}
