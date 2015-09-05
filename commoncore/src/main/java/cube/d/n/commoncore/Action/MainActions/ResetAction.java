package cube.d.n.commoncore.Action.MainActions;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/4/2015.
 */
public class ResetAction extends MainAction {
    boolean acting = false;
    public ResetAction(Main owner) {
        super(owner);
    }

    @Override
    public boolean canAct(){
        return !acting;
    }

    @Override
    protected void privateAct() {
        acting = true;
        main.reset(new Runnable(){
            @Override
            public void run(){
                acting = false;
            }
        });
    }
}
