package cube.d.n.commoncore.eq.write;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.ErrorReporter;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.DragEquation;
import cube.d.n.commoncore.eq.EquationDis;
import cube.d.n.commoncore.eq.FixedSize;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Colin on 1/6/2015.
 */
public class WritingEquation extends Equation {

    public WritingEquation(EquationLine o) {
        super(o);
        init();
    }



    private void init() {
        display = "\"";
    }

    public WritingEquation(EquationLine owner, WritingEquation equations) {
        super(owner, equations);
        init();
    }


    public boolean deepLegal() {
        return deepLegal(this);
    }

    private boolean deepLegal(Equation eq) {
        if (illegal()){
            return false;
        }
        for (Equation e : eq) {
             if (e instanceof WritingLeafEquation) {
                if (((WritingLeafEquation) e).illegal()) {
                    return false;
                }
            } else  if (e instanceof  WritingEquation){
                if (!((WritingEquation) e).deepLegal()){
                   return false;
                }
            } else {
                if (!deepLegal(e)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Equation copy() {
        Equation result = new WritingEquation(owner,this);
        return result;
    }

    public ArrayList<EquationDis> closest(DragEquation dragging) {
        if (size() !=0){
            return super.closest(dragging);
        }else{
            ArrayList<EquationDis> result = new ArrayList<>();
            result.add(new EquationDis(this, dragging, EquationDis.Side.left));
            result.add(new EquationDis(this, dragging, EquationDis.Side.right));
            return result;
        }
    }

    public ArrayList<EquationDis> closest(float x, float y) {
        if (size() !=0){
            return super.closest(x,y);
        }else{
            ArrayList<EquationDis> result = new ArrayList<>();
            result.add(new EquationDis(this, x,y, EquationDis.Side.left));
            result.add(new EquationDis(this, x,y, EquationDis.Side.right));
            return result;
        }
    }

    @Override
    public Paint getPaint() {
        // check if we are legal
        if (illegal()){
            Paint temp = new Paint(super.getPaint());
            temp.setColor(Color.RED);
            return temp;
        }
        return super.getPaint();
    }

    private boolean illegal() {
        return size()==0;
    }

    /**
     * x,y is the center of the equation to be drawn
     */
    @Override
    protected void privateDraw(Canvas canvas, float x, float y) {
        lastPoint = new ArrayList<MyPoint>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        if (parenthesis()) {
            drawParentheses(canvas, x, y, temp);
            currentX += getParnWidthAddition() / 2;
        }

        for (int i = 0; i < size(); i++) {
            float currentWidth = get(i).measureWidth();
            float currentHeight = get(i).measureHeight();
            get(i).draw(canvas,
                    x - (totalWidth / 2) + currentX + (currentWidth / 2), y);
            currentX += currentWidth;
        }
        if (size() == 0){
            if (canvas != null ) {
                Rect out = new Rect();
                temp.getTextBounds("A", 0, "A".length(), out);
                float h = out.height();
                float w = out.width();
                canvas.drawText("?", x, y + (h / 2), temp);
            }
            MyPoint point = new MyPoint(getMyWidth(), getMyHeight());
            point.x = (int) (x);
            point.y = (int) (y);
            lastPoint.add(point);
        }
    }

    @Override
    public void tryOperator(int i) {
        // do nothing!
    }

    protected float privateMeasureHeightUpper() {
        if (size() != 0){
            return super.privateMeasureHeightUpper();
        }else {
            float totalHeight= (float) (getMyHeight());

            if (parenthesis()){
                totalHeight += PARN_HEIGHT_ADDITION();
            }
            return totalHeight/2f;
        }
    }

    protected float privateMeasureHeightLower() {
        if (size() != 0){
            return super.privateMeasureHeightLower();
        }else {
            float totalHeight= (float) (getMyHeight());

            if (parenthesis()){
                totalHeight += PARN_HEIGHT_ADDITION();
            }
            return totalHeight/2f;
        }
    }

    @Override
    protected float privateMeasureWidth() {
        float totalWidth = 0;

        if (size() ==0){
            totalWidth = Util.varWidth(getMyWidth(), "?", getPaint());
        }else {
            for (int i = 0; i < size(); i++) {
                totalWidth += get(i).measureWidth();
            }
        }
        if (parenthesis()) {
            totalWidth += getParnWidthAddition();
        }
        return totalWidth;
    }

    public Equation convert() {

        //TODO flatten?
        ErrorReporter.log("converting:",toString());

        addImpliedMultiplication();

        Log.i("after doing implied multiplication", toString());

        Equation root = null;
        Equation currentToAdd = null;
        Equation left = null;
        //boolean openParen = false;
        boolean wasParen = false;
        int minus = 0;
        boolean plusMinus = false;

        for (int i = 0; i < size(); i++) {
            Equation at = get(i);
            Log.i("root", (root == null ? "null" : root.toString()));
            Log.i("at", at.toString());
            if (currentToAdd == null) {
                Log.i("left", (left == null ? "null" : left.toString()));
                if (at.getDisplay(-1).equals("+")) {
                    currentToAdd = new AddEquation(owner);
                } else if (at.getDisplay(-1).equals(times())) {
                    currentToAdd = new MultiEquation(owner);
                } else if (at.getDisplay(-1).equals("=")) {
                    currentToAdd = new EqualsEquation(owner);
                } else if (at.getDisplay(-1).equals("-") && (i == 0 || (get(i - 1) instanceof WritingLeafEquation && get(i - 1).getDisplay(-1).equals("=")))) {
                    minus++;
                } else if (at.getDisplay(-1).equals("-")) {
                    currentToAdd = new AddEquation(owner);
                    minus++;
                }  else if (at.getDisplay(-1).equals("\u00B1")) {
                    currentToAdd = new AddEquation(owner);
                    plusMinus=true;
                } else {
                    if (at instanceof WritingPraEquation && ((WritingPraEquation) at).left) {
                        boolean wasSqrt =at instanceof WritingSqrtEquation;

                        Equation temp = ((WritingPraEquation) at).popBlock();
                        temp.remove(0);
                        temp.remove(temp.size() - 1);
                        // we need to remove the first and last
                        left = convert(temp);
                        if (wasSqrt){
                            Equation oldEq = left;
                            left = new PowerEquation(owner);
                            left.add(oldEq);
                            left.add(new NumConstEquation(new BigDecimal(.5),owner));
                        }
                        wasParen = true;

                    } else {
                        left = convert(at);
                    }

                    if (plusMinus){
                        left = left.plusMinus();
                        plusMinus = false;
                        minus=0;
                    }else {
                        //TODO this minus could go inside but we are not going to worry about that now
                        while (minus > 0) {
                            left = left.negate();
                            minus--;
                        }
                    }
                    if (left instanceof AddEquation || left instanceof MultiEquation) {
                        currentToAdd = left;
                        left = null;
                    }
                }
                if (currentToAdd != null) {
                    if (root == null) {
                        root = currentToAdd;
                    } else {
                        root.add(currentToAdd);
                    }
                    if (left != null) {
                        currentToAdd.add(left);
                    }
                    if (currentToAdd instanceof EqualsEquation) {
                        currentToAdd = null;
                    }
                    left = null;
                }
            } else {
                if (at instanceof WritingLeafEquation) {
                    Equation newEq = null;
                    if (at.getDisplay(-1).equals("+")) {
                        newEq = new AddEquation(owner);
                    } else if (at.getDisplay(-1).equals(times())) {
                        newEq = new MultiEquation(owner);
                    } else if (at.getDisplay(-1).equals("=")) {
                        newEq = new EqualsEquation(owner);
                    } else if (at instanceof WritingPraEquation && ((WritingPraEquation) at).left) {
                        //openParen = true;
                    } else if (at.getDisplay(-1).equals("-")) {
                        if (!get(i - 1).getDisplay(-1).equals(times())) {
                            newEq = new AddEquation(owner);
                        }
                        minus++;
                    } else if (at.getDisplay(-1).equals("\u00B1")) {
                        if (!get(i - 1).getDisplay(-1).equals(times())) {
                            newEq = new AddEquation(owner);
                        }
                        plusMinus = true;
                    }
                    if (newEq != null) {
                        if (newEq.getClass().equals(currentToAdd.getClass())) {
                            //we don't worry about it
                        } else {
                            if (newEq instanceof MultiEquation) {
                                //Equation last = get(i - 1);
                                //(last instanceof WritingPraEquation && !((WritingPraEquation) last).left) ||
                                if (wasParen) {
                                    if (currentToAdd.parent instanceof MultiEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        if (currentToAdd.parent == null) {
                                            newEq.add(currentToAdd);
                                            root = newEq;
                                        } else {
                                            Equation oldEq = currentToAdd;
                                            oldEq.replace(newEq);
                                            newEq.add(oldEq);
                                        }
                                    }
                                    currentToAdd = newEq;
                                } else {
                                    Equation moving = currentToAdd.get(currentToAdd.size() - 1);
                                    currentToAdd.justRemove(moving);
                                    if (currentToAdd.parent instanceof MultiEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        currentToAdd.add(newEq);
                                    }
                                    newEq.add(moving);
                                    currentToAdd = newEq;
                                }
                                //openParen = false;
                            } else if (newEq instanceof AddEquation) {
                                //if (openParen) {
                                //    Equation moving = currentToAdd.get(currentToAdd.size() - 1);
                                //    currentToAdd.justRemove(moving);
                                //    if (currentToAdd.parent instanceof AddEquation) {
                                //        newEq = currentToAdd.parent;
                                //   } else {
                                //        currentToAdd.add(newEq);
                                //    }
                                //    newEq.add(moving);
                                //    currentToAdd = newEq;
                                //} else {
                                    if (currentToAdd.parent instanceof AddEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        if (currentToAdd.parent == null) {
                                            newEq.add(currentToAdd);
                                            root = newEq;
                                        } else {
                                            Equation oldEq = currentToAdd;
                                            oldEq.replace(newEq);
                                            newEq.add(oldEq);
                                        }
                                    }
                                    currentToAdd = newEq;
                                //}
                                //openParen = false;
                            } else if (newEq instanceof EqualsEquation) {
                                Equation before;
                                if (root != null) {
                                    before = root;
                                } else {
                                    before = left;
                                }
                                newEq.add(before);
                                root = newEq;
                                currentToAdd = null;
                                left = null;
                            }
                        }
                    } else {
                        if (at instanceof WritingPraEquation && ((WritingPraEquation) at).left) {
                            boolean wasSqrt = at instanceof WritingSqrtEquation;
                            Equation temp = ((WritingPraEquation) at).popBlock();
                            temp.remove(0);
                            temp.remove(temp.size() - 1);
                            // we need to remove the first and last
                            at = convert(temp);
                            if (wasSqrt){
                                Equation oldEq = at;
                                at = new PowerEquation(owner);
                                at.add(oldEq);
                                at.add(new NumConstEquation(new BigDecimal(.5),owner));
                            }
                            wasParen = true;

                            if (plusMinus){
                                at = at.plusMinus();
                                plusMinus = false;
                                minus=0;
                            }else {
                                //TODO this minus could go inside but we are not going to worry about that now
                                while (minus > 0) {
                                    at = at.negate();
                                    minus--;
                                }
                            }
                            currentToAdd.add(at);
                        }
                    }
                } else {

                    at = convert(at);

                    if (plusMinus){
                        at = at.plusMinus();
                        plusMinus = false;
                        minus=0;
                    }else {
                        //TODO this minus could go inside but we are not going to worry about that now
                        while (minus > 0) {
                            at = at.negate();
                            minus--;
                        }
                    }
                    currentToAdd.add(at);
                }
                if (!(at instanceof WritingPraEquation)) {
                    wasParen = false;
                }
            }
        }
        if (root == null) {
            Log.i("conversion result-left", left.toString());
            return left;
        } else {

            if (root.size() == 1 && left != null) {
                // if have something of the form =---1
                root.add(left);
            }else if (root.size() == 1 && (root instanceof AddEquation || root instanceof MultiEquation)){
                root = root.get(0);
            }
            Log.i("conversion result", root.toString());
            return root;
        }
    }

    private String times() {
        return owner.modeController().getMultiSymbol();
    }

    private Equation convert(Equation at) {

        if (at instanceof WritingEquation) {
            return ((WritingEquation) at).convert();
        } else if (at instanceof FixedSize) {
            // convert the top and the bottom
            // and then add it
            at = convertFixedSize((FixedSize) at);
            return at;
        } else {
            return at;
        }
    }

    private void addImpliedMultiplication() {
        // we interate over and handle xx or 2( or 2x or )x or x2 or 2(2/4) or (2/4)2
        for (int i = 0; i < size() - 1; i++) {
            Equation left = get(i);
            if (left instanceof BinaryEquation) {
                left = addImpliedMultiplicationDiv((BinaryEquation) left);
            }
            Equation right = get(i + 1);
            if (right instanceof BinaryEquation) {
                right = addImpliedMultiplicationDiv((BinaryEquation) right);
            }
            if ((left instanceof BinaryEquation || left instanceof NumConstEquation || left instanceof VarEquation || (left instanceof WritingPraEquation && !((WritingPraEquation) left).left)) &&
                    (right instanceof BinaryEquation || right instanceof NumConstEquation || right instanceof VarEquation || (right instanceof WritingPraEquation && ((WritingPraEquation) right).left))) {
                // we need to insert a *
                add(i + 1, new WritingLeafEquation(times(), (InputLine)owner));
            }
        }
    }

    private Equation addImpliedMultiplicationDiv(BinaryEquation at) {
        for (Equation e : (Equation) at) {
            if (e instanceof WritingEquation) {
                ((WritingEquation) e).addImpliedMultiplication();
            } else if (e instanceof BinaryEquation) {
                e.replace(addImpliedMultiplicationDiv((BinaryEquation) e));
            }
        }
        return (Equation) at;
    }

    private Equation convertFixedSize(FixedSize at) {
        for (Equation e : (Equation) at) {
            if (e instanceof WritingEquation) {
                e.replace(((WritingEquation) e).convert());
            }
        }
        return (Equation) at;
    }
}
