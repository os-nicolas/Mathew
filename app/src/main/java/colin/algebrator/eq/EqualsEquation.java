package colin.algebrator.eq;

import android.graphics.Canvas;
import android.util.Log;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.SuperView;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EqualsEquation extends Equation {

    @Override
    public void integrityCheck() {
        if (size() != 2){
            Log.e("ic", "this should always be size 2");
        }
    }

    public EqualsEquation(SuperView owner) {
		super(owner);
        init();
	}

    private void init() {
        display = "=";
        myWidth = Algebrator.getAlgebrator().getDefaultSize();
        myHeight = Algebrator.getAlgebrator().getDefaultSize();
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

    public EqualsEquation(SuperView owner, EqualsEquation eqEq){
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
        float diffX = +this.measureWidth()/2 - get(0).measureWidth() - (myWidth + myWidthAdd())/2;
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
}
