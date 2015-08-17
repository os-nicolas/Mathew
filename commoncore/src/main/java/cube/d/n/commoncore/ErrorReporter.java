package cube.d.n.commoncore;

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
        final String res = "THROWABLE:\n\n"+ Log.getStackTraceString(ex)+"RECENT LOG:\n\n"+ recentLog();
        Log.e("got a err",res);
        Thread th = new Thread( new Runnable() {
            @Override
            public void run() {
                SES.sendEmail("MathildaApp@gmail.com","crash!",res);
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
}
