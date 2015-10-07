package cube.d.n.commoncore.eq.any;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.SeletedRowEquationButton;
import cube.d.n.commoncore.eq.BinaryOperator;
import cube.d.n.commoncore.eq.FixedSize;
import cube.d.n.commoncore.eq.MultiCountData;
import cube.d.n.commoncore.eq.MultiCountDatas;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.Operation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin on 1/10/2015.
 */
public class PowerEquation extends Operation implements BinaryEquation, BinaryOperator,FixedSize
{

    public PowerEquation(EquationLine owner) {
        super(owner);

        init();
    }

    @Override
    public void tryOperator(int i) {
        if (i!= size()-1) {
            ArrayList<Equation> toOp = new ArrayList<Equation>();
            toOp.add(get(i));
            toOp.add(get(i + 1));
            tryOperator(toOp);
        }
    }

    private void init() {
        display = "^";
    }

    public PowerEquation(EquationLine owner, PowerEquation equations) {
        super(owner, equations);
        init();
    }

    @Override
    public Equation copy() {
        Equation result = new PowerEquation(this.owner, this);
        return result;
    }

    protected float getScale(Equation e) {
        if (indexOf(e) == 1) {
            if (parent != null) {
                return .75f * parent.getScale(this);
            }
            return 0.75f;
        } else {
            return super.getScale(e);
        }
    }

    @Override
    protected float privateMeasureWidth() {
        if (isSqrt()) {
            return sqrtMeasureWidth();
        } else {
            // TODO
            float totalWidth = 0;

            for (int i = 0; i < size(); i++) {
                totalWidth += get(i).measureWidth();
            }
            if (parenthesis()) {
                totalWidth += getParnWidthAddition();
            }
            return totalWidth;
        }
    }

    private float sqrtMeasureWidth() {
        return get(0).measureWidth() + BaseApp.getApp().getSqrtWidthAdd(this);
    }

    @Override
    public void integrityCheck() {
        super.integrityCheck();
        if (size() != 2) {
            Log.e("ic", "this should be size 2");
        }
    }

   @Override
   public  boolean add(Equation e){
       boolean result = super.add(e);

       // this is a bit dangerous but OK i think
       if (size()==2){
           if (isSqrt() && this.get(1) instanceof DivEquation) {
               Equation oldEq = this.get(1);
               Equation newEq = NumConstEquation.create(new BigDecimal(.5), owner);
               if (oldEq.isSelected()) {
                   newEq.setSelected(true);
               }
               oldEq.replace(newEq);
           }
       }
       return result;
   }

    @Override
    public void tryOperator(ArrayList<Equation> eqs) {
        //TODO handle inbeddedness


        String db = "";
        for (Equation e : eqs) {
            db += e.toString() + ",";
        }
        Log.i("try op power", (eqs.size() == 0 ? "no eqs passed in" : db));


        Equation result = null;

        // this is really dangerous


        if (!this.get(1).isPlusMinus()) {
            boolean wasEvenRoot = isEvenRut();
            boolean wasEven = get(1) instanceof NumConstEquation && ((NumConstEquation) get(1)).getValue().remainder(new BigDecimal(2)).doubleValue() == 0;


            if (power_canPowerZero()) {// if we have asdf^0 return 1
                power_PowerZero();
            } else if (power_canPowerOne()) {// if we have adsfsd^1 return adsfsd
                power_PowerOne();
            } else if (power_canPowerPower()) {// if it is a power equation
                power_PowerPower(result, wasEvenRoot);
            } else if (power_canFlip()) { // a ^-b to (1/a)^b
                power_flip();
            } else if (power_canDivDistribute()) { // (a/b)^c -> (a^c)/(b^c)
                power_DivDistribute();
            } else if (power_canDistribute()) {
                power_Distribute();
            } else if (power_canPowerIsAdd()) {
                power_PowerIsAdd();
            } else if (power_canPowerNum(wasEven)){
                power_PowerNum(result, wasEvenRoot, wasEven);
            }
        }
    }

