package cube.d.n.commoncore;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.LeafEquation;
import cube.d.n.commoncore.eq.any.MinusEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.SignEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.eq.write.WritingSqrtEquation;
import cube.d.n.commoncore.keyboards.KeyBoardManager;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.AlgebraLineNoKeyBoard;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.CalcLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.HiddenInputLine;
import cube.d.n.commoncore.lines.ImageLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;
import cube.d.n.commoncore.lines.OutputLine;

/**
* Created by Colin_000 on 5/7/2015.
*/
public class Main extends View implements View.OnTouchListener, NoScroll {

    final public KeyBoardManager keyBoardManager = new KeyBoardManager();

    final ArrayList<Line> lines = new ArrayList<>();

    public float height;
    public float width;

    public Main(Context context) {
        super(context);
        init(context,InputLineEnum.INPUT);
    }

    public Main(Context context,InputLineEnum startLine) {
        super(context);
        init(context,startLine);
    }

    public  Main(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context, attrs,InputLineEnum.INPUT);
    }

    public void init(Context context, AttributeSet attrs,InputLineEnum startLayout) {
        Log.d("main init","main init");
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Main,
                0, 0);

        try {
            startLayout = InputLineEnum.values()[a.getInteger(R.styleable.Main_start_layout, 0)];
        } finally {
            a.recycle();
        }

        init(context,startLayout);
    }

    public  Main(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context, attrs,InputLineEnum.INPUT);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        this.setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
