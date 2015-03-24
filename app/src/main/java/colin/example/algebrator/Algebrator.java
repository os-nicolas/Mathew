package colin.example.algebrator;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;

import colin.algebrator.eq.Equation;
import colin.example.algebrator.tuts.AddTut;
import colin.example.algebrator.tuts.TutMessage;

/**
 * Created by Colin on 12/30/2014.
 */
public class Algebrator extends Application {
    public static final int DEFAULT_TEXT_SIZE = 40;
    private static final String PROPERTY_ID = "UA-59613283-1";
    private static Algebrator instance;
    private int TEXT_SIZE= DEFAULT_TEXT_SIZE;
    private int DEFAULT_SIZE =40;
    public long doubleTapSpacing = 300;
    private float doubleTapDistance = 50;
    public int lightColor;
    public int darkColor;
    public TextPaint textPaint = new TextPaint();
    public Paint bkgPaint = new Paint();
    public EmilyView writeView  = null;
    public ColinView solveView  = null;
    public int at=0;
    private float dpi;
    public int veryDarkColor;
    public int darkDarkColor;
    float scale =1f;

    public float getScale(){
        return scale;
    }

    public float getStrokeWidth(Equation eq) {
        return (float)(1.5f*getDpi()*eq.owner.zoom);
    }

    public float getPranIn(Equation eq) {
        return (float)(9*getDpi()*eq.owner.zoom);
    }

    public float getPranEdgeX(Equation eq) {
        return (float)((eq.owner instanceof ColinView?7*getDpi():7*getDpi())*eq.owner.zoom);
    }

    public float getPranEdgeY(Equation eq) {
        return (float)((eq.owner instanceof ColinView?2*getDpi():2*getDpi())*eq.owner.zoom);
    }

    public float getSqrtHeightAdd(Equation eq) {
        return(float)( 20*getDpi()*eq.owner.zoom);
    }

    public float getSqrtWidthAdd(Equation eq) {
        return(float)( 40*getDpi()*eq.owner.zoom);
    }

    public float getCornor(Equation eq) {
        return (float)(getCornor()*eq.owner.zoom);
    }
    public float getCornor() {
        return (float)(10*Algebrator.getAlgebrator().getDpi());
    }

    public float getbkgBuffer(Equation eq) {
        return (float)(5*getDpi()*eq.owner.zoom);
    }

    public float getDivWidthAdd(Equation eq) {
        return (float)(20*getDpi()*eq.owner.zoom);
    }

    public int getTopLineWidth() {
        return (int)((int)(6*getDpi()));
    }

    public float getShadowFade() {
        return (float)(1 + .3/Algebrator.getAlgebrator().getDpi());
    }

    public float getBuffer(Equation eq) {
        return (float)(getBuffer()*eq.owner.zoom);
    }
    public float getBuffer() {
        return 10*getDpi();
    }

    // TODO we zoom in get paint for this is that wrong?
    public int getTextSize(){
        return this.TEXT_SIZE;
    }

