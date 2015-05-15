package cube.d.n.commoncore;

import android.graphics.Canvas;

import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.Line;

/**
 * Created by Colin on 1/21/2015.
 */
public class DragStarted extends Animation {
    float startAlpha=0xff;
    long time;
    double startedAt;
    float top;
    float left;

    public DragStarted(Line owner, float startAlpha,float top,float left){
        super(owner);
        this.startAlpha = startAlpha;
        this.startedAt = System.currentTimeMillis();
        this.time = BaseApp.getApp().doubleTapSpacing;
        this.left = left;
        this.top = top;
    }

    @Override
    public void draw(Canvas canvas) {
        long now = System.currentTimeMillis();
        if ((now - startedAt) < time) {
            float currentAlpha = (float)(((double)startAlpha)*((time -(now - startedAt))/time));
            ((AlgebraLine)owner).drawProgress(canvas,top,left, 1f, (int)currentAlpha);
        }else{
            remove();
        }
    }
}
