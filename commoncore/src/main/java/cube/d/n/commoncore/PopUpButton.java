package cube.d.n.commoncore;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 3/26/2015.
 */
public class PopUpButton extends Button {

    float startAtX=0f;
    float endAtX=1f;
    float targetHeight=1f/9f;
    float currentHeight=0f;
    protected boolean can = false;

    public boolean getCan(){
        return can;
    }

    public PopUpButton( String text, Action myAction) {
        super(text, myAction);
        targetBkgColor = BaseApp.getApp().lightLightColor;

//        targetBkgColor = targetBkgColor%0x1000000;
//        targetBkgColor = targetBkgColor + 0x88000000;

        this.textPaint.setAlpha(0);
    }

    public void setTargets(float targetHeight, float startAtX, float endAtX){
        this.startAtX=startAtX;
        this.endAtX=endAtX;
        this.targetHeight=targetHeight;
    }



    public void updateLocation(KeyBoard owner){
        float rate = BaseApp.getApp().getRate()/2f;
        updateCanAct();
            if (can){
                if (currentHeight < targetHeight) {
                    currentHeight = (currentHeight * (rate - 1) + targetHeight) / rate;
                    if ((int)(currentHeight*100)==(int)((currentHeight * (rate - 1) + targetHeight)*100 / rate)){
                        currentHeight=targetHeight;
                    }
                }else {
                    float currentAlpha = this.textPaint.getAlpha();
                    currentAlpha = ((float)(currentAlpha * (rate - 1) + 0xff)) / rate;
                    if ((int)(currentAlpha)==(int)((currentAlpha * (rate - 1) + 0xff) / rate)){
                        currentAlpha=0xff;
                    }
                    this.textPaint.setAlpha((int) currentAlpha);
                }
            }else{
                float currentAlpha = this.textPaint.getAlpha();
                if (0 < currentAlpha) {
                    currentAlpha = (currentAlpha * (rate - 1) ) / rate;
                    if ((int)currentAlpha==(int)((currentAlpha * (rate - 1)) / rate)){
                        currentAlpha=0;
                    }
                    this.textPaint.setAlpha((int) currentAlpha);
                }else {
                    currentHeight = (currentHeight * (rate - 1) + 0) / rate;
                    if ((int)currentHeight*100==(int)((currentHeight * (rate - 1) + 0)*100 / rate)){
                        currentHeight=0;
                        fullyHidden();
                    }
                }
            }
            float ybot = 1-owner.buttonsPercent;
            float ytop = ybot-currentHeight;

            owner.buttonsPercent = (1-ytop);

            setLocation(startAtX,endAtX,ytop,ybot);

    }


    //boolean lastcan = can;
    public void updateCanAct() {
        //lastcan = can;
        can = myAction.canAct();
        //if (can && !lastcan){
        //}
    }

    protected void fullyHidden() {
    }

    protected float targetHeight() {
        return targetHeight*canvasHeight;
    }

    public float getTargetHeight() {
        return targetHeight;
    }
}
