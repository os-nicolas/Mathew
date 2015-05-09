package cube.d.n.commoncore.v2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
* Created by Colin_000 on 5/7/2015.
*/
public class Main extends View implements View.OnTouchListener {

    final private KeyBoardManager keyBoardManager = new KeyBoardManager();

    public ArrayList<Line> lines = new ArrayList<>();

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
        keyBoardManager.hardSet(new EnptyKeyboard(this));
        lines.add(new InputLine(this));
        keyBoardManager.set(new InputKeyboard(this, (InputLine) lines.get(0)));
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (event.getPointerCount() == 1) {
            // pass it on to my bros


        }else{
            // handle zoom or whatever

        }



        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        height = canvas.getHeight();
        width = canvas.getWidth();

        float top = 0;
        float left = 0;
        for (Line l: lines){
            if (inScreen(l,top)){
                l.draw(canvas,top,left,new Paint());
                top+= l.measureHeight();
            }
        }

        keyBoardManager.draw(canvas, top, left, new Paint());
    }

    private boolean inScreen(Line l, float top) {
        if (top > height- keyBoardManager.get().measureHeight() &&
                keyBoardManager.getNextKeyboard()== null || top > height- keyBoardManager.getNextKeyboard().measureHeight()){
            return false;
        }
        if (top + l.measureHeight() < 0){
            return false;
        }
        return true;
    }

}
