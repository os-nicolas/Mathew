package colin.algebrator.eq;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.SuperView;

public abstract class LeafEquation extends Equation {

    public LeafEquation(SuperView owner, LeafEquation leafEqu) {
        super(owner,leafEqu);
        init();
    }

    @Override
    public void integrityCheck(){
        super.integrityCheck();
        if (size() != 0){
            Log.e("ic","this should be size 0");
        }
    }

	public LeafEquation(SuperView owner) {
		super(owner);
        init();
	}

    private void init() {
    }

    @Override
	public String getDisplay(int pos){
	    return display;
	}

    @Override
    protected HashSet<Equation> getEquationsFormLastPoint(int i) {
        HashSet<Equation> result = new HashSet<Equation>();
        result.add(this);
        return result;
    }

    @Override
    protected HashSet<Equation> getEquationsFormLastPointForSelect(int i) {
        HashSet<Equation> result = new HashSet<Equation>();
        result.add(this);
        return result;
    }


	@Override
	public ArrayList<EquationDis> closest(DragEquation dragging){
		ArrayList<EquationDis> result = new  ArrayList<EquationDis>();
		result.add(new EquationDis(this,dragging, EquationDis.Side.left));
        result.add(new EquationDis(this,dragging,EquationDis.Side.right));
        result.add(new EquationDis(this,dragging,EquationDis.Side.top));
        result.add(new EquationDis(this,dragging,EquationDis.Side.bottom));
		return result;
	}

    @Override
    public ArrayList<EquationDis> closest(float x, float y){
        ArrayList<EquationDis> result = new  ArrayList<EquationDis>();
        result.add(new EquationDis(this,x,y, EquationDis.Side.left));
        result.add(new EquationDis(this,x,y,EquationDis.Side.right));
        result.add(new EquationDis(this,x,y,EquationDis.Side.top));
        result.add(new EquationDis(this,x,y,EquationDis.Side.bottom));
        return result;
    }


    @Override
    protected float privateMeasureWidth() {
		// not tested
		float totalWidth= (float) (getMyWidth()) + getPaint().measureText(getDisplay(-1)) -getPaint().measureText("A");//Math.max(myWidth,textPaint.measureText(display)); //-textPaint.measureText(display.subSequence(0, 1)+"")
		
		if (parenthesis()){
			totalWidth += getParnWidthAddition();
		}
		return totalWidth;
	}

    @Override
    protected float privateMeasureHeight() {
//		Rect out =  new Rect();
//		textPaint.getTextBounds(display, 0, display.length(),out);
//		float totalHeight= out.height();
		
		float totalHeight= (float) (getMyHeight());
		
		if (parenthesis()){
			totalHeight += PARN_HEIGHT_ADDITION();
		}
		return totalHeight;
	}

	@Override
	public void privateDraw(Canvas canvas, float x, float y) {
		//drawBkgBox(canvas, x, y);

        if (canvas !=null ) {
            Paint temp = getPaint();
            if (parenthesis()){
                drawParentheses(canvas,x,y,temp);
            }
            Rect out =  new Rect();
            temp.getTextBounds("A", 0, "A".length(),out);
            float h= out.height();
            float w= out.width();
            canvas.drawText(getDisplay(-1), x, y + (h / 2), temp);
        }

        lastPoint =new ArrayList<MyPoint>();
		MyPoint point = new MyPoint(measureWidth(),measureHeight());
		point.x =(int) x;
		point.y = (int) y;
		lastPoint.add(point);
	}

	public void tryOperator(ArrayList< Equation> yos){}

}
