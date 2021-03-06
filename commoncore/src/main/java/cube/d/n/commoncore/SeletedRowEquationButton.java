package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 6/22/2015.
 */
public class SeletedRowEquationButton extends SelectedRowButtons {
    public final Equation myEq;

    public SeletedRowEquationButton(Equation eq, Action myAction) {
        super("", myAction);
        myEq = eq.copy();
        myEq.parent = null;
    }

    public void draw(Canvas canvas, Paint p){
        drawEqButton(this,canvas, p,myEq);
    }


}
