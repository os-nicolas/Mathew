package cube.d.n.commoncore.eq.any;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.lines.EquationLine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Colin on 1/28/2015.
 */
public abstract class MonaryEquation extends Equation {

    public MonaryEquation(EquationLine owner2) {
        super(owner2);
        init();
    }

    private void init() {
        setMyWidth((int)(BaseApp.getApp().getDefaultSize()*.75));

    }

    @Override
    public void tryOperator(int i) {
        ArrayList<Equation> toOp = new ArrayList<Equation>();
        toOp.add(get(i));
        tryOperator(toOp);
    }


    public MonaryEquation(EquationLine owner, MonaryEquation monEq) {
        super(owner,monEq);
        init();
    }

    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        if(drawSign()) {
            lastPoint = new ArrayList<MyPoint>();
            float totalWidth = measureWidth();
            float currentX = 0;
            Paint temp = getPaint();
            if (parenthesis()) {
                    drawParentheses(canvas, x, y, temp);
                currentX+= getParnWidthAddition()/2;
            }
            Rect out = new Rect();
            temp.getTextBounds("A", 0, display.length(), out);
            float h = out.height();

            // draw the minus sign
            MyPoint point = new MyPoint(getMyWidth(),getMyHeight());
            point.x = (int) (x - (totalWidth / 2) + currentX + (getMyWidth() / 2));
            point.y = (int) (y + (h / 2));
            if (canvas != null) {
                canvas.drawText(getDisplay(0), point.x, point.y, temp);
            }
            point.y = (int) (y);
            lastPoint.add(point);
            currentX += getMyWidth();

            //draw the contence
            float currentWidth = get(0).measureWidth();
            float currentHeight = get(0).measureHeight();
            get(0).draw(canvas,
                    x - (totalWidth / 2) + currentX + (currentWidth / 2), y);
            currentX += currentWidth;
        }else{
            super.privateDraw(canvas,x,y);
        }
    }

    public boolean drawSign() {
        if (parenthesis()){
            return true;
        }

        // if you have 5-(4a+6) we want to write it 5+-(4a+6) so they can tap the - to distribute
        if (get(0) instanceof AddEquation && get(0).size() >1){
            return true;
        }
        Equation at = parent;
        int count = 0;
        while (at instanceof  AddEquation) {
                if (parent.indexOf(this) != 0) {
                    return false;
                }
            at = at.parent;
            count++;
            if (count >30){
                Log.e("the bed is","shat");
            }
        }
        return true;

//        Equation lft = left();

//        if (parent instanceof MinusEquation){
//            return true;
//        }

//        return lft == null || !(lft.parent instanceof AddEquation);
    }

    @Override
    protected float privateMeasureWidth() {
        // if we are not the first element in an add equation
        float result;
        if (drawSign()) {
            result= (float) (getMyWidth() + super.privateMeasureWidth());
        }else{
            result=super.privateMeasureWidth();
        }
        return result;
    }


    @Override
    protected HashSet<Equation> getEquationsFormLastPoint(int i) {
        HashSet<Equation> result = new HashSet<Equation>();
        result.add(this);
        return result;
    }

    @Override
    protected HashSet<Equation> getEquationsFormLastPointForSelect(int i) {
        HashSet<Equation> result = new HashSet<Equation>();
        result.add(this);
        return result;
    }


    @Override
    public void remove(){
        super.remove();
        if (size() ==0){
            add(new NumConstEquation(BigDecimal.ONE,owner));
        }
    }

    @Override
    public void integrityCheck() {
        super.integrityCheck();
        if (size()!=1){
            Log.e("ic", "this should always be size 1");
        }
    }
}
