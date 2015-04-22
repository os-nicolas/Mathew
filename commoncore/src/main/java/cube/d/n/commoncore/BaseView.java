package cube.d.n.commoncore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.DragEquation;
import cube.d.n.commoncore.eq.Equation;

/**
 * Created by Colin_000 on 4/20/2015.
 */
public abstract class BaseView extends View {

    public Equation selected;
    public boolean hasUpdated;
    public DragEquation dragging;
    public ArrayList<Animation> animation = new ArrayList<Animation>();
    protected Equation stupid;

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


    public enum pm  {WRITE,BOTH,SOLVE}

    public abstract pm parentThesisMode();
}
