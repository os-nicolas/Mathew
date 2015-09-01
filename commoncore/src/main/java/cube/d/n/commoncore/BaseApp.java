package cube.d.n.commoncore;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.SolveScreen.Done;
import cube.d.n.commoncore.Action.WriteScreen.Solve;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.keyboards.AlgebraKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.ReturnKeyBoard;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 4/20/2015.
 */
public abstract class BaseApp extends Application{
    private static BaseApp instance;
    public final static long aveTime=4000;
    private float dpi;
    private int DEFAULT_SIZE =40;
    public double zoom = 1;
    public static final int DEFAULT_TEXT_SIZE = 40;
    private int TEXT_SIZE= DEFAULT_TEXT_SIZE;
    float scale =1f;
    public long doubleTapSpacing = 300;
    public int darkLightColor;
    public int lightLightColor;
    public int darkdarkLightColor;
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

        Thread.UncaughtExceptionHandler x = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new ErrorReporter(x));

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

    }

    public abstract String getPropertyId();

    public synchronized Tracker getTracker() {
        if (myTracker !=null){
            return myTracker;
        }else{
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            myTracker =  analytics.newTracker(getPropertyId());
            myTracker.enableAdvertisingIdCollection(true);
            return  myTracker;
        }
    }

    public String about(){
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            return "Version Code: " + pInfo.versionCode + " Version Name: " + pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public Action getEnter(InputLine line) {
        return new Solve(line);
    }

    public Action getDone(EquationLine line) {
        return new Done(line);
    }

    public Action getOk(BothSidesLine line) {
        return new CheckAction(line);
    }

    public Action getCancel(BothSidesLine line) {
        return new CancelAction(line);
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

        darkLightColor = BaseApp.colorFade(lightColor, Color.BLACK);
        lightLightColor =  BaseApp.colorFade(lightColor, Color.WHITE);
        lightLightColor =  BaseApp.colorFade(lightLightColor, Color.WHITE);

        darkdarkLightColor = BaseApp.colorFade(darkLightColor, darkColor);
        darkdarkLightColor = BaseApp.colorFade(darkdarkLightColor, Color.BLACK);

        //darkColor =0xffd5080b;
        darkDarkColor = darkColor;
        darkDarkColor = BaseApp.colorFade(darkDarkColor, Color.BLACK);
        darkDarkColor = BaseApp.colorFade(darkDarkColor, Color.BLACK);
        darkDarkColor = BaseApp.colorFade(darkDarkColor, Color.BLACK);


        Log.d("ddc","ddc:alpha "+ android.graphics.Color.alpha(darkDarkColor));
        Log.d("ddc","ddc:red "+ android.graphics.Color.red(darkDarkColor));
        Log.d("ddc","ddc:green "+ android.graphics.Color.green(darkDarkColor));
        Log.d("ddc","ddc:blue "+ android.graphics.Color.blue(darkDarkColor));


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

    public AWSCredentials getCreds(){
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:16e09fa5-2934-44b2-bc02-f4b887bd1712", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        return credentialsProvider.getCredentials();

        //return new BasicAWSCredentials( PropertyLoader.getInstance().getAccessKey(), PropertyLoader.getInstance().getSecretKey() );
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


    /**
     * large scale more currentColor
     * scale of 2 is average of current color and target color
     * scale should be larger than 1
     */
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

    public static int colorPrecent(int currentColor, int targetColor,float p ) {
        int currentAlpha = android.graphics.Color.alpha(currentColor);
        int currentRed = android.graphics.Color.red(currentColor);
        int currentGreen = android.graphics.Color.green(currentColor);
        int currentBlue = android.graphics.Color.blue(currentColor);


        int targetAlpha = android.graphics.Color.alpha(targetColor);
        int targetRed = android.graphics.Color.red(targetColor);
        int targetGreen = android.graphics.Color.green(targetColor);
        int targetBlue = android.graphics.Color.blue(targetColor);

        currentColor = android.graphics.Color.argb(
                (int) (((1-p) * currentAlpha +  p*targetAlpha) ),
                (int) (((1-p) * currentRed +  p*targetRed) ),
                (int) (((1 - p) * currentGreen + p*targetGreen) ),
                (int) (((1 - p) * currentBlue + p*targetBlue) ));
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

    public float getPranEdgeX(Equation equation) {
        return (float)(7*getDpi()*equation.root().getMyZoom());
    }

    public float getPranEdgeY(Equation equation) {
        return (float)(2*getDpi()*equation.root().getMyZoom());
    }

    public float getCornor() {
        return (float)(10*BaseApp.getApp().getDpi());
    }

    public float getPranIn(Equation equation) {
        return (float)(9*getDpi()*equation.root().getMyZoom());
    }

    public float getStrokeWidth(Equation equation) {
        return (float)(1.5f*getDpi()*equation.root().getMyZoom());
    }

    public float getDivWidthAdd(DivEquation equation) {
        return (float)(20*getDpi()*equation.root().getMyZoom());
    }

    public float getBuffer() {
        return 10*getDpi();
    }

    public float getBuffer(Equation equation) {
        return (float)(getBuffer()*equation.root().getMyZoom());
    }

    public float getSqrtWidthAdd(Equation equation) {
        return(float)( 40*getDpi()*equation.root().getMyZoom());
    }

    public float getSqrtHeightAdd(Equation equation) {
        return(float)( 20*getDpi()*equation.root().getMyZoom());
    }

    public float getbkgBuffer(Equation equation) {
        return (float)(5*getDpi()*equation.root().getMyZoom());
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


    public boolean includeClear() {
        return false;
    }

    public EquationLine getInputLine(Main owner) {
        return new InputLine(owner);
    }

    public KeyBoard getSolveScreenKeyboard(Main owner, AlgebraLine algebraLine) {
//        SharedPreferences settings = BaseApp.getApp().getSharedPreferences("crazy", 0);
//        boolean firstTime = settings.getBoolean("firstTime", true);
//
//        if (firstTime){
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putBoolean("firstTime", false);
//            editor.commit();
//            return new ReturnKeyBoard(owner,algebraLine);
//        }
        return  new AlgebraKeyboard(owner,algebraLine);
    }

    public Typeface getDJV(){
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans.ttf");
        return dj;
    }

    public Typeface getDJVL(){
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        return dj;
    }

    public int getGreyTextColor() {
        return 0xff888888;
    }


    // this is a weird backdoor
    // a few of my apps only use a in the keyboard
    // for example algebra practice
    public boolean hasB() {
        return true;
    }

    public void recordScreen(String s) {
        // Get tracker.
        Tracker t = getTracker();

        // Set screen name.
        t.setScreenName(s);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    public abstract boolean bothSidesPopUps();
}
