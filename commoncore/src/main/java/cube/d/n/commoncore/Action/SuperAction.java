package cube.d.n.commoncore.Action;

import cube.d.n.commoncore.ErrorReporter;

/**
 * Created by Colin_000 on 9/4/2015.
 */
public abstract class SuperAction {
    public boolean canAct(){
        return true;
    }

    protected abstract void privateAct();

    public void act(){
        if (canAct()){
            privateAct();
        }
    }
}
