package colin.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.Animation;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.DragLocation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Physical;
import colin.example.algebrator.Pop;
import colin.example.algebrator.SuperView;

abstract public class Equation extends ArrayList<Equation> implements Physical {

    protected static final float PARN_HEIGHT_ADDITION = 6 * Algebrator.getAlgebrator().getDpi();

    private static int idBacker = 0;
    public Equation parent;
    public boolean demo = false;
    public float x = 0;
    public float y = 0;
    public ArrayList<MyPoint> lastPoint = new ArrayList<MyPoint>();
    protected String display = "";
    protected boolean selected = false;
    protected int myWidth;
    protected int myHeight;
    public SuperView owner;
    private int id;
    private int buffer = 10;


    protected float lastMeasureWidth = -1;
    protected float lastMeasureHeight = -1;
    protected float lastMeasureHeightUpper = -1;
    protected float lastMeasureHeightLower = -1;
    protected long lastMeasureWidthAt = -1;
    protected long lastMeasureHeightAt = -1;
    protected long lastMeasureHeightUpperAt = -1;
    protected long lastMeasureHeightLowerAt = -1;
    private int bkgColor = Algebrator.getAlgebrator().lightColor;
    public boolean active = true;

    public Equation(SuperView owner2, Equation eq) {
        init(owner2);
        // copy all the kiddos and set this as their parent
        for (int i = 0; i < eq.size(); i++) {
            add(eq.get(i).copy());
        }
    }

    public boolean parenthesis() {
        // are we an add inside a * or a -
        boolean result = this instanceof AddEquation && (this.parent instanceof MultiEquation || this.parent instanceof MinusEquation);
        // are we an a the first element of a ^
        if (owner instanceof ColinView) {
            result = result || (this.parent instanceof PowerEquation && this.parent.indexOf(this) == 0 && this.size() != 0);
            //result = result || (this.parent instanceof MultiEquation && (this instanceof MinusEquation || this instanceof PlusMinusEquation));
        }
        if (owner instanceof EmilyView) {
            if (this.parent instanceof PowerEquation && this.parent.indexOf(this) == 0) {
                return true;
            }
        }
        return result;
    }

    public Equation(SuperView owner2) {
        init(owner2);
    }

    private void init(SuperView owner2) {
        owner = owner2;
        id = idBacker++;
    }

    // we could template this in C++ can we in java?

    public void integrityCheck() {
        if (this.parent!= null && !this.parent.contains(this)){
            Log.e("kid not in parent",this.toString() + " " + parent.toString());
        }
        if (parent != null && owner.stupid.deepContains(this) && ! owner.stupid.deepContains(parent)){
            Log.e("parent is wrong",this.toString() + " " + parent.toString());
        }
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDisplay(int pos) {
        return display;
    }

    /**
     * makes a copy the entire equation tree below and including this node good
     * for showing work
     *
     * @return
     */
    public abstract Equation copy();

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        Log.i("selecting", this.toString());
        if (selected) {
            if (owner.selected != null) {
                owner.selected.setSelected(false);
            }
            owner.selected = this;
        } else if (owner.selected.equals(this)) {
            owner.selected = null;
        }
        this.selected = selected;
    }

