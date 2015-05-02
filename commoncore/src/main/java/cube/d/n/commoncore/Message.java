package cube.d.n.commoncore;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Colin on 2/24/2015.
 */
public class Message {
    public String[] text;
    long runTime;
    private boolean quited = false;
    private long startedAt =0L;
    private Callable func;

    public Message(String[] text, long runTime){
        this.text = text;
        this.runTime = runTime;
    }

    public Message(String[] text, long runTime,Callable func){
        this(text,runTime);
        this.func = func;
    }

    public void click(){
        if (func!= null) {
            try {
                func.call();
            } catch (Exception e) {
                Log.e("Message.click", e.getStackTrace() + "");
            }
        }
    }

    public void quit() {
        quited = true;
    }

    public boolean isDone() {
        return quited || (hasStarted() && runTime < System.currentTimeMillis() - startedAt);
    }

    public boolean hasStarted(){
        return startedAt !=0;
    }

    public void start() {
        startedAt = System.currentTimeMillis();
    }

    public void db(String message) {
        startedAt = System.currentTimeMillis();
        runTime = 1000L;
        text = new String[]{message};
    }
}
