package cube.d.n.commoncore.lines;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.drawable.PictureDrawable;
import android.text.TextPaint;
import android.view.View;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 6/29/2015.
 */
public  class ImageLine extends Line  {


    private Bitmap bitMap;
    private TextPaint titlePaint;
    private TextPaint bodyPaint;

    public ImageLine(Main owner) {
        super(owner);

    }


    @Override
    public KeyBoard getKeyboad() {
        return null;
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {



            if (bitMap == null){
                bitMap = getBitMap(bitMap,canvas.getWidth());
            }

            if (bitMap!=null){
                canvas.drawBitmap(bitMap,left,top,paint);
            }
    }

    private Bitmap getBitMap(Bitmap old, int width) {

        Picture p = new Picture();

        //so we have problem title a the top
        // and problem text in a block below it

        // we need to know the hight and width before we start recording




        textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        while (out.width() + 2 * buffer > measureWidth() || out.height() + 2 * buffer > targetHeight()) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        }



        Canvas c =  p.beginRecording(overlay.getWidth(),overlay.getHeight());

        overlay.draw(c);

        p.endRecording();

        PictureDrawable pd = new PictureDrawable(p);
        if (old == null) {
            old = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(old);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawPicture(pd.getPicture());
        return old;
    }


    @Override
    public float requestedMaxX() {
        return measureWidth();
    }

    @Override
    public float requestedMinX() {
        return measureWidth() ;
    }

    @Override
    public float measureWidth() {
        return (bitMap==null?0:bitMap.getWidth());
    }

    @Override
    public float measureHeight() {
        return (bitMap==null?0:bitMap.getHeight());
    }
}
