package cube.d.n.commoncore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.keyboards.KeyBoardManager;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;
import cube.d.n.commoncore.lines.OutputLine;

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

    private TouchMode myMode;
    private double lastZoomDis = -1;
    private Point lastCenter;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()== MotionEvent.ACTION_DOWN){
            lastDragY = -1;
            lastDragX=-1;
            moveX =false;
            moveY = false;
            myMode = TouchMode.SOMEONEELSE;
            vy = 0;
            fingerDown= true;
        }


            // pass it on to my bros
            boolean keepGoing = true;
            if (keepGoing && keyBoardManager.getNextKeyboard() == null) {
                keepGoing = !keyBoardManager.get().onTouch(event);
            }
            if (keepGoing) {
                keepGoing = !lines.get(lines.size()-1).onTouch(event);
            }
        if (keepGoing == true) {
            if (event.getPointerCount() == 1) {

                // we are dragging!
                if ( myMode != TouchMode.SLIDE) {
                    myMode = TouchMode.SLIDE;
                    lastDragY = event.getY();
                    lastDragX = event.getX();
                    lastVelocityUpdate = System.currentTimeMillis();
                } else {
                    updateVelocity(event);
                }
            } else if (event.getPointerCount() == 2) {
                if (myMode != TouchMode.ZOOM) {
                    myMode=TouchMode.ZOOM;
                    lastZoomDis = getPointerDistance(event);
                    lastCenter = getCenter(event);
                } else {
                    double currentZoomDis = getPointerDistance(event);
                    Point touchCenter = getCenter(event);
                    Point screenCenter = getCenter();
                    double oldZoom = BaseApp.getApp().zoom;
                    BaseApp.getApp().zoom = BaseApp.getApp().zoom * (currentZoomDis / lastZoomDis);
                    //maybe this should be in snap back, would make sense;
                    if (BaseApp.getApp().zoom < .7) {
                        BaseApp.getApp().zoom = .7;
                    }
                    if (BaseApp.getApp().zoom > 1.5) {
                        BaseApp.getApp().zoom = 1.5;
                    }
                    lastZoomDis = currentZoomDis;
                    //float oldDisx =lastCenter.x - (screenCenter.x +offsetX);
                    float oldDisy = lastCenter.y - (screenCenter.y + offsetY);
                    //offsetX =(float) -((Algebrator.getAlgebrator().zoom/oldZoom)*(oldDisx) -touchCenter.x + screenCenter.x);
                    offsetY = (float) -((BaseApp.getApp().zoom / oldZoom) * (oldDisy) - touchCenter.y + screenCenter.y);
                    lastCenter = touchCenter;

                    for (Line l:lines) {
                        l.stupid.get().deepNeedsUpdate();
                    }
                }
            } else {
                myMode = TouchMode.NOPE;
            }
        }

        if (event.getAction()== MotionEvent.ACTION_UP){
            fingerDown= false;
        }



        return true;
    }

    private double getPointerDistance(MotionEvent event) {
        if (event.getPointerCount() != 2){
            Log.e("SuperView.getPointerDistance","pointer count should be 2");
            return -1;
        }else {
            float dx = event.getX(0) - event.getX(1);
            float dy = event.getY(0) - event.getY(1);
            return Math.sqrt(dx*dx + dy*dy);
        }

    }

    private Point getCenter() {
        Point result = new Point();
        result.x = (int) (width / 2 );
        result.y = (int) (height / 3 );
        return result;
    }

    private Point getCenter(MotionEvent event) {
        Point result = new Point();
        if (event.getPointerCount() != 2){
            Log.e("SuperView.getPointerDistance","pointer count should be 2");
        }else {
            result.x = (int)(event.getX(0) + event.getX(1))/2;
            result.y = (int)(event.getY(0) + event.getY(1))/2;
        }
        return result;
    }

    private float step = 1000f / 60f;
    private boolean moveX =false;
    private boolean moveY = false;
    private float minMove = 15*BaseApp.getApp().getDpi();
    private void updateVelocity(MotionEvent event) {

        if (!moveX && !moveY){
            float dy = (event.getY() - lastDragY);
            float dx = (event.getX() - lastDragX);
            if (Math.abs(dx)> Math.abs(dy) &&Math.abs(dx) > minMove ){
                moveX=true;
            }else if ( Math.abs(dy)>minMove){
                moveY=true;
            }
        }

        final float maxSteps = 5f;
        long now = System.currentTimeMillis();
        long timePass = now - lastVelocityUpdate;
        if (timePass != 0) {
            float stepsPass = timePass / step;
            if (moveX){

                float dx = (event.getX() - lastDragX);
                offsetX += dx;
                float currentVx = (dx) / stepsPass;
                lastDragX = event.getX();

                if (stepsPass < maxSteps) {
                    vx = ((maxSteps - stepsPass) * vx + (stepsPass) * currentVx) / maxSteps;
                } else {
                    vx = currentVx;
                }
            }
            if (moveY) {
                float dy = (event.getY() - lastDragY);
                offsetY += dy;
                float currentVy = (dy) / stepsPass;

                lastDragY = event.getY();
                lastVelocityUpdate = now;
                if (stepsPass < maxSteps) {
                    vy = ((maxSteps - stepsPass) * vy + (stepsPass) * currentVy) / maxSteps;
                } else {
                    vy = currentVy;
                }
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

        Rect r = new Rect(0,0,(int)width,(int)height);
        Paint p = new Paint();
        p.setColor(0xffffffff);
        canvas.drawRect(r,p);

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
            // we know the first line is input
            maxOffsetY = height - keyBoardManager.get().measureHeight()- lines.get(0).measureHeight();
            for (Line l:lines){
                maxOffsetY+= l.measureHeight();
            }
        }

        if (offsetY > maxOffsetY){
            offsetY = (offsetY*BaseApp.getApp().getRate() +maxOffsetY)/(BaseApp.getApp().getRate()+1);
        }

        float minOffsetY=Math.min(height - keyBoardManager.get().measureHeight(),lines.get(lines.size()-1).stupid.get().measureHeight()+ 2*Line. getBuffer()
        + (lines.get(lines.size()-1) instanceof InputLine?2*Line. getBuffer():0)
        );

        if (offsetY < minOffsetY){
            offsetY = (offsetY*BaseApp.getApp().getRate() +minOffsetY)/(BaseApp.getApp().getRate()+1);
        }


        float maxOffsetX = 0;
        float minOffsetX = 0;

        float bot = offsetY;
        for (int i= lines.size()-1;i>=0;i--){
            Line l = lines.get(i);
            float top = bot - l.measureHeight();
            if (inScreen(l,top)){
                maxOffsetX = Math.max(maxOffsetX,l.requestedMaxX());
                minOffsetX = Math.min(minOffsetX,l.requestedMinX());
            }
            bot-= l.measureHeight();
        }

        if (offsetX > maxOffsetX){
            offsetX = (offsetX*BaseApp.getApp().getRate() +maxOffsetX)/(BaseApp.getApp().getRate()+1);
        }


        if (offsetX < minOffsetX){
            offsetX = (offsetX*BaseApp.getApp().getRate() +minOffsetX)/(BaseApp.getApp().getRate()+1);
        }

    }

    private void slide() {
        long now = System.currentTimeMillis();
        long diff = now - lastVelocityUpdate;
        float steps = diff / step;

        double friction = .95;
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

    public Line lastLine() {
        return getLine(getLinesSize()-1);
    }

    public Equation getLast(){
        if (getLinesSize()!=1){
            Line targetLine = getLine(getLinesSize()-2);
            if (targetLine instanceof OutputLine){
                Equation result =targetLine.stupid.get().copy();
                result.updateOwner(lastLine());
                return result;
            }
        }
        return null;



    }
}
