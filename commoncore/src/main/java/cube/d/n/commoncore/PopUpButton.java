package cube.d.n.commoncore;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.v2.KeyBoard;

/**
 * Created by Colin_000 on 3/26/2015.
 */
public class PopUpButton extends Button {

    float startAtX=0f;
    float endAtX=1f;
    float targetHeight=1f/9f;
    float currentHeight=0f;

    public PopUpButton( String text, Action myAction) {
        super(text, myAction);
        this.textPaint.setAlpha((int) 0);
    }

    public void setTargets(float targetHeight, float startAtX, float endAtX){
        this.startAtX=startAtX;
        this.endAtX=endAtX;
        this.targetHeight=targetHeight;
    }

    public void updateLocation(KeyBoard owner){

        float ybot = owner.buttonsPercent;
        float rate = BaseApp.getApp().getRate();
            if (myAction.canAct()){
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
                    }
                }
            }
            float ytop = ybot-currentHeight;

            owner.buttonsPercent = ytop;

            setLocation(startAtX,endAtX,ytop,ybot);

    }

    protected float targetHeight() {
        return targetHeight*canvasHeight;
    }
}
