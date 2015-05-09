package cube.d.n.commoncore.v2;

        import android.graphics.Canvas;
        import android.graphics.Paint;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class HistoryLine extends Line {

    public HistoryLine(Main owner){
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
