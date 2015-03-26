package colin.example.algebrator;

import colin.example.algebrator.Actions.Action;

/**
 * Created by Colin_000 on 3/26/2015.
 */
public class PopUpButton extends Button {

    float startAtX=0f;
    float endAtX=1f;
    float targetHeight=1/9f;
    float currentHeight=0f;

    public PopUpButton(SuperView owner, String text, Action myAction) {
        super(owner,text,myAction);
    }

    public void setTargets(float targetHeight, float startAtX, float endAtX){
        this.startAtX=startAtX;
        this.endAtX=endAtX;
        this.targetHeight=targetHeight;
    }

    public void updateLocation(){

            float ybot = owner.buttonsPercent;
            if (myAction.canAct()){
                float rate = Algebrator.getAlgebrator().getRate();
                currentHeight = (currentHeight*(rate-1) + targetHeight)/rate;
            }
            float ytop = ybot-currentHeight;

            owner.buttonsPercent = ytop;

            setLocation(startAtX,endAtX,ytop,ybot);
    }
}
