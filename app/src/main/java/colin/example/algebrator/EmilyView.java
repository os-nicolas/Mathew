package colin.example.algebrator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.EquationDis;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.Actions.WriteScreen.ClearAction;
import colin.example.algebrator.Actions.WriteScreen.DecimalAction;
import colin.example.algebrator.Actions.WriteScreen.DeleteAction;
import colin.example.algebrator.Actions.WriteScreen.DivAction;
import colin.example.algebrator.Actions.WriteScreen.EqualsAction;
import colin.example.algebrator.Actions.WriteScreen.LeftAction;
import colin.example.algebrator.Actions.WriteScreen.MinusAction;
import colin.example.algebrator.Actions.WriteScreen.NumberAction;
import colin.example.algebrator.Actions.WriteScreen.ParenthesesAction;
import colin.example.algebrator.Actions.WriteScreen.PlusAction;
import colin.example.algebrator.Actions.WriteScreen.PowerAction;
import colin.example.algebrator.Actions.WriteScreen.RightAction;
import colin.example.algebrator.Actions.WriteScreen.Solve;
import colin.example.algebrator.Actions.WriteScreen.SqrtAction;
import colin.example.algebrator.Actions.WriteScreen.TimesAction;
import colin.example.algebrator.Actions.WriteScreen.VarAction;
import colin.example.algebrator.tuts.TutMessage;

public class EmilyView extends SuperView {

    public EmilyView(Context context) {
        super(context);
        init(context);
    }

    public EmilyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmilyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
        //Equation yo = new PowerEquation(this);
        //yo.add(new NumConstEquation(2.0,this));
        //yo.add(new NumConstEquation(2.0,this));

