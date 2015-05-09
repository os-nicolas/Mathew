package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.tuts.TutMessage;

/**
 * Created by Colin_000 on 3/27/2015.
 */
public class ClearAction extends Action<EmilyView> {


    public ClearAction(EmilyView emilyView) {
        super(emilyView);
    }


    long lastCalled=0l;
    long lastShown=0l;
    long lastActed =0l;
    long showFor = TutMessage.aveTime;
    long timeOut = 1000;

    @Override
    public boolean canAct(){
        long now = System.currentTimeMillis();
        if (now - lastCalled > timeOut  && myView.getStupid().size() != 1 && myView.getStupid() instanceof WritingEquation){
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
        myView.initEq();

        ((PlaceholderEquation) myView.selected).goDark();
    }


}
