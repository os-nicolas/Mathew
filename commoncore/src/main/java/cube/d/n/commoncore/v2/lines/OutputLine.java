package cube.d.n.commoncore.v2.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import cube.d.n.commoncore.v2.Main;
import cube.d.n.commoncore.v2.keyboards.KeyBoard;
import cube.d.n.commoncore.v2.lines.Line;


/**
* Created by Colin_000 on 5/7/2015.
*/
public class OutputLine extends Line {

    public OutputLine(Main owner){
        super(owner);
    }

    @Override
    public KeyBoard getKeyboad() {
        return null;
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {

    }

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }
}
