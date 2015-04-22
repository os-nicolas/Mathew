package colin.example.algebrator;

import android.graphics.Canvas;

import cube.d.n.commoncore.Animation;

/**
 * Created by Colin on 1/21/2015.
 */
public class DragStarted extends Animation {
    float startAlpha=0xff;
    long time;
    double startedAt;

    public DragStarted(SuperView owner, float startAlpha){
        super(owner);
        this.startAlpha = startAlpha;
        this.startedAt = System.currentTimeMillis();
        this.time = Algebrator.getAlgebrator().doubleTapSpacing;
    }

    @Override
    public void draw(Canvas canvas) {
        long now = System.currentTimeMillis();
        if ((now - startedAt) < time) {
            float currentAlpha = (float)(((double)startAlpha)*((time -(now - startedAt))/time));
            owner.drawProgress(canvas, 1f, currentAlpha);
        }else{
            remove();
        }
    }
}
