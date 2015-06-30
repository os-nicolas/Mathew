package cube.d.n.commoncore.keyboards;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;


import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.Measureable;
import cube.d.n.commoncore.PopUpButton;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.TouchMode;
import cube.d.n.commoncore.lines.EquationLine;

/**
* Created by Colin_000 on 5/7/2015.
*/
public abstract class KeyBoard implements Measureable {

    public final Main owner;
    ArrayList<Button> buttons = new ArrayList<Button>();
    public ArrayList<PopUpButton> popUpButtons = new ArrayList<>();
    public ArrayList<SelectedRow> popUpLines = new ArrayList<>();
    public float buttonsPercent;
    protected final EquationLine line;
    private boolean active= true;



    public KeyBoard(Main owner,EquationLine line){
        this.owner = owner;
        this.line = line;
        addButtons();
    }

    public float measureHeight(){
        return owner.height*buttonsPercent;
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

    public float measureWidth(){
        return owner.width;
    }

    protected void addButtonsRow(ArrayList<Button> row, float top, float bottum) {
        addButtonsRow(row, 0, 1, top, bottum);
    }

    /**
     * null in row indicate a skip
     * @param row
     * @param left
     * @param right
     * @param top
     * @param bottum
     */
    protected void addButtonsRow(ArrayList<Button> row, float left, float right, float top, float bottum) {
        float count = row.size();
        float at = left;
        float step = (right - left) / count;

        for (float i = 0; i < count; i++) {
            Button b = row.get((int) i);
            if (b!=null){
                b.setLocation(at, at + step, top, bottum);
                buttons.add(b);
            }
            at += step;
        }

    }

    TouchMode myMode;

    public boolean onTouch(MotionEvent event) {
        if (active) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (in(event)) {
                    myMode = TouchMode.KEYBOARD;

                } else {
                    myMode = TouchMode.NOPE;
                }
            }
            if (myMode == TouchMode.KEYBOARD) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    for (Button myBut : buttons) {
                        myBut.click(event);
                    }
                    for (PopUpButton pub : popUpButtons) {
                        pub.click(event);
                    }
                    for (SelectedRow pub : popUpLines) {
                        pub.click(event);
                    }
                } else {
                    for (Button myBut : buttons) {
                        myBut.hover(event);
                    }
                    for (PopUpButton pub : popUpButtons) {
                        pub.hover(event);
                    }
                    for (SelectedRow pub : popUpLines) {
                        pub.hover(event);
                    }
                }
                return true;
            }
        }
        return false;
    }


    protected boolean in(MotionEvent event) {
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
        for (SelectedRow pub : popUpLines) {
            if (pub.couldClick(event)) {
                return true;
            }
        }
        return false;
    }


    public void draw(Canvas canvas, float top, float left, Paint paint){
        for (Button myBut: buttons) {
            myBut.draw(canvas,paint);
        }


        buttonsPercent = getBaseButtonsPercent();
        int startAt1 = ((int) (owner.height - measureHeight()));

        for (PopUpButton myPUB: popUpButtons) {
            myPUB.updateLocation(this);
            myPUB.draw(canvas, paint);
        }



        for (SelectedRow myPUB: popUpLines) {
            myPUB.updateLocation(this);
            myPUB.draw(canvas, paint);
        }
        drawShadow(canvas,paint.getAlpha()/2,startAt1);

        drawShadow(canvas,paint.getAlpha()/2);
    }

    private void drawShadow(Canvas canvas, int alpha, int at) {
        Paint p = new Paint();
        int color = Color.BLACK;//BaseApp.getApp().darkDarkColor;
        p.setColor(color);
        p.setAlpha(alpha);
//        for (int i=0;i<2f/Algebrator.getAlgebrator().getDpi();i++){
//            canvas.drawLine(0,at,width,at,p);
//            at--;
//        }
        p.setAlpha((int)(0x8f*(alpha/((float)0xff))));
        while (p.getAlpha() > 1) {
            canvas.drawLine(0, at, measureWidth(), at, p);
            p.setAlpha((int) (p.getAlpha() / BaseApp.getApp().getShadowFade()));
            at--;
        }
    }

    protected void drawShadow(Canvas canvas,int alpha) {
        int startAt = ((int) (owner.height - measureHeight()));
        drawShadow(canvas,alpha,startAt);
    }

    abstract protected void addButtons();

    public abstract float getBaseButtonsPercent();

    public void deactivate() {
        active = false;
    }

    public void reactivate() {
        active = true;
    }
}
