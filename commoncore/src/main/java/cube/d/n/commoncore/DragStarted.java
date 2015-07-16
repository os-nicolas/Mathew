package cube.d.n.commoncore;

import android.graphics.Canvas;

import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin on 1/21/2015.
 */
public class DragStarted extends Animation {
    float startAlpha=0xff;
    long time;
    double startedAt;
    float top;
    float left;
    final AlgebraLine owner;

    public DragStarted(AlgebraLine owner, float startAlpha,float top,float left){
        super(owner.getAfterAnimations());
        this.owner = owner;
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
            owner.owner.progressManager.drawProgress(left, 1f,(int)currentAlpha,canvas.getWidth(),canvas.getHeight());
        }else{
            remove();
        }
    }
}
