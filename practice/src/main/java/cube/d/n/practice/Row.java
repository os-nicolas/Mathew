package cube.d.n.practice;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Colin_000 on 7/4/2015.
 */
public interface Row {

    public abstract View makeView(Context context,ViewGroup parent,int pos,int size);
}
