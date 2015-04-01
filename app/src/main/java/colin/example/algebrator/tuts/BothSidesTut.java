package colin.example.algebrator.tuts;

import colin.algebrator.eq.AddEquation;
import colin.algebrator.eq.EqualsEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.BothSidesView;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin_000 on 3/31/2015.
 */
public class BothSidesTut extends TutMessage {

    protected String getSp_key() {
        return "bothsides";
    }

    protected BothSidesTut(){}
//    private static AddTut instance;
//    public static AddTut getInstance(){
//        if (instance == null){
//            instance = new AddTut();
//        }
//        return instance;
//    }


    @Override
    protected boolean privateShouldShow(SuperView view) {
        return view instanceof BothSidesView;
    }

    @Override
    protected void privateShow(SuperView view) {
        if (((BothSidesView)view).getBothSidesMode() == BothSidesView.BothSidesMode.ADD){
            view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_both_1_add));
        }else if (((BothSidesView)view).getBothSidesMode() == BothSidesView.BothSidesMode.SUB){
            view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_both_1_sub));
        }else if (((BothSidesView)view).getBothSidesMode() == BothSidesView.BothSidesMode.MULTI){
            view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_both_1_multi));
        }else if (((BothSidesView)view).getBothSidesMode() == BothSidesView.BothSidesMode.DIV){
            view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_both_1_div));
        }else if (((BothSidesView)view).getBothSidesMode() == BothSidesView.BothSidesMode.POWER){
            view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_both_1_power));
        }

        view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_both_2));
        view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_both_3));
    }
}