//    }

    private void init(Context context, InputLineEnum startLine) {
        Log.d("life is complex",""+startLine);
        if ( startLine == InputLineEnum.INPUT) {
            lines.add(new InputLine(this));
        }else if (startLine == InputLineEnum.CALC){
            lines.add(new CalcLine(this));
        }else if (startLine == InputLineEnum.PROBLEM_WC){
            lines.add(new ImageLine(this));
            lines.add(new InputLine(this));
        }else if (startLine == InputLineEnum.PROBLEM_WCI){
            lines.add(new ImageLine(this));
            lines.add(new InputLine(this));
        }else if (startLine == InputLineEnum.PROBLEM_WE){
            lines.add(new ImageLine(this));
            lines.add(new HiddenInputLine(this));
            lines.add(new AlgebraLine(this));
        }else if (startLine == InputLineEnum.TUT_E){
            lines.add(new HiddenInputLine(this));
            lines.add(new AlgebraLineNoKeyBoard(this));
        }else{
            Log.e("main.init","InputLineEnum not recognized");
            lines.add(new InputLine(this));
        }
        keyBoardManager.hardSet(lastLine().getKeyboad());
        setOnTouchListener(this);
    }

    float lastDragY;
    float lastDragX;
    float offsetY;
    float vy;
    long lastVelocityUpdate;
    public boolean fingerDown= false;
    public boolean trackFinger = false;
    public float trackFingerX = -1;
    public float trackFingerY = -1;

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
            stopSliding();
            fingerDown= true;
        }

        if (event.getPointerCount() == 1) {
            trackFingerX = event.getX();
            trackFingerY = event.getY();
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
                        //offsetY = (float) (offsetY * (currentZoomDis / lastZoomDis)*(.7f/BaseApp.getApp().zoom));
                        BaseApp.getApp().zoom = .7;
                    }else if (BaseApp.getApp().zoom > 1.5) {
                        //offsetY = (float) (offsetY * (currentZoomDis / lastZoomDis)*(1.5f/BaseApp.getApp().zoom));
                        BaseApp.getApp().zoom = 1.5;
                    }
                    //else{
                        //offsetY = (float) (offsetY * (currentZoomDis / lastZoomDis));
                    //}
                    lastZoomDis = currentZoomDis;
                    //float oldDisx =lastCenter.x - (screenCenter.x +offsetX);
                    float oldDisy = offsetY - (height/2f);
                    //offsetX =(float) -((Algebrator.getAlgebrator().zoom/oldZoom)*(oldDisx) -touchCenter.x + screenCenter.x);
                    float newDisy = (float) ((oldDisy/oldZoom)*BaseApp.getApp().zoom);
                    offsetY += newDisy-oldDisy;

                    lastCenter = touchCenter;

                    for (Line l:lines) {
                        if (l instanceof  EquationLine) {
                            ((EquationLine)l).stupid.get().deepNeedsUpdate();
                        }
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
                for (int i =0;i<lines.size();i++){
                    if (lines.get(i) instanceof  EquationLine) {
                        EquationLine l = (EquationLine)lines.get(i);
                        if ((l.getY() + (l.measureHeight() / 2f) > event.getY() || i == lines.size() - 1) &&
                                l.getY() - (l.measureHeight() / 2f) < event.getY()) {//|| i == 0
                            nextInputLine(i).updateVeloctiyX(stepsPass, maxSteps, dx);
                            break;
                        }
                    }
                }
                lastDragX = event.getX();
                lastVelocityUpdate = now;
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

    private void stopSliding() {
        vy=0;
        for (Line l : lines){
                if (l instanceof InputLine && !(l instanceof BothSidesLine)) {
                    ((InputLine) l).stopSliding();
                }
        }
    }

    long startTime = System.currentTimeMillis();
    int frames = 1;
    Equation lastEq = null;

    @Override
    protected void onDraw(Canvas canvas) {


        if (height == 0) {
            height = canvas.getHeight();
            offsetY = height/3f;
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
        addToOffestX();

        float bot = offsetY;
        float left;
        for (int i= lines.size()-1;i>=0;i--){
            if (lines.get(i) instanceof HasHeaderLine) {
                left = nextInputLine(i).getOffsetX();
            }else{
                left = 0;
            }
            Line l = lines.get(i);
            float top = bot - l.measureHeight();
            if (i==lines.size()-1){
                if (l instanceof EquationLine) {
                    top += ((EquationLine)l).stupid.get().measureHeightLower();
                }
            }
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

        if (lastLine() instanceof EquationLine &&(lastEq == null ||   !((EquationLine)lastLine()).stupid.get().reallySame(lastEq)) ){
            lastEq = ((EquationLine)lastLine()).stupid.get().copy();
            Log.i("current", lastEq.toString()+"");
        }



        if (trackFinger && fingerDown){
            Paint fingerPaint = new Paint();
            fingerPaint.setARGB(0xff/2,(0xd5-(0xff/2))*2,0x04,0x06);
            canvas.drawCircle(trackFingerX,trackFingerY,20*BaseApp.getApp().getDpi(),fingerPaint);
        }

    }




    public InputLine nextInputLine(int at) {
        for (int i= at;i>=0;i--){
            if (lines.get(i) instanceof InputLine && !(lines.get(i) instanceof BothSidesLine)){
                return (InputLine) lines.get(i);
            }
        }
        return null;
    }


    public void addToOffsetY(float toAdd){
        offsetY += toAdd;
    }

    public void toAddToOffsetY(float toAdd){
        toAddToOffsetY+=toAdd;
    }

    float toAddToOffsetY=0;
    private void addToOffestY() {
        float toAdd =toAddToOffsetY/BaseApp.getApp().getRate();
        offsetY+= toAdd;
        toAddToOffsetY-= toAdd;
    }

    public void toAddToOffsetX(EquationLine l,float toAdd){
        int at = lines.indexOf(l);
        InputLine inputLine =  nextInputLine(at);
        inputLine.toAddToOffsetX(toAdd);
    }

    private void addToOffestX() {
        for (Line l:lines){
            if ( l instanceof  InputLine  && !(l instanceof BothSidesLine)){
                ((InputLine) l).addToOffsetX();
            }
        }

    }

    private void snapeBack() {
        float maxOffsetY;
        if (lines.size()==1) {
            maxOffsetY= height - keyBoardManager.get().measureHeight();
        }else{
            // we know the first line is input
            maxOffsetY = Math.max(0,height - keyBoardManager.get().measureHeight()- lines.get(0).measureHeight() - 2* EquationLine.getBuffer());
            for (Line l:lines){
                maxOffsetY+= l.measureHeight();
            }
//            if (bitMap != null){
//                maxOffsetY += bitMap.getHeight();
//            }
        }

        if (offsetY > maxOffsetY){
            offsetY = (offsetY*BaseApp.getApp().getRate() +maxOffsetY)/(BaseApp.getApp().getRate()+1);
            vy =0;
        }

        float minOffsetY=Math.min(
                height - keyBoardManager.get().measureHeight() - (lines.get(lines.size()-1).measureHeight()/2f),
                (lines.get(lines.size()-1).measureHeight()/2f) + (lines.get(lines.size()-1) instanceof InputLine? EquationLine.getBuffer():0)
        );

        if (offsetY < minOffsetY){
            offsetY = (offsetY*BaseApp.getApp().getRate() +minOffsetY)/(BaseApp.getApp().getRate()+1);
            vy =0;
        }

        for (int i= lines.size()-1;i>=0;i--){
            Line l = lines.get(i);
            if ( l instanceof InputLine && !(l instanceof BothSidesLine)){
                ((InputLine)l).snapBack();
            }
        }


    }

    private void slide() {
        long now = System.currentTimeMillis();
        long diff = now - lastVelocityUpdate;
        float steps = diff / step;

        double friction = .95;

        float dy = (float) (vy * ((Math.pow(friction, steps) - 1) / Math.log(friction)));

        for (int i= lines.size()-1;i>=0;i--){
            Line l = lines.get(i);
            if ( l instanceof InputLine  && !(l instanceof BothSidesLine)){
                ((InputLine)l).slide(friction,steps);
            }
        }

        vy = (float) (vy * Math.pow(friction, steps));

        lastVelocityUpdate = now;

        offsetY+=dy;
    }

    private boolean inScreen(Line l, float top) {
        if (top > height- keyBoardManager.get().measureHeight() &&
                (keyBoardManager.getNextKeyboard()== null || top > height- keyBoardManager.getNextKeyboard().measureHeight())){
            return false;
        }
        if (top + l.measureHeight() < top()){
            return false;
        }
        return true;
    }

    private float top() {
        //if (bitMap == null) {
            return 0;
        //}else{
        //    return bitMap.getHeight();
        //}
    }

    public void addLine(EquationLine line) {
        if(lastLine().getKeyboad() != null) {
            lastLine().getKeyboad().deactivate();
        }
        lines.add(line);
        offsetY += line.measureHeight();
        toAddToOffsetY(-line.measureHeight());
        keyBoardManager.set(line.getKeyboad());
    }

    public void revert() {
        lastLine().getKeyboad().deactivate();
        removeLine(lines.size() - 1);
        keyBoardManager.set(lines.get(lines.size()-1).getKeyboad());
        lines.get(lines.size()-1).getKeyboad().reactivate();
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
                EquationLine el = (OutputLine)targetLine;

                Equation result =el.stupid.get().copy();
                result = addParnsIfNeeded( convert(result));
                result.updateOwner((EquationLine)lastLine());
                return result;
            }
        }
        return null;

    }


    //TODO this needs to hanlde ()
    private Equation convert(Equation eq) {
        Equation toAdd;
        Equation write = null;
        InputLine owner = (InputLine)lastLine();
        if (eq instanceof EqualsEquation || eq instanceof AddEquation || eq instanceof MultiEquation){

            String s ="";
            if (eq instanceof EqualsEquation){
                s ="=";
            }
            if (eq instanceof AddEquation){
                s ="+";
            }
            if (eq instanceof MultiEquation){
                s ="*";
            }

            write = new WritingEquation(owner);
            for (Equation e: eq){
                toAdd = convert(e);
                if (toAdd instanceof WritingEquation){
                    write.addAll(toAdd);
                }else{
                    write.add(toAdd);
                }
                if (eq.indexOf(e) != eq.size()-1){
                    toAdd = new WritingLeafEquation(s,owner);
                    write.add(toAdd);
                }
            }

        }else if (eq instanceof SignEquation){
            write = new WritingEquation(owner);
            String s ="";
            if (eq instanceof MinusEquation){
                s ="-";
            }
            if (eq instanceof PlusMinusEquation){
                s ="\u00B1";
            }
            toAdd = new WritingLeafEquation(s,owner);
            write.add(toAdd);
            toAdd = convert(eq.get(0));
            write.add(toAdd);

        }else if (eq instanceof PowerEquation && ((PowerEquation) eq).isSqrt()){
            write = new WritingEquation(owner);
            write.add(new WritingSqrtEquation(owner));
            write.add(convert(eq.get(0)));
            write.add(new WritingPraEquation(false,owner));
        }else if(eq instanceof BinaryEquation){
            if (eq instanceof DivEquation){
                write = new DivEquation(owner);
            }
            if (eq instanceof PowerEquation){
                write = new PowerEquation(owner);
            }
            for (Equation e: eq){
                toAdd = convert(e);
                write.add(toAdd);
            }
        }else if (eq instanceof LeafEquation){
            write = eq.copy();
        }
        if (eq.parenthesis()){
            if (write instanceof WritingEquation){
                write.add(0,new WritingPraEquation(true,owner));
                write.add(new WritingPraEquation(false,owner));
            }else{
                Equation old = write;
                write = new WritingEquation(owner);
                write.add(new WritingPraEquation(true,owner));
                write.add(old);
                write.add(new WritingPraEquation(false,owner));
            }

        }

        return write;
    }


    public Equation addParnsIfNeeded(Equation write){
        if (write.size() >1) {

            InputLine owner = (InputLine) lastLine();
            write.add(0, new WritingPraEquation(true, owner));
            write.add(new WritingPraEquation(false, owner));
        }
        return write;
    }

    public int getLineIndex(EquationLine line) {
        return lines.indexOf(line);
    }

    public float getOffsetY() {
        return offsetY;
    }

    public ImageLine getProblemImage() {
        if (lines.get(0) instanceof  ImageLine){
            return (ImageLine)lines.get(0);
        }
        Log.e("getProblemImage", "bad to the bone");
        return null;
    }

    public void initWE(Equation equation) {
        ((HiddenInputLine)lines.get(1)).stupid.set(equation.copy());
        ((AlgebraLine)lines.get(2)).initEquation(equation.copy());
    }

    public void initE(Equation equation) {
        ((HiddenInputLine)lines.get(0)).stupid.set(equation.copy());
        ((AlgebraLine)lines.get(1)).initEquation(equation.copy());
    }
}