    @Override
    public ArrayList<SelectedRow> getSelectedRow() {

        ArrayList<SelectedRowButtons> buttons = new ArrayList<>();

        final PowerEquation that = this;

        if (!this.get(1).isPlusMinus()) {
            final boolean wasEvenRoot = isEvenRut();
            final boolean wasEven = get(1) instanceof NumConstEquation && ((NumConstEquation) get(1)).getValue().remainder(new BigDecimal(2)).doubleValue() == 0;

            if (power_canPowerZero()) {// if we have asdf^0 return 1


                buttons.add(new SeletedRowEquationButton(NumConstEquation.create(BigDecimal.ONE, owner), new Action(owner) {
                    @Override
                    protected void privateAct() {
                        MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                        that.power_PowerZero();
                        changed(p);
                    }
                }));

            } else if (power_canPowerOne()) {// if we have adsfsd^1 return adsfsd

                buttons.add(new SeletedRowEquationButton(this.get(0).copy(), new Action(owner) {
                    @Override
                    protected void privateAct() {
                        MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                        that.power_PowerOne();
                        changed(p);
                    }
                }));
            } else {
                if (power_canPowerPower()) {// if it is a power equation

                    buttons.add(new SeletedRowEquationButton(power_PowerPowerEquation(null), new Action(owner) {
                        @Override
                        protected void privateAct() {
                            MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                            that.power_PowerPower(null, wasEvenRoot);
                            changed(p);
                        }
                    }));
                }

                if (power_canFlip()) { // a ^-b to (1/a)^b

                    buttons.add(new SeletedRowEquationButton(power_flipEquation(), new Action(owner) {
                        @Override
                        protected void privateAct() {
                            MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                            that.power_flip();
                            changed(p);
                        }
                    }));
                }

                if (power_canDivDistribute()) { // (a/b)^c -> (a^c)/(b^c)

                    buttons.add(new SeletedRowEquationButton(power_DivDistributeEquation(), new Action(owner) {
                        @Override
                        protected void privateAct() {
                            MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                            that.power_DivDistribute();
                            changed(p);
                        }
                    }));
                }

                if (power_canDistribute()) {
                    buttons.add(new SeletedRowEquationButton(power_DistributeEquation(), new Action(owner) {
                        @Override
                        protected void privateAct() {
                            MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                            that.power_Distribute();
                            changed(p);
                        }
                    }));
                }

                if (power_canPowerIsAdd()) {

                    buttons.add(new SeletedRowEquationButton(power_PowerIsAddEquation(), new Action(owner) {
                        @Override
                        protected void privateAct() {
                            MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                            that.power_PowerIsAdd();
                            changed(p);
                        }
                    }));
                }

                if (power_canPowerNum(wasEven)){

                    buttons.add(new SeletedRowEquationButton(power_IsNumEquation(null, wasEven,wasEvenRoot), new Action(owner) {
                        @Override
                        protected void privateAct() {
                            MyPoint p = that.getNoneNullLastPoint(that.getX(), that.getY());
                            that.power_PowerNum(null, wasEvenRoot, wasEven);
                            changed(p);
                        }
                    }));
                }


            }
        }
        // we try to reduce too
        tryToReduce(buttons, that);

        if (buttons.size() != 0) {
            SelectedRow sr = new SelectedRow(1f / 9f);
            sr.addButtonsRow(buttons, 0, 1);
            ArrayList<SelectedRow> res = new ArrayList<SelectedRow>();
            res.add(sr);
            return res;
        }else{
            ArrayList<SelectedRow> startWith = super.getSelectedRow();
            return startWith;
        }
    }

    private Equation power_PowerPowerEquation(Equation result) {
        result = power_PowerPowerExp(result);

        Equation res;

        if (result instanceof NumConstEquation && ((NumConstEquation) result).getValue().doubleValue() == 1) {
            res =  get(0).get(0).copy();
        } else {
            res = new PowerEquation(owner);
            res.add(get(0).get(0).copy());
            res.add(power_PowerPowerExp(result));
        }

        return res;
    }

    private boolean power_canPowerNum(boolean wasEven) {
        Equation temp = get(1).removeNeg();
        Equation target = (wasEven? get(0).removeSign():get(0));

        if (!(target instanceof NumConstEquation)){
            return false;
        }

        return  temp instanceof NumConstEquation &&(isPosInt(((NumConstEquation) temp).getValue()) || target instanceof NumConstEquation ) ;
    }

    public void power_PowerZero() {
        replace(NumConstEquation.create(BigDecimal.ONE, owner));
    }

    public boolean power_canPowerZero() {
        return Operations.sortaNumber(get(1)) && Operations.getValue(get(1)).doubleValue() == 0;
    }

    public void power_PowerOne() {
        replace(get(0).copy());
    }

    public boolean power_canPowerOne() {
        return Operations.sortaNumber(get(1)) && Operations.getValue(get(1)).doubleValue() == 1;
    }

