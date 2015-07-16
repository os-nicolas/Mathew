package cube.d.n.commoncore;

import android.graphics.Canvas;

import java.util.ArrayList;

import cube.d.n.commoncore.lines.EquationLine;


/**
 * Created by Colin on 1/21/2015.
 */
public abstract class Animation {
    public ArrayList<Animation> holder;

    public Animation(ArrayList<Animation> holder){
        this.holder =holder;
    }

    public abstract void draw(Canvas canvas);

    protected void remove(){
        if (holder != null) {
            holder.remove(this);
        }
    }

}
