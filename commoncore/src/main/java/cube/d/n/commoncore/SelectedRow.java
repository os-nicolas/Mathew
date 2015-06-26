package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 6/8/2015.
 */
public class SelectedRow {

    ArrayList<SelectedRowButtons> backEnd = new ArrayList<>();
    private float targetHeight;
    private boolean dead= false;
    private float currentHeight;
    private Paint textPaint;

    public void kill(){
        dead = true;
    }

    public SelectedRow(float targetHeight){
        setTargets(targetHeight);

        this.textPaint = new TextPaint(BaseApp.getApp().textPaint);
        this.textPaint.setAlpha(0);
    }

    public void setTargets(float targetHeight){
        this.targetHeight=targetHeight;

    }

    public void addButtonsRow(ArrayList<SelectedRowButtons> row, float left, float right) {
        float count = row.size();
        float at = left;
        float step = (right - left) / count;

        for (float i = 0; i < count; i++) {
            SelectedRowButtons b = row.get((int) i);
            if (b!=null){
                b.setLeftAndRight(at, at + step);
                backEnd.add(b);
            }
            at += step;
        }

    }

    public void updateLocation(KeyBoard owner){
        float rate = BaseApp.getApp().getRate();
        if (!dead){
            if (currentHeight < targetHeight) {
                currentHeight = (currentHeight * (rate - 1) + targetHeight) / rate;
                if ((int)(currentHeight*100)==(int)((currentHeight * (rate - 1) + targetHeight)*100 / rate)){
                    currentHeight=targetHeight;
                }
            }else {
                float currentAlpha = this.textPaint.getAlpha();
                currentAlpha = ((float)(currentAlpha * (rate - 1) + 0xff)) / rate;
                if ((int)(currentAlpha)==(int)((currentAlpha * (rate - 1) + 0xff) / rate)){
                    currentAlpha=0xff;
                }
                this.textPaint.setAlpha((int) currentAlpha);
            }
        }else{
            float currentAlpha = this.textPaint.getAlpha();
            if (0 < currentAlpha) {
                currentAlpha = (currentAlpha * (rate - 1) ) / rate;
                if ((int)currentAlpha==(int)((currentAlpha * (rate - 1)) / rate)){
                    currentAlpha=0;
                }
                this.textPaint.setAlpha((int) currentAlpha);
            }else {
                currentHeight = (currentHeight * (rate - 1) + 0) / rate;
                if ((int)currentHeight*100==(int)((currentHeight * (rate - 1) + 0)*100 / rate)){
                    currentHeight=0;
                }
            }
        }
        float ybot = 1-owner.buttonsPercent;
        float ytop = ybot-currentHeight;

        owner.buttonsPercent = (1-ytop);

        for (SelectedRowButtons srb: backEnd) {
            srb.setLocation(ytop, ybot);
            srb.setTextPaint(textPaint);
        }
    }

    public void click(MotionEvent event) {
        if (!dead) {
            for (SelectedRowButtons srb : backEnd) {
                srb.click(event);
            }
        }
    }

    public void hover(MotionEvent event) {
        if (!dead) {
            for (SelectedRowButtons srb : backEnd) {
                srb.hover(event);
            }
        }
    }

    public boolean couldClick(MotionEvent event) {
        if (!dead) {
            for (SelectedRowButtons srb : backEnd) {
                if (srb.couldClick(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void draw(Canvas canvas, Paint paint) {
        for (SelectedRowButtons srb : backEnd){
            srb.draw(canvas);
        }
    }

    public boolean done() {
        return dead && currentHeight == 0;
    }
}
