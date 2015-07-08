package cube.d.n.commoncore.tuts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cube.d.n.commoncore.FadeInTextView;
import cube.d.n.commoncore.R;

/**
 * Created by Colin_000 on 5/4/2015.
 */
public class TutTextFrag extends TutFrag{

    public String title;
    public String videoSub;
    public String at;

    public static TutTextFrag make(String title, String videoSub,String at){
        TutTextFrag result = new TutTextFrag();
        Bundle args = new Bundle();
        args.putString("TITLE",title);
        args.putString("VIDEO_SUB",videoSub);
        args.putString("AT", at);
        result.setArguments(args);
        Log.i("make", "set arguments " + result.hashCode() + " args " + result.getArguments());
        return result;
    }

    @Override
    protected void pstart(View rootView){
        super.pstart(rootView);
        ((FadeInTextView) rootView.findViewById(R.id.tut_text_body)).start();
        if (extaTimeOut != -1){
            ((FadeInTextView) rootView.findViewById(R.id.tut_text_body)).hangTime +=extaTimeOut;
        }
    }

    public void updateData(Bundle args){
        super.updateData(args);
        this.title = args.getString("TITLE");
        this.at = args.getString("AT");
        this.videoSub = args.getString("VIDEO_SUB");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Log.i("hey","create view "+this+"");
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.tuttext, container, false);


        Log.i("make","got arguments " +hashCode() +" args "+getArguments());
        updateData(getArguments());

        ((TextView) rootView.findViewById(R.id.tut_title)).setText(title);
        ((TextView) rootView.findViewById(R.id.tut_text_body)).setText(videoSub);
        ((TextView) rootView.findViewById(R.id.tut_at)).setText(at);

        rootView.findViewById(R.id.tut_title).setBackgroundColor(ribbonColor);
         rootView.findViewById(R.id.tut_text_body).setBackgroundColor(ribbonColor);
        rootView.findViewById(R.id.ribbon).setBackgroundColor(ribbonColor);

        if (drawOnStart){start(rootView);}

        return rootView;
    }


}
