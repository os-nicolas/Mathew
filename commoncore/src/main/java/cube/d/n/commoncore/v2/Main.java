package cube.d.n.commoncore.v2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.v2.keyboards.EnptyKeyboard;
import cube.d.n.commoncore.v2.keyboards.InputKeyboard;
import cube.d.n.commoncore.v2.keyboards.KeyBoardManager;
import cube.d.n.commoncore.v2.lines.AlgebraLine;
import cube.d.n.commoncore.v2.lines.InputLine;
import cube.d.n.commoncore.v2.lines.Line;

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
        Line myLine = new InputLine(this);
        keyBoardManager.hardSet(myLine.getKeyboad());
        lines.add(myLine);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (event.getPointerCount() == 1) {
            // pass it on to my bros
            boolean keepGoing = true;
            if (keepGoing) {
                keepGoing = !keyBoardManager.get().onTouch(event);
            }
            if (keepGoing) {
                keepGoing = !lines.get(lines.size()-1).onTouch(event);
            }

        }else{
            // handle zoom or whatever

        }



        return true;
    }

    long startTime = System.currentTimeMillis();
    int frames = 1;

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
        // TODO this is probably really bad for CPU and GPU use
        invalidate();

        long now = System.currentTimeMillis();
        float elapsedTime = (now - startTime) / 1000f;
        frames++;
        if (frames % 100 == 0) {
            Log.i("fps", "" + frames / elapsedTime);
        }
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
        keyBoardManager.set(line.getKeyboad());
    }
}
