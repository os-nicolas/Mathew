package cube.d.n.commoncore;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.drawable.PictureDrawable;

/**
 * Created by Colin_000 on 7/15/2015.
 */
public class ProgressManager  {
    public boolean top = false;
    private Bitmap progressBitmap= null;
    private float pbwidth=-1;
    private float bmHeight=-1;

    private float percent;
    private int startAt;
    private int width;
    private int height;
    private float left;
    private boolean toDraw = false;

    public void drawProgress(float left, float percent, int startAt, int width,int height) {
        if (progressBitmap == null || width != pbwidth){
            pbwidth=width;
            progressBitmap = generateProgressBitmap(progressBitmap);
        }
        this.percent =percent;
        this.startAt = startAt;
        this.width = width;
        this.height = height;
        this.left = left;
        this.toDraw = true;
    }

    private Bitmap generateProgressBitmap(Bitmap old) {

        Picture picture = new Picture();

        float scaleBy = BaseApp.getApp().getShadowFade();
        int fadelen= (int)Math.ceil(Math.log(0xff)/Math.log(scaleBy));

        bmHeight = BaseApp.getApp().getTopLineWidth()+fadelen;
        Canvas canvas =  picture.beginRecording((int)(pbwidth+fadelen),(int)bmHeight);

        Paint p = new Paint();
        p.setColor(BaseApp.getApp().darkColor);
        float targetAlpha = 0xff;
        int at = (top?0:(int)bmHeight);
        for (int i = 0; i < BaseApp.getApp().getTopLineWidth(); i++) {
            p.setAlpha((int) targetAlpha);
            canvas.drawLine(0, at, pbwidth, at, p);
            float atX = ((int) (0+ pbwidth));
            while (p.getAlpha() > 1) {
                canvas.drawLine(atX, at, atX + 1, at, p);
                p.setAlpha((int) (p.getAlpha() / scaleBy));
                atX++;
            }
            at+=(top?1:-1);
        }
        p.setAlpha(0x7f);
        while (targetAlpha > 1) {
            p.setAlpha((int) targetAlpha);
            canvas.drawLine(0, at, pbwidth, at, p);
            float atX = ((int) (pbwidth));
            while (p.getAlpha() > 1) {
                canvas.drawLine(atX, at, atX + 1, at, p);
                p.setAlpha((int) (p.getAlpha() / scaleBy));
                atX++;
            }
            targetAlpha = targetAlpha / scaleBy;
            at+=(top?1:-1);
        }

        picture.endRecording();

        PictureDrawable pd = new PictureDrawable(picture);
        if (old == null) {
            old = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas2 = new Canvas(old);
        canvas2.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas2.drawPicture(pd.getPicture());
        return old;
    }

    public void draw(Canvas canvas) {
        if (toDraw) {
            Paint p = new Paint();
            p.setAlpha(startAt);

            if (top) {
                canvas.drawBitmap(progressBitmap, left - (width * (1 - percent)), 0, p);
            } else {
                canvas.drawBitmap(progressBitmap, left - (width * (1 - percent)), height - (int) bmHeight, p);
            }
        }
        toDraw = false;
    }
}
