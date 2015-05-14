package cube.d.n.commoncore.v2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.v2.keyboards.KeyBoardManager;
import cube.d.n.commoncore.v2.lines.InputLine;
import cube.d.n.commoncore.v2.lines.Line;

/**
* Created by Colin_000 on 5/7/2015.
*/
public class Main extends View implements View.OnTouchListener {

    final private KeyBoardManager keyBoardManager = new KeyBoardManager();

    private ArrayList<Line> lines = new ArrayList<>();

    public float height;
    public float width;

    public Main(Context context) {
        super(context);
        init(context);
    }

    public  Main(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public  Main(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Line myLine = new InputLine(this);
        keyBoardManager.hardSet(myLine.getKeyboad());
        lines.add(myLine);
        setOnTouchListener(this);
    }


    float lastDragY;
    float lastDragX;
    float offsetY;
    float offsetX;
    float vy;
    float vx;
    long lastVelocityUpdate;
    public boolean fingerDown= false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()== MotionEvent.ACTION_DOWN){
            lastDragY = -1;
            lastDragX=-1;
            vy = 0;
            fingerDown= true;
        }

        if (event.getPointerCount() == 1) {
            // pass it on to my bros
            boolean keepGoing = true;
            if (keepGoing && keyBoardManager.getNextKeyboard() == null) {
                keepGoing = !keyBoardManager.get().onTouch(event);
            }
            if (keepGoing) {
                keepGoing = !lines.get(lines.size()-1).onTouch(event);
            }
            if (keepGoing == true){
                // we are dragging!
                if (lastDragY==-1){
                    lastDragY = event.getY();
                    lastDragX = event.getX();
                    lastVelocityUpdate = System.currentTimeMillis();
                }else{
                    updateVelocity(event);
                }
            }

        }else{
            // handle zoom or whatever

        }

        if (event.getAction()== MotionEvent.ACTION_UP){
            fingerDown= false;
        }



