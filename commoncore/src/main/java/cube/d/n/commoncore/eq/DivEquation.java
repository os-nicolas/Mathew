package cube.d.n.commoncore.eq;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.BaseView;

public class DivEquation extends Operation implements MultiDivSuperEquation, BinaryEquation {

    @Override
    public void integrityCheck(){
        super.integrityCheck();
        if (size() != 2){
            Log.e("ic","this should be size 2");
        }
    }

    public DivEquation(BaseView owner,DivEquation divEq){
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

	public DivEquation(BaseView owner) {
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

		return maxWidth + BaseApp.getAlgebrator().getDivWidthAdd(this);
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
				MyPoint point = new MyPoint(measureWidth() - BaseApp.getAlgebrator().getDivWidthAdd(this),getMyHeight());
				point.x = (int) x;
				point.y = (int) (currentY + (getMyHeight()) / 2);
                // TODO scale by dpi
				temp.setStrokeWidth(BaseApp.getAlgebrator().getStrokeWidth(this));
				int halfwidth = (int) ((measureWidth() - (2 * BaseApp.getAlgebrator().getDivWidthAdd(this))) / 2);
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
	
	public void tryOperator(ArrayList<Equation> eqs) {

        Equation a = get(0);
        Equation b = get(1);
        //it should cancel any commonalties
        //if you have .../1 it should handle that
        //if youhave 0/... needs to handle that too
        // (6 + 5) / x -> 6/x + 5/x
        //

        Equation result =Operations.divide(a,b);

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
    protected Equation plusMinus() {
        Equation result = this.copy();
        result.set(0,result.get(0).plusMinus());
        return result;
    }

    private double lcm(double a, double b)
    {
        return a * (b / gcd(a, b));
    }


}
