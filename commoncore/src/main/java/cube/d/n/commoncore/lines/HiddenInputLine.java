package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import cube.d.n.commoncore.HasHeaderLine;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin on 6/30/2015.
 */
public class HiddenInputLine extends  InputLine implements HasHeaderLine {
    public HiddenInputLine(Main owner) {
        super(owner);
    }

    @Override
    public void innerDraw(Canvas canvas, float top, float left, Paint paint) {
        // do nothing
    }

    @Override
    public float measureHeight() {
        return 2 *  getBuffer();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    public float requestedMaxX() {
        return 0f;
    }

    @Override
    public float requestedMinX() {return 0f;}
}
