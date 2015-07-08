package cube.d.n.commoncore.tuts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cube.d.n.commoncore.R;

/**
 * Created by Colin_000 on 5/18/2015.
 */
public class TutStart extends TutFrag {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.tutstart, container, false);

        updateData(getArguments());

        rootView.findViewById(R.id.ribbon).setBackgroundColor(ribbonColor);

        return rootView;
    }

    @Override
    protected void pstart(View rootView) {
    }
}


