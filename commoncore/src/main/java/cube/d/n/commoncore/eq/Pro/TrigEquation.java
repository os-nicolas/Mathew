package cube.d.n.commoncore.eq.Pro;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public abstract class TrigEquation<Inverse extends Equation> extends MonaryEquation {


    public TrigEquation(EquationLine owner) {
        super(owner);
    }

    protected  TrigEquation(EquationLine owner, TrigEquation toCopy) {
        super(owner,toCopy);
    }

    public boolean canOperate(){
        try{
            Inverse test = ((Inverse)get(0));
            return true;
        }catch(ClassCastException e){
            // hush it up
        }
        return Operations.sortaNumber(get(0));
    }

    @Override
    public void tryOperator(ArrayList<Equation> equation) {
        if (canOperate()){
            protectedOperate(equation.get(0));
        }
    }

    protected abstract void protectedOperate(Equation equation);

    @Override
    public void privateDraw(Canvas canvas, float x, float y){
        //
        float LHSwidth = measureWidth() - get(0).measureWidth() - (parenthesis()? getParnWidthAddition():0);

        float leftSide = x - measureWidth()/2f;


        Paint temp = getPaint();
        if (parenthesis()) {
            drawParentheses(canvas, x, y, temp);
            leftSide += getParnWidthAddition() / 2;
            if (canvas != null) {
                drawParentheses(canvas, x, y, temp);
            }
        }

        Rect out = new Rect();
        temp.getTextBounds("A", 0, "A".length(), out);
        float h = out.height();
        float w = out.width();
        if (canvas != null) {
            canvas.drawText(getDisplay(-1), leftSide + (LHSwidth/2), y + (h / 2), temp);
        }


        lastPoint = new ArrayList<MyPoint>();
        MyPoint point = new MyPoint(measureWidth(), measureHeight());
        point.x = (int) (leftSide + (LHSwidth/2f));
        point.y = (int) y;
        lastPoint.add(point);

        leftSide += LHSwidth;

        if (canvas!= null) {
            get(0).draw(canvas,leftSide+(get(0).measureWidth()/2f),y);
        }

    }

    @Override
    protected float privateMeasureWidth() {

        float totalWidth= Util.varWidth(getMyWidth(), getDisplay(-1), getPaint());//Math.max(myWidth,textPaint.measureText(display)); //-textPaint.measureText(display.subSequence(0, 1)+"")

        if (parenthesis()){
            totalWidth += getParnWidthAddition();
        }

        return totalWidth+ get(0).measureWidth();
    }

    @Override
    protected float privateMeasureHeightLower() {
        float stringHeightLower= (float) (getMyHeight())/2f;

        float totalHeight = Math.max(stringHeightLower,get(0).measureHeightLower());

        if (parenthesis()){
            totalHeight += PARN_HEIGHT_ADDITION();
        }
        return totalHeight;

    }

    @Override
    protected float privateMeasureHeightUpper() {
        float stringHeightLower= (float) (getMyHeight())/2f;

        float totalHeight = Math.max(stringHeightLower,get(0).measureHeightUpper());

        if (parenthesis()){
            totalHeight += PARN_HEIGHT_ADDITION();
        }
        return totalHeight;
    }
}
