package cube.d.n.commoncore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Pop;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 7/15/2015.
 */
public class HappyView extends View {
    long startedAt=-1;
    private final long runTime= 400;

    ArrayList<Animation> pops = new ArrayList<>();

    public HappyView(Context context) {
        super(context);
    }

    public HappyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start(){
        if (!started()) {
            startedAt = System.currentTimeMillis();
            invalidate();
        }
    }

    private boolean started() {
        return startedAt != -1 && System.currentTimeMillis() < startedAt + runTime;
    }

    @Override
    public void onDraw(Canvas canvas){
        for(int i=pops.size()-1;i>-1;i--){
            pops.get(i).draw(canvas);
        }

        if (started()) {
            // add a pop
            float at = getWidth() * (precent());

            Random r = new Random();
            float x = at + ((r.nextFloat()-.5f)*(getHeight())/2f);
            float y= r.nextFloat()*(getHeight()-45* BaseApp.getApp().getDpi());

            Log.d("adding a pop", "adding a pop at: "+ at + " x: "+x+ " y: " + y);

            Point point = new Point((int)x,(int)y);
            Pop p =new Pop(point,pops).widthColor(0xffff0000);
            pops.add(p);
        }

        if (started() || !pops.isEmpty()){
          invalidate();
        }
    }

    private float precent() {
        return ((float)(System.currentTimeMillis() - startedAt))/(float)runTime;
    }

}
