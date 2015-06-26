package cube.d.n.commoncore;

import android.graphics.Paint;
import android.text.TextPaint;

import cube.d.n.commoncore.Action.Action;

/**
 * Created by Colin_000 on 6/8/2015.
 */
public class SelectedRowButtons extends PopUpButton {
    public void setLocation(float ytop, float ybot) {
        setLocation(left,right,ytop,ybot);
    }

    public SelectedRowButtons( String text, Action myAction) {
        super(text, myAction);

    }

    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }

    public void setLeftAndRight(float l, float r) {
        left =l;
        right = r;
    }
}
