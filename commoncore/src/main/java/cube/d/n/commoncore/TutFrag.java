package cube.d.n.commoncore;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutFrag extends Fragment{

    public  String title;
    public  String body;
    //android.resource://com.pac.myapp/raw/master
    public  String videoLocation;

    public static TutFrag make(String title,String body,String videoLocation){
        TutFrag result = new TutFrag();
        Bundle args = new Bundle();
        args.putString("TITLE",title);
        args.putString("BODY",body);
        args.putString("VIDEO_LOCATION",videoLocation);
        result.setArguments(args);
        Log.i("make","set arguments " +result.hashCode() + " args "+ result.getArguments());
        return result;
    }


    public void updateData(Bundle args){
        this.title = args.getString("TITLE");
        this.body = args.getString("BODY");
        this.videoLocation = args.getString("VIDEO_LOCATION");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.tutscreen, container, false);


        Log.i("make","got arguments " +hashCode() +" args "+getArguments());
        updateData(getArguments());

        ((TextView) rootView.findViewById(R.id.tut_title)).setText(title);
        ((TextView) rootView.findViewById(R.id.tut_body)).setText(body);
        Uri video = Uri.parse(videoLocation);
        ((VideoView) rootView.findViewById(R.id.tut_video)).setVideoURI(video);



        return rootView;
    }
}
