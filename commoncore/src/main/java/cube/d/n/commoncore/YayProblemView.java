package cube.d.n.commoncore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.Random;

/**
 * Created by Colin_000 on 7/10/2015.
 * main you sloppy son of a bitch way to copy code
 * this is YayTutView
 */
public class YayProblemView extends LinearLayout implements YayView {
    View view;
    public YayProblemView(Context context) {
        super(context);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.problem_winner,this);
        ((android.widget.Button)view.findViewById(R.id.reset)).setTypeface(BaseApp.getApp().getDJVL());
        ((android.widget.Button)view.findViewById(R.id.next)).setTypeface(BaseApp.getApp().getDJVL());
        (view.findViewById(R.id.overlay_background)).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ((TextView)view.findViewById(R.id.winner_yaytext)).setTypeface(BaseApp.getApp().getDJVL());
        ((TextView)view.findViewById(R.id.winner_yaytext)).setText(getText());
    }
    public YayProblemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public String getText() {
        Random r = new Random();
        String[] res = {"Yay!","Well Done!", "Good Work!", "Easy Peasy!", "Nice!"};
        int num = r.nextInt(res.length);
        return res[num];
    }

    public void initOnClickListeners(final Main main){
        ((android.widget.Button)view.findViewById(R.id.reset)).setOnClickListener(new OnClickListener() {
            boolean clicked = false;
            @Override
            public void onClick(View v) {
                if (!clicked){
                    clicked = true;
                    main.reset();
                }
            }
        });
        ((android.widget.Button)view.findViewById(R.id.next)).setOnClickListener(new OnClickListener() {
            boolean clicked = false;
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    clicked = true;
                    Log.d("Next clicked", "Next clicked");
                }
            }
        });

    }
}