package cube.d.n.commoncore.Action;

import cube.d.n.commoncore.MessageAction;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/9/2015.
 */
public class RunnableAction extends MessageAction {
    Runnable runnable;
    public RunnableAction(EquationLine equationLine, Runnable r) {
        super(equationLine);
        this.runnable = r;
    }

    @Override
    protected void privateAct(){
        super.privateAct();
        runnable.run();
    }
}
