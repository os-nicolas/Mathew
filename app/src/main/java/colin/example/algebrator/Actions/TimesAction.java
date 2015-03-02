package colin.example.algebrator.Actions;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.example.algebrator.EmilyView;

public class TimesAction extends InlineOpAction {

    String display;
    public TimesAction(EmilyView emilyView) {
        super(emilyView);

        char[] timesUnicode = { '\u00D7'};
        display = new String(timesUnicode);
    }

    @Override
    public void act() {
        Equation newEq = new WritingLeafEquation(display, emilyView);
        inlineInsert(newEq);
    }
}
