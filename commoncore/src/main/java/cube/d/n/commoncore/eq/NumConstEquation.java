package cube.d.n.commoncore.eq;

import android.util.Log;


import cube.d.n.commoncore.BaseView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumConstEquation extends LeafEquation implements LegallityCheck {

	public NumConstEquation(BigDecimal number, BaseView owner) {
		super(owner);
        init(number);
	}

    public NumConstEquation(double i, BaseView emilyView) {
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

    public NumConstEquation(BigDecimal value, BaseView owner, NumConstEquation equations) {
        super(owner,equations);
        init(value);
    }

    public String getDisplaySimple(){
        return display;
    }

    @Override
    public String getDisplay(int pos){
        if (owner.parentThesisMode() == BaseView.pm.WRITE) {
            DecimalFormat df = new DecimalFormat();

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
            df.setMaximumFractionDigits(3);

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

    public static Equation create(BigDecimal number, BaseView o) {
        if (number.compareTo(BigDecimal.ZERO) < 0){
            return new NumConstEquation(number.negate(),o).negate();
        }else{
            return new NumConstEquation(number,o);
        }
    }

    public static Equation create(double number, BaseView o) {
        if (number < 0){
            return new NumConstEquation(-number,o).negate();
        }else{
            return new NumConstEquation(number,o);
        }
    }


    public boolean isZero() {
        return getValue().doubleValue() == 0.0;
    }
}


