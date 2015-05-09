package cube.d.n.commoncore;

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


import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 4/20/2015.
 */
public abstract class BaseApp extends Application{
    private static BaseApp instance;
    private float dpi;
    private int DEFAULT_SIZE =40;
    public double zoom = 1;
    public static final int DEFAULT_TEXT_SIZE = 40;
    private int TEXT_SIZE= DEFAULT_TEXT_SIZE;
    float scale =1f;
    public long doubleTapSpacing = 300;
    private float doubleTapDistance = 50;
    public int darkColor;
    public Paint bkgPaint = new Paint();
    public int veryDarkColor;
    public int darkDarkColor;
    private Tracker myTracker = null;

    public int at=0;

    public int lightColor;
    public TextPaint textPaint = new TextPaint();
    public long acceptedTime = 1000l;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Log.i("BaseApp", "created");

        initColors();

        bkgPaint.setTextAlign(Paint.Align.CENTER);
        bkgPaint.setAntiAlias(true);
        bkgPaint.setColor(BaseApp.getApp().lightColor);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/DejaVuSans.ttf");
        //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/DejaVuSans-ExtraLight.ttf");
        textPaint.setTypeface(myTypeface);
        textPaint.setColor(veryDarkColor);//0xff000000

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


        // Get tracker.
        Tracker t = getTracker();

        // Enable Advertising Features.
        t.enableAdvertisingIdCollection(true);
    }

    public abstract String getPropertyId();

    public synchronized Tracker getTracker() {
        if (myTracker !=null){
            return myTracker;
        }else{
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            myTracker =  analytics.newTracker(getPropertyId());
            return  myTracker;
        }
    }

    public int getRate() {
        return 5;
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


        //if (useOGColors() || r.nextInt(10) ==0){
            lightColor = 0xffddd7d7;
            darkColor =0xffd5080b;
        //}

        //darkColor =0xffd5080b;
        darkDarkColor = darkColor;
        darkDarkColor = BaseApp.colorFade(darkDarkColor, Color.BLACK);
        darkDarkColor = BaseApp.colorFade(darkDarkColor, Color.BLACK);
        darkDarkColor = BaseApp.colorFade(darkDarkColor, Color.BLACK);
        veryDarkColor=lightColor;
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);
        veryDarkColor = BaseApp.colorFade(veryDarkColor, Color.BLACK);

    }


    public static BaseApp getApp(){
        return instance;
    }

    public int getDefaultSize() {
        return (int)(DEFAULT_SIZE*dpi);
    }

    public float getDpi(){
        return dpi;
    }

    public float getScale(){
        return scale;
    }

    public void setDpiAndScale(float dpi, float inches) {

        if (inches < 7){
            scale = (inches+5f)/(7f+5f);
            scale = Math.max(scale,.6f);
        }
        this.dpi = dpi*scale;

        Log.d("setting text size: ", DEFAULT_TEXT_SIZE * this.dpi + "");
        setTextSize((int) (DEFAULT_TEXT_SIZE * this.dpi));
    }

    public float getDoubleTapDistance(){
        return doubleTapDistance*getDpi();

    }

    public static int colorFade(int currentColor, int targetColor ) {

       return colorFade(currentColor, targetColor ,BaseApp.getApp().getRate());

    }

    public static int colorFade(int currentColor, int targetColor,float scale ) {
        int currentRed = android.graphics.Color.red(currentColor);
        int currentGreen = android.graphics.Color.green(currentColor);
        int currentBlue = android.graphics.Color.blue(currentColor);


        int targetRed = android.graphics.Color.red(targetColor);
        int targetGreen = android.graphics.Color.green(targetColor);
        int targetBlue = android.graphics.Color.blue(targetColor);

        //if(currentAlpha > 0) {
        int lastColor = currentColor;

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


    private boolean useOGColors() {
        SharedPreferences settings = getSharedPreferences("First", 0);
        boolean alreadyShown = settings.getBoolean("UseOGColors", true);

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("UseOGColors", false);

        editor.commit();

        return alreadyShown;
    }

    public float getPranEdgeX(Equation eq) {
        return (float)(7*getDpi()*zoom);
    }

    public float getPranEdgeY(Equation eq) {
        return (float)(2*getDpi()*zoom);
    }

    public float getCornor() {
        return (float)(10*BaseApp.getApp().getDpi());
    }

    public float getPranIn(Equation eq) {
        return (float)(9*getDpi()*zoom);
    }

    public float getStrokeWidth(Equation eq) {
        return (float)(1.5f*getDpi()*zoom);
    }

    public float getDivWidthAdd(Equation eq) {
        return (float)(20*getDpi()*zoom);
    }

    public float getBuffer() {
        return 10*getDpi();
    }

    public float getBuffer(Equation eq) {
        return (float)(getBuffer()*zoom);
    }

    public float getSqrtWidthAdd(Equation eq) {
        return(float)( 40*getDpi()*zoom);
    }

    public float getSqrtHeightAdd(Equation eq) {
        return(float)( 20*getDpi()*zoom);
    }

    public float getbkgBuffer(Equation eq) {
        return (float)(5*getDpi()*zoom);
    }

    public int getTopLineWidth() {
        return (int)((int)(6*getDpi()));
    }

    public float getShadowFade() {
        return (float)(1 + .3/BaseApp.getApp().getDpi());
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


}
