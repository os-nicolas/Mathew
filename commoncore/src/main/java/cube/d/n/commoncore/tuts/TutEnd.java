package cube.d.n.commoncore.tuts;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import cube.d.n.commoncore.FadeInTextView;
import cube.d.n.commoncore.R;

/**
 * Created by Colin_000 on 5/5/2015.
 */
public class TutEnd extends TutFrag {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.tutend, container, false);

        rootView.findViewById(R.id.ribbon).setBackgroundColor(ribbonColor);

        return rootView;
    }

    @Override
    protected void pstart(View rootView) {
    }
}
