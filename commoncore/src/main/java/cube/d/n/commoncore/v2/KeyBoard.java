package cube.d.n.commoncore.v2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;


import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.Measureable;
import cube.d.n.commoncore.PopUpButton;

/**
* Created by Colin_000 on 5/7/2015.
*/
public abstract class KeyBoard implements Measureable {

    public final Main owner;
    ArrayList<Button> buttons = new ArrayList<Button>();
    public ArrayList<PopUpButton> popUpButtons = new ArrayList<>();
    public float buttonsPercent;

    public KeyBoard(Main owner){
        this.owner = owner;
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

    public void onTouch(MotionEvent event){
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

    public void draw(Canvas canvas, float top, float left, Paint paint){
        for (Button myBut: buttons) {
            myBut.draw(canvas,paint);
        }

        buttonsPercent = getBaseButtonsPercent();
        for (PopUpButton myPUB: popUpButtons) {
            myPUB.updateLocation(this);
            myPUB.draw(canvas,paint);
        }
    }

    abstract protected void addButtons();

    public abstract float getBaseButtonsPercent();

}