    public boolean power_canPowerPower() {
        return get(0) instanceof PowerEquation;
    }

    public void power_PowerPower(Equation result, boolean wasEvenRoot) {
        result = power_PowerPowerExp(result);

        Equation inner = null;
        if (result instanceof NumConstEquation && ((NumConstEquation) result).getValue().doubleValue() == 1) {
            inner =get(0).get(0);
        } else {
            inner =get(0).copy();
            inner.get(1).replace(result);
        }

        if (wasEvenRoot && !(Operations.sortaNumber(result) && Operations.getValue(result).doubleValue() == 0)) {
            Equation toReplace= this;
            while (toReplace.parent instanceof PlusMinusEquation){
                toReplace=toReplace.parent;
            }
            toReplace.replace(inner.plusMinus());
        } else {
            replace(inner);
        }
    }

    private Equation power_PowerPowerExp(Equation result) {
        // we multi

        MultiCountDatas left = new MultiCountDatas(get(0).get(1).copy());
        MultiCountDatas right = new MultiCountDatas(get(1).copy());

        MultiCountDatas outcome = Operations.Multiply(left, right, true, owner);

        if (outcome.size() == 1) {
            MultiCountData mine = ((MultiCountData) outcome.toArray()[0]);
            result = mine.getEquation(owner);
        } else if (outcome.size() > 1) {
            result = new AddEquation(owner);
            for (MultiCountData e : outcome) {
                result.add(e.getEquation(owner));
            }
        }

        if (outcome.neg) {
            result = result.negate();
        }
        return result;
    }


    public boolean power_canFlip() {
        boolean neg = false;

        Equation temp = get(1).copy();
        while (temp instanceof MinusEquation) {
            temp = temp.get(0);
            neg = !neg;
        }

        return neg;
    }

    public void power_flip() {
        Equation result = power_flipEquation();

        replace(result);
    }

    private Equation power_flipEquation() {
        Equation result;// we don't worry about ---4 because if we hit one of those it should have operated first
        Equation exp = get(1).get(0).copy();
        if (get(0) instanceof DivEquation) {
            // if its a div we just flip it
            Equation inner = new DivEquation(owner);
            inner.add(this.get(0).get(1).copy());
            inner.add(this.get(0).get(0).copy());
            result = new PowerEquation(owner);
            result.add(inner);
            result.add(exp);

        } else {
            // else write 1/get(0)
            Equation inner = new PowerEquation(owner);
            inner.add(this.get(0).copy());
            inner.add(exp);
            result = new DivEquation(owner);
            result.add(NumConstEquation.create(BigDecimal.ONE, owner));
            result.add(inner);
        }
        return result;
    }

    public boolean power_canDivDistribute() {
        return get(0) instanceof DivEquation;
    }

    public void power_DivDistribute() {
        Equation result = power_DivDistributeEquation();
        replace(result);
    }

    private Equation power_DivDistributeEquation() {
        Equation result;
        result = new DivEquation(owner);
        Equation top = new PowerEquation(owner);
        top.add(this.get(0).get(0).copy());
        top.add(this.get(1).copy());
        result.add(top);
        Equation bot = new PowerEquation(owner);
        bot.add(this.get(0).get(1).copy());
        bot.add(this.get(1).copy());
        result.add(bot);
        return result;
    }

    public boolean power_canDistribute() {
        return get(0) instanceof MultiEquation;
    }

    public void power_Distribute() {
        Equation result = power_DistributeEquation();
        replace(result);
    }

    private Equation power_DistributeEquation() {
        Equation result;
        result = new MultiEquation(owner);
        Equation cpy = get(0).copy();
        for (Equation e : cpy ) {
            Equation power = new PowerEquation(owner);
            power.add(e);
            power.add(get(1).copy());
            result.add(power);
        }
        return result;
    }

    public boolean power_canPowerIsAdd() {
        return get(1) instanceof AddEquation;
    }

    public void power_PowerIsAdd() {
        Equation result = power_PowerIsAddEquation();
        replace(result);
    }

    private Equation power_PowerIsAddEquation() {
        Equation result;
        result = new MultiEquation(owner);
        Equation cpy = get(1);
        for (Equation e : cpy ) {
            Equation pow = new PowerEquation(owner);
            pow.add(get(0).copy());
            pow.add(e.copy());
            result.add(pow);
        }
        return result;
    }

