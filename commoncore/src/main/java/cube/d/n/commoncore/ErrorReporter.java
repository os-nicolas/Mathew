package cube.d.n.commoncore;

import android.util.Log;

/**
 * Created by Colin on 8/15/2015.
 */
public class ErrorReporter implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler old;
    public ErrorReporter(Thread.UncaughtExceptionHandler old){
        this.old = old;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex){
        String res = "THROWABLE:\n\n"+
                Log.getStackTraceString(ex)+"RECENT LOG:\n\n"+ recentLog();
        SES.sendEmail("MathildaApp@gmail.com","crash!",res);
        old.uncaughtException(thread,ex);
    }

    private String recentLog() {
        return "";
    }
}
