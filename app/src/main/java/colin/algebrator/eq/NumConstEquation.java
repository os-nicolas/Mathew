package colin.algebrator.eq;

import android.util.Log;

import colin.example.algebrator.ColinView;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.SuperView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumConstEquation extends LeafEquation implements LegallityCheck {

	public NumConstEquation(BigDecimal number, SuperView owner) {
		super(owner);
        init(number);
	}

    public NumConstEquation(double i, SuperView emilyView) {
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

    public NumConstEquation(BigDecimal value, SuperView owner, NumConstEquation equations) {
        super(owner,equations);
        init(value);
    }

    public String getDisplaySimple(){
        return display;
    }

    @Override
    public String getDisplay(int pos){
        if (owner instanceof EmilyView) {
            DecimalFormat df = new DecimalFormat();
            if (owner instanceof ColinView) {
                df.setMaximumFractionDigits(3);
            }
            String result = df.format(getValue());
            // we need to deel with trailing zeros
            int at = display.length()-1;
            String toAdd ="";
            while (at >=0&&display.charAt(at)=='0'){
                toAdd = toAdd +'0';
                at--;
            }
            if (at >=0 && display.charAt(at)=='.'){
                result += "."+toAdd;
            }
            return result;
        }else {
            DecimalFormat df;
            if (((getValue().abs().compareTo(new BigDecimal(.001)) <0) && !(getValue().doubleValue() == 0)) ||(getValue().compareTo(new BigDecimal(100000)) >0)){
                df = new DecimalFormat("0.000E0");
            }else{
                df = new DecimalFormat();
            }
            if (owner instanceof ColinView) {
                df.setMaximumFractionDigits(3);
            }
            String result = df.format(getValue());
            return result;
        }
    }

    //TODO
    public boolean illegal() {
        return true;
    }

    BigDecimal value;
	public BigDecimal getValue(){
        value = new BigDecimal(display);
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
		return getValue().equals(e.getValue());
	}

    public static Equation create(BigDecimal number, SuperView o) {
        if (number.compareTo(BigDecimal.ZERO) < 0){
            return new NumConstEquation(number.negate(),o).negate();
        }else{
            return new NumConstEquation(number,o);
        }
    }
}


