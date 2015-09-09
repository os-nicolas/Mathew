package cube.d.n.commoncore.tuts;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import cube.d.n.commoncore.FadeInTextView;
import cube.d.n.commoncore.R;

/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutVideoFrag extends TutFrag{

    public String title;
    public String body;
    public String at;
    //android.resource://com.pac.myapp/raw/master
    public  String videoLocation;

    public static TutVideoFrag make(String title,String body,String videoLocation,String at){
        TutVideoFrag result = new TutVideoFrag();
        Bundle args = new Bundle();
        args.putString("TITLE",title);
        args.putString("BODY",body);
        args.putString("VIDEO_LOCATION",videoLocation);
        args.putString("AT",at);
        result.setArguments(args);
        Log.i("make","set arguments " +result.hashCode() + " args "+ result.getArguments());
        return result;
    }

    public void updateData(Bundle args){
        this.title = args.getString("TITLE");
        this.body = args.getString("BODY");
        this.videoLocation = args.getString("VIDEO_LOCATION");
        this.ribbonColor = args.getInt("RIBBON_COLOR",0xff6C0304);
        this.at = args.getString("AT");
    }

    @Override
    protected void pstart(View rootView){
        super.pstart(rootView);
        ((FadeInTextView) rootView.findViewById(R.id.tut_body)).start();

        if (extaTimeOut != -1){
            ((FadeInTextView) rootView.findViewById(R.id.tut_body)).hangTime +=extaTimeOut;
        }

        final VideoView vv = ((VideoView) rootView.findViewById(R.id.tut_video));
        final Activity that = getActivity();
        Thread th = new Thread(){
            @Override
            public void run(){
                try{
                    Thread.sleep(1300);
                    that.runOnUiThread(new Runnable() {
                        public void run() {
                            vv.start();
                        }
                    });

                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("hey","create view "+this+"");
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.tutscreen, container, false);


        Log.i("make","got arguments " +hashCode() +" args "+getArguments());
        updateData(getArguments());

        ((TextView) rootView.findViewById(R.id.tut_title)).setText(title);
        ((TextView) rootView.findViewById(R.id.tut_body)).setText(body);
        ((TextView) rootView.findViewById(R.id.tut_at)).setText(at);

        rootView.findViewById(R.id.tut_title).setBackgroundColor(ribbonColor);
        rootView.findViewById(R.id.tut_body).setBackgroundColor(ribbonColor);
        rootView.findViewById(R.id.ribbon).setBackgroundColor(ribbonColor);


//        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
//        Uri video = Uri.parse(vidAddress);
        Uri video = Uri.parse(videoLocation);
        Log.i("video",video.toString());

        final VideoView vv = ((VideoView) rootView.findViewById(R.id.tut_video));
        vv.setVideoURI(video);
//        vv.seekTo(1);


        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();

                mp.pause();
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                vv.start();
            }
        });

        if (drawOnStart){start(rootView);}

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        View rootView = getView();
        final VideoView vv = ((VideoView) rootView.findViewById(R.id.tut_video));
        vv.pause();
    }
}
