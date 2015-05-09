package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.v2.InputLine;

public class TimesAction extends InlineOpAction {

    String display;
    public TimesAction(InputLine emilyView) {
        super(emilyView);

        char[] timesUnicode = { '\u00D7'};
        display = new String(timesUnicode);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new WritingLeafEquation(display, ((InputLine)owner));
        inlineInsert(newEq);
    }
}
