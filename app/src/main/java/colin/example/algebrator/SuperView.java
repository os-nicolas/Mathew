package colin.example.algebrator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;

import colin.example.algebrator.tuts.TutMessage;
import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.BaseView;
import cube.d.n.commoncore.CanTrackChanges;
import cube.d.n.commoncore.DragLocation;
import cube.d.n.commoncore.eq.AddEquation;
import cube.d.n.commoncore.eq.DragEquation;
import cube.d.n.commoncore.eq.EqualsEquation;
import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.MultiEquation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.WritingEquation;
import cube.d.n.commoncore.eq.WritingLeafEquation;

public abstract class SuperView extends BaseView implements
        OnTouchListener {
    protected float BASE_BUTTON_PERCENT=1f;

    //SurfaceHolder surfaceHolder;
    Thread thread = null;
    volatile boolean running = false;

    float offsetX = 0;
    float offsetY = 0;
    protected float buttonsPercent;


    public ArrayList<PopUpButton> popUpButtons = new ArrayList<PopUpButton>();
    public boolean trackFinger = false;
    public boolean trackFingerUp = true;
    public float trackFingerX = -1;
    public float trackFingerY = -1;

    public Activity myActivity = null;

    private double lastZoomDis;

    public boolean disabled = false;
    int stupidAlpha = 0xff;

    private Point lastCenter;

    public ArrayList<EquationButton> history = new ArrayList<EquationButton>();
    protected boolean skipZero = true;

    abstract protected void addButtons();


    protected float buttonLine() {
        return height * buttonsPercent;
    }

    int highlight;

    ArrayList<Button> buttons = new ArrayList<Button>();

    public SuperView(Context context) {
        super(context);
        init(context);
    }

    public SuperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SuperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void resume() {
        super.resume();
        // stupid is null when we are first creating the view

    }

    private void init(Context context) {
        //surfaceHolder = getHolder();
        this.setOnTouchListener(this);

        measureScreen(context);

        highlight = Algebrator.getAlgebrator().darkColor;

        Log.i("lifeCycle", "SuperView-init");

        addButtons();
    }

    public void measureScreen(Context context) {
        // Get the height of the whole display
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        int screenheight = metrics.heightPixels; // get screen height (dependent
        // on rotation)
        int screenwidth = metrics.widthPixels; // get screen width (dependent on
        // rotation)

        Log.i("screenheight, screenwidth", screenheight + "," + screenwidth);

        // Get the heights of status, title, decorations, etc.
        Window win = ((Activity) context).getWindow();
        Rect rect = new Rect();

        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top; // Get the height of the status bar
        int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT)
                .getTop(); // Get height occupied by decoration contents
        int titleBarHeight = contentViewTop - statusBarHeight;

        Log.i("statusBarHeight, contentViewTop, titleBarHeight",
                statusBarHeight + "," + contentViewTop + "," + titleBarHeight);

        // Get navigation bar height
        int navBarHeight = 0;
        /*
         * Resources resources = context.getResources(); int resourceId =
		 * resources.getIdentifier("navigation_bar_height", "dimen", "android");
		 * if (resourceId > 0) { navBarHeight =
		 * resources.getDimensionPixelSize(resourceId); }
		 *
		 * Log.i("navBarHeight", navBarHeight+"");
		 */

        // Calculate actual height
        height = screenheight
                - (titleBarHeight + statusBarHeight + navBarHeight);
        width = screenwidth;

        Log.i("actual height, width", height + ", " + width);

    }

    private String lastLog = "";

    private long lastVelocityUpdate = 0l;
    // 60 frames for second in theory
    private float step = 1000f / 60f;
    private float friction = 0.90f;

    @Override
    protected void onDraw(Canvas canvas) {
        height = canvas.getHeight();
        width = canvas.getWidth();

        // canvas.drawColor(0xFFFFFFFF, Mode.CLEAR);
        Algebrator.getAlgebrator().at++;



        canvas.drawColor(0xFFFFFFFF, Mode.ADD);

//        //color testing
//        Rect rTest = new Rect(0,0,100,100);
//        Paint pTest = new Paint();
//        pTest.setColor(Algebrator.getAlgebrator().lightColor);
//        canvas.drawRect(rTest,pTest);
//        rTest = new Rect(0,100,100,200);
//        pTest.setColor(Algebrator.getAlgebrator().darkColor);
//        canvas.drawRect(rTest,pTest);
//        rTest = new Rect(0,200,100,300);
//        pTest.setColor(Algebrator.getAlgebrator().darkDarkColor);
//        canvas.drawRect(rTest,pTest);
//        rTest = new Rect(0,300,100,400);
//        pTest.setColor(Algebrator.getAlgebrator().veryDarkColor);
//        canvas.drawRect(rTest,pTest);
//        Paint p = new Paint();
//        p.setColor(Color.GREEN);
//        Point tcp = getStupidCenter();
//
//        Rect r = new Rect((int)(tcp.x+(200*zoom)),
//                (int)(tcp.y+(200*zoom)),
//                (int)(tcp.x+(400*zoom)),
//                (int)(tcp.y+(400*zoom)));
//        canvas.drawRect(r,p);
//
//        r = new Rect((int)(tcp.x+(-400*zoom)),
//                (int)(tcp.y+(-500*zoom)),
//                (int)(tcp.x+(100*zoom)),
//                (int)(tcp.y+(-400*zoom)));
//        canvas.drawRect(r,p);
//
//        r = new Rect((int)(tcp.x+(-300*zoom)),
//                (int)(tcp.y+(200*zoom)),
//                (int)(tcp.x+(-100*zoom)),
//                (int)(tcp.y+(400*zoom)));
//        canvas.drawRect(r,p);


//        for (DragLocation dl:dragLocations){
//            float dlx = dl.x + stupid.lastPoint.get(0).x;
//            float dly = dl.y + stupid.lastPoint.get(0).y;
//            Paint temp =new Paint();
//            temp.setColor(Color.GREEN);
//            canvas.drawCircle(dlx,dly,15,temp);
//        }

//        Picture pic = new Picture();
//        Canvas c = pic.beginRecording(canvas.getWidth(),canvas.getHeight());
//        Paint testP = new Paint();
//        testP.setColor(0xffff00ff);
//        c.drawRect(0,0,200,200,testP);
//        pic.endRecording();
//        pic.draw(canvas);

//        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bmp);
//        Paint testP = new Paint();
//        testP.setColor(0xffff00ff);
//        c.drawRect(0,0,200,200,testP);
//        canvas.drawBitmap(bmp,10,10,testP);

        if (this instanceof EmilyView) {
            Point stupidCenter = getStupidCenter();
            stupid.draw(canvas, stupidCenter.x, stupidCenter.y);
        } else {
            //TODO - clean up stupid should probably own it's own alpha
            int targetAlpha;
            if (EquationButton.current != null && EquationButton.current.lastLongTouch != null){
                stupidAlpha = (int)(Math.max(.7f-EquationButton.current.lastLongTouch.percent(),0)*0xff);
                targetAlpha = stupidAlpha;
            }else{
                targetAlpha=0xff;
            }
            int rate = Algebrator.getAlgebrator().getRate();
            stupidAlpha = (stupidAlpha * rate + targetAlpha) / (rate + 1);
            stupid.setAlpha(stupidAlpha);
            Point stupidCenter = getStupidCenter();
            if (this instanceof ColinView && stupid instanceof EqualsEquation) {
                ((EqualsEquation) stupid).drawCentered(canvas, stupidCenter.x, stupidCenter.y);
            }else{
                stupid.draw(canvas,stupidCenter.x,stupidCenter.y);
            }
        }

        drawHistory(canvas);

        // keep selected on the screen
        long now = System.currentTimeMillis();
        long diff = now - lastVelocityUpdate;

        //TODO scale by dpi
        float pushV = 1f * Algebrator.getAlgebrator().getDpi();
        float maxV = 4f * Algebrator.getAlgebrator().getDpi();
        float chunk = 20f * Algebrator.getAlgebrator().getDpi();

        float steps = diff / step;

        float or = outRight();
        if (or != 0) {
            vx -= step * pushV * or / chunk;
            if (vx < -maxV) {
                vx = -maxV;
            }
            slidding = true;
        }
        float ob = outBottom();
        if (ob != 0) {
            vy -= step * pushV * ob / chunk;
            if (vy < -maxV) {
                vy = -maxV;
            }
            slidding = true;
        }
        float ol = outLeft();
        if (ol != 0) {
            vx += step * pushV * ol / chunk;
            if (vx > maxV) {
                vx = maxV;
            }
            slidding = true;
        }
        float ot = outTop();
        if (ot != 0) {
            vy += step * pushV * ot / chunk;
            if (vy > maxV) {
                vy = maxV;
            }
            slidding = true;
        }

        if (slidding) {
            //if (steps == 0){
                //Log.e("steps was zero!","diff:" + steps);
            //}

            float dx = (float) (vx * ((Math.pow(friction, steps) - 1) / Math.log(friction)));
            float dy = (float) (vy * ((Math.pow(friction, steps) - 1) / Math.log(friction)));

//            vx= friction*vx;
//            vy = friction*vy;

//            float lastVx=vx;
            vx = (float) (vx * Math.pow(friction, steps));
//            if (lastVx == vx){
//                Log.e("vx","diff:" + diff);
//            }
//
//            float lastVy=vy;
            vy = (float) (vy * Math.pow(friction, steps));
//            if (lastVy == vy){
//                Log.e("vy","diff:" + diff);
//            }
            lastVelocityUpdate = now;
            updateOffsetX(dx);
            updateOffsetY(dy);
            if (vx == 0 && vy == 0) {
                slidding = false;
            }

        }

        if (dragging != null) {
            dragging.eq.draw(canvas, dragging.eq.x, dragging.eq.y);
        }

        stupid.integrityCheckOuter();
        if (!stupid.toString().equals(lastLog)) {
            Log.i("", stupid.toString());
            lastLog = stupid.toString();
        }

        for (Button myBut: buttons) {
            myBut.draw(canvas);
        }

        buttonsPercent = BASE_BUTTON_PERCENT;
        for (PopUpButton myPUB: popUpButtons) {
            myPUB.updateLocation();
            myPUB.draw(canvas);
        }

        drawShadow(canvas);

        for (int i = 0; i < animation.size(); i++) {
            animation.get(i).draw(canvas);
        }

        for (int i = 0; i < afterAnimations.size(); i++) {
            afterAnimations.get(i).draw(canvas);
        }

        invalidate();
    }

    //TODO scale by dpi
    int buffer = (int) (50 * Algebrator.getAlgebrator().getDpi() *Algebrator.getAlgebrator().zoom);


    protected float outTop() {
        if (dragging != null) {
            if (dragging.eq.y - 2*buffer < 0) {
                Log.d("out,top","dragging");
                //message.db("outtop, dragging");
                return -(dragging.eq.y - 2*buffer);
            }
        }
        if (selected != null) {
            if (selected.y - buffer < 0) {
                Log.d("out,top","selected");
                //.db("outtop, selected");
                return -(selected.y - buffer);
            }
        }
        return 0f;
    }

    protected float outLeft() {
        if (dragging != null) {
            if (dragging.eq.x - 2*buffer < 0) {
                Log.d("out,left","dragging");
                //message.db("outleft, dragging");
                return -(dragging.eq.x - 2*buffer);
            }
        }
        if (selected != null) {
            if (selected.x - buffer < 0) {
                Log.d("out,left","selected");
                //message.db("outleft, select");
                return -(selected.x - buffer);
            }
        }
        return 0f;
    }

   protected float outBottom() {
        if (dragging != null) {
            if (dragging.eq.y + 2*buffer > buttonLine()) {
                Log.d("out,bot","dragging");
                //message.db("outbot, dragging");
                return (dragging.eq.y + 2*buffer) - buttonLine();
            }
        }
        if (selected != null) {
            if (selected.y + buffer > buttonLine()) {
                Log.d("out,bot","selected");
                //message.db("outbot, selected");
                return (selected.y + buffer) - buttonLine();
            }
        }
        return 0f;
    }

    protected float outRight() {
        if (dragging != null) {
            if (dragging.eq.x + 2*buffer > width) {
                Log.d("out,right","dragging");
                //message.db("outright, dragging");
                return (dragging.eq.x + 2*buffer) - width;
            }
        }
        if (selected != null) {
            if (selected.x + buffer > width) {
                Log.d("out,right","selected");
                //message.db("outright,selected");
                return (selected.x + buffer) - width;
            }
        }
        return 0f;
    }



    private void updateOffsetX(float vx) {
        offsetX += vx;
    }

    private void updateOffsetY(float vy) {
        offsetY += vy;
    }


    private long lastTapTime;
    private Point lastTapPoint;


    //moving the whole equation
    protected float lastX;
    protected float lastY;
    private float eqDragPadding = 25;

    // TODO I think we can remove slidding
    private boolean slidding = false;
    private float vx = 0;
    private float vy = 0;

    public void updateOwner() {
        if (stupid!=null) {
            stupid.updateOwner(this);
        }
    }

    public void removeSelected() {
        if (selected instanceof PlaceholderEquation) {
            if (!(selected.parent.size() == 1 && selected.parent != null)) {
                Equation oldEq = selected;
                selected.setSelected(false);
                oldEq.remove();
            }
        }
        if (selected != null) {
            if ( selected.parent != null && selected.parent.size() != 1 ) {
                Equation oldEq = selected;
                selected.setSelected(false);
                oldEq.tryFlatten();
            }else {
                selected.setSelected(false);
            }
        }
    }





    enum TouchMode {BUTTON, DRAG, SELECT, MOVE, ZOOM, DEAD, MESSAGE}

    public boolean canDrag = false;

    protected TouchMode myMode;

    LongTouch lastLongTouch = null;

    private int disabledAlpha = 0;

    protected void onDrawAfter(Canvas canvas) {
        TutMessage.tryShowAll(this);
        message.draw(canvas);

        if (trackFinger && !trackFingerUp){
            Paint p = new Paint();
            //0xffd5080b
            p.setARGB(0xff/2,(0xd5-(0xff/2))*2,0x04,0x06);
            //p.setARGB(0xff/2,(0xd5-(0xff/2))*2,(0x08-(0xff/2))*2,(0x0b-(0xff/2))*2);
            //p.setARGB(255/2,(221-(255/2))*2,(215-(255/2))*2,(215-(255/2))*2);
            canvas.drawCircle(trackFingerX,trackFingerY,20*Algebrator.getAlgebrator().getDpi(),p);
        }

        if (disabled) {
            disabledAlpha = (int) ((5f * disabledAlpha + 0x80) / 6f);
            Rect r = new Rect(0, 0, width, height);
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setAlpha(disabledAlpha);
            canvas.drawRect(r, p);


        }
    }


    long lastTouch = System.currentTimeMillis();
    final long inactive = 2000;
    public boolean active(){
        return System.currentTimeMillis() - lastTouch < inactive;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        lastTouch = System.currentTimeMillis();
        if (!disabled) {


            if (event.getPointerCount() == 1) {
                if (!hasUpdated){
                    stupid.updateLocation();
                }
                hasUpdated = false;

                trackFingerX = event.getX();
                trackFingerY = event.getY();

                // we need to know if they started in the box
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    trackFingerUp =false;
                    lastVelocityUpdate = System.currentTimeMillis();
                    // figure out the mode;
                    if (inButtons(event)) {
                        myMode = TouchMode.BUTTON;
                        doButtons(event);
                    } else if (stupid.nearAny(event.getX(), event.getY())) {
                        myMode = TouchMode.SELECT;
                        resolveSelected(event);
                    } else if (message.inBar(event)) {
                        myMode = TouchMode.MESSAGE;
                    }else{
                        myMode = TouchMode.MOVE;
                        if (selected != null && canDrag) {
                            removeSelected();
                        }
                    }
                    long startTime = System.currentTimeMillis();
                    lastX = event.getX();
                    lastY = event.getY();
                    // stop stupid sliding
                    slidding = false;
                    vx = 0;
                    vy = 0;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // check if they selected anything
                    if (myMode == TouchMode.SELECT) {
                        // if they get too far from were they started we are going to start dragging
                        //TODO scale by dpi

                            selectMoved(event);
                    }

                    // if we are dragging something move it
                    // drag is only a mode in the solve screen
                    if (myMode == TouchMode.DRAG) {
                        dragging.eq.x = event.getX();
                        dragging.eq.y = event.getY();

                        DragLocation closest = ((ColinView)this).dragLocations.closest(event);

                        if (closest != null) {
                            stupid = closest.myStupid;
                        }
                    }

                    // if they are moving the equation
                    if (myMode == TouchMode.MOVE) {
                        updateVelocity(event);
                        if (selected != null && selected instanceof PlaceholderEquation){
                            ((PlaceholderEquation)selected).goDark();
                        }
                    }

                    if (myMode == TouchMode.BUTTON) {
                        doButtons(event);
                    }
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    trackFingerUp =true;
                    // did we click anything?
                    boolean clicked = false;
                    long now = System.currentTimeMillis();
                    Point tapPoint = new Point();
                    tapPoint.x = (int) event.getX();
                    tapPoint.y = (int) event.getY();
                    long tapSpacing = now - lastTapTime;
                    if (tapSpacing < Algebrator.getAlgebrator().doubleTapSpacing && dis(tapPoint, lastTapPoint) < Algebrator.getAlgebrator().getDoubleTapDistance() && myMode == TouchMode.SELECT) {
                        Log.i("", "doubleTap! dis: " + dis(tapPoint, lastTapPoint) + " time: " + tapSpacing);
                        if (canDrag) {
                            stupid.tryOperator(event.getX(),
                                    event.getY());
                            clicked = true;
                            if (this instanceof CanTrackChanges) {
                                if (((CanTrackChanges) this).hasChanged()) {
                                    clicked = false;
                                    // TODO
                                    // TODO
                                    // this is def a band-aid
                                    // the problem is that some operation don't change the eq
                                    // while making a new copy of it
                                    // in these case we still select - altho maybe we don't need to
                                    // however sometimes selected is in the old version of stupid
                                    // this casues problem when we try to select it
                                    // the real solution is to pass selected on in a good way
                                    // this probably mean editing copy and rewriting it like constructors
                                    // TODO
                                    // TODO
                                    if (selected != null) {
                                        selected.setSelected(false);
                                    }
                                } else {
                                    if (selected != null) {
                                        selected.setSelected(false);
                                    }
                                }
                            }
                        }

                        // set the lastTapTime to zero so they can not triple tap and get two double taps
                        lastTapTime = 0;
                    } else {
                        lastTapTime = now;
                    }
                    lastTapPoint = tapPoint;
                    if (!clicked) {
                        endOnePointer(event);
                    }

                    // if we were dragging everything around
                    if (myMode == TouchMode.MOVE) {
                        updateVelocity(event);
                        slidding = true;
                    }

                    lastLongTouch = null;

                }

            } else if (event.getPointerCount() == 2) {
                if (myMode != TouchMode.ZOOM) {
                    endOnePointer(event);
                    myMode = TouchMode.ZOOM;
                    lastZoomDis = getPointerDistance(event);
                    lastCenter = getCenter(event);
                    // we need to tell the buttons to turn off hover
                    doButtons(event);
                }else{
                    double currentZoomDis = getPointerDistance(event);
                    Point touchCenter = getCenter(event);
                    Point screenCenter = getCenter();
                    double oldZoom = Algebrator.getAlgebrator().zoom;
                    Algebrator.getAlgebrator().zoom = Algebrator.getAlgebrator().zoom*(currentZoomDis/lastZoomDis);
                    if (Algebrator.getAlgebrator().zoom < .7){Algebrator.getAlgebrator().zoom = .7;}
                    if (Algebrator.getAlgebrator().zoom > 1.5){Algebrator.getAlgebrator().zoom = 1.5;}
                    lastZoomDis = currentZoomDis;
                    //float oldDisx =lastCenter.x - (screenCenter.x +offsetX);
                    float oldDisy =lastCenter.y - (screenCenter.y +offsetY);
                    //offsetX =(float) -((Algebrator.getAlgebrator().zoom/oldZoom)*(oldDisx) -touchCenter.x + screenCenter.x);
                    offsetY =(float) -((Algebrator.getAlgebrator().zoom/oldZoom)*(oldDisy)-touchCenter.y + screenCenter.y);
                    lastCenter =touchCenter;

                    stupid.deepNeedsUpdate();
                }
            } else{
                myMode = TouchMode.DEAD;
            }
        } else {
            myMode = TouchMode.DEAD;
        }

    Log.i("", stupid.toString());
        return true;
    }

    private Point getCenter() {
        Point result = new Point();
        result.x = (int) (width / 2 );
        result.y = (int) (height / 3 );
        return result;
    }

    private Point getStupidCenter() {
        Point result = getCenter();
        result.x = (int) (result.x + offsetX);
        result.y = (int) (result.y + offsetY);
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

    protected abstract void selectMoved(MotionEvent event);

    private void updateVelocity(MotionEvent event) {
        long now = System.currentTimeMillis();
        long timePass = now - lastVelocityUpdate;
        if (timePass != 0) {
            float stepsPass = timePass / step;
            float dx = (event.getX() - lastX);
            float dy = (event.getY() - lastY);
            updateOffsetX(dx);
            updateOffsetY(dy);
            float currentVx = (dx) / stepsPass;
            float currentVy = (dy) / stepsPass;

            lastX = event.getX();
            lastY = event.getY();
            lastVelocityUpdate = now;
            final float maxSteps = 5f;
            if (stepsPass < maxSteps) {
                vx = ((maxSteps - stepsPass) * vx + (stepsPass) * currentVx) / maxSteps;
                vy = ((maxSteps - stepsPass) * vy + (stepsPass) * currentVy) / maxSteps;
            } else {
                vx = currentVx;
                vy = currentVy;
            }
        }
    }

    private void updateLastTouch(MotionEvent event) {
        if (lastLongTouch == null) {
            lastLongTouch = new LongTouch(event);
        } else if (lastLongTouch.outside(event)) {
            lastLongTouch = new LongTouch(event);
        }
    }

    protected boolean inButtons(MotionEvent event) {
        for (Button b : buttons) {
            if (b.couldClick(event)) {
                return true;
            }
        }
        for (Button b : popUpButtons) {
            if (b.couldClick(event)) {
                return true;
            }
        }
        return false;
    }

    private float dis(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    private void endOnePointer(MotionEvent event) {
        if (myMode == TouchMode.BUTTON) {
            doButtons(event);
        } else if (myMode == TouchMode.SELECT) {
                resolveSelected(event);
        } else if (myMode == TouchMode.DRAG) {
            DragLocation closest = ((ColinView)this).dragLocations.closest(event);

            if (closest != null) {
                closest.select();
            }


            stupid.fixIntegrety();


            dragging.demo.isDemo(false);
            dragging = null;

            if (selected != null) {
                selected.setSelected(false);
            }
        }else if(myMode == TouchMode.MESSAGE){
            message.onClick(event);
        }
        stupid.updateLocation();
    }

    private void doButtons(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            for (Button myBut : buttons) {
                myBut.click(event);
            }
            for (PopUpButton pub : popUpButtons) {
                pub.click(event);
            }
        }else{
            for (Button myBut : buttons) {
                myBut.hover(event);
            }
            for (PopUpButton pub : popUpButtons) {
                pub.hover(event);
            }
        }
    }

    protected void selectSet(ArrayList<Equation> selectingSet) {
        Equation lcp = null;
        // if anything in selectingSet is contained by something else remove it
        for (int i1 = 0; i1 < selectingSet.size(); i1++) {
            Equation eq1 = selectingSet.get(i1);
            for (int i2 = i1 + 1; i2 < selectingSet.size(); i2++) {
                Equation eq2 = selectingSet.get(i2);
                if (!(eq1.equals(eq2)) && eq1.deepContains(eq2)) {
                    Log.i("removed eq2", eq2.toString() +" killed by: "+ eq1.toString());
                    selectingSet.remove(i2);
                    i2--;
                }
                if (!(eq1.equals(eq2)) && eq2.deepContains(eq1)) {
                    Log.i("removed eq1", eq1.toString()+" killed by: "+ eq2.toString());
                    selectingSet.remove(i1);
                    i2--;
                    i1--;
                    break;
                }
            }
        }
        for (Equation eq : selectingSet) {
            if (lcp == null) {
                lcp = eq;//eq.parent
            } else { //if (!eq.parent.equals(lcp))
                lcp = lcp.lowestCommonContainer(eq);
            }
        }
        if (lcp != null && selectingSet.size() > 1) {
            // make sure they are a continous block
            ArrayList<Integer> indexs = new ArrayList<Integer>();
            for (Equation eq : selectingSet) {
                int index = lcp.deepIndexOf(eq);
                if (index == -1) {
                    Log.e("index is -1!", lcp.toString() + " " + eq.toString());
                }

                if (!indexs.contains(index)) {
                    indexs.add(index);
                }
            }
            Collections.sort(indexs);
            //Collections.reverse(indexs);
            int min = indexs.get(0);
            Log.d("min", min + "");
            if (lcp.get(min) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(min)).isOpRight();
                if (min != 0) {
                    indexs.add(0, min - 1);
                }
            }
            int max = indexs.get(indexs.size() - 1);
            Log.d("max", max + "");
            if (lcp.get(max) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(max)).isOpLeft();
                if (max != lcp.size() - 1) {
                    indexs.add(max + 1);
                }
            }

            // the we need to add the indexes between min and max
            for (int newIndex = min + 1; newIndex < max; newIndex++) {
                if (!indexs.contains(newIndex)) {
                    indexs.add(newIndex);
                }
            }
            Collections.sort(indexs);

            // if they do not make up all of lcp
            if (indexs.size() != lcp.size()) {
                // we make a new equation of the type of lcp
                Equation toSelect = null;
                if (lcp instanceof MultiEquation) {
                    toSelect = new MultiEquation(this);
                } else if (lcp instanceof AddEquation) {
                    toSelect = new AddEquation(this);
                } else if (lcp instanceof WritingEquation) {
                    toSelect = new WritingEquation(this);
                }
                //sort selected set
                ArrayList<Equation> selectedList = new ArrayList<Equation>();
                for (int i : indexs) {
                    selectedList.add(lcp.get(i));
                }
                // remove the selectingSet from lcp and add it to our
                // new equation
                for (Equation eq : selectedList) {
                    lcp.justRemove(eq);
                    toSelect.add(eq);
                }
                // insert the new equation in to lcp
                lcp.add(indexs.get(0), toSelect);
                // and select the new equation
                toSelect.setSelected(true);
            } else {
                lcp.setSelected(true);
            }
        } else {
            if (lcp != null) {
                lcp.setSelected(true);
            }
        }
    }

    double lastZoom = Algebrator.getAlgebrator().zoom;
    float baseBuffer = 75 * Algebrator.getAlgebrator().getDpi();
    float fade = 0.4f;
    protected void drawHistory(Canvas canvas) {
        float buffer = (float)(baseBuffer*Algebrator.getAlgebrator().zoom);

        float atHeight = -stupid.measureHeightUpper() - buffer;
        float currentPercent = fade;
        for (EquationButton eb : history) {
            if (lastZoom != Algebrator.getAlgebrator().zoom){
                eb.myEq.deepNeedsUpdate();
                eb.updateZoom(lastZoom,Algebrator.getAlgebrator().zoom);
            }
            if ( (!skipZero) || (!eb.equals(history.get(0)))) {

                atHeight -= eb.myEq.measureHeightLower();
                eb.targetColor = getHistoryColor(0xff000000, Algebrator.getAlgebrator().darkColor, currentPercent);
                eb.x = 0;
                eb.targetY = atHeight;
                int centerX;
                int centerY;
                if (stupid instanceof EqualsEquation){
                    centerX= stupid.lastPoint.get(0).x;
                    centerY= stupid.lastPoint.get(0).y;
                }else{
                    centerX= (int) stupid.getX();
                    centerY= (int) stupid.getY();
                }

                eb.update(centerX,centerY);
                if ((centerY + atHeight + eb.myEq.measureHeightLower()) > 0 &&
                        (centerY + atHeight - eb.myEq.measureHeightUpper()) < height) {
                    eb.draw(canvas, centerX, centerY);
                } else  if (  centerY > -(height/4)*Algebrator.getAlgebrator().getDpi()
                        || centerY < (height*5/4)*Algebrator.getAlgebrator().getDpi()){
                    // update the locations
                    eb.updateLocations(centerX, centerY);
                }
                atHeight -= eb.myEq.measureHeightUpper() + buffer;
                currentPercent *= fade;
            }
        }
        lastZoom = Algebrator.getAlgebrator().zoom;
    }

    private int getHistoryColor(int currentColor, int targetColor, float percent) {
        int currentRed = android.graphics.Color.red(currentColor);
        int currentGreen = android.graphics.Color.green(currentColor);
        int currentBlue = android.graphics.Color.blue(currentColor);

        int targetRed = android.graphics.Color.red(targetColor);
        int targetGreen = android.graphics.Color.green(targetColor);
        int targetBlue = android.graphics.Color.blue(targetColor);

        currentColor = android.graphics.Color.argb(
                0xff,
                (int) ((percent * currentRed) + ((1 - percent) * targetRed)),
                (int) ((percent * currentGreen) + ((1 - percent) * targetGreen)),
                (int) ((percent * currentBlue) + ((1 - percent) * targetBlue)));

        return currentColor;
    }

    protected abstract void resolveSelected(MotionEvent event);

    protected void addButtonsRow(ArrayList<Button> row, float top, float bottum) {
        addButtonsRow(row, 0, 1, top, bottum);
    }

    protected void addButtonsRow(ArrayList<Button> row, float left, float right, float top, float bottum) {
        float count = row.size();
        float at = left;
        float step = (right - left) / count;

        for (float i = 0; i < count; i++) {
            Button b = row.get((int) i);
            b.setLocation(at, at + step, top, bottum);
            buttons.add(b);
            at += step;
        }

    }

    protected void drawShadow(Canvas canvas) {
        Paint p = new Paint();
        int color = Algebrator.getAlgebrator().darkDarkColor;
        p.setColor(color);
        p.setAlpha(0xff);
        int at = ((int) buttonLine());
//        for (int i=0;i<2f/Algebrator.getAlgebrator().getDpi();i++){
//            canvas.drawLine(0,at,width,at,p);
//            at--;
//        }
        p.setAlpha(0x8f);
        while (p.getAlpha() > 1) {
            canvas.drawLine(0, at, width, at, p);
            p.setAlpha((int) (p.getAlpha() / Algebrator.getAlgebrator().getShadowFade()));
            at--;
        }
    }

}
