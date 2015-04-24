package cube.d.n.commoncore;

import android.graphics.Canvas;


/**
 * Created by Colin on 1/21/2015.
 */
public abstract class Animation {
    public final BaseView owner;

    public Animation(BaseView owner){
        this.owner =owner;
    }

    public abstract void draw(Canvas canvas);

    protected void remove(){
        owner.animation.remove(this);
    }

}
