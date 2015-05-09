package colin.example.algebrator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.DragEquation;
import cube.d.n.commoncore.eq.any.Equation;

/**
* Created by Colin_000 on 4/20/2015.
*/
public abstract class BaseView extends View {

    public MessageBar message = new MessageBar(this);

    public Equation selected;
    public boolean hasUpdated;
    public DragEquation dragging;
    public ArrayList<Animation> animation = new ArrayList<Animation>();
    public ArrayList<Animation> afterAnimations = new ArrayList<Animation>();
    public Equation stupid;

    protected int width;
    protected int height;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Equation getStupid(){
        return stupid;
    }

    public void setStupid(Equation newStupid) {
        this.stupid = newStupid;

        this.stupid.parent = null;
        if (!this.stupid.owner.equals(this)){
            this.stupid.updateOwner(this);
            this.stupid.deepNeedsUpdate();
        }
    }

    public abstract void removeSelected();

    public void resume() {
        if (stupid != null) {
            stupid.deepNeedsUpdate();
        }
        BaseApp.getApp().setActive(this);
    }



    public abstract pm parentThesisMode();

    public void drawProgress(Canvas canvas, float percent, float startAt) {
        Paint p = new Paint();
        p.setColor(BaseApp.getApp().darkColor - 0xff000000);
        float targetAlpha = startAt;
        p.setAlpha((int) targetAlpha);
        float scaleBy = BaseApp.getApp().getShadowFade();
        int at = Math.max(0, (int) message.currentBodyHeight());
        for (int i = 0; i < BaseApp.getApp().getTopLineWidth(); i++) {
            p.setAlpha((int) targetAlpha);
            canvas.drawLine(0, at, width * percent, at, p);
            float atX = ((int) (width * percent));
            while (p.getAlpha() > 1) {
                canvas.drawLine(atX, at, atX + 1, at, p);
                p.setAlpha((int) (p.getAlpha() / scaleBy));
                atX++;
            }
            at++;
        }
        p.setAlpha(0x7f);
        while (targetAlpha > 1) {
            p.setAlpha((int) targetAlpha);
            canvas.drawLine(0, at, width * percent, at, p);
            float atX = ((int) (width * percent));
            while (p.getAlpha() > 1) {
                canvas.drawLine(atX, at, atX + 1, at, p);
                p.setAlpha((int) (p.getAlpha() / scaleBy));
                atX++;
            }
            targetAlpha = targetAlpha / scaleBy;
            at++;
        }
    }
}
