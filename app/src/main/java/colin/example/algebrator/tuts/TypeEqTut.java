package colin.example.algebrator.tuts;

import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/19/2015.
 */
public class TypeEqTut extends WriteMessage {
    @Override
    protected String getSp_key() {
        return "writeEq";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view.message.veryOpen()){
            return true;
        }
        return false;
    }

    protected TypeEqTut(){}
//    private static TypeEqTut instance;
//    public static TypeEqTut getInstance(){
//        if (instance == null){
//            instance = new TypeEqTut();
//        }
//        return instance;
//    }

    @Override
    protected void privateShow(SuperView view) {
//        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_writemessage_3));
     }

}
