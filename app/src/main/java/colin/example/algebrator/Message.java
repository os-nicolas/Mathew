package colin.example.algebrator;

import java.util.ArrayList;

/**
 * Created by Colin on 2/24/2015.
 */
public class Message {
    public String[] text;
    long runTime;
    private boolean quited = false;
    private long startedAt =0L;

    public Message(String[] text, long runTime){
        this.text = text;
        this.runTime = runTime;
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
