package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.concurrent.Callable;


/**
 * Created by Colin on 2/24/2015.
 */
public class MessageBar {
    float targetHeight = 0;
    float shadowHeight = 20*BaseApp.getApp().getDpi();
    float bodyHeight = 55*BaseApp.getApp().getDpi();
    float currentHeight = 0;
    final BaseView owner;

    private long lastShown =-1;
    private long veryOpenFor = 300;

    ArrayList<Message> que = new ArrayList<Message>();

    Paint bkgPaint;
    Paint textPaint;
    Paint shadowPaint;

    int targetTextAlpha = 0xff;
    float currentTextAlpha = 0xff;

    public MessageBar(BaseView owner){
        this.owner = owner;
        bkgPaint = new Paint();
        bkgPaint.setColor(BaseApp.getApp().darkColor);
        textPaint = new Paint(BaseApp.getApp().textPaint);
        textPaint.setColor(Color.WHITE);
        shadowPaint = new Paint();
        shadowPaint.setColor(BaseApp.getApp().lightColor);
    }

    private Message current(){
        if (que.size() !=0) {
            return que.get(0);
        }else{
            return null;
        }
    }

    public boolean isOpen(){
        return current()==null;
    }

    /**
     *
     * @return
     */
    public boolean veryOpen(){
        if (isOpen()&& System.currentTimeMillis() - lastShown > veryOpenFor
                ){
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas){

        if (current()==null || current().isDone()){
            targetTextAlpha = 0x00;
        }else{
            targetTextAlpha = 0xff;
        }

        if (current()==null){
            targetHeight = 0;
        }else{
            targetHeight = shadowHeight+ totalbodyHeight();
        }

        if (current() != null && !current().hasStarted()) {
            current().start();
        }

        float rate = BaseApp.getApp().getRate();
        currentHeight = (currentHeight*(rate-1) + targetHeight)/rate;
        currentTextAlpha = (currentTextAlpha*(rate-1) + targetTextAlpha)/rate;
        if (current() != null && currentTextAlpha <0x10 && targetTextAlpha == 0x00){
            next();
        }

        // draw the box
        RectF r = new RectF(0,0,measureWidth(),currentBodyHeight());
        canvas.drawRect(r, bkgPaint);

        // draw the text
        if (current() != null) {
            int at =0;
            for (String text : current().text) {
                Rect out = new Rect();
                float buffer = 1.5f*BaseApp.getApp().getBuffer();
                textPaint.getTextBounds(text, 0, text.length(), out);
                while (out.width() + 2*buffer > measureWidth() || out.height() + 2 * buffer > bodyHeight) {
                    textPaint.setTextSize(textPaint.getTextSize() - 1);
                    textPaint.getTextBounds(text, 0, text.length(), out);
                }
                float h = out.height();
                float w = out.width();
                textPaint.setAlpha((int) currentTextAlpha);
                canvas.drawText(text, (measureWidth() / 2), currentHeight - shadowHeight - bodyHeight*(current().text.length-1-at)- (bodyHeight / 2) + (h / 2), textPaint);
                at++;
            }
        }

        //draw the shadow
        // a few solid lines
        shadowPaint.setAlpha(0xff);
        float at = (int)currentBodyHeight();
        for (int i=0;i<2f/BaseApp.getApp().getDpi();i++){
            canvas.drawLine(0,at,measureWidth(),at,shadowPaint);
            at++;
        }

        // and then the fad
        shadowPaint.setAlpha(0x7f);
        while (at < currentHeight){
            canvas.drawLine(0,at,measureWidth(),at,shadowPaint);
            shadowPaint.setAlpha((int) (shadowPaint.getAlpha() / BaseApp.getApp().getShadowFade()));
            at++;
        }
    }

    private float totalbodyHeight() {
        if (current() != null) {
            return bodyHeight * current().text.length;
        }
        return  0;
    }

    public void db(String message){
        if (current() !=null){
            current().db(message);
        }else{
            enQue(1000,message);
        }

    }


    private void next() {
        if (current() != null) {
            lastShown = System.currentTimeMillis();
            que.remove(0);
        }
    }

    public void enQue(long runTime,String text){
        Message newMessage = new Message(new String[]{text},runTime);
        que.add(newMessage);
    }
    public void enQue(long runTime,String[] text){
        Message newMessage = new Message(text,runTime);
        que.add(newMessage);
    }
    public void enQue(long runTime,String text,Callable func){
        Message newMessage = new Message(new String[]{text},runTime,func);
        que.add(newMessage);
    }
    public void enQue(long runTime,String[] text,Callable func){
        Message newMessage = new Message(text,runTime,func);
        que.add(newMessage);
    }

    public void onClick(MotionEvent event){
        if (inBar(event)){
            // we don't need to show tap to skip
//            TutMessage.getMessage(TapToSkip.class).alreadyDone();
            //
            if (current() != null){
                current().click();
            }

            quit();
        }
    }

    private void quit() {
        if (current() != null){
            current().quit();
        }
    }

    public float measuerHeight(){
        return  currentHeight;
    }

    public float measureWidth(){
        return owner.width;
    }

    public boolean inBar(MotionEvent event) {
        return event.getY() <measuerHeight();
    }

    public float currentBodyHeight() {
        return  currentHeight - shadowHeight;
    }
}