    private void setTextSize(int newTextSize){
        this.TEXT_SIZE = newTextSize;
        bkgPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextSize(TEXT_SIZE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;




        initColors();

        bkgPaint.setTextAlign(Paint.Align.CENTER);
        bkgPaint.setAntiAlias(true);
        bkgPaint.setColor(Algebrator.getAlgebrator().lightColor);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/DejaVuSans.ttf");
        //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/DejaVuSans-ExtraLight.ttf");
        textPaint.setTypeface(myTypeface);
        textPaint.setColor(veryDarkColor);//0xff000000

        // Get tracker.
        Tracker t = getTracker();

        // Enable Advertising Features.
        t.enableAdvertisingIdCollection(true);

        WindowManager wm = (WindowManager) this
                .getSystemService(this.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        int h = metrics.heightPixels;
        int w = metrics.widthPixels;
        double d = Math.sqrt(h*h + w*w);
        double inches = d/metrics.densityDpi;
        Log.d("inches", "" + inches);

        setDpiAndScale(metrics.densityDpi / 160f, (float) inches);
    }

    public void initColors() {
        Random r = new Random();
        // we only want light colors for the main color
        int redShift =  (r.nextInt(0x35) + 0x05);
        int greenShift =  (r.nextInt(0x35) + 0x05);
        int blueShift =  (r.nextInt(0x35) + 0x05) ;

        lightColor = 0xffffffff                //alpha and a lower bound
                - redShift * 0x010000    // red
                - greenShift * 0x000100    // green
                - blueShift * 0x000001;   //blue
        //lightColor = 0xffddd7d7;
        darkColor = lightColor
                // we shift again so you can tell the colors apart
                - (redShift*2) * 0x010000     // red
                - (greenShift*2)  * 0x000100   // green
                - (blueShift*2) * 0x000001  //blue
                - r.nextInt(0x60) * 0x010000    // add some random red
                - r.nextInt(0x60) * 0x000100    // add some random green
                - r.nextInt(0x60) * 0x000001;  // add some random blue
        if (useOGColors() || r.nextInt(10) ==0){
            lightColor = 0xffddd7d7;
            darkColor =0xffd5080b;
        }

        //darkColor =0xffd5080b;
        darkDarkColor = darkColor;
        darkDarkColor = Algebrator.colorFade(darkDarkColor, Color.BLACK);
        darkDarkColor = Algebrator.colorFade(darkDarkColor, Color.BLACK);
        darkDarkColor = Algebrator.colorFade(darkDarkColor, Color.BLACK);
        veryDarkColor=lightColor;
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = Algebrator.colorFade(veryDarkColor, Color.BLACK);

    }

    private boolean useOGColors() {
        SharedPreferences settings = getSharedPreferences("First", 0);
        boolean alreadyShown = settings.getBoolean("UseOGColors", true);

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("UseOGColors", false);

        editor.commit();

        return alreadyShown;
    }

    private Tracker myTracker = null;

    public synchronized Tracker getTracker() {
        if (myTracker !=null){
            return myTracker;
        }else{
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            myTracker =  analytics.newTracker(PROPERTY_ID);
            return  myTracker;
        }
    }

    public float getDoubleTapDistance(){
        return doubleTapDistance*dpi;

    }

    public static Algebrator getAlgebrator(){
        return instance;
    }

    public void setDpiAndScale(float dpi, float inches) {

        if (inches < 7){
            scale = (inches+5f)/(7f+5f);
            scale = Math.max(scale,.6f);
        }
        this.dpi = dpi*scale;

        Log.d("setting text size: ",DEFAULT_TEXT_SIZE * this.dpi+"");
        setTextSize((int) (DEFAULT_TEXT_SIZE * this.dpi));
    }

    public int getDefaultSize() {
        return (int)(DEFAULT_SIZE*dpi);
    }

    public float getDpi(){
        return dpi;
    }

    public static int colorFade(int currentColor, int targetColor ) {
        int currentRed = android.graphics.Color.red(currentColor);
        int currentGreen = android.graphics.Color.green(currentColor);
        int currentBlue = android.graphics.Color.blue(currentColor);


        int targetRed = android.graphics.Color.red(targetColor);
        int targetGreen = android.graphics.Color.green(targetColor);
        int targetBlue = android.graphics.Color.blue(targetColor);

        //if(currentAlpha > 0) {
        int lastColor = currentColor;
        int scale = Algebrator.getAlgebrator().getRate();

        currentColor = android.graphics.Color.argb(
                0xff,
                (int) (((scale - 1) * currentRed + targetRed) / scale),
                (int) (((scale - 1) * currentGreen + targetGreen) / scale),
                (int) (((scale - 1) * currentBlue + targetBlue) / scale));
        if (lastColor == currentColor) {
            currentColor = targetColor;
        }
        return currentColor;
    }

    public int getRate() {
        return 5;
    }

}
