package colin.example.algebrator;

import cube.d.n.commoncore.BaseApp;

/**
 * Created by Colin on 12/30/2014.
 */
public class Algebrator extends BaseApp {

    public EmilyView writeView  = null;
    public BothSidesView addBothView = null;
    public ColinView solveView  = null;

    public String getPropertyId(){
        return "UA-59613283-1";
    }

    public static Algebrator getAlgebrator(){
        return (Algebrator) BaseApp.getApp();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    BaseView active= null;
    public BaseView getActive() {
        //TODO
        return active;
    }

    public void setActive(BaseView baseView) {
        active = baseView;
    }

}
