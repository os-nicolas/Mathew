package cube.d.n.commoncore.Action;

import java.util.Random;

import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.MessageAction;
import cube.d.n.commoncore.PopUpButton;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/9/2015.
 */
public class SolvedAction extends MessageAction {

    public SolvedAction(EquationLine owner) {
        super(owner);

    }

    public String getDisplay() {
        Random r = new Random();
        String[] res = {"Yay!","Well Done!", "Good Work!", "Solved!", "Nice!"};
        int num = r.nextInt(res.length);
        String str=res[num] + " (";
        if (owner.owner.next.hasNext()){
            str+= "→";
        }else {
            str+= "Menu";
        }
        return  str + ")";
    }

    @Override
    protected void privateAct() {
        super.privateAct();
        if (owner.owner.next.hasNext()){
            owner.owner.next.next();
        }else {
            owner.owner.next.finish();
        }
    }
}
