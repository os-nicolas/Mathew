package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import colin.example.algebrator.EmilyView;

public class TimesAction extends InlineOpAction {

    String display;
    public TimesAction(EmilyView emilyView) {
        super(emilyView);

        char[] timesUnicode = { '\u00D7'};
        display = new String(timesUnicode);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new WritingLeafEquation(display, myView);
        inlineInsert(newEq);
    }
}
