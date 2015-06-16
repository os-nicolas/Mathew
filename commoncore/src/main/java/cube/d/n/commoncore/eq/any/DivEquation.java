package cube.d.n.commoncore.eq.any;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.eq.BinaryOperator;
import cube.d.n.commoncore.eq.MultiCountData;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.Operation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.Line;

public class DivEquation extends Operation implements MultiDivSuperEquation, BinaryEquation , BinaryOperator {

    @Override
    public void integrityCheck(){
        super.integrityCheck();
        if (size() != 2){
            Log.e("ic","this should be size 2");
        }
    }

    public DivEquation(Line owner,DivEquation divEq){
        super(owner,divEq);
        init();
        this.display = divEq.getDisplay(-1);

    }

	@Override
	public Equation copy() {
		return new DivEquation(owner,this);
	}

	@Override
	public boolean onTop(Equation eq) {
		if (equals(eq)) {
			return true;
		}
		if (eq.equals(get(0))) {
			return true;
		}
		if (eq.equals(get(1))) {
			return false;
		}
		if (get(0).deepContains(eq)) {
			if (get(0) instanceof MultiDivSuperEquation) {
				MultiDivSuperEquation next = (MultiDivSuperEquation) get(0);
				return next.onTop(eq);
			}else{
                return true;
            }
		}
		if (get(1).deepContains(eq)) {
			if (get(1) instanceof MultiDivSuperEquation) {
				MultiDivSuperEquation next = (MultiDivSuperEquation) get(1);
				return !next.onTop(eq);
			}else {
                return false;
            }
		}
		Log.e("123","onTop for something this does not contain ");
		return false;
	}

	public DivEquation(Line owner) {
		super(owner);
        init();
	}

    private void init() {
        display = "/";
    }

    @Override
    protected float privateMeasureWidth() {
		float maxWidth = getMyWidth();

		for (int i = 0; i < size(); i++) {
			if (get(i).measureWidth() > maxWidth) {
				maxWidth = get(i).measureWidth();
			}
		}
		if (parenthesis()) {
			maxWidth += getParnWidthAddition();
		}

		return maxWidth + BaseApp.getApp().getDivWidthAdd();
	}

	@Override
	public void privateDraw(Canvas canvas, float x, float y) {
		//drawBkgBox(canvas, x, y);
		lastPoint = new ArrayList<MyPoint>();
		float totalHieght = measureHeight();
		float currentY = -(totalHieght / 2) + y;
		Paint temp = getPaint();
		if (parenthesis()) {
                drawParentheses(canvas, x, y, temp);
			currentY += PARN_HEIGHT_ADDITION() / 2;
		}

		for (int i = 0; i < size(); i++) {
			get(i).draw(canvas, x, currentY + get(i).measureHeightUpper() );
			currentY += get(i).measureHeight();
			if (i != size() - 1) {
				MyPoint point = new MyPoint(measureWidth() - BaseApp.getApp().getDivWidthAdd(),getMyHeight());
				point.x = (int) x;
				point.y = (int) (currentY + (getMyHeight()) / 2);
                // TODO scale by dpi
				temp.setStrokeWidth(BaseApp.getApp().getStrokeWidth());
				int halfwidth = (int) ((measureWidth() - (2 * BaseApp.getApp().getDivWidthAdd())) / 2);
                if (canvas !=null ) {
                    canvas.drawLine(point.x - halfwidth, point.y, point.x
                            + halfwidth, point.y, temp);
                }
				lastPoint.add(point);
				currentY += getMyHeight();
			}
		}
	}

	@Override
	public float measureHeight() {
		float totalHeight = getMyHeight();

		for (int i = 0; i < size(); i++) {
			totalHeight += get(i).measureHeight();
		}
		if (parenthesis()) {
			totalHeight += PARN_HEIGHT_ADDITION();
		}
		return totalHeight;
	}

    @Override
    protected float privateMeasureHeightUpper(){
        return measureHeight()/2;
    }

    @Override
    protected float privateMeasureHeightLower(){
        return measureHeight()/2;
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
        if (pos == 0) {
            Equation result = get(0);
            this.get(0).replace(new NumConstEquation(BigDecimal.ONE, owner));
            return result;
        } else if (pos == 1) {
            this.replace(get(0));
            return get(1);
        }
        return null;
    }
	
