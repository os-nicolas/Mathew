package cube.d.n.commoncore;

import android.graphics.Canvas;

import cube.d.n.commoncore.lines.Line;


/**
 * Created by Colin on 1/21/2015.
 */
public abstract class Animation {
    public final Line owner;

    public Animation(Line owner){
        this.owner =owner;
    }

    public abstract void draw(Canvas canvas);

    protected void remove(){
        owner.removeAnimation(this);
    }

}
