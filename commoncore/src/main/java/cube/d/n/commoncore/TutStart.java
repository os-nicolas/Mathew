package cube.d.n.commoncore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Colin_000 on 5/18/2015.
 */
public class TutStart extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.tutstart, container, false);




        return rootView;
    }
}