    public void power_PowerNum(Equation result, boolean wasEvenRoot, boolean wasEven) {
        // if it's an add split it up
        // if it's numb and number you can just do it
        // if it's numb on top
        result = power_IsNumEquation(result, wasEven,wasEvenRoot);


        if (result != null) {
            if (result instanceof MultiEquation && this.parent instanceof MultiEquation) {
                int at = this.parent.indexOf(this);
                for (Equation e : result) {
                    this.parent.add(at++, e);
                }
                justRemove();
            } else {
                if (this.parent != null && this.parent instanceof PlusMinusEquation){
                    while (result instanceof PlusMinusEquation) {
                        result = result.get(0);
                    }
                }
                replace(result);
            }
        }
    }

    private Equation power_IsNumEquation(Equation result, boolean wasEven, boolean wasEvenRoot) {
        // if the right is a number
        BigDecimal toPowerBD = Operations.getValue(get(1).copy());

        // if it is an int
        if (isPosInt(toPowerBD)) {
            int leftValue = toInt(toPowerBD);

            // if left is a var write x^3 -> x*x*x
            if (leftValue > 1 && get(0).reallyInstanceOf(VarEquation.class)) {
                result = new MultiEquation(owner);
                for (int i = 0; i < leftValue; i++) {
                    result.add(get(0).copy());
                }
            } else {
                MultiCountDatas left = new MultiCountDatas();
                left.add(new MultiCountData());
                for (int i = 0; i < leftValue; i++) {
                    MultiCountDatas right = new MultiCountDatas(get(0).copy());
                    left = Operations.Multiply(left, right, true, owner);
                }
                if (left.size() == 1) {
                    MultiCountData mine = ((MultiCountData) left.toArray()[0]);
                    result = mine.getEquation(owner);
                } else if (left.size() > 1) {
                    result = new AddEquation(owner);
                    for (MultiCountData e : left) {
                        result.add(e.getEquation(owner));
                    }
                }

                if (left.neg) {
                    if (result instanceof NumConstEquation) {
                        result = result.get(0);
                    } else {
                        result = result.negate();
                    }
                }

                if (wasEven && result instanceof PlusMinusEquation) {
                    result = result.get(0);
                }
            }
        } else {

            double leftValue = Operations.getValue(get(0).copy()).doubleValue();
            double rightValue = Operations.getValue(get(1).copy()).doubleValue();
            boolean plusMinus = false;

            Equation leftTemp = get(0);
            while (leftTemp instanceof SignEquation ) {
                if (leftTemp instanceof PlusMinusEquation) {
                    plusMinus = true;
                }
                leftTemp = leftTemp.get(0);
            }

            if (leftValue ==0){
                String db = "";
            }

            if (leftTemp instanceof NumConstEquation && (leftValue >=0 || Math.floor(rightValue) == rightValue)) {

                double resultValue = Math.pow(leftValue, rightValue);

                if (!Double.isInfinite(resultValue) && !Double.isNaN(resultValue)) {

                    result = NumConstEquation.create(new BigDecimal(resultValue), owner,true);
                    if (plusMinus && !wasEven && resultValue != 0) {
                        result = result.plusMinus();
                    }
                }

            }
        }
        if (result != null) {
            if (wasEvenRoot && !(Operations.sortaNumber(result) && Operations.getValue(result).doubleValue() == 0)) {
                result = result.plusMinus();
            }
        }

        return result;
    }

    private boolean isPosInt(BigDecimal value) {
       return  (Math.floor(value.doubleValue()) == value.doubleValue() && !get(0).reallyInstanceOf(NumConstEquation.class) && value.compareTo(new BigDecimal(15)) < 0);
    }

    private  int toInt(BigDecimal value){
        return (int)Math.floor(value.doubleValue());
    }

    // is used to tell if we need to +/- cases A^(1/(o*2)) where o is odd

    private boolean isEvenRut() {
        if (Operations.sortaNumber(get(1)) && Operations.getValue(get(1)).doubleValue() == 0) {
            return false;
        }

        if (Operations.sortaNumber(get(1))){
            double power = Operations.getValue(get(1)).doubleValue();
            if ((1/(power))%2 ==0){//-Math.floor(power)
                return true;
            }
        }
        // there are other cases but we do not worry about them
        return false;

    }

    public boolean isSqrt() {
        if (get(1) instanceof NumConstEquation && ((NumConstEquation) get(1)).getValue().equals(new BigDecimal(.5))) {
            return true;
        }
        if (get(1) instanceof DivEquation && get(1).get(0) instanceof NumConstEquation && ((NumConstEquation) get(1).get(0)).getValue().doubleValue() == 1 && get(1).get(1) instanceof NumConstEquation && ((NumConstEquation) get(1).get(1)).getValue().equals(new BigDecimal(2))) {
            return true;
        }
        // there are other cases but we do not worry about them
        return false;
    }

