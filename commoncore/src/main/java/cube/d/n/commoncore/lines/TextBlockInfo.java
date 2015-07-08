package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import cube.d.n.commoncore.TextDrawInfo;

/**
 * Created by Colin_000 on 7/8/2015.
 */
public class TextBlockInfo extends ArrayList<TextDrawInfo> {
    private int height;
    public int getHeight() {
        return this.height;

    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void draw(Canvas c) {
        for (TextDrawInfo line: this){
            line.draw(c);
        }
    }
}
