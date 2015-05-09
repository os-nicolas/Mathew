package cube.d.n.commoncore.v2;

import android.graphics.Canvas;
import android.graphics.Paint;

import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class AlgebraLine extends Line {


    public Equation dragging;

    public AlgebraLine(Main owner) {
        super(owner);
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {

    }

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }
}
