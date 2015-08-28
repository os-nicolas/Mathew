package cube.d.n.commoncore.eq.Pro;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.SeletedRowEquationButton;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.FixedSize;
import cube.d.n.commoncore.eq.MultiCountData;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public abstract class TrigEquation<Inverse extends Equation> extends MonaryEquation implements FixedSize {


    public TrigEquation(EquationLine owner) {
        super(owner);
    }


    protected  TrigEquation(EquationLine owner, TrigEquation toCopy) {
        super(owner,toCopy);
        this.display = toCopy.getDisplay(-1);
    }

    public boolean canOperate(){
        return isInverse(get(0)) || Operations.sortaNumber(get(0)) && inDomain(Operations.getValue(get(0)).doubleValue());
    }

    protected abstract boolean inDomain(double value);

    @Override
    protected boolean willOperateOn(ArrayList<Equation> onsList) {
        return true;
    }

    @Override
    public SelectedRow getSelectedRow() {
        final Equation a = get(0);


        ArrayList<SelectedRowButtons> buttons = new ArrayList<>();

        final Equation that = this;

        if (canOperate()) {
            buttons.add(new SeletedRowEquationButton(innerOperate(get(0).copy()),new Action(owner) {
                @Override
                protected void privateAct() {
                    MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                    that.replace(innerOperate(get(0).copy()));
                    changed(p);
                }
            }));
        }


        // we try to reduce too
        tryToReduce(buttons, that);

        if (buttons.size() != 0){
            SelectedRow sr = new SelectedRow(1f/9f);
            sr.addButtonsRow(buttons,0,1);
            return sr;
        }else{
            return null;
        }
    }

    @Override
    public void tryOperator(ArrayList<Equation> equation) {
        Log.d("tried trig", this.toString());
        if (canOperate()) {
            Equation newEq = innerOperate(get(0));
            this.replace(newEq);
        }
    }

    protected abstract Equation protectedOperate(Equation equation);


    protected Equation innerOperate(Equation equation){
        if (isInverse(equation)){
            return equation.get(0);
        }
        return protectedOperate(equation);
    }

    protected abstract boolean isInverse(Equation equation);
//    {
//        try{
//            Inverse test = ((Inverse)equation);
//            int temp = test.size();
//            return true;
//        }catch(ClassCastException e){
//            // hush it up
//        }
//        return false;
//    }

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
