package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Colin_000 on 7/1/2015.
 */
public class MainRow {


    private Class<?> targetActivity;
    public String title;
    public String subtitle;

    public MainRow(String title, String subtitle, Class<?> targetActivity){
        this(title,subtitle);
        this.targetActivity = targetActivity;

    }

    // this is for sub classes that override go
    protected MainRow(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public void go(Activity that) {
        Intent intent = new Intent(that,targetActivity);
        that.startActivity(intent);
    }
}
