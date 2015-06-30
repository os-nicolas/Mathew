package cube.d.n.commoncore.lines;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.drawable.PictureDrawable;
import android.view.View;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 6/29/2015.
 */
public  class ImageLine extends Line  {


    private Bitmap bitMap;
    View source;

    public ImageLine(Main owner) {
        super(owner);
    }

    public void setSource(View source){
        this.source =  source;
    }

    @Override
    public KeyBoard getKeyboad() {
        return null;
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {
        if (source != null){

            if (bitMap == null && source.getMeasuredHeight() != 0){
                bitMap = getBitMap(source,bitMap);
            }

            if (bitMap!=null){
                canvas.drawBitmap(bitMap,left,top,paint);
            }

        }
    }

    private Bitmap getBitMap(View overlay,Bitmap old) {

        Picture p = new Picture();

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
