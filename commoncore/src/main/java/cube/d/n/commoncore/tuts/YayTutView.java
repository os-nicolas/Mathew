package cube.d.n.commoncore.tuts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Random;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.YayView;


/**
 * Created by Colin_000 on 7/5/2015.
 */
public class YayTutView extends LinearLayout implements YayView {
    View view;
    public YayTutView(Context context) {
        super(context);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.winner,this);
        ((Button)view.findViewById(R.id.reset)).setTypeface(BaseApp.getApp().getDJVL());
//        ((Button)view.findViewById(R.id.stay)).setTypeface(BaseApp.getApp().getDJV());
//        ((Button)view.findViewById(R.id.next)).setTypeface(BaseApp.getApp().getDJV());
        ((View)view.findViewById(R.id.overlay_background)).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ((TextView)view.findViewById(R.id.winner_yaytext)).setTypeface(BaseApp.getApp().getDJVL());
        ((TextView)view.findViewById(R.id.winner_yaytext)).setText(getText());
    }
    public YayTutView(Context context, AttributeSet attrs) {
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

//        ((Button)view.findViewById(R.id.stay)).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        ((Button)view.findViewById(R.id.reset)).setOnClickListener(new OnClickListener() {
            boolean clicked = false;
            @Override
            public void onClick(View v) {
                if (!clicked){
                    clicked = true;
                    main.reset();
                }
            }
        });
//        ((Button)view.findViewById(R.id.next)).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }
}