    public boolean tryFlatten() {
        if (((this instanceof WritingEquation && this.parent instanceof WritingEquation) ||
                (this instanceof AddEquation && this.parent instanceof AddEquation) ||
                (this instanceof MultiEquation && this.parent instanceof MultiEquation))) {
            // add all the bits of this to it's parent
            int at = this.parent.indexOf(this);
            justRemove();
            for (Equation e : this) {
                parent.add(at++, e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Equation equation) {
        boolean result = super.add(equation);
        equation.parent = this;
        return result;
    }

    @Override
    public boolean addAll(int i,Collection<? extends Equation> equations) {
        for (Equation e:equations){
            this.add(i,e);
            i++;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Equation> equations) {
        for (Equation e:equations){
            this.add(e);
        }
        return true;
    }


    @Override
    public void add(int i, Equation equation) {
        super.add(i, equation);
        equation.parent = this;
    }

    //public abstract boolean isFlex();

    public ArrayList<EquationDis> closest(DragEquation dragging) {
        ArrayList<EquationDis> result = new ArrayList<EquationDis>();
        for (int i = 0; i < size(); i++) {
            //result.add(new EquationDis(get(i),x,y));
            result.addAll(get(i).closest(dragging));

        }
        if (this instanceof DivEquation) {
            result.add(new EquationDis(this, dragging, EquationDis.Side.left));
            result.add(new EquationDis(this, dragging, EquationDis.Side.right));
        }

        Collections.sort(result);
        return result;
    }

    public ArrayList<EquationDis> closest(float x, float y) {
        ArrayList<EquationDis> result = new ArrayList<EquationDis>();
        for (int i = 0; i < size(); i++) {
            //result.add(new EquationDis(get(i),x,y));
            result.addAll(get(i).closest(x, y));

        }
        if (this instanceof DivEquation) {
            result.add(new EquationDis(this, x, y, EquationDis.Side.left));
            result.add(new EquationDis(this, x, y, EquationDis.Side.right));
        }

        Collections.sort(result);
        return result;
    }

    public float measureWidth() {
        if (Algebrator.getAlgebrator().at == lastMeasureWidthAt || (!active && lastMeasureWidth != -1)) {
            return lastMeasureWidth;
        } else {
            lastMeasureWidth = privateMeasureWidth();
            lastMeasureWidthAt = Algebrator.getAlgebrator().at;
            return lastMeasureWidth;
        }
    }

    protected float privateMeasureWidth() {
        float totalWidth = 0;
        for (int i = 0; i < size() - 1; i++) {
            if (!(this instanceof MultiEquation) || (((MultiEquation) this).hasSign(i))) {
                totalWidth += myWidth + myWidthAdd();
            }
        }

        for (int i = 0; i < size(); i++) {
            totalWidth += get(i).measureWidth();
        }

        if (parenthesis()) {
            totalWidth += getParnWidthAddition();
        }
        return totalWidth;
    }

    protected float myWidthAdd() {
        return 2*Algebrator.getAlgebrator().getDpi();
    }

    public void draw(Canvas canvas, float x, float y) {
        this.x = x;
        this.y = y;

        owner.hasUpdated= true;


        drawBkgBox(canvas, x, y);

        privateDraw(canvas, x, y);

        drawLastPoints(canvas);

    }

    private void drawLastPoints(Canvas canvas) {
        for (MyPoint point : lastPoint) {
            point.draw(canvas);
        }
    }


    /**
     * x,y is the center of the equation to be drawn
     * if canvas is null it just updates the location but does not draw anything
     */
    protected void privateDraw(Canvas canvas, float x, float y) {
        lastPoint = new ArrayList<MyPoint>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        if (parenthesis()) {
            drawParentheses(canvas, x, y, temp);
            currentX += getParnWidthAddition() / 2;
        }
        Rect out = new Rect();
        //we always want to center operation?
        temp.getTextBounds(display, 0, display.length(), out);
        float h = out.height();
        for (int i = 0; i < size(); i++) {
            float currentWidth = get(i).measureWidth();
            float currentHeight = get(i).measureHeight();
            get(i).draw(canvas,
                    x - (totalWidth / 2) + currentX + (currentWidth / 2), y);
            currentX += currentWidth;

            if (i != size() - 1) {
                if (!(this instanceof MultiEquation) || (((MultiEquation) this).hasSign(i))) {
                    float pointWidth = myWidth + myWidthAdd();
                    MyPoint point = new MyPoint(pointWidth, myHeight);

                    point.x = (int) (x - (totalWidth / 2) + currentX + (pointWidth / 2));
                    point.y = (int) (y + (h / 2));
                    if (canvas != null) {
                        canvas.drawText(getDisplay(i + 1), point.x, point.y, temp);
                    }
                    point.y = (int) (y);
                    lastPoint.add(point);
                    currentX += pointWidth;
                } else {
                    MyPoint point = new MyPoint(myWidth, myHeight);
                    point.x = (int) (x - (totalWidth / 2) + currentX);
                    point.y = (int) (y);
                    lastPoint.add(point);
                }
            }
        }
    }


    public float measureHeightLower() {
        if (Algebrator.getAlgebrator().at == lastMeasureHeightLowerAt || (!active && lastMeasureHeightLower != -1)) {
            return lastMeasureHeightLower;
        } else {

            lastMeasureHeightLower = privateMeasureHeightLower();
            lastMeasureHeightLowerAt = Algebrator.getAlgebrator().at;
            return lastMeasureHeightLower;
        }
    }

    protected float privateMeasureHeightLower() {

        float totalHeight = myHeight / 2;

        for (int i = 0; i < size(); i++) {
            if (get(i).measureHeightLower() > totalHeight) {
                totalHeight = get(i).measureHeightLower();
            }
        }
        if (parenthesis()) {
            totalHeight += PARN_HEIGHT_ADDITION / 2f;
        }
        return totalHeight;
    }

    public float measureHeightUpper() {
        if (Algebrator.getAlgebrator().at == lastMeasureHeightUpperAt || (!active && lastMeasureHeightUpper != -1)) {
            return lastMeasureHeightUpper;
        } else {
            lastMeasureHeightUpper = privateMeasureHeightUpper();
            lastMeasureHeightUpperAt = Algebrator.getAlgebrator().at;
            return lastMeasureHeightUpper;
        }


    }

    protected float privateMeasureHeightUpper() {
        float totalHeight = myHeight / 2f;

        for (int i = 0; i < size(); i++) {
            if (get(i).measureHeightUpper() > totalHeight) {
                totalHeight = get(i).measureHeightUpper();
            }
        }
        if (parenthesis()) {
            totalHeight += PARN_HEIGHT_ADDITION / 2f;
        }
        return totalHeight;
    }

    public boolean deepContains(Equation equation) {
        Equation current = equation;
        while (true) {

            if (current.equals(this)) {
                return true;
            }
            if (current.parent == null) {
                return false;
            } else {
//                if (!current.parent.contains(current)) {
//                    Log.e("deepContains", "current not in parent");
//                }

                current = current.parent;
            }
        }
    }

    public float measureHeight() {
        if (Algebrator.getAlgebrator().at == lastMeasureHeightAt || (!active && lastMeasureHeight != -1)) {
            return lastMeasureHeight;
        } else {
            lastMeasureHeight = privateMeasureHeight();
            lastMeasureHeightAt = Algebrator.getAlgebrator().at;
            return lastMeasureHeight;
        }

    }

    protected float privateMeasureHeight() {
        return measureHeightLower() + measureHeightUpper();
    }

    public HashSet<Equation> on(float x, float y) {

        for (int i = 0; i < lastPoint.size(); i++) {
            if (lastPoint.get(i).on(x, y)) {
                return getEquationsFormLastPoint(i);
            }
        }
        return new HashSet<Equation>();
    }

    protected HashSet<Equation> getEquationsFormLastPoint(int i) {
        HashSet<Equation> result = new HashSet<Equation>();
        // we need to get the left
        if (this instanceof PowerEquation) {
            result.add(get(1));
        } else {
            Log.d("at: ", this.toString());
            Log.d("at: ", this.lastPoint.size() + "");
            Equation at = get(i);
            while (at instanceof AddEquation || at instanceof MultiEquation || at instanceof MinusEquation) {
                at = at.get(at.size() - 1);
            }
            result.add(at);
            at = get(i + 1);
            while (at instanceof AddEquation || at instanceof MultiEquation || at instanceof MinusEquation) {
                at = at.get(0);
            }
            result.add(at);
        }
        return result;
    }


    protected HashSet<Equation> getEquationsFormLastPointForSelect(int i) {
        HashSet<Equation> result = new HashSet<Equation>();
        // we need to get the left
        if (this instanceof PowerEquation) {
            result.add(get(1));
        } else {
            Log.d("at: ", this.toString());
            Log.d("at: ", this.lastPoint.size() + "");
            Equation at = get(i);
            result.add(at);
            at = get(i + 1);
            result.add(at);
        }
        return result;
    }

    public HashSet<Equation> OnAnyEqualsIncluded(float x, float y) {
        HashSet<Equation> result = on(x, y);
        if (result.size() != 0) {
            return result;
        }
        for (int i = 0; i < size(); i++) {
            result = get(i).onAny(x, y);
            if (result.size() != 0) {
                return result;
            }
        }
        return result;
    }

    public HashSet<Equation> onAny(float x, float y) {
        HashSet<Equation> result = on(x, y);

        if (!(this instanceof EqualsEquation)) {

            if (result.size() != 0) {
                return result;
            }
        }
        for (int i = 0; i < size(); i++) {
            result = get(i).onAny(x, y);
            if (result.size() != 0) {
                return result;
            }
        }
        return result;
    }

    public boolean tryOperator(float x, float y) {
        for (Equation e : this) {
            if (e.tryOperator(x, y)) {
                return true;
            }
        }

        owner.stupid.updateLocation();
        Object[] ons = on(x, y).toArray();
        Equation old = owner.stupid.copy();

        String db = "";
        for (Object o : ons) {
            Equation e = (Equation) o;
            db += e.toString() + " , ";
        }
        Log.i("tryOperator ", db);

        if (ons.length != 0) {
            ArrayList<Equation> onsList = new ArrayList<Equation>();
            String debug = "";
            for (Object o : ons) {
                Equation e = (Equation) o;
                if (o != this) {
                    int at = deepIndexOf(e);
                    Equation toAdd = get(at);
                    debug += toAdd.toString() + ",";
                    onsList.add(toAdd);
                }
            }
            Log.i("tryOperator", debug);
            if (onsList.size() != 0) {
                tryOperator(onsList);
            } else if (this instanceof MinusEquation) {
                tryOperator(onsList);
            }

            if (owner instanceof ColinView) {
                MyPoint myPoint = getLastPoint(x, y);
                Log.i("did it change?",old.toString() + " " + owner.stupid.toString());
                if (!(old.same(owner.stupid))) {
                    ((ColinView) owner).changed = true;

                    // we are moving this code anyway
//                    boolean samePower = (get(0).removeNeg() instanceof PowerEquation &&
//                            get(1).removeNeg() instanceof PowerEquation &&
//                            get(0).removeNeg().get(1).same(get(1).removeNeg().get(1)));
//
//                    // see if we need to to warn
//                    if (this instanceof DivEquation
//                            && !(this.get(0) instanceof AddEquation)    // if the top is an add they just rewrote it
//                            && !samePower                               // if they are the same power they just rewrote it
//                            ){
//                        Equation warn = ((DivEquation)this).botCouldBeZero();
//                        if (warn!=null){
//                            ((ColinView) owner).changedEq = warn;
//                        }
//                    }

                    // if we operated we should be the one to pop
                    for (int i = 0; i < owner.afterAnimations.size(); i++) {
                        Animation a = owner.afterAnimations.get(i);
                        if (a instanceof Pop) {
                            owner.afterAnimations.remove(a);
                            i--;
                        }
                    }
                    owner.afterAnimations.add(new Pop(myPoint, owner));
                    return true;
                }

                // otherwise we clear and add if we are the closest
                // and we are not a num const equation
                //if (!(this instanceof NumConstEquation)){
                float ourDis = myPoint.distance(x, y);
                Log.d("dis", this.toString() + " " + ourDis);
                boolean closest = true;
                for (Animation a : owner.afterAnimations) {
                    if (a instanceof Pop) {
                        float aDis = ((Pop) a).myPoint.distance(x, y);
                        closest = ourDis <= aDis;
                        if (!closest) {
                            break;
                        }
                    }
                }
                if (closest) {
                    for (int i = 0; i < owner.afterAnimations.size(); i++) {
                        Animation a = owner.afterAnimations.get(i);
                        if (a instanceof Pop) {
                            owner.afterAnimations.remove(a);
                            i--;
                        }
                    }
                    owner.afterAnimations.add(new Pop(myPoint, owner));
                }

                //}

            }
        }
        return false;
    }

    private MyPoint getLastPoint(float x, float y) {
        for (int i = 0; i < lastPoint.size(); i++) {
            if (lastPoint.get(i).on(x, y)) {
                return lastPoint.get(i);
            }
        }
        return null;
    }


    public void tryOperator(ArrayList<Equation> equation) {
    }

    float MIN_TEXT_SIZE = 12;

    protected float getScale(Equation e) {
        if (parent != null) {
            return parent.getScale(this);
        }
        return 1f;
    }


    private Paint mPaint = null;

    public Paint getPaint() {
        if (mPaint == null) {
            updatePaint();
        }
        float targetTextSize = Algebrator.getAlgebrator().textPaint.getTextSize();
        if (parent != null) {
            targetTextSize *= parent.getScale(this);
            if (targetTextSize < MIN_TEXT_SIZE) {
                targetTextSize = MIN_TEXT_SIZE;
            }
        }
        mPaint.setTextSize(targetTextSize);
        return mPaint;
    }

    private void updatePaint() {
        if (parent != null) {
            mPaint = new Paint(parent.getPaint());
        } else {

            mPaint = new Paint(Algebrator.getAlgebrator().textPaint);
            // probably not needed
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setSubpixelText(true);
        }
    }


    protected int getMaxBkgAlpha() {
        Equation at = this;
        int currentMax = at.bkgAlpha;
        while (at.parent != null) {
            if (at.bkgAlpha > currentMax) {
                currentMax = at.bkgAlpha;
            }
            at = at.parent;
        }
        return currentMax;
    }

    public int bkgAlpha = 0x00;
    //TODO scale by dpi


    protected void drawBkgBox(Canvas canvas, float x, float y) {
        updateBkgColors();
        float bkgBuffer = Algebrator.getAlgebrator().getbkgBuffer();
        if (canvas != null && bkgAlpha == getMaxBkgAlpha()) {
            Paint p = new Paint();
            p.setColor(bkgColor);
            p.setAlpha(bkgAlpha);
            RectF r = new RectF((int) (x - measureWidth() / 2) - bkgBuffer,
                    (int) (y - measureHeightUpper()) - bkgBuffer,
                    (int) (x + measureWidth() / 2) + bkgBuffer, (int) (y + measureHeightLower() + bkgBuffer));
            canvas.drawRoundRect(r, Algebrator.getAlgebrator().getCornor(), Algebrator.getAlgebrator().getCornor(), p);
        }
    }

    long lastNoUpdate = 0;
    long noUpdateFor = 200;

    protected void updateBkgColors() {
        long now = System.currentTimeMillis();
        if (now - lastNoUpdate > noUpdateFor) {

            bkgColor = Algebrator.colorFade(bkgColor, Algebrator.getAlgebrator().lightColor);
            int scale = Algebrator.getAlgebrator().getRate();

            boolean dark = isSelected() || demo;

            if (this instanceof PlaceholderEquation) {
                dark = ((PlaceholderEquation) this).drawBkg;
            }

            if (dark) {
                bkgAlpha = (bkgAlpha * (scale - 1) + 0xFF) / scale;
            } else {
                bkgAlpha = (bkgAlpha * (scale - 1) + 0x00) / scale;
            }
        }
    }


    public Equation lowestCommonContainer(Equation eq) {
        // TODO slow
        Equation at = this;
        while (true) {
            if (at == null) {
                Log.e("lowestCommonContainer at is null", toString() + " " + eq.toString());
            }
            if (at.deepContains(eq)) {
                return at;
            } else {
                at = at.parent;
            }
        }
    }

    protected static void drawParentheses(boolean left, Canvas canvas, float x, float y, Paint ptemp, float uh, float lh, Equation myEq) {
        float edgeX = Algebrator.getAlgebrator().getPranEdgeX(myEq);
        float edgeY = Algebrator.getAlgebrator().getPranEdgeY(myEq);
        float in = Algebrator.getAlgebrator().getPranIn();

        if (left) {
            //left side
            canvas.drawLine(x + edgeX, y - uh + edgeY, x + in + edgeX, y - uh + edgeY, ptemp);
            canvas.drawLine(x + edgeX, y - uh + edgeY, x + edgeX, y + lh - edgeY, ptemp);
            canvas.drawLine(x + edgeX, y + lh - edgeY, x + in + edgeX, y + lh - edgeY, ptemp);
        } else {
            //right side
            canvas.drawLine(x - edgeX, y - uh + edgeY, x - in - edgeX, y - uh + edgeY, ptemp);
            canvas.drawLine(x - edgeX, y - uh + edgeY, x - edgeX, y + lh - edgeY, ptemp);
            canvas.drawLine(x - edgeX, y + lh - edgeY, x - in - edgeX, y + lh - edgeY, ptemp);
        }
    }


    protected void drawParentheses(Canvas canvas, float x, float y, Paint temp) {
        if (canvas != null) {
            Paint ptemp = new Paint(temp);
            ptemp.setStrokeWidth(Algebrator.getAlgebrator().getStrokeWidth());
            float w = measureWidth();
            float hu = measureHeightUpper();
            float hl = measureHeightLower();

            drawParentheses(true, canvas, x - (w / 2), y, ptemp, hu, hl, this);
            drawParentheses(false, canvas, x + (w / 2), y, ptemp, hu, hl, this);

        }
    }


    public boolean addContain(Equation equation) {
        Equation current = equation;
        while (true) {
            if (!(current instanceof EqualsEquation || current instanceof AddEquation || current.equals(equation) || current instanceof MinusEquation )) {//|| current instanceof PlusMinusEquation
                return false;
            } else if (current.equals(this)) {
                return true;
            } else {
                current = current.parent;
            }
        }
    }

    public EqualsEquation getEquals() {
        Equation at = this.parent;
        while (!(at instanceof EqualsEquation)) {
            if (at instanceof WritingEquation) {
                return null;
            }
            at = at.parent;
        }
        return ((EqualsEquation) at);
    }

    /**
     * check to see if all the generations between an equation and this are
     * divEquations or MultiEquations equations are considered to contain
     * themselves
     *
     * @param equation
     * @return
     */
    public boolean DivMultiContain(Equation equation) {
        Equation current = equation;
        while (true) {
            if (!(current instanceof EqualsEquation || current instanceof MinusEquation || current instanceof PlusMinusEquation || current instanceof MultiDivSuperEquation || current.equals(equation))) {
                return false;
            } else if (current.equals(this)) {
                return true;
            } else {
                current = current.parent;
            }
        }
    }

    @Override
    public boolean remove(Object e) {
        if (e instanceof Equation && this.contains(e)) {
            remove(indexOf(e));
            return true;
        }
        return false;
    }

    @Override
    public Equation remove(int pos) {
        Equation result = super.remove(pos);
        if (result != null) {
            if (this.size() == 1 && this.parent != null) {
                this.replace(get(0));
            } else if (size() == 0) {
                remove();
            }
        }
        return result;
    }

    @Override
    public Equation set(int index, Equation eq) {
        Equation result = super.set(index, eq);
        eq.parent = this;

        return result;

    }

    public boolean same(Equation eq) {
        if (!this.getClass().equals(eq.getClass())) {
            return false;
        }
        if (this.size() != eq.size()) {
            return false;
        }

        ArrayList<Equation> list2 = new ArrayList<Equation>(eq);

        for (Equation e : this) {
            boolean any = false;
            for (Equation ee : list2) {
                if (ee.same(e)) {
                    list2.remove(ee);
                    any = true;
                    break;
                }
            }
            if (!any) {
                return false;
            }
        }
        return true;

    }

    public void replace(Equation eq) {
        if (parent != null) {
            int index = parent.indexOf(this);
            // oh man does this seem dangerous
            if ((parent instanceof MultiEquation && eq instanceof MultiEquation)
                ||(parent instanceof MultiEquation && eq instanceof MultiEquation)){
                for (Equation e:eq){
                    parent.add(index++,e);
                }
                this.parent.remove(this);
            }else {
                this.parent.set(index, eq);
            }
        } else {
            owner.stupid = eq;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Equation) {
            return ((Equation) other).hashCode() == hashCode();
        }
        return false;
    }

    protected void operateRemove(ArrayList<Equation> ops) {
        for (Equation e : ops) {
            if (contains(e)) {
                e.justRemove();
            } else {
                e.remove();
            }
        }
    }

    public boolean reallyInstanceOf(Class t) {
        Equation at = removeNeg();
        return t.isInstance(at);
    }

    public Equation removeNeg() {
        Equation at = this;
        while (at instanceof MinusEquation || at instanceof PlusMinusEquation) {
            at = at.get(0);
        }
        return at;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void remove() {
        parent.remove(this);
    }

//	public boolean canPop() {
//		// we can remove if it is equals equations all the way up
//		Equation at = parent;
//		while (at instanceof AddEquation || at instanceof EqualsEquation){
//			if (at instanceof EqualsEquation){
//				return true;
//			}
//			at = at.parent;
//		}
//		
//		// or if multiDivSupers all the way up
//		// and if not like a/(b/c) can't remove b ... but we will let you
//		// if a/(b/c) you can drag c to be (a*c)/b
//		
//		at = parent;
//		while (at instanceof MultiDivSuperEquation || at instanceof EqualsEquation){
//			if (at instanceof EqualsEquation){
//				return true;
//			}
//			at = at.parent;
//		}
//		
//		return false;
//	}

    public void isDemo(boolean b) {
        if (b) {
            if (owner.dragging != null && owner.dragging.demo != null) {
                owner.dragging.demo.isDemo(false);
            }
            owner.dragging.demo = this;
            demo = true;
        } else {
            owner.dragging.demo = null;
            demo = false;
        }

    }

    public int side() {
        Equation equals = getEquals();
        if (equals != null) {
            return ((EqualsEquation) equals).side(this);
        } else {
            //find the root
            Equation at = this;
            while (at.parent != null) {
                at = at.parent;
            }
            if (at instanceof WritingEquation) {
                int ourIndex = at.deepIndexOf(this);
                int equalsIndex = -1;
                for (Equation e : at) {
                    if (e instanceof WritingLeafEquation && e.getDisplay(-1).equals("=")) {
                        equalsIndex = at.indexOf(e);
                    }
                }
                if (equalsIndex == -1) {
                    Log.e("", "it should really have an equals");
                    return 0;
                }
                if (ourIndex == equalsIndex) {
                    Log.e("", "we should really not be checking the side of the equals");
                    return 0;
                }
                return (ourIndex < equalsIndex ? 0 : 1);
            } else {
                Log.e("", "this is bad");
                return 0;
            }
        }
    }

    public void integrityCheckOuter() {
        integrityCheck();
        if (parent != null) {
            if (!parent.deepContains(this)) {
                Log.e("ic", "parent does not contain this");
            }
        } else if (!(this instanceof EqualsEquation || this instanceof WritingEquation)) {
            Log.e("ic", "has no parent");
        }
        for (Equation e : this) {
            e.integrityCheckOuter();
        }
    }

    public void fixIntegrety() {
        for (Equation e : this) {
            e.fixIntegrety();
        }
    }

    public void updateOwner(SuperView sv) {
        owner = sv;
        for (Equation e : this) {
            e.updateOwner(sv);
        }
    }

    public Equation left() {
        return next(true);
    }

    public Equation right() {
        return next(false);
    }

    private Equation next(boolean left) {
        Equation at = this;
        if (at.parent == null) {
            return null;
        }
        while (at.parent.indexOf(at) == (left ? 0 : (at.parent.size() - 1))) {
            at = at.parent;
            if (at.parent == null) {
                return null;
            }
        }
        return at.parent.get(at.parent.indexOf(at) + (left ? -1 : 1));
    }

    public Integer deepIndexOf(Equation eq) {
        Equation at = eq;
        if (at.parent == null) {
            debug();
        }
        while (at.parent != this) {
            at = at.parent;
            if (at.parent == null) {
                debug();
            }
        }
        int result = indexOf(at);
        if (result == -1) {
            Log.i("cgrrr", "oh man is that bad");
        }
        return result;
    }

    private void debug() {

    }

    // returns an updated version of dragging, removed or added equals sign or whatever
    public Equation tryOp(Equation dragging, boolean right, Op op) {
        Log.i("try", this.hashCode() + " " + this.display);

        if (parent.indexOf(this) == -1) {
            Log.i("", "dead on arival");
        }
        boolean can = false;
        if (op == Op.ADD) {
            can = CanAdd(dragging);
        } else if (op == Op.MULTI) {
            can = canMuli(dragging);
        } else if (op == Op.DIV) {
            can = canDiv(dragging);
        } else if (op == Op.POWER) {
            can = canPower(dragging);
        }

        if (can) {
            boolean notSameSide = (op == Op.ADD && side() != dragging.side());

            boolean thisNeg = false;
            if (op == Op.ADD) {
                Equation at = this.parent;
                while (at != null) {
                    if (at instanceof MinusEquation) {
                        thisNeg = !thisNeg;
                    }
                    at = at.parent;
                }
            }
            boolean dragNeg = false;
            if (op == Op.ADD) {
                Equation at = dragging.parent;
                while (at != null) {
                    if (at instanceof MinusEquation) {
                        dragNeg = !dragNeg;
                    }
                    at = at.parent;
                }
            }

            //peel off the minus signs
            ArrayList<MinusEquation> minusSigns = new ArrayList<MinusEquation>();
            while (this.parent instanceof MinusEquation) {
                minusSigns.add((MinusEquation) this.parent);
                this.parent.replace(this);
            }

            if (op == Op.POWER) {
                dragging.remove();
                Equation power;
                // write as 1/8 or decimal?
                //&& Math.floor(1/((NumConstEquation) dragging).getValue()) == 1/((NumConstEquation) dragging).getValue()
                if (dragging instanceof NumConstEquation && ((NumConstEquation) dragging).getValue().doubleValue() != 0) {
                    power = new NumConstEquation(BigDecimal.ONE.divide(((NumConstEquation) dragging).getValue(), 20, RoundingMode.HALF_UP), ((NumConstEquation) dragging).owner);
                } else {
                    power = Operations.flip(dragging);
                }
                Equation newEq = new PowerEquation(owner);
                Equation oldEq = this;
                oldEq.replace(newEq);
                newEq.add(this);
                newEq.add(power);
                //dragging.getAndUpdateDemo(power);
                //dragging.updateOps(dragging.demo);
            } else if (this instanceof NumConstEquation && ((NumConstEquation) this).getValue().doubleValue() == BigDecimal.ZERO.doubleValue() && op == Op.ADD) {
                dragging.remove();
                dragging = update(dragging, notSameSide, thisNeg, dragNeg, op);
                this.replace(dragging);
                return dragging;
            } else if (this instanceof NumConstEquation && ((NumConstEquation) this).getValue().doubleValue() == BigDecimal.ONE.doubleValue() && op == Op.MULTI) {
                dragging.remove();
                dragging = update(dragging, notSameSide, thisNeg, dragNeg, op);
                this.replace(dragging);
                // bring back the minus signs on demo
                for (MinusEquation me : minusSigns) {
                    me.clear();
                    dragging.replace(me);
                    me.add(dragging);
                }
                return dragging;
            } else if ((parent instanceof AddEquation && op == Op.ADD) ||
                    (parent instanceof MultiEquation && op == Op.MULTI)) {
                if (parent.equals(dragging.parent)) {
                    dragging.justRemove();
                } else {
                    if (op == Op.MULTI && dragging.parent instanceof EqualsEquation) {
                        dragging.replace(new NumConstEquation(BigDecimal.ONE, owner));
                    } else {
                        dragging.remove();
                    }
                }
                int myIndex = parent.indexOf(this);
                Log.i("", "added to existing");
                if (!right) {
                    dragging = update(dragging, notSameSide, thisNeg, dragNeg, op);
                    parent.add(myIndex + 1, dragging);
                } else {
                    dragging = update(dragging, notSameSide, thisNeg, dragNeg, op);
                    parent.add(myIndex, dragging);
                }
            } else {
                if ((op == Op.DIV || op == Op.MULTI) && dragging.parent instanceof EqualsEquation) {
                    dragging.replace(new NumConstEquation(BigDecimal.ONE, owner));
                } else {
                    dragging.remove();
                }
                Log.i("added to new", "" + this.toString());
                Equation oldEq = this;
                Equation newEq = null;
                if (op == Op.ADD) {
                    newEq = new AddEquation(owner);
                } else if (op == Op.DIV) {
                    newEq = new DivEquation(owner);
                } else if (op == Op.MULTI) {
                    newEq = new MultiEquation(owner);
                }

                oldEq.replace(newEq);
                if (op != Op.DIV) {
                    if (right) {
                        dragging = update(dragging, notSameSide, thisNeg, dragNeg, op);
                        newEq.add(dragging);
                        newEq.add(oldEq);
                    } else {
                        newEq.add(oldEq);
                        dragging = update(dragging, notSameSide, thisNeg, dragNeg, op);
                        newEq.add(dragging);
                    }
                } else {
                    newEq.add(oldEq);
                    dragging = update(dragging, notSameSide, thisNeg, dragNeg, op);
                    newEq.add(dragging);
                }
            }
            // bring back the minus signs
            Equation at = this;
            for (MinusEquation me : minusSigns) {
                me.clear();
                at.replace(me);
                me.add(at);
            }
        }
        return dragging;
    }

    private Equation update(Equation dragging, boolean notSameSide, boolean thisNeg, boolean dragNeg, Op op) {
        Equation toInsert;
        if (dragNeg && !thisNeg && !(dragging instanceof PlusMinusEquation)) {
            toInsert = dragging.negate();
        } else {
            toInsert = dragging;
        }
        Equation toInsert2;
        if (notSameSide) {
            toInsert2 = toInsert.negate();
        } else {
            toInsert2 = toInsert;
        }

        return toInsert2;
    }

    protected Equation negate() {
        Equation result = new MinusEquation(owner);
        result.add(this.copy());
        return result;
    }

    protected Equation plusMinus() {
        Equation result = new PlusMinusEquation(owner);
        result.add(this.copy());
        return result;
    }

    private boolean canPower(Equation dragging) {
        if (dragging.parent instanceof PowerEquation && dragging.parent.parent instanceof EqualsEquation) {
            if (this.parent instanceof EqualsEquation && dragging.side() != this.side()) {
                return true;
            }
        }
        return false;
    }

    public void justRemove() {
        parent.justRemove(this);
    }

    public void justRemove(Equation equation) {
        super.remove(equation);
    }

    private boolean CanAdd(Equation dragging) {
        Equation lcc = lowestCommonContainer(dragging);

        // if these are in the same add block
        if (lcc.addContain(this) && lcc.addContain(dragging)) {//lcc instanceof AddEquation &&
            return true;
        }
        // if they are only both only adds away form equals
        if (lcc instanceof EqualsEquation) {
            EqualsEquation ee = (EqualsEquation) lcc;
            if (ee.addContain(dragging) && ee.addContain(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean canMuli(Equation dragging) {
        return canMultiDiv(dragging, true);
    }

    public boolean canDiv(Equation dragging) {
        if (!canMultiDiv(dragging, false)) {
            return false;
        }
        //we don't allow a/b -> (a/1)/b
        if (this instanceof DivEquation && dragging.equals(this.get(1))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String internals = "";
        for (int i = 0; i < size(); i++) {
            internals += get(i).toString();
            if (i != size() - 1) {
                internals += ",";
            }
        }
        if (internals != "") {
            internals = "(" + internals + ")";
        }
        return display + internals;
    }

    private boolean canMultiDiv(Equation dragging, boolean multi) {
        boolean result = false;
        Equation lcc = lowestCommonContainer(dragging);
        // if these are in the same multi block
        if (lcc instanceof MultiDivSuperEquation && lcc.DivMultiContain(this) && lcc.DivMultiContain(dragging)) {
            MultiDivSuperEquation lccmdse = (MultiDivSuperEquation) lcc;
            result = ((lccmdse.onTop(this) == lccmdse.onTop(dragging)) == multi);
            if (!result) {
                Log.i("", "same sides, tops are wrong. multi: " + multi);
            } else {
                Log.i("", "pass. multi: " + multi);
            }
            return result;
        }
        // if they are only div/multi away form the equals
        if (lcc instanceof EqualsEquation) {
            EqualsEquation ee = (EqualsEquation) lcc;
            if (ee.DivMultiContain(dragging) && ee.DivMultiContain(this)) {

                // 1*2=_ the _ on top and multiDivContained by the equals
                boolean myTop = true;
                Equation at = ee.get(ee.side(this));
                // peel of Minus Equations in search of a MultiDiv
                while (at instanceof MonaryEquation) {
                    at = at.get(0);
                }
                if (this.deepContains(at)){
                    myTop = true;
                }else if (at instanceof MultiDivSuperEquation) {
                    myTop = ((MultiDivSuperEquation) at).onTop(this);
                }
                boolean eqTop = true;
                at = ee.get(ee.side(dragging));
                // peel of Minus Equations in search of a MultiDiv
                while (at instanceof MonaryEquation) {
                    at = at.get(0);
                }
                if (at instanceof MultiDivSuperEquation) {
                    eqTop = ((MultiDivSuperEquation) at).onTop(dragging);
                }
                result = ((myTop != eqTop) == multi);
                if (!result) {
                    Log.i("", "opisite sides, tops are wrong. multi: " + multi);
                } else {
                    Log.i("", "pass. multi: " + multi);
                }
                return result;
            }
        }
        Log.i("", "not div multi contained. multi: " + multi);
        return false;
    }

    public void updateLocation() {
        draw(null, x, y);
    }


    public void getDragLocations(Equation dragging, DragLocations dragLocations, ArrayList<Op> ops) {
        if (this.parent != null && !dragging.deepContains(this)) {
            if (ops.contains(Op.MULTI) && canMuli(dragging) && !(this instanceof MultiEquation)) {
                dragLocations.add(new DragLocation(Op.MULTI, dragging, this, true));
                dragLocations.add(new DragLocation(Op.MULTI, dragging, this, false));
            }
            if (ops.contains(Op.ADD) && CanAdd(dragging) && !(this instanceof AddEquation)) {
                dragLocations.add(new DragLocation(Op.ADD, dragging, this, true));
                dragLocations.add(new DragLocation(Op.ADD, dragging, this, false));
            }
            if (ops.contains(Op.DIV) && canDiv(dragging)) {
                DragLocation newset = new DragLocation(Op.DIV, dragging, this, false);
                dragLocations.add(newset);
            }
            if (ops.contains(Op.POWER) && canPower(dragging)) {
                DragLocation newset = new DragLocation(Op.POWER, dragging, this, false);
                dragLocations.add(newset);
            }

        }
        if (dragging.equals(this)) {
            // left and op
            DragLocation newset = new DragLocation(this);
            dragLocations.add(newset);
        } else {
            for (Equation e : this) {
                e.getDragLocations(dragging, dragLocations, ops);
            }
        }
    }

    public ArrayList<Equation> getLeafs() {
        ArrayList<Equation> result = new ArrayList<Equation>();
        if (size() == 0) {//|| size()==1
            result.add(this);
        } else {
            if (size() == 1) {
                result.add(this);
            }
            for (Equation e : this) {
                result.addAll(e.getLeafs());
            }
        }
        return result;
    }

    public void deDemo() {
        this.demo = false;
        for (Equation e : this) {
            e.deDemo();
        }
    }

    public boolean nearAny(float x, float y) {
        for (MyPoint p : lastPoint) {
            if (p.near(x, y)) {
                return true;
            }
        }
        for (Equation e : this) {
            if (e.nearAny(x, y)) {
                return true;
            }
        }
        return false;
    }

    public void setAlpha(int currentAlpha) {
        getPaint().setAlpha(currentAlpha);
        for (Equation e : this) {
            e.setAlpha(currentAlpha);
        }
    }

    public void setColor(int currentColor) {
        getPaint().setColor(currentColor);
        for (Equation e : this) {
            e.setColor(currentColor);
        }
    }

    public boolean isNeg() {
        boolean result = false;
        Equation at = this;
        while (at instanceof MinusEquation || at instanceof PlusMinusEquation) {
            if (at instanceof MinusEquation) {
                result = !result;
            }
            at = this.get(0);
        }

        return result;
    }

    public boolean isPlusMinus() {
        Equation at = this;
        while (at instanceof MinusEquation || at instanceof PlusMinusEquation) {
            if (at instanceof PlusMinusEquation) {
                return true;
            }
            at = this.get(0);
        }

        return false;
    }


    private class Clostest {
        public float dis;
        public HashSet<Equation> eqs;

        public Clostest(float dis, HashSet<Equation> eqs) {
            this.dis = dis;
            this.eqs = eqs;
        }
    }

    public HashSet<Equation> closetOn(float x, float y) {
        return closetOn(x, y, new Clostest(9999999, new HashSet<Equation>())).eqs;
    }

    private Clostest closetOn(float x, float y, Clostest clostest) {
        for (int i = 0; i < lastPoint.size(); i++) {
            if (lastPoint.get(i).on(x, y)) {
                float myDis = (float) Math.sqrt(Math.pow(x - lastPoint.get(i).x, 2) + Math.pow(y - lastPoint.get(i).y, 2));
                if (myDis < clostest.dis) {
                    clostest = new Clostest(myDis, getEquationsFormLastPointForSelect(i));
                }
            }
        }
        for (Equation eq : this) {
            clostest = eq.closetOn(x, y, clostest);
        }
        return clostest;
    }

    /**
     * looking form the left
     *
     * @return
     */
    public boolean isOpLeft() {
        if (parent instanceof BinaryEquation && parent.indexOf(this) == 0) {
            return true;
        }
        String dis = getDisplay(-1);
        char[] timesUnicode = {'\u00D7'};
        if (dis.equals("+") || dis.equals("-") || dis.equals(new String(timesUnicode)) || dis.equals("=")) {
            return true;
        }
        return false;
    }

    /**
     * looking form the left
     *
     * @return
     */
    public boolean isOpRight() {
        if (parent instanceof BinaryEquation && parent.indexOf(this) == 1) {
            return true;
        }
        String dis = getDisplay(-1);
        char[] timesUnicode = {'\u00D7'};
        if (dis.equals("+") || dis.equals("-") || dis.equals(new String(timesUnicode)) || dis.equals("=")) {
            return true;
        }
        return false;
    }

    //private static final float PARN_WIDTH_ADDITION = 24;
    protected float getParnWidthAddition() {
        if (owner instanceof EmilyView) {
            return 48 * Algebrator.getAlgebrator().getDpi();
        } else {
            return 28 * Algebrator.getAlgebrator().getDpi();
        }
    }

    // null -> false
    public Equation CouldBeZero() {
        if (botCouldBeZeroHelper(this)){
            return this;
        }
        return null;
    }

    private boolean botCouldBeZeroHelper(Equation eq){
        if (eq instanceof VarEquation && (!(this instanceof DivEquation) || !((DivEquation)this).onTop(eq))){
            return true;
        }
        for (Equation e:eq){
            boolean pass = botCouldBeZeroHelper(e);
            if ( pass ){
                return true;
            }
        }
        return false;
    }


    public float getX(){
        return x;
    }

    // believe it or not this is not the same as Y, it is centered
    public float getY(){
        return y - measureHeightUpper() + (measureHeight()/2);
    }

    public enum Op {ADD, DIV, POWER, MULTI}
}
