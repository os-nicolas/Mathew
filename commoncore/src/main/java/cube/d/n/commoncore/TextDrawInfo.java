package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;

/**
 * Created by Colin_000 on 7/8/2015.
 */
public class TextDrawInfo {
    public int top;
    public int left;
    public Rect out;
    public String string;
    public TextPaint textPaint;
    public TextDrawInfo(int top, int left, Rect out, String string,TextPaint textPaint) {
        this.top = top;
        this.left = left;
        this.out = out;
        this.string = string;
        this.textPaint = textPaint;
    }

    public void draw(Canvas c) {
        c.drawText(string,left,top,textPaint);
    }
}