        addButtons();
        BASE_BUTTON_PERCENT = 4f / 6f;
        buttonsPercent = BASE_BUTTON_PERCENT;
    }

    @Override
    public void resume() {
        final EmilyView ev =this;
        if (stupid == null) {
            initEq();
        }
    }

    public void initEq() {
        // umm i totally don't need this
        if (selected != null){
            selected.setSelected(false);
        }

        stupid = new WritingEquation(this);
        Equation empty = new PlaceholderEquation(this);
        //stupid.add(yo);
        stupid.add(empty);
        empty.setSelected(true);
    }

    private void addButtons() {

        popUpButtons.add(new PopUpButton(this,getResources().getString(R.string.clear)  ,new ClearAction(this)));

        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button(this,"7", new NumberAction(this, "7")));
        firstRow.add(new Button(this,"8", new NumberAction(this, "8")));
        firstRow.add(new Button(this,"9", new NumberAction(this, "9")));
        firstRow.add(new Button(this,"a", new VarAction(this, "a")));
        firstRow.add(new Button(this,"b", new VarAction(this, "b")));
        firstRow.add(new Button(this,"+", new PlusAction(this)));
        firstRow.add(new Button(this,"-", new MinusAction(this)));
        firstRow.add(new Button(this,"=", new EqualsAction(this)));
        //TODO this does not work since my font does not support this
        char[] backSpaceUnicode = { '\u232B'};
        Button del = new Button(this,new String(backSpaceUnicode), new DeleteAction(this));
        Typeface myTypeface = Typeface.createFromAsset(Algebrator.getAlgebrator().getAssets(), "fonts/DejaVuSans.ttf");
        del.textPaint.setTypeface(myTypeface);
        firstRow.add(del);//

        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button(this,"4", new NumberAction(this, "4")));
        secondRow.add(new Button(this,"5", new NumberAction(this, "5")));
        secondRow.add(new Button(this,"6", new NumberAction(this, "6")));
        secondRow.add(new Button(this,"(", new ParenthesesAction(this, true)));
        secondRow.add(new Button(this,")", new ParenthesesAction(this, false)));
        char[] timesUnicode = {'\u00D7'};
        secondRow.add(new Button(this,new String(timesUnicode), new TimesAction(this)));
        char[] divisionUnicode = {'\u00F7'};
        secondRow.add(new Button(this,new String(divisionUnicode), new DivAction(this)));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button(this,"1", new NumberAction(this, "1")));
        thridRow.add(new Button(this,"2", new NumberAction(this, "2")));
        thridRow.add(new Button(this,"3", new NumberAction(this, "3")));
        thridRow.add(new Button(this,"0", new NumberAction(this, "0")));
        thridRow.add(new Button(this,".", new DecimalAction(this, ".")));
        thridRow.add(new Button(this,"cⁿ", new PowerAction(this)));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button(this,new String(sqrtUnicode), new SqrtAction(this)));
        char[] leftUnicode = {'\u2190'};
        thridRow.add(new Button(this,new String(leftUnicode), new LeftAction(this)));
        char[] rightUnicode = {'\u2192'};
        thridRow.add(new Button(this,new String(rightUnicode), new RightAction(this)));

        addButtonsRow(firstRow, 6f / 9f, 7f / 9f);
        addButtonsRow(secondRow, 0f, 7f / 9f, 7f / 9f, 8f / 9f);
        Button solve = new Button(this,getResources().getString(R.string.solve), new Solve(this));
        solve.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 8f / 9f, 9f / 9f);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        onDrawAfter(canvas);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return super.onTouch(view, event);
    }

    @Override
    protected void selectMoved(MotionEvent event) {
        resolveSelected(event);
    }

    @Override
    protected void resolveSelected(MotionEvent event) {
        // now we need to figure out what we are selecting
        // find the least commond parent

        int currentBkgAlpha = 0x00;
        if (selected != null){
            currentBkgAlpha=selected.bkgAlpha;
        }

        removeSelected();

        Equation lcp = null;
        // if it's an action up
        // and it was near the left of stupid
        ArrayList<EquationDis> closest = stupid.closest(
                event.getX(), event.getY());

        lcp = closest.get(0).equation;

        // TODO 100 to var scale by dpi
        //float minDis = 100 * Algebrator.getAlgebrator().getDpi();
        //if (Math.abs(event.getY() - lcp.y) < minDis) {
            if (lcp instanceof PlaceholderEquation) {
                lcp.setSelected(true);
            } else {
                // the the lcp is the left or right end of something we might want to select it's parent
                Equation current = lcp;

                boolean left = event.getX() < lcp.x;
                int depth = 0;
                // find how many layor deep we are
                while (current.parent != null && current.parent.indexOf(current) == (left ? 0 : current.parent.size() - 1)) {
                    // we don't count binary eq because they are fixed size
                    //if (!(current.parent instanceof BinaryEquation)) {
                    depth++;
                    //}
                    current = current.parent;
                }
                // we really should figure how much space we have to work with so we can divide it up


                if (depth != 0) {
                    float distance = 100 * Algebrator.getAlgebrator().getDpi() + (lcp.measureWidth() / 2f);

                    // this means use left or right
                    Equation next = (left ? lcp.left() : lcp.right());
                    if (next != null) {
                        distance = Math.abs(lcp.x - next.x) / 2;
                    }

                    float each = distance / ((float) (depth + 1));
                    int num = (int) Math.floor(Math.min(Math.abs(lcp.x - event.getX()), distance) / each);

                    if (num == depth + 1) {
                        num = depth;
                    }

                    current = lcp;
                    while (num != 0) {
                        //if (!(current.parent instanceof BinaryEquation)) {
                        num--;
                        //}
                        current = current.parent;
                    }
                    lcp = current;
                }


                // insert a Placeholder to the left of everything
                Equation toSelect = new PlaceholderEquation(this);
                toSelect.bkgAlpha = currentBkgAlpha;
                toSelect.x = event.getX();
                toSelect.y = event.getY();
                // add toSelect left of lcp
                if (lcp instanceof WritingEquation) {
                    lcp.add((left ? 0 : lcp.size()), toSelect);
                    toSelect.setSelected(true);
                } else if (lcp.parent instanceof WritingEquation) {
                    int at = lcp.parent.indexOf(lcp);
                    lcp.parent.add(at + (left ? 0 : 1), toSelect);
                    toSelect.setSelected(true);
                } else {
                    Equation oldEq = lcp;
                    Equation holder = new WritingEquation(this);
                    oldEq.replace(holder);
                    if (left) {
                        holder.add(toSelect);
                        holder.add(oldEq);
                    } else {
                        holder.add(oldEq);
                        holder.add(toSelect);
                    }
                    toSelect.setSelected(true);
                }

            }
        //}
        if (selected != null) {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getPointerCount() != 1) {
                ((PlaceholderEquation) selected).drawBkg = false;
            } else {
                ((PlaceholderEquation) selected).drawBkg = true;
                ((PlaceholderEquation) selected).goDark();
            }
        }

        return;
    }

    // returns the equation left of the selected
    public Equation left() {
        return selected.left();
    }

    public void insert(Equation newEq) {
        selected.parent.add(selected.parent.indexOf(selected), newEq);
    }

    public void insertAt(Equation addTo, int at, Equation newEq) {

        // we just move selected and than call insert
        if (selected instanceof PlaceholderEquation) {
            Equation placeHolder = selected;
            selected.remove();
            addTo.add(at, placeHolder);
            insert(newEq);
        }
    }

    @Override
    protected float outTop() {
        if (stupid.y + stupid.measureHeightLower() - buffer < 0 && stupid.y + stupid.measureHeightLower() + buffer < buttonLine()) {
            return -(stupid.y + stupid.measureHeightLower() - buffer);
        }
        return super.outTop();
    }

    @Override
    protected float outLeft() {
        if (stupid.x + stupid.measureWidth() / 2 - buffer < 0 && stupid.x + stupid.measureWidth() / 2 + buffer < width) {
            return -(stupid.x + stupid.measureWidth() / 2 - buffer);
        }
        return super.outLeft();
    }

    @Override
    protected float outBottom() {
        if (stupid.y - stupid.measureHeightUpper() + buffer > buttonLine() && stupid.y - stupid.measureHeightUpper() - buffer > 0) {
            return (stupid.y - stupid.measureHeightUpper() + buffer) - buttonLine();
        }
        return super.outBottom();
    }

    @Override
    protected float outRight() {
        if (stupid.x - stupid.measureWidth() / 2 + buffer > width && stupid.x - stupid.measureWidth() / 2 - buffer > 0) {
            return (stupid.x - stupid.measureWidth() / 2 + buffer) - width;
        }
        return super.outRight();
    }

}
