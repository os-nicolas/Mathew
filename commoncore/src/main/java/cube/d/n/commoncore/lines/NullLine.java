package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 6/24/2015.
 */
public class NullLine extends EquationLine {



    public NullLine() {
        super(null);
    }

    @Override
    public KeyBoard getKeyboad() {
        Log.e("error","this should not be called");
        return null;
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {
        Log.e("error","this should not be called");
    }

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }

    @Override
    public float requestedMaxX() {
        Log.e("error","this should not be called");
        return 0;
    }

    @Override
    public float requestedMinX() {
        Log.e("error","this should not be called");
        return 0;
    }
}
