package cube.d.n.commoncore;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Colin_000 on 5/3/2015.
 */
public class FadeInTextView extends TextView {
    public long hangTime =500L;
    public int currentColor = 0x00000000;
    public int targetColor;
    public int realPaddingT;
    public int realPaddingB;
    public int realPaddingL;
    public int realPaddingR;
    public float currentPadding=50;
    public int targetPadding=0;

    long startAt=-1;

   public  FadeInTextView(Context context){
       super(context);
       init();
   }

    private void init() {
        targetColor = getCurrentTextColor();
        //ColorDrawable cd = (ColorDrawable) getBackground();
        currentColor = 0xffffff;//cd.getColor();
        setTextColor(currentColor);
        //setTextSize(getTextSize()*BaseApp.getApp().getScale());
        Typeface myTypeface = Typeface.createFromAsset(BaseApp.getApp().getAssets(), "fonts/DejaVuSans-ExtraLight.ttf");
        setTypeface(myTypeface);
        realPaddingB = getPaddingBottom();
        realPaddingT = getPaddingTop();
        realPaddingL = getPaddingLeft();
        realPaddingR = getPaddingRight();
        setPadding(realPaddingL + (int) currentPadding, realPaddingT, realPaddingR - (int) currentPadding, realPaddingB);

    }



    public  FadeInTextView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    public  FadeInTextView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FadeInTextView,
                0, 0);

        try {
            hangTime = (long)a.getInteger(R.styleable.FadeInTextView_timeOut, (int)hangTime);
        } finally {
            a.recycle();
        }
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        long now = System.currentTimeMillis();

       if(startAt != -1 && now-startAt>hangTime) {
            float scale = 45;
            currentColor = BaseApp.colorFade(currentColor, targetColor, scale);
            this.setTextColor(currentColor);
            float fastScale = scale/5f;
            currentPadding = ((fastScale-1)*currentPadding + targetPadding)/fastScale;
            setPadding(realPaddingL+(int)currentPadding,realPaddingT,realPaddingR-(int)currentPadding,realPaddingB);
        }
        if (currentColor != targetColor){
            invalidate();
        }
    }

    public void start() {
        startAt = System.currentTimeMillis();
    }
}
