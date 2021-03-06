package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.lines.InputLine;

public class TimesAction extends InlineOpAction {

    String display;
    public TimesAction(InputLine emilyView) {
        super(emilyView);

        display = owner.modeController().getMultiSymbol();
    }

    @Override
    protected void privateAct() {
        Equation newEq = new WritingLeafEquation(display, ((InputLine)owner));
        inlineInsert(newEq);
    }
}
