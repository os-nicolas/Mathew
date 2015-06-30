package cube.d.n.commoncore.eq.any;

import android.graphics.Canvas;
import android.util.Log;


import java.math.BigDecimal;
import java.util.ArrayList;

import cube.d.n.commoncore.lines.EquationLine;

public class EqualsEquation extends Equation {


    @Override
    public void integrityCheck() {
        super.integrityCheck();
        if (size() != 2){
            Log.e("ic", "this should always be size 2");
        }
    }

    public EqualsEquation(EquationLine owner) {
		super(owner);
        init();
	}

    @Override
    public void tryOperator(int i) {
    }

    private void init() {
        display = "=";
    }

    public int side(Equation equation)  {
		if (get(0).deepContains(equation)){
			return 0;
		}
		if (get(1).deepContains(equation)){
			return 1;
		}
		// else error out
		return -1;
	}

    public EqualsEquation(EquationLine owner, EqualsEquation eqEq){
        super(owner,eqEq);
        init();
        this.display = eqEq.display;
    }

	@Override
	public Equation copy() {
		Equation result = new EqualsEquation(this.owner,this);
		return result;
	}


	public void tryOperator(ArrayList<Equation> eqs){}

    // this is badly named this is draw centered on the equals
    public void drawCentered(Canvas canvas, float x, float y) {
        // we need to figure out where the equals is
        float diffX = (float) (+this.measureWidth()/2 - get(0).measureWidth() - (getMyWidth() + myWidthAdd())/2f);
        super.draw(canvas, x+diffX, y);
    }

    @Override
	public Equation remove(int pos) {
		Equation result = get(pos);
		super.justRemove(get(pos));
		//TODO this is only sort right
		NumConstEquation num = new NumConstEquation(BigDecimal.ZERO, owner);
		add(pos,num);
		return result;
	}
	
	@Override
	public void justRemove(Equation equation) {
		int pos = indexOf(equation);
		super.justRemove(equation);
		NumConstEquation num = new NumConstEquation(BigDecimal.ZERO, owner);
		add(pos,num);
	}

    public float measureLeft() {
        return (float) ( get(0).measureWidth() + (getMyWidth() + myWidthAdd())/2f);
    }

    public float measureRight() {
        return (float) ( get(1).measureWidth() + (getMyWidth() + myWidthAdd())/2f);
    }

    public float getCenter() {
        return getX() - measureWidth()/2f + measureLeft();

    }
}