        return true;
    }

    private float step = 1000f / 60f;
    private void updateVelocity(MotionEvent event) {
        long now = System.currentTimeMillis();
        long timePass = now - lastVelocityUpdate;
        if (timePass != 0) {
            float stepsPass = timePass / step;
            float dy = (event.getY() - lastDragY);
            float dx = (event.getX() - lastDragX);
            offsetY += dy;
            offsetX += dx;
            float currentVy = (dy) / stepsPass;
            float currentVx = (dx) / stepsPass;

            lastDragY = event.getY();
            lastDragX = event.getX();
            lastVelocityUpdate = now;
            final float maxSteps = 5f;
            if (stepsPass < maxSteps) {
                vy = ((maxSteps - stepsPass) * vy + (stepsPass) * currentVy) / maxSteps;
                vx = ((maxSteps - stepsPass) * vx + (stepsPass) * currentVx) / maxSteps;
            } else {
                vy = currentVy;
                vx = currentVx;
            }
        }
    }

    long startTime = System.currentTimeMillis();
    int frames = 1;

    @Override
    protected void onDraw(Canvas canvas) {


        if (height == 0) {
            height = canvas.getHeight();
            offsetY = ((height - keyBoardManager.get().measureHeight())/2f)+ lines.get(lines.size()-1).measureHeight()/2f;
            offsetX = 0;
        }
        height = canvas.getHeight();
        width = canvas.getWidth();

        if (!fingerDown) {
            slide();
            snapeBack();
        }

        addToOffestY();

        float bot = offsetY;
        float left = offsetX;
        for (int i= lines.size()-1;i>=0;i--){
            Line l = lines.get(i);
            float top = bot - l.measureHeight();
            if (inScreen(l,top)){
                l.draw(canvas,top,left,new Paint());

            }
            bot= top;
        }

        keyBoardManager.draw(canvas, bot, 0, new Paint());
        // TODO this is probably really bad for CPU and GPU use
        invalidate();

        long now = System.currentTimeMillis();
        float elapsedTime = (now - startTime) / 1000f;
        frames++;
        if (frames % 100 == 0) {
            Log.i("fps", "" + frames / elapsedTime);
        }

    }

    public void toAddToOffsetY(float toAdd){
        toAddToOffsetY+=toAdd;
    }


    public void addToOffsetY(float toAdd){
        offsetY += toAdd;
    }

    float toAddToOffsetY=0;
    private void addToOffestY() {
        float toAdd =toAddToOffsetY/BaseApp.getApp().getRate();
        offsetY+= toAdd;
        toAddToOffsetY-= toAdd;
    }

    private void snapeBack() {
        float maxOffsetY;
        if (lines.size()==1) {
            maxOffsetY= height - keyBoardManager.get().measureHeight();
        }else{
            maxOffsetY = height - keyBoardManager.get().measureHeight()- 4*Line.buffer;
            for (Line l:lines){
                maxOffsetY+= l.measureHeight();
            }
        }

        if (offsetY > maxOffsetY){
            offsetY = (offsetY*BaseApp.getApp().getRate() +maxOffsetY)/(BaseApp.getApp().getRate()+1);
        }

        float minOffsetY=Math.min(height - keyBoardManager.get().measureHeight(),lines.get(lines.size()-1).stupid.get().measureHeight()+ 2*Line.buffer);

        if (offsetY < minOffsetY){
            offsetY = (offsetY*BaseApp.getApp().getRate() +minOffsetY)/(BaseApp.getApp().getRate()+1);
        }


        float maxOffsetX = 0;

        float bot = offsetY;
        for (int i= lines.size()-1;i>=0;i--){
            Line l = lines.get(i);
            float top = bot - l.measureHeight();
            if (inScreen(l,top)){
                maxOffsetX = Math.max(maxOffsetX,Math.max(0,(l.requestedWidth()-width)/2));
            }
            bot-= l.measureHeight();
        }

        if (offsetX > maxOffsetX){
            offsetX = (offsetX*BaseApp.getApp().getRate() +maxOffsetX)/(BaseApp.getApp().getRate()+1);
        }

        float minOffsetX=-maxOffsetX;

        if (offsetX < minOffsetX){
            offsetX = (offsetX*BaseApp.getApp().getRate() +minOffsetX)/(BaseApp.getApp().getRate()+1);
        }

    }

    private void slide() {
        long now = System.currentTimeMillis();
        long diff = now - lastVelocityUpdate;
        float steps = diff / step;

        double friction = .85;
        float dx = (float) (vx * ((Math.pow(friction, steps) - 1) / Math.log(friction)));
        float dy = (float) (vy * ((Math.pow(friction, steps) - 1) / Math.log(friction)));

        vx = (float) (vx * Math.pow(friction, steps));
        vy = (float) (vy * Math.pow(friction, steps));

        lastVelocityUpdate = now;
        offsetX+=dx;
        offsetY+=dy;
    }

    private boolean inScreen(Line l, float top) {
        if (top > height- keyBoardManager.get().measureHeight() &&
                (keyBoardManager.getNextKeyboard()== null || top > height- keyBoardManager.getNextKeyboard().measureHeight())){
            return false;
        }
        if (top + l.measureHeight() < 0){
            return false;
        }
        return true;
    }

    public void addLine(Line line) {
        lines.add(line);
        offsetY += line.measureHeight();
        toAddToOffsetY(-line.measureHeight());
        keyBoardManager.set(line.getKeyboad());
    }

    public void revert() {
        removeLine(lines.size() - 1);
        keyBoardManager.set(lines.get(lines.size()-1).getKeyboad());
    }

    public void removeLine(int at){
        toAddToOffsetY(lines.get(at).measureHeight());
        lines.remove(at);
    }

    public Line getLine(int at){return lines.get(at);}
    public int getLinesSize(){return lines.size();}
}