    // y is not centered is that a problem - this at a problem
    // yes i think it is
    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        Paint temp = getPaint();
        lastPoint = new ArrayList<MyPoint>();
        if (isSqrt()) {
            sqrtDraw(canvas, x, y, temp);
        } else {
            float totalWidth = measureWidth();
            float atX = x - (totalWidth / 2);
            float atY = y;

            if (parenthesis()) {
                drawParentheses(canvas, x, y, temp);
                atX += getParnWidthAddition() / 2;
            }
            Rect out = new Rect();
            temp.getTextBounds(display, 0, display.length(), out);
            float h = out.height();


            for (int i = 0; i < size(); i++) {
                float currentWidth = get(i).measureWidth();

                atX += (currentWidth / 2);

                if (i == 1) {
                    // we want the bottom of 1 to be at 2/3 height of 0
                    // what's the math then
                    float baseLoc = get(0).measureHeightLower() - (get(0).measureHeight() * 2f / 3f);
                    atY += baseLoc - get(i).measureHeightLower();
                }

                get(i).draw(canvas, atX, atY);
                if (i == 1) {
                    Equation tempEq = get(i);
                    while (tempEq instanceof MinusEquation) {
                        tempEq = tempEq.get(0);
                    }

                    if (!(tempEq instanceof PowerEquation)) {
                        MyPoint point = new MyPoint(tempEq.measureWidth(), tempEq.measureHeight());
                        point.x = (int) atX;
                        point.y = (int) (atY - tempEq.measureHeightUpper() + tempEq.measureHeight() / 2f);
                        lastPoint.add(point);
                        //Paint green = new Paint();
                        //green.setColor(Color.GREEN);
                        //green.setAlpha(0x80);
                        if (canvas != null) {
                            Rect r = new Rect(point.x - getMyWidth() / 2, point.y - getMyHeight() / 2, point.x + getMyWidth() / 2, point.y + getMyHeight() / 2);
                            //.drawRect(r, green);
                        }
                    }
                }
                atX += (currentWidth / 2);
            }
        }
    }

    public static void sqrtSignDraw(Canvas canvas, float atX, float y, Paint p, Equation e) {
        sqrtSignDraw(canvas, atX, y, p, e.measureHeightLower(), e.measureHeightUpper(), e.x + e.measureWidth() / 2,e);
    }

    public static void sqrtSignDraw(Canvas canvas, float atX, float y, Paint p, float lower, float upper, float end,Equation equation) {
        if (canvas != null) {
            // TODO scale by dpi
            float width_addition = BaseApp.getApp().getSqrtWidthAdd(equation);
            float height_addition = BaseApp.getApp().getSqrtHeightAdd(equation);
            p.setStrokeWidth(BaseApp.getApp().getStrokeWidth(equation));
            atX += width_addition / 5f;
            canvas.drawLine(atX, y, atX + width_addition / 5f, y, p);
            atX += width_addition / 5f;
            canvas.drawLine(atX, y, atX + width_addition / 5f, y + lower - height_addition / 2, p);
            atX += width_addition / 5f;
            canvas.drawLine(atX, y + lower - height_addition / 2, atX + width_addition / 5f, (y - upper) + height_addition / 2, p);
            atX += width_addition / 5f;
            canvas.drawLine(atX, (y - upper) + height_addition / 2, end, (y - upper) + height_addition / 2, p);
        }
    }


    private void sqrtDraw(Canvas canvas, float x, float y, Paint p) {
        drawSqrtBkg(canvas, x, y);

        // let's start by drawing the sqrt sign
        float atX = x - measureWidth() / 2;
        sqrtSignDraw(canvas, atX, y, p, this);

        // now let's draw the content
        get(0).draw(canvas, x + BaseApp.getApp().getSqrtWidthAdd(this) / 2, y);

        // now we need to put the last point
        MyPoint point = new MyPoint(BaseApp.getApp().getSqrtWidthAdd(this), measureHeight());
        point.x = (int) (x - measureWidth() / 2 + BaseApp.getApp().getSqrtWidthAdd(this) / 2);
        point.y = (int) (y - measureHeightUpper() + (measureHeight() / 2));
        //canvas.drawLine(point.x + 3, point.y, point.x - 3, point.y, p);
        lastPoint.add(point);

        //update the location of thing
        get(1).x = point.x;
        get(1).y = point.y;
    }

    private int mybkgAlpha = 0x00;

    private void drawSqrtBkg(Canvas canvas, float x, float y) {
        updateBkgColors();

        int scale = BaseApp.getApp().getRate();

        if (get(1).isSelected() || get(1).demo) {
            mybkgAlpha = (mybkgAlpha * (scale - 1) + 0xFF) / scale;
        } else {
            mybkgAlpha = (mybkgAlpha * (scale - 1) + 0x00) / scale;
        }

        if (canvas != null) {//&& mybkgAlpha >= getMaxBkgAlpha()
            Paint p = new Paint();
            p.setColor(BaseApp.getApp().lightColor);
            p.setAlpha(mybkgAlpha);
            p.setStrokeWidth(BaseApp.getApp().getStrokeWidth(this));

            float bkgBuffer = BaseApp.getApp().getbkgBuffer(this);

            // we use a path to draw this weird shape
            float top = (y - measureHeightUpper() - bkgBuffer);
            float left = (x - (measureWidth() / 2f) - bkgBuffer);
            float bot1 = (y + measureHeightLower() + bkgBuffer);
            float right1 = ((x - measureWidth() / 2) + BaseApp.getApp().getSqrtWidthAdd(this));//+ bkgBuffer/4
            float bot2 = ((y - measureHeightUpper()) + BaseApp.getApp().getSqrtHeightAdd(this)); //+bkgBuffer/4
            float right2 = ((x + measureWidth() / 2) + bkgBuffer);

            Path path = new Path();
            //p.setStyle(Paint.Style.FILL);

            float rounded = BaseApp.getApp().getCornor();

            // start top left right of the first curve
            path.moveTo(left + rounded, top);

            //top left cornor
            RectF tlc = new RectF(left, top, left + 2 * rounded, top + 2 * rounded);
            path.arcTo(tlc, -90, -90);

            // left line
            path.lineTo(left, bot1 - rounded);

            // bottom left cornor
            RectF blc = new RectF(left, bot1 - 2 * rounded, left + 2 * rounded, bot1);
            path.arcTo(blc, -180, -90);

            // first bottom line
            path.lineTo(right1 - rounded, bot1);

            //first right left cornor
            RectF brc1 = new RectF(right1 - 2 * rounded, bot1 - 2 * rounded, right1, bot1);
            path.arcTo(brc1, 90, -90);

            // first right line
            path.lineTo(right1, bot2 + rounded);

            // inside cornor
            RectF ic = new RectF(right1, bot2, right1 + 2 * rounded, bot2 + 2 * rounded);
            path.arcTo(ic, 180, 90);

            // second bottom line
            path.lineTo(right2 - rounded, bot2);

            // second right left cornor
            RectF brc2 = new RectF(right2 - 2 * rounded, bot2 - 2 * rounded, right2, bot2);
            path.arcTo(brc2, 90, -90);

            // second right line
            path.lineTo(right2, top + rounded);

            // top right cornor
            RectF trc = new RectF(right2 - 2 * rounded, top, right2, top + 2 * rounded);
            path.arcTo(trc, 0, -90);

            // final line
            path.moveTo(left + rounded, top);

            path.close();
            canvas.drawPath(path, p);
        }
    }

    @Override
    protected float privateMeasureHeightLower() {
        if (isSqrt()) {
            return sqrtMeasureHeightLower();
        } else {
            float result = get(0).measureHeightLower();
            if (parenthesis()) {
                result += PARN_HEIGHT_ADDITION() / 2f;
            }
            return result;
        }
    }

    private float sqrtMeasureHeightLower() {
        return get(0).measureHeightLower();
    }

    @Override
    protected float privateMeasureHeightUpper() {
        if (isSqrt()) {
            return sqrtMeasureHeightUpper();
        } else {

            float r0 = get(0).measureHeightUpper();


            float r1 = -get(0).measureHeightLower() + (get(0).measureHeight() * 2f / 3f) + get(1).measureHeight();

            float result = Math.max(r0, r1);
            if (parenthesis()) {
                result += PARN_HEIGHT_ADDITION() / 2f;
            }
            return result;
        }
    }

    private float sqrtMeasureHeightUpper() {
        return get(0).measureHeightUpper() + BaseApp.getApp().getSqrtHeightAdd(this);
    }

    @Override
    protected float privateMeasureHeight() {
        return measureHeightLower() + measureHeightUpper();
    }
}
