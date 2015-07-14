package cube.d.n.commoncore;

import android.util.Log;

/**
 * Created by Colin_000 on 7/13/2015.
 */
public class Nextmanager {

        public boolean hasNext() {
            return false;
        }

        public void next(){
            Log.e("Nextmanager", "this should not be called");
        }
}
