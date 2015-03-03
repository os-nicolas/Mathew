package colin.example.algebrator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.EquationDis;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.Actions.DecimalAction;
import colin.example.algebrator.Actions.DeleteAction;
import colin.example.algebrator.Actions.DivAction;
import colin.example.algebrator.Actions.EqualsAction;
import colin.example.algebrator.Actions.LeftAction;
import colin.example.algebrator.Actions.MinusAction;
import colin.example.algebrator.Actions.NumberAction;
import colin.example.algebrator.Actions.ParenthesesAction;
import colin.example.algebrator.Actions.PlusAction;
import colin.example.algebrator.Actions.PowerAction;
import colin.example.algebrator.Actions.RightAction;
import colin.example.algebrator.Actions.Solve;
import colin.example.algebrator.Actions.SqrtAction;
import colin.example.algebrator.Actions.TimesAction;
import colin.example.algebrator.Actions.VarAction;

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
        stupid = new WritingEquation(this);
        Equation empty = new PlaceholderEquation(this);
        //stupid.add(yo);
        stupid.add(empty);
        empty.setSelected(true);

        addButtons();

        buttonsPercent = 4f / 6f;
    }

    private void addButtons() {

        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button("7", new NumberAction(this, "7")));
        firstRow.add(new Button("8", new NumberAction(this, "8")));
        firstRow.add(new Button("9", new NumberAction(this, "9")));
        firstRow.add(new Button("A", new VarAction(this, "A")));
        firstRow.add(new Button("B", new VarAction(this, "B")));
        firstRow.add(new Button("(", new ParenthesesAction(this, true)));
        firstRow.add(new Button(")", new ParenthesesAction(this, false)));
        firstRow.add(new Button("=", new EqualsAction(this)));
        //TODO this does not work since my font does not support this
        //char[] backSpaceUnicode = { '\u232B'};
        firstRow.add(new Button("DEL", new DeleteAction(this)));//

        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button("4", new NumberAction(this, "4")));
        secondRow.add(new Button("5", new NumberAction(this, "5")));
        secondRow.add(new Button("6", new NumberAction(this, "6")));
        secondRow.add(new Button(".", new DecimalAction(this, ".")));
        secondRow.add(new Button("+", new PlusAction(this)));
        char[] timesUnicode = {'\u00D7'};
        secondRow.add(new Button(new String(timesUnicode), new TimesAction(this)));
        secondRow.add(new Button("^", new PowerAction(this)));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button("1", new NumberAction(this, "1")));
        thridRow.add(new Button("2", new NumberAction(this, "2")));
        thridRow.add(new Button("3", new NumberAction(this, "3")));
        thridRow.add(new Button("0", new NumberAction(this, "0")));
        thridRow.add(new Button("-", new MinusAction(this)));
        char[] divisionUnicode = {'\u00F7'};
        thridRow.add(new Button(new String(divisionUnicode), new DivAction(this)));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button(new String(sqrtUnicode), new SqrtAction(this)));
        char[] leftUnicode = {'\u2190'};
        thridRow.add(new Button(new String(leftUnicode), new LeftAction(this)));
        char[] rightUnicode = {'\u2192'};
        thridRow.add(new Button(new String(rightUnicode), new RightAction(this)));

        addButtonsRow(firstRow, 6f / 9f, 7f / 9f);
        addButtonsRow(secondRow, 0f, 7f / 9f, 7f / 9f, 8f / 9f);
        Button solve = new Button("SOLVE", new Solve(this));
        solve.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 8f / 9f, 9f / 9f);

    }

    private void addButtonsRow(ArrayList<Button> row, float top, float bottum) {
        addButtonsRow(row, 0, 1, top, bottum);

    }

    private void addButtonsRow(ArrayList<Button> row, float left, float right, float top, float bottum) {
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawShadow(canvas);

        onDrawAfter(canvas);
    }


    private void drawShadow(Canvas canvas) {
        Paint p = new Paint();
        int color = Algebrator.getAlgebrator().darkDarkColor;
        p.setColor(color);
        p.setAlpha(0xff);
        int at = ((int) buttonLine());
//        for (int i=0;i<2f/Algebrator.getAlgebrator().getDpi();i++){
//            canvas.drawLine(0,at,width,at,p);
//            at--;
//        }
        p.setAlpha(0x8f);
        while (p.getAlpha() > 1) {
            canvas.drawLine(0, at, width, at, p);
            p.setAlpha((int) (p.getAlpha() / Algebrator.getAlgebrator().getShadowFade()));
            at--;
        }
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
        removeSelected();

        Equation lcp = null;
        // if it's an action up
        // and it was near the left of stupid
        ArrayList<EquationDis> closest = stupid.closest(
                event.getX(), event.getY());

        lcp = closest.get(0).equation;

        // TODO 100 to var scale by dpi
        float minDis = 100 * Algebrator.getAlgebrator().getDpi();
        if (Math.abs(event.getY() - lcp.y) < minDis) {
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
