package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 3/27/2015.
 */
public class ClearAction extends Action {


    public ClearAction(InputLine emilyView) {
        super(emilyView);
    }


    long lastCalled=0l;
    long lastShown=0l;
    long lastActed =0l;
    long showFor = BaseApp.getApp().acceptedTime*4;
    long timeOut = 1000;

    @Override
    public boolean canAct(){
        long now = System.currentTimeMillis();
        if (now - lastCalled > timeOut  && owner.stupid.get().size() != 1 && owner.stupid.get() instanceof WritingEquation){
            lastShown= now;
            lastCalled = now;
            return true;
        }else if (now - lastShown < showFor && lastShown > lastActed){
            lastCalled = now;
            return true;
        }
        lastCalled = now;
        return false;
    }

    @Override
    public void privateAct(){
        lastActed = System.currentTimeMillis();
        ((InputLine)owner).initEq();

        ((InputLine)owner).getSelected().goDark();
    }


}
