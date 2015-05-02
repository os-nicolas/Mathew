package cube.d.n.commoncore.eq;

import android.graphics.Canvas;
import android.graphics.Paint;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.BaseView;


/**
 * Created by Colin on 1/29/2015.
 */
public class WritingSqrtEquation extends WritingPraEquation {

    public WritingSqrtEquation(BaseView owner){
        super(true,owner);
    }

    public WritingSqrtEquation(BaseView owner, WritingSqrtEquation equations) {
        super(true,owner, equations);
    }

    @Override
    public Equation copy() {
        Equation result = new WritingSqrtEquation(this.owner,this);
        return result;
    }


    //TODO scale by dpi
    //private float buffer = 10* Algebrator.getAlgebrator().getDpi();
    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        Paint temp = getPaint();

        Equation match = getMatch();
        float end;
        if (match != null){
            end =match.x+(match.measureWidth()/2);
        }else{
            Equation current = this;
            end = current.x + (current.measureWidth()/2);
            while (keepGoing(current) &&current.right() != null){
                end = current.x + (current.measureWidth()/2);
                current =  current.right();
            }
        }

        PowerEquation.sqrtSignDraw(canvas, x - BaseApp.getApp().getSqrtWidthAdd(this)/2 ,  y, temp ,measureHeightLower(), measureHeightUpper(),end,this);

        super.privateDraw(canvas,x+ BaseApp.getApp().getSqrtWidthAdd(this)/2 ,y);
    }

    @Override
    protected float privateMeasureHeightUpper() {
        return super.privateMeasureHeightUpper() + BaseApp.getApp().getSqrtHeightAdd(this) ;
    }

    @Override
    protected float privateMeasureWidth(){
        return super.privateMeasureWidth() + BaseApp.getApp().getSqrtWidthAdd(this);
    }


}
