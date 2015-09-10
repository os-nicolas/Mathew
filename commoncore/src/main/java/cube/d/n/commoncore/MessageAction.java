package cube.d.n.commoncore;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 8/21/2015.
 */
public class MessageAction extends Action {
    public MessageAction(EquationLine equationLine) {
        super(equationLine);
    }

    private boolean can = true;

    @Override
    public boolean canAct(){
        return can;
    }

    @Override
    protected void privateAct() {
        done();
    }

    public synchronized void done() {
        can = false;
    }
}
