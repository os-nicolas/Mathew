package cube.d.n.commoncore;

import android.app.Activity;
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
import android.view.ViewParent;
import android.widget.EditText;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.Pro.ACosEquation;
import cube.d.n.commoncore.eq.Pro.ASineEquation;
import cube.d.n.commoncore.eq.Pro.ATanEquation;
import cube.d.n.commoncore.eq.Pro.CosEquation;
import cube.d.n.commoncore.eq.Pro.SineEquation;
import cube.d.n.commoncore.eq.Pro.TanEquation;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
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
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.eq.write.WritingSqrtEquation;
import cube.d.n.commoncore.keyboards.AlgebraKeyboardNoReturn;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.KeyBoardManager;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.AlgebraLineNoKeyBoard;
import cube.d.n.commoncore.lines.AlgebraLineNoReturn;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.CalcLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.HiddenInputLine;
import cube.d.n.commoncore.lines.ImageLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;
import cube.d.n.commoncore.lines.OutputLine;
import cube.d.n.commoncore.lines.TrigInput;
import cube.d.n.commoncore.tuts.TutMainFrag;
import cube.d.n.commoncore.tuts.YayTutView;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class Main extends View implements View.OnTouchListener, NoScroll {

    public boolean allowRevert = true;
    public boolean allowPopups = true;
    public boolean allowTouch = true;
    public boolean allowSolve = false;
    public boolean allowDoubleTap = true;
    public boolean allowDrag = true;

    final public KeyBoardManager keyBoardManager = new KeyBoardManager();
    public ProgressManager progressManager = new ProgressManager();

    final ArrayList<Line> lines = new ArrayList<>();

    public float height;
    public float width;
    private InputLineEnum startLine ;
    public ViewParent holder;

    public Main(Context context) {
        super(context);
        init(context, InputLineEnum.INPUT);
    }

    public Main(Context context, InputLineEnum startLine) {
        super(context);
        init(context, startLine);
    }

    public Main(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, InputLineEnum.INPUT);
    }

    public void init(Context context, AttributeSet attrs, InputLineEnum startLayout) {
        Log.d("main init", "main init");
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Main,
                0, 0);

        try {
            startLayout = InputLineEnum.values()[a.getInteger(R.styleable.Main_start_layout, 0)];
        } finally {
            a.recycle();
        }

        init(context, startLayout);
    }

    public Main(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, InputLineEnum.INPUT);
    }

    private void init(Context context, InputLineEnum startLine) {

        Log.d("life is complex", "" + startLine);
        this.startLine=startLine;
        initStartLines(startLine);
        keyBoardManager.hardSet(lastLine().getKeyboad());
        setOnTouchListener(this);
    }

    private void initStartLines(InputLineEnum startLine) {
        if (startLine == InputLineEnum.TRIG) {
            lines.add(new TrigInput(this));
        } else if (startLine == InputLineEnum.INPUT) {
            lines.add(new InputLine(this));
        } else if (startLine == InputLineEnum.CALC) {
            lines.add(new CalcLine(this));
        } else if (startLine == InputLineEnum.PROBLEM_WC) {
            lines.add(new ImageLine(this));
            lines.add(new InputLine(this));
        } else if (startLine == InputLineEnum.PROBLEM_WCI) {
            lines.add(new ImageLine(this));
            lines.add(new InputLine(this));
        } else if (startLine == InputLineEnum.PROBLEM_WE) {
            lines.add(new ImageLine(this));
            lines.add(new HiddenInputLine(this));
            lines.add(new AlgebraLineNoReturn(this));
        } else if (startLine == InputLineEnum.TUT_E) {
            lines.add(new HiddenInputLine(this));
            lines.add(new AlgebraLineNoKeyBoard(this));
        } else if (startLine == InputLineEnum.TUT_EK) {
            lines.add(new HiddenInputLine(this));
            lines.add(new AlgebraLineNoReturn(this));
        } else if (startLine == InputLineEnum.PROBLEM_WI) {
            lines.add(new ImageLine(this));
            lines.add(new InputLine(this));
        }else {
            Log.e("main.init", "InputLineEnum not recognized");
            lines.add(new InputLine(this));
        }
    }

    float lastDragY;
    float lastDragX;
    float offsetY;
    float vy;
    long lastVelocityUpdate;
    public boolean fingerDown = false;
    public boolean trackFinger = false;
    public float trackFingerX = -1;
    public float trackFingerY = -1;

    private TouchMode myMode;
    private double lastZoomDis = -1;
    private Point lastCenter;

    @Override
    public synchronized boolean onTouch(View v, MotionEvent event) {
        // we need to finish up a touch that the user has started
        // even if we are not allowing touches
        if (allowTouch || event.getAction() != MotionEvent.ACTION_DOWN) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                lastDragY = -1;
                lastDragX = -1;
                moveX = false;
                moveY = false;
                myMode = TouchMode.SOMEONEELSE;
                stopSliding();
                fingerDown = true;
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
                keepGoing = !lines.get(lines.size() - 1).onTouch(event);
            }
            if (keepGoing == true) {
                if (event.getPointerCount() == 1) {

                    // we are dragging!
                    if (myMode != TouchMode.SLIDE) {
                        myMode = TouchMode.SLIDE;
                        lastDragY = event.getY();
                        lastDragX = event.getX();
                        lastVelocityUpdate = System.currentTimeMillis();
                    } else {
                        updateVelocity(event);
                    }
                } else if (event.getPointerCount() == 2) {
                    if (myMode != TouchMode.ZOOM) {
                        myMode = TouchMode.ZOOM;
                        lastZoomDis = getPointerDistance(event);
                        lastCenter = getCenter(event);
                    } else {
                        double currentZoomDis = getPointerDistance(event);
                        Point touchCenter = getCenter(event);
                        Point screenCenter = getCenter();
                        double oldZoom = BaseApp.getApp().zoom;
                        BaseApp.getApp().zoom = BaseApp.getApp().zoom * (currentZoomDis / lastZoomDis);


                        //maybe this should be in snap back, would make sense;
                        float minZoom = .5f;
                        float maxZoom = 2f;
                        if (BaseApp.getApp().zoom < minZoom) {
                            //offsetY = (float) (offsetY * (currentZoomDis / lastZoomDis)*(.7f/BaseApp.getApp().zoom));
                            BaseApp.getApp().zoom = minZoom;
                        } else if (BaseApp.getApp().zoom > maxZoom) {
                            //offsetY = (float) (offsetY * (currentZoomDis / lastZoomDis)*(1.5f/BaseApp.getApp().zoom));
                            BaseApp.getApp().zoom = maxZoom;
                        }
                        //else{
                        //offsetY = (float) (offsetY * (currentZoomDis / lastZoomDis));
                        //}
                        lastZoomDis = currentZoomDis;
                        //float oldDisx =lastCenter.x - (screenCenter.x +offsetX);
                        float oldDisy = offsetY - (height / 2f);
                        //offsetX =(float) -((Algebrator.getAlgebrator().zoom/oldZoom)*(oldDisx) -touchCenter.x + screenCenter.x);
                        float newDisy = (float) ((oldDisy / oldZoom) * BaseApp.getApp().zoom);
                        offsetY += newDisy - oldDisy;

                        lastCenter = touchCenter;

                        for (Line l : lines) {
                            if (l instanceof EquationLine) {
                                ((EquationLine) l).stupid.get().deepNeedsUpdate();
                            }
                        }
                    }
                } else {
                    myMode = TouchMode.NOPE;
                }
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                fingerDown = false;
            }

            //Log.d("Main.onTouch","closing out on touch");
            return true;
        } else {
            //Log.d("Main.onTouch","closing out on touch");
            return false;
        }
    }

    private double getPointerDistance(MotionEvent event) {
        if (event.getPointerCount() != 2) {
            Log.e("SuperView.getPointerDistance", "pointer count should be 2");
            return -1;
        } else {
            float dx = event.getX(0) - event.getX(1);
            float dy = event.getY(0) - event.getY(1);
            return Math.sqrt(dx * dx + dy * dy);
        }

    }

    private Point getCenter() {
        Point result = new Point();
        result.x = (int) (width / 2);
        result.y = (int) (height / 3);
        return result;
    }

    private Point getCenter(MotionEvent event) {
        Point result = new Point();
        if (event.getPointerCount() != 2) {
            Log.e("SuperView.getPointerDistance", "pointer count should be 2");
        } else {
            result.x = (int) (event.getX(0) + event.getX(1)) / 2;
            result.y = (int) (event.getY(0) + event.getY(1)) / 2;
        }
        return result;
    }

    private float step = 1000f / 60f;
    private boolean moveX = false;
    private boolean moveY = false;
    private float minMove = 15 * BaseApp.getApp().getDpi();

    private void updateVelocity(MotionEvent event) {

        if (!moveX && !moveY) {
            float dy = (event.getY() - lastDragY);
            float dx = (event.getX() - lastDragX);
            if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > minMove) {
                moveX = true;

            } else if (Math.abs(dy) > minMove) {
                moveY = true;

            }
        }

        final float maxSteps = 5f;
        long now = System.currentTimeMillis();
        long timePass = now - lastVelocityUpdate;
        if (timePass != 0) {
            float stepsPass = timePass / step;
            if (moveX) {
                float dx = (event.getX() - lastDragX);
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i) instanceof EquationLine) {
                        EquationLine l = (EquationLine) lines.get(i);
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
        vy = 0;
        for (Line l : lines) {
            if (l instanceof InputLine && !(l instanceof BothSidesLine)) {
                ((InputLine) l).stopSliding();
            }
        }
    }

    long startTime = System.currentTimeMillis();
    int frames = 1;
    Equation lastEq = null;

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //Log.d("draw","I drew!");
        width = canvas.getWidth();
        if (height == 0) {
            height = canvas.getHeight();
            initOffset();
        }
        if (height != canvas.getHeight()){
            //Log.d("setting from height, width" ,height + "," + width);
            height = canvas.getHeight();
            width = canvas.getWidth();
            //Log.d("setting to height, width" ,height + "," + width);
        }

        Rect r = new Rect(0, 0, (int) width, (int) height);
        Paint p = new Paint();
        p.setColor(0xffffffff);
        canvas.drawRect(r, p);


        if (!fingerDown) {
            slide();
            snapeBack();
        }

        addToOffestY();
        addToOffestX();

        float bot = offsetY;
        float left;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (lines.get(i) instanceof HasHeaderLine) {
                left = nextInputLine(i).getOffsetX();
            } else {
                left = 0;
            }
            Line l = lines.get(i);
            float top = bot - l.measureHeight();
            if (i == lines.size() - 1) {
                if (l instanceof EquationLine) {
                    top += ((EquationLine) l).stupid.get().measureHeightLower();
                }
            }
            if (inScreen(l, top)) {
                //Log.d("started drawing line",l.toString());
                l.draw(canvas, top, left, new Paint());
                //Log.d("drew line",l.toString());

            }
            bot = top;
        }

        //Log.d("started drawing keyboaed","yep");
        keyBoardManager.draw(canvas, bot, 0, new Paint());

        //Log.d("drew keyboaed","yep");
        progressManager.draw(canvas);
        // TODO this is probably really bad for CPU and GPU use
        invalidate();

        long now = System.currentTimeMillis();
        float elapsedTime = (now - startTime) / 1000f;
        frames++;
        if (frames % 100 == 0) {
            Log.i("fps", "" + frames / elapsedTime);
        }

        if (lastLine() instanceof EquationLine && (lastEq == null || !((EquationLine) lastLine()).stupid.get().reallySame(lastEq))) {
            lastEq = ((EquationLine) lastLine()).stupid.get().copy();
            Log.i("current", lastEq.toString() + "");
        }


        if (trackFinger && fingerDown) {
            Paint fingerPaint = new Paint();
            fingerPaint.setARGB(0xff / 2, (0xd5 - (0xff / 2)) * 2, 0x04, 0x06);
            canvas.drawCircle(trackFingerX, trackFingerY, 20 * BaseApp.getApp().getDpi(), fingerPaint);
        }

    }

    public void initOffset() {
        if (startLine == InputLineEnum.CALC || startLine == InputLineEnum.INPUT) {
            offsetY = height / 3f;
        }else if(startLine == InputLineEnum.PROBLEM_WE || startLine == InputLineEnum.PROBLEM_WI){
            float tolHeight =0;
            for (Line l: lines){
                if (l instanceof ImageLine){
                    ((ImageLine) l).updateBitMap((int)width);
                }
                tolHeight += l.measureHeight();
            }
            offsetY = tolHeight;
        }
    }


    public InputLine nextInputLine(int at) {
        for (int i = at; i >= 0; i--) {
            if (lines.get(i) instanceof InputLine && !(lines.get(i) instanceof BothSidesLine)) {
                return (InputLine) lines.get(i);
            }
        }
        return null;
    }


    public void addToOffsetY(float toAdd) {
        offsetY += toAdd;
    }

    public void toAddToOffsetY(float toAdd) {
        toAddToOffsetY += toAdd;
    }

    float toAddToOffsetY = 0;

    private void addToOffestY() {
        float toAdd = toAddToOffsetY / BaseApp.getApp().getRate();
        offsetY += toAdd;
        toAddToOffsetY -= toAdd;
    }

    public void toAddToOffsetX(EquationLine l, float toAdd) {
        int at = lines.indexOf(l);
        InputLine inputLine = nextInputLine(at);
        inputLine.toAddToOffsetX(toAdd);
    }

    private void addToOffestX() {
        for (Line l : lines) {
            if (l instanceof InputLine && !(l instanceof BothSidesLine)) {
                ((InputLine) l).addToOffsetX();
            }
        }

    }

    private void snapeBack() {
        float maxOffsetY;
        if (lines.size() == 1) {
            maxOffsetY = height - keyBoardManager.get().measureHeight();
        } else {
            maxOffsetY = height - keyBoardManager.get().measureHeight();// - lines.get(0).measureHeight() - lines.get(0).measureHeight() - 2* EquationLine.getBuffer()
            if (lines.get(0) instanceof HiddenInputLine) {
                maxOffsetY -= lines.get(1).measureHeight();//+ 2* EquationLine.getBuffer();
            } else if (lines.get(0) instanceof InputLine) {
                maxOffsetY -= lines.get(0).measureHeight() + 2 * EquationLine.getBuffer();
            } else {
                maxOffsetY -= lines.get(0).measureHeight();
            }
            maxOffsetY = Math.max(0, maxOffsetY);


            // this has throw concurrent modification
            for (int i =0; i< lines.size(); i++) {
                maxOffsetY += lines.get(i).measureHeight();
            }
//            if (bitMap != null){
//                maxOffsetY += bitMap.getHeight();
//            }
        }

        if (offsetY > maxOffsetY) {
            offsetY = (offsetY * BaseApp.getApp().getRate() + maxOffsetY) / (BaseApp.getApp().getRate() + 1);
            vy = 0;
        }

        float minOffsetY = Math.min(
                height - keyBoardManager.get().measureHeight() - (lines.get(lines.size() - 1).measureHeight() / 2f),
                (lines.get(lines.size() - 1).measureHeight() / 2f) + (lines.get(lines.size() - 1) instanceof InputLine ? EquationLine.getBuffer() : 0)
        );

        if (offsetY < minOffsetY) {
            offsetY = (offsetY * BaseApp.getApp().getRate() + minOffsetY) / (BaseApp.getApp().getRate() + 1);
            vy = 0;
        }

        for (int i = lines.size() - 1; i >= 0; i--) {
            Line l = lines.get(i);
            if (l instanceof InputLine && !(l instanceof BothSidesLine)) {
                ((InputLine) l).snapBack();
            }
        }
    }

    private void slide() {
        long now = System.currentTimeMillis();
        long diff = now - lastVelocityUpdate;
        float steps = diff / step;

        double friction = .95;

        float dy = (float) (vy * ((Math.pow(friction, steps) - 1) / Math.log(friction)));

        for (int i = lines.size() - 1; i >= 0; i--) {
            Line l = lines.get(i);
            if (l instanceof InputLine && !(l instanceof BothSidesLine)) {
                ((InputLine) l).slide(friction, steps);
            }
        }

        vy = (float) (vy * Math.pow(friction, steps));

        lastVelocityUpdate = now;

        offsetY += dy;
    }

    private boolean inScreen(Line l, float top) {
        if (top > height - keyBoardManager.get().measureHeight() &&
                (keyBoardManager.getNextKeyboard() == null || top > height - keyBoardManager.getNextKeyboard().measureHeight())) {
            return false;
        }
        if (top + l.measureHeight() < top()) {
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
        if (lastLine().getKeyboad() != null) {
            lastLine().getKeyboad().deactivate();
        }
        lines.add(line);
        offsetY += line.measureHeight();
        toAddToOffsetY(-line.measureHeight());
        keyBoardManager.set(line.getKeyboad());
        couldHaveSolved(line.stupid.get().copy());

    }

    public void revert() {
        lastLine().getKeyboad().deactivate();
        removeLine(lines.size() - 1);
        keyBoardManager.set(lines.get(lines.size() - 1).getKeyboad());
        lines.get(lines.size() - 1).getKeyboad().reactivate();
    }

    public void removeLine(int at) {

        offsetY -= lines.get(at).measureHeight();
        toAddToOffsetY(lines.get(at).measureHeight());
        lines.remove(at);
    }

    public Line getLine(int at) {
        return lines.get(at);
    }

    public int getLinesSize() {
        return lines.size();
    }

    public Line lastLine() {
        return getLine(getLinesSize() - 1);
    }

    public Equation getLast() {
        if (getLinesSize() != 1) {
            Line targetLine = getLine(getLinesSize() - 2);
            //TODO remove this limitation
            if (targetLine instanceof EquationLine) {
                EquationLine el = (EquationLine) targetLine;

                Equation result = el.stupid.get().copy();
                if (result instanceof EqualsEquation){
                    if (result.get(0) instanceof VarEquation){
                        result = convert(result.get(1));//addParnsIfNeeded()
                        result.updateOwner((EquationLine) lastLine());
                        return result;
                    }else if (result.get(1) instanceof VarEquation){
                        result = convert(result.get(0));//addParnsIfNeeded()
                        result.updateOwner((EquationLine) lastLine());
                        return result;
                    }else {
                        return null;
                    }
                }else {

                    result = convert(result);//addParnsIfNeeded()
                    result.updateOwner((EquationLine) lastLine());
                    return result;
                }
            }
        }
        return null;

    }


    //TODO this needs to hanlde ()
    private Equation convert(Equation eq) {
        Equation toAdd;
        Equation write = null;
        InputLine owner = (InputLine) lastLine();
        if (eq instanceof EqualsEquation || eq instanceof AddEquation || eq instanceof MultiEquation) {

            String s = "";
            if (eq instanceof EqualsEquation) {
                s = "=";
            }
            if (eq instanceof AddEquation) {
                s = "+";
            }
            if (eq instanceof MultiEquation) {
                s = BaseApp.getApp().getMultiSymbol();
            }

            write = new WritingEquation(owner);
            for (Equation e : eq) {
                toAdd = convert(e);
                if (toAdd instanceof WritingEquation) {
                    write.addAll(toAdd);
                } else {
                    write.add(toAdd);
                }
                if (eq.indexOf(e) != eq.size() - 1) {
                    toAdd = new WritingLeafEquation(s, owner);
                    write.add(toAdd);
                }
            }

        } else if (eq instanceof SignEquation) {
            write = new WritingEquation(owner);
            String s = "";
            if (eq instanceof MinusEquation) {
                s = "-";
            }
            if (eq instanceof PlusMinusEquation) {
                s = "\u00B1";
            }
            toAdd = new WritingLeafEquation(s, owner);
            write.add(toAdd);
            toAdd = convert(eq.get(0));
            write.add(toAdd);

        } else if (eq instanceof PowerEquation && ((PowerEquation) eq).isSqrt()) {
            write = new WritingEquation(owner);
            write.add(new WritingSqrtEquation(owner));
            write.add(convert(eq.get(0)));
            write.add(new WritingPraEquation(false, owner));
        } else if (eq instanceof BinaryEquation) {
            if (eq instanceof DivEquation) {
                write = new DivEquation(owner);
            }
            if (eq instanceof PowerEquation) {
                write = new PowerEquation(owner);
            }
            for (Equation e : eq) {
                Equation inner =convert(e);
                if (!(inner instanceof WritingEquation)){
                    Equation holder = new WritingEquation(owner);
                    holder.add(inner);
                    inner = holder;
                }
                write.add(inner);
            }
        } else if (eq instanceof LeafEquation) {
            write = eq.copy();
        } else  if (eq instanceof TrigEquation){
            if (eq instanceof SineEquation){
                write = new SineEquation(owner);
            }else if (eq instanceof ASineEquation){
                write = new ASineEquation(owner);
            }else if (eq instanceof CosEquation){
                write = new CosEquation(owner);
            }else if (eq instanceof ACosEquation){
                write = new ACosEquation(owner);
            }else if (eq instanceof TanEquation){
                write = new TanEquation(owner);
            }else if (eq instanceof ATanEquation){
                write = new ATanEquation(owner);
            }else{
                Log.e("Main.convert", "I should have hit one of those cases");
            }
            Equation inner =convert(eq.get(0));
            if (!(inner instanceof WritingEquation)){
                Equation holder = new WritingEquation(owner);
                holder.add(inner);
                inner = holder;
            }
            write.add(inner);
        }
        if (eq.parenthesis(EquationLine.pm.SOLVE) && !eq.parenthesis(EquationLine.pm.WRITE)) {
            if (write instanceof WritingEquation) {
                write.add(0, new WritingPraEquation(true, owner));
                write.add(new WritingPraEquation(false, owner));
            } else {
                Equation old = write;
                write = new WritingEquation(owner);
                write.add(new WritingPraEquation(true, owner));
                write.add(old);
                write.add(new WritingPraEquation(false, owner));
            }
        }
        return write;
    }


    public Equation addParnsIfNeeded(Equation write) {
        if (write.size() > 1) {

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
        if (lines.get(0) instanceof ImageLine) {
            return (ImageLine) lines.get(0);
        }
        Log.e("getProblemImage", "bad to the bone");
        return null;
    }

    public void initWE(Equation equation) {
        ((HiddenInputLine) lines.get(1)).stupid.set(equation.copy());
        ((AlgebraLine) lines.get(2)).initEquation(equation.copy());
        keyBoardManager.hardSet(lines.get(2).getKeyboad());
    }

    public void initWI() {
        keyBoardManager.hardSet(lines.get(1).getKeyboad());
    }

    public void initE(Equation equation) {
        ((HiddenInputLine) lines.get(0)).stupid.set(equation.copy());
        ((AlgebraLine) lines.get(1)).initEquation(equation.copy());
    }

    public void initEK(Equation equation) {
        initE(equation);

        lines.get(1).setKeyBoard(new AlgebraKeyboardNoReturn(this,(AlgebraLine)lines.get(1)));

        keyBoardManager.hardSet(lines.get(1).getKeyboad());
    }

    private Equation goal;
    private int overlayId;
    private boolean alreadySolved=false;
    private ISolveController myMainTut;

    public void solvable(Equation goal,int overlayId , ISolveController controller){
        allowSolve = true;
        this.goal = goal;
        this.overlayId = overlayId;
        myMainTut = controller;
    }

    public Equation getGoal(){
        return goal;
    }


    public void couldHaveSolved(Equation copy) {
        if (allowSolve && ! alreadySolved){
            if (copy.same(goal)){
                alreadySolved = true;
                //allowTouch =false;
                final View root = (View)this.getParent();;
//                while (root.getParent() != null && root.getParent() instanceof View){
//                    root = (View)root.getParent();
//                }
                final View overlay = root.findViewById(overlayId);
                overlay.setVisibility(VISIBLE);
                overlay.setAlpha(0);
                final Main that = this;
                myMainTut.solved(new Runnable() {
                    @Override
                    public void run() {
                        final YayView ytv = (YayView)root.findViewById(overlayId);
                        ytv.turnOn(that);
                        overlay.animate().alpha(1).setDuration(500).withLayer().withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                ytv.initOnClickListeners(that);
                            }
                        });
                    }
                });
            }
        }
    }


    public void reset(final Runnable runnable) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                lines.clear();
                initStartLines(startLine);
                alreadySolved = false;
                runnable.run();
            }
        };


        myMainTut.reset(this,r);
    }

    public void addStep(Equation equation) {
        ((AlgebraLine) lines.get(1)).addStep(equation);
    }



    public Nextmanager next = new Nextmanager(){
        @Override
        public void next() {}

        @Override
        public void finish() {}
    };

    public void floorScroll() {
        if (offsetY >  height - keyBoardManager.get().measureHeight()){
            toAddToOffsetY(offsetY - (height - keyBoardManager.get().measureHeight()));
        }
    }

    public void message(final String toDisp){

        Line ll = lastLine();
        final KeyBoard k = ll.getKeyboad();
        if (k!= null&& ll instanceof EquationLine){
            final MessageButton mb = new MessageButton(toDisp,(EquationLine)ll);
            k.popUpButtons.add(mb);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (PopUpButton pub: k.popUpButtons){
                        if (!mb.equals(pub) &&pub instanceof  MessageButton && ((MessageButton)pub).message.equals(toDisp)){
                            ((MessageAction)pub.myAction).done();
                        }

                    }
                }
            }).start();



        }
    }

}
