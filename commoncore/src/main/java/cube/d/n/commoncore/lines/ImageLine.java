package cube.d.n.commoncore.lines;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.CircleDrawer;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.TextDrawInfo;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 6/29/2015.
 */
public  class ImageLine extends Line  {


    private Bitmap bitMap;
    private TextPaint titlePaint= new TextPaint();
    private TextPaint bodyPaint= new TextPaint();
    String title;
    float bffr = BaseApp.getApp().getBuffer()*2;
    String body;
    public CircleDrawer circleDrawer = new CircleDrawer();
    int tr = (int)(50*BaseApp.getApp().getDpi());

    int maxWith = (int)(650* BaseApp.getApp().getDpi());
    private int bmWidth=-1;

    public ImageLine(Main owner) {
        super(owner);
        titlePaint.setTypeface(BaseApp.getApp().getDJVL());
        titlePaint.setTextSize(BaseApp.getApp().getTextSize()*.75f);
        titlePaint.setDither(true);
        titlePaint.setAntiAlias(true);
        //tr = (int)((BaseApp.getApp().getTextSize()*1.2f)/2f);

        bodyPaint.setTypeface(BaseApp.getApp().getDJVL());
        bodyPaint.setTextSize(BaseApp.getApp().getTextSize()*.45f);
        //bodyPaint.setColor(BaseApp.getApp().getGreyTextColor());
        bodyPaint.setDither(true);
        bodyPaint.setAntiAlias(true);

        circleDrawer.setColors("5",BaseApp.colorFade(0xffff0000,0xffffffff,1.2f),BaseApp.colorFade(0xff000000,0xffffffff,1.2f));
    }

    @Override
    public KeyBoard getKeyboad() {
        return null;
    }

    @Override
    public void setKeyBoard(KeyBoard k) {

    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {
            if (bitMap == null || bmWidth != canvas.getWidth()){
                Log.d("updated bitmap","ggrrrrr");
                bitMap = getBitMap(bitMap,canvas.getWidth());
                bmWidth = canvas.getWidth();
            }

            int startAtX = Math.max(0,(canvas.getWidth() - maxWith)/2);
            circleDrawer.draw(canvas,tr,(int)(startAtX+tr+bffr),(int)(top +tr ));
            if (bitMap!=null){
                canvas.drawBitmap(bitMap,left,top,paint);
            }
    }

    public void updateBitMap(int width){
        bitMap = getBitMap(bitMap,width);
    }

    private Bitmap getBitMap(Bitmap old, int width) {

        int left = Math.max(0,(width - maxWith)/2);
        width = Math.min(maxWith,width);

        Picture p = new Picture();

        //so we have problem title a the top
        // and problem text in a block below it

        // we need to know the hight and width before we start recording

        // first me measure the title


        //TextBlockInfo titleLines = getTextDrawInfo(title,width,(int)bffr,0,titlePaint);
        TextBlockInfo bodyLines = getTextDrawInfo(body,width,(int)(tr*2 ),left,bodyPaint);

        Canvas c =  p.beginRecording(width+left,(int)(tr*2 + (!body.equals("")? bodyLines.getHeight():0)));//3*bffr +


        c.drawText(title,(int)(tr*2+2*bffr + left),tr + titlePaint.getTextSize()/2f, titlePaint);
//        titleLines.draw(c);
        bodyLines.draw(c);

        p.endRecording();

        PictureDrawable pd = new PictureDrawable(p);
        //if (old == null) {
            old = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Bitmap.Config.ARGB_8888);
        //}
        Canvas canvas = new Canvas(old);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawPicture(pd.getPicture());
        return old;
    }

    private TextBlockInfo getTextDrawInfo(String text, int width, int top, int left, TextPaint paint) {
        //Log.d("max width", ""+ width);
        TextBlockInfo res =new TextBlockInfo();
        String[] words = text.split(" ");
        boolean done = false;
        float lineSpacing =BaseApp.getApp().getDpi()*5;
        int startTop = top;
        top+= bffr;
        left += bffr;
        int at = 0;
        Rect out = new Rect();
        paint.getTextBounds("Ay", 0, "Ay".length(), out);
        float lineHeight = out.height();
        while (!done) {
            boolean go = true;
            String myComString = "";
            while (go) {

                String nextCompString = myComString+ words[at] + " ";
                paint.getTextBounds(nextCompString, 0, nextCompString.length(), out);
                if (out.width() > width-(2*bffr) ){
                    top+= lineHeight;
                    res.add(new TextDrawInfo(top,left,out,myComString,paint));
                    //Log.d("my width", ""+ currentWidth + " " + out.width());
                    //paint.getTextBounds(myComString, 0, myComString.length(), out);
                    //Log.d("but really my width is", ""+ out.width());
                    top+=lineSpacing;
                    go = false;
                    if (at == words.length) {
                        done = true;
                    }
                }else if (at == words.length-1){
                    top+= lineHeight;
                    res.add(new TextDrawInfo(top,left,out,nextCompString,paint));
                    go = false;
                    done = true;
                }else{
                    at++;
                    myComString=nextCompString;
                }
            }
        }
        res.setHeight((int)(top + bffr - startTop));
        return res;
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
        return (bitMap==null?0:bitMap.getHeight()-bffr);
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
