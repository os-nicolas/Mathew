package cube.d.n.commoncore;

import android.util.Log;

/**
 * Created by Colin_000 on 7/13/2015.
 */
public abstract class Nextmanager {

        public boolean hasNext() {
            return false;
        }

        public abstract void next();

        public abstract void finish();

    public boolean hasLast(){return false;}

    public abstract void last();
}
