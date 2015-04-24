package cube.d.n.commoncore.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.BaseView;

/**
 * Created by Colin on 1/7/2015.
 */
public class WritingPraEquation extends WritingLeafEquation {
    public boolean left;


    public WritingPraEquation(boolean left, BaseView emilyView) {
        super((left ? "(" : ")"), emilyView);
        init(left);
    }

    private void init(boolean left) {
        this.left = left;
    }

    public WritingPraEquation(boolean left, BaseView owner, WritingPraEquation equations) {
        super((left ? "(" : ")"), owner, equations);
        init(left);
    }

    @Override
    public Equation copy() {
        Equation result = new WritingPraEquation(this.left, this.owner, this);
        return result;
    }

    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        lastPoint = new ArrayList<MyPoint>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        MyPoint point = new MyPoint(getMyWidth(), getMyHeight());
        point.x = (int) x;
        point.y = (int) y;
        lastPoint.add(point);
        drawParentheses(canvas, x, y, temp, left);
    }

    @Override
    public boolean illegal() {
        Equation match = getMatch();
        // if it does not have a match
        if (match == null)
            return true;
        // if the match is right next to it
        if (match.equals(left()) || match.equals(right()))
            return true;
        // otherwise we are ok
        return false;
    }


    protected void drawParentheses(Canvas canvas, float x, float y, Paint temp, boolean left) {
        if (canvas != null) {
            Paint ptemp = new Paint(temp);
            ptemp.setStrokeWidth(BaseApp.getAlgebrator().getStrokeWidth(this));
            float uh = measureHeightUpper() + (this instanceof WritingSqrtEquation ? -BaseApp.getAlgebrator().getSqrtHeightAdd(this) : 0) + (getMatch() instanceof WritingSqrtEquation ? -BaseApp.getAlgebrator().getSqrtHeightAdd(this) : 0);
            float lh = measureHeightLower();


            drawParentheses(left,canvas, x, y, ptemp, uh, lh,this);
        }
    }



    @Override
    protected float privateMeasureHeightUpper() {
        // unfortunately, we always need to remeasure these
        // since it can resize when it siblings resize
        // it should not be a problem since it only on the write screen
        this.needsUpdate();
        return measureHeightHelper(true);
    }

    @Override
    protected float privateMeasureHeightLower() {
        // unfortunately, we always need to remeasure these
        // since it can resize when it siblings resize
        // it should not be a problem since it only on the write screen
        this.needsUpdate();
        return measureHeightHelper(false);
    }

    public float measureHeightHelper(boolean upper) {
        float totalHeight = getMyHeight() / 2;

        // if left
        // move left through parent
        // match our height to the tallest thing left of us that is:
        // not a open
        boolean go = true;
        int depth = 1;
        Equation current = this;
        if (left) {
            current = current.right();
            while (go && depth != 0) {
                if (current != null && this.parent.deepContains(current)) {
                    if (current instanceof WritingPraEquation ) {
                        if (((WritingPraEquation) current).left) {
                            if (depth == 1) {
                                totalHeight = Math.max(totalHeight, (upper ? current.measureHeightUpper() : current.measureHeightLower()) + PARN_HEIGHT_ADDITION() / 2);
                            }
                            depth++;
                        } else {
                            depth--;
                            if (depth == 0) {
                                go = false;
                            }
                        }
                    } else {
                        if (depth == 1) {
                            totalHeight = Math.max(totalHeight, (upper ? current.measureHeightUpper() : current.measureHeightLower()) + PARN_HEIGHT_ADDITION() / 2);
                        }
                    }
                    current = current.right();
                } else {
                    go = false;
                }
                if (current != null) {
                    go = go && keepGoing(current);
                }
            }
        } else {
            current = current.left();
            while (go && depth != 0) {
                if (current != null && this.parent.deepContains(current)) {
                    if (current instanceof WritingPraEquation) {
                        if (!((WritingPraEquation) current).left) {
                            depth++;
                        } else {
                            depth--;
                            if (depth == 0) {
                                totalHeight = (upper ? current.measureHeightUpper() : current.measureHeightLower());
                                go = false;
                            }
                        }
                    }
                    current = current.left();
                } else {
                    go = false;
                }

                if (current != null) {
                    go = go && keepGoing(current);
                }
            }
        }
        return totalHeight;
    }

    protected boolean keepGoing(Equation current) {
        if (current instanceof WritingLeafEquation && current.getDisplay(-1).equals("=")) {
            return false;
        }
        if (this.parent != null && !this.parent.deepContains(current)) {
            return false;
        }
        if (this.parent != null && this.parent instanceof BinaryEquation) {
            return false;
        }
        return true;
    }

    public Equation selectBlock() {
        // we need to deselect whatever is selected
        owner.removeSelected();

        Equation toSelect = popBlock();

        // and select the new equation
        toSelect.setSelected(true);

        return toSelect;
    }

    public Equation getMatch() {
        int depth = 1;
        Equation current = this;
        if (left) {
            current = current.right();
            while (depth != 0 && current != null && this.parent.deepContains(current)) {
                if (current instanceof WritingPraEquation) {
                    if (((WritingPraEquation) current).left) {
                        depth++;
                    } else {
                        depth--;
                        if (depth == 0) {
                            return current;
                        }
                    }
                }
                if (current != null && !keepGoing(current)) {
                    return null;
                }
                current = current.right();
            }
        } else {
            current = current.left();
            while (depth != 0 && current != null && this.parent.deepContains(current)) {
                if (current instanceof WritingPraEquation) {
                    if (!((WritingPraEquation) current).left) {
                        depth++;
                    } else {
                        depth--;
                        if (depth == 0) {
                            return current;
                        }
                    }
                }
                if (current != null && !keepGoing(current)) {
                    return null;
                }

                current = current.left();
            }
        }
        return null;
    }

    public Equation popBlock() {

        // we need to find it's other end
        Equation eq = getMatch();
        int min = Math.min(this.parent.indexOf(this), this.parent.indexOf(eq));
        int max = Math.max(this.parent.indexOf(this), this.parent.indexOf(eq));
        ArrayList<Equation> list = new ArrayList<Equation>();
        for (int i = min; i < max + 1; i++) {
            list.add(this.parent.get(i));
        }
        Equation toSelect = null;
        if (this.parent instanceof MultiEquation) {
            toSelect = new MultiEquation(owner);
        } else if (this.parent instanceof AddEquation) {
            toSelect = new AddEquation(owner);
        } else if (this.parent instanceof WritingEquation) {
            toSelect = new WritingEquation(owner);
        }
        if (toSelect == null) {
            Log.e("", "ToSelect should not be null");
        }

        Equation oldEq = this.parent;

        for (Equation e : list) {
            oldEq.justRemove(e);
            toSelect.add(e);
        }

        // add it back
        oldEq.add(min, toSelect);

        return toSelect;

    }
}
