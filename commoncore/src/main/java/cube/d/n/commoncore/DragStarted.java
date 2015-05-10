package cube.d.n.commoncore;

import android.graphics.Canvas;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.v2.lines.AlgebraLine;
import cube.d.n.commoncore.v2.lines.Line;

/**
 * Created by Colin on 1/21/2015.
 */
public class DragStarted extends Animation {
    float startAlpha=0xff;
    long time;
    double startedAt;

    public DragStarted(Line owner, float startAlpha){
        super(owner);
        this.startAlpha = startAlpha;
        this.startedAt = System.currentTimeMillis();
        this.time = BaseApp.getApp().doubleTapSpacing;
    }

    @Override
    public void draw(Canvas canvas) {
        long now = System.currentTimeMillis();
        if ((now - startedAt) < time) {
            float currentAlpha = (float)(((double)startAlpha)*((time -(now - startedAt))/time));
            ((AlgebraLine)owner).drawProgress(canvas, 1f, (int)currentAlpha);
        }else{
            remove();
        }
    }
}
