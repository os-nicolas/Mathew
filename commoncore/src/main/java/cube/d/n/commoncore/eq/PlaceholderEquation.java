package cube.d.n.commoncore.eq;

import android.graphics.Canvas;
import android.graphics.Paint;

import cube.d.n.commoncore.BaseView;


public class PlaceholderEquation extends LeafEquation {
	private long lastUpdate;
    public boolean drawBkg=false;

    public PlaceholderEquation(BaseView owner) {
		super(owner);
        init();
	}

    private void init() {
        display = "I";//"\u007C";
        setMyWidth(0);
        goDark();
    }

    public void goDark() {
        lastUpdate = System.currentTimeMillis();
    }

    public PlaceholderEquation(BaseView owner, PlaceholderEquation equations) {
        super(owner, equations);
        init();
    }

    @Override
	public Equation copy() {
		//Log.e("copy", "this should prolly not be called");
		Equation result = new PlaceholderEquation(this.owner,this);
		return result;
	}

    @Override
    protected float privateMeasureWidth() {
        return (parenthesis()?getParnWidthAddition():0);
    }

    @Override
    public Paint getPaint(){
        Paint p = new Paint(super.getPaint());
        long now = (System.currentTimeMillis()-lastUpdate)/4;
        now = now % 360;
        int alpha =(int)((1+Math.cos(Math.toRadians(now)))*127.5);
        p.setAlpha(alpha);
        return p;
    }

    @Override
    protected void drawParentheses(Canvas canvas, float x, float y, Paint temp) {
        temp.setAlpha(0xff);
        super.drawParentheses(canvas,x,y,temp);
    }
}
