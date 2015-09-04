package cube.d.n.commoncore.eq.any;

import android.util.Log;

import cube.d.n.commoncore.eq.LegallityCheck;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class NumConstEquation extends LeafEquation implements LegallityCheck {

    private  boolean round = false;
    public boolean showAll = false;

    public void setRound(boolean round){
        this.round = round;
    }

	public NumConstEquation(BigDecimal number, EquationLine owner) {
		super(owner);
        if (owner instanceof InputLine){
            showAll = true;
        }
        init(number);
	}


    public NumConstEquation(double i, EquationLine emilyView) {
        this(new BigDecimal(i), emilyView);
    }

    private void init(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) < 0){
            Log.e("", "should be positive");
        }
        this.display = number+"";
        if (display.contains(".")) {
            while (display.charAt(display.length() - 1) == '0' || display.charAt(display.length() - 1) == '.') {
                if (display.charAt(display.length() - 1) == '.'){
                    display = display.substring(0, display.length() - 1);
                    break;
                }
                display = display.substring(0, display.length() - 1);
            }
        }
    }

    public NumConstEquation(BigDecimal value, EquationLine owner, NumConstEquation equations) {
        super(owner,equations);
        round = equations.round;
        init(value);
    }

    public String getDisplaySimple(){
        return display;
    }

    @Override
    public String getDisplay(int pos){
        if (showAll) {
            DecimalFormat df = new DecimalFormat();

            df.setMaximumFractionDigits(500);

            String result = df.format(getValue());
            // we need to deel with trailing zeros
            if (display.contains(".")) {
                int at = display.length() - 1;
                String toAdd = "";
                while (at >= 0 && display.charAt(at) == '0') {
                    toAdd = toAdd + '0';
                    at--;
                }
                if (at >= 0 && display.charAt(at) == '.') {
                    result += "." + toAdd;
                } else {
                    result += toAdd;
                }
            }
            return result;
        }else {
            DecimalFormat df;
            if (((getValue().abs().compareTo(new BigDecimal(.001)) <0) && !(getValue().doubleValue() == 0)) ||(getValue().compareTo(new BigDecimal(100000)) >0)){
                df = new DecimalFormat("0.000E0");
            }else{
                df = new DecimalFormat();
            }
            df.setMaximumFractionDigits(3);

            String result = df.format(getValue());
            return result;
        }
    }

    //TODO
    public boolean illegal() {
        return true;
    }


	public BigDecimal getValue(){
        BigDecimal value = new BigDecimal(display);
		return value;
	}
	
	@Override
	public Equation copy() {
		Equation result = new NumConstEquation(this.getValue(), this.owner,this);

		return result;
	}
	
	
	@Override
	public boolean same(Equation eq){
		if (!(eq instanceof NumConstEquation)) {
            return false;
        }
        NumConstEquation e = (NumConstEquation)eq;
        if (!this.round && !e.round) {
            return getValue().equals(e.getValue());
        }else{
            return Math.abs(getValue().doubleValue() -e.getValue().doubleValue())<.001f;
        }
	}

    public static Equation create(BigDecimal number, EquationLine o, boolean rounded) {
        if (number.compareTo(BigDecimal.ZERO) < 0){
            NumConstEquation numConstEquation = new NumConstEquation(number.negate(),o);
            numConstEquation.round = rounded;
            return numConstEquation.negate();
        }else{
            NumConstEquation numConstEquation = new NumConstEquation(number,o);
            numConstEquation.round = rounded;
            return numConstEquation;
        }
    }

    public static Equation create(BigDecimal number, EquationLine o) {
        return  create(number,o,false);
    }

    public static Equation create(double number, EquationLine o) {
        return  create(number,o,false);
    }

    public static Equation create(double number, EquationLine o,boolean rounded) {
        if (number < 0){
            NumConstEquation numConstEquation = new NumConstEquation(-number,o);
            numConstEquation.round = rounded;
            return numConstEquation.negate();
        }else{
            NumConstEquation numConstEquation = new NumConstEquation(number,o);
            numConstEquation.round = rounded;
            return numConstEquation;
        }
    }


    public boolean isZero() {
        return getValue().doubleValue() == 0.0;
    }

    public int decimalDigits() {
        if (display.contains(".")){
            int count = 0;
            for (int at = display.length()-1;at>=0 && display.charAt(at) != '.';at--){
                count++;
            }
            return count;
        }
        return 0;
    }

    public void cutDecimalsDown() {
        if (display.contains(".")){
            int end = display.length()-1;
            for (;end>=0 && display.charAt(end) != '.';end--){
            }
            // +4 because we are including the '.'
            display = display.substring(0,Math.min(end + 4, display.length()));

        }
    }

}