	@Override
	public boolean same(Equation eq){
		if (!(eq instanceof DivEquation))
			return false;
		DivEquation e = (DivEquation)eq;
		return get(0).same(e.get(0)) && get(1).same(e.get(1));
	}

    public SelectedRow getSelectedRow() {
        final Equation a = get(0);
        final Equation b = get(1);


        ArrayList<SelectedRowButtons> buttons = new ArrayList<>();

        final Equation selected = this;
        if (Operations.divide_CanSamePower(a, b)) {
            buttons.add(new SelectedRowButtons("samePower",new Action(owner) {
                @Override
                protected void privateAct() {
                    selected.replace(Operations.divide_samePower(a, b, owner));
                    MyPoint p = new MyPoint(getX(),getY());
                    changed(p);
                }
            }));
        }

        if (Operations.divide_CanSplitUp(a)) {
            buttons.add(new SelectedRowButtons("split",new Action(owner) {
                @Override
                protected void privateAct() {
                    selected.replace(Operations.divide_SplitUp(a, b, owner));
                    MyPoint p = new MyPoint(getX(),getY());
                    changed(p);
                }
            }));
        }


        final MultiCountData top = new MultiCountData(a);
        final MultiCountData bot = new MultiCountData(b);

        final MultiCountData common = Operations.deepFindCommon(top, bot);

        if (Operations.divide_CanCancel(common) && !Operations.divide_CanSortaNumbers(top, bot)) {
            buttons.add(new SelectedRowButtons("cancel",new Action(owner) {
                @Override
                protected void privateAct() {
                    selected.replace(Operations.divide_Cancel(owner, top, bot, common));
                    MyPoint p = new MyPoint(getX(),getY());
                    changed(p);
                }
            }));
        }

        if (Operations.divide_CanSortaNumbers(top, bot) && Operations.divide_CanReduce(top, bot)) {
            buttons.add(new SelectedRowButtons("reduce",new Action(owner) {
                @Override
                protected void privateAct() {
                    selected.replace(Operations.divide_Reduce(owner, top, bot));
                    MyPoint p = new MyPoint(getX(),getY());
                    changed(p);
                }
            }));
        }

        if (Operations.divide_CanSortaNumbers(top, bot)) {
            buttons.add(new SelectedRowButtons("divide",new Action(owner) {
                @Override
                protected void privateAct() {
                    selected.replace(Operations.divide_Divide(owner, top, bot));
                    MyPoint p = new MyPoint(getX(),getY());
                    changed(p);
                }
            }));
        }

        if (buttons.size() != 0){
            SelectedRow sr = new SelectedRow(1f/9f);
            sr.addButtonsRow(buttons,0,1);
            return sr;
        }else{
            return null;
        }
    }
	
	public void tryOperator(ArrayList<Equation> eqs) {

        Equation a = get(0);
        Equation b = get(1);
        //it should cancel any commonalties
        //if you have .../1 it should handle that
        //if youhave 0/... needs to handle that too
        // (6 + 5) / x -> 6/x + 5/x
        //

        Equation result = Operations.divide(a, b,owner);

        replace(result);

	}

//    private MultiCountData common(MultiCountData top, MultiCountData bot) {
//        MultiCountData result = new MultiCountData();
//        for (Equation b:top.key){
//            for (Equation a:bot.key){
//                if (a.same(b)){
//                    result.key.add(a);
//                }
//            }
//        }
//        for (Equation b:top.numbers){
//            for (Equation a:bot.numbers){
//                if (a.same(b)){
//                    result.numbers.add(a);
//                }
//            }
//        }
//        return result;
//    }

    @Override
    public void tryOperator(int i) {

        if (i!= size()-1) {
            ArrayList<Equation> toOp = new ArrayList<Equation>();
            toOp.add(get(i));
            toOp.add(get(i + 1));
            tryOperator(toOp);
        }
    }

    private double gcd(double a, double b){
        while (b > 0)
        {
            double temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }


    @Override
    public Equation negate() {
        Equation result = this.copy();
        result.set(0,result.get(0).negate());
        return result;
    }

    @Override
    public Equation plusMinus() {
        Equation result = this.copy();
        result.set(0,result.get(0).plusMinus());
        return result;
    }

    private double lcm(double a, double b)
    {
        return a * (b / gcd(a, b));
    }


}
