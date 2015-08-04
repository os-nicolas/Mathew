package cube.d.n.practice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import cube.d.n.commoncore.CircleView;
import cube.d.n.commoncore.EquationView;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.NullLine;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemRow implements Row,CanUpdatePrecent {


    public final Problem myProblem;
    public final String title;


    public ProblemRow(String line){
        myProblem = new Problem(line);
        title = "";
        myProblem.setRow(this);
    }

    public ProblemRow(String name,String circleText){
        myProblem =null;
        setCircleText(circleText);
        title = name;
    }


    private  String circleText="";
    public String getCircleText() {
        if (!"".equals(circleText)){
            return circleText;
        }else if (myProblem!=null){
            int p =myProblem.getIndex();
            return (p<10?"0":"")+p;
        }else{
            return "";
        }
    }
    public void setCircleText(String circleText){
        this.circleText = circleText;
    }
    public ProblemRow withCircleText(String circleText){
        setCircleText(circleText);
        return this;
    }

    View rowView;

    @Override
    public View makeView(Context context,ViewGroup parent,int i,int size) {



        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (myProblem != null) {
            if (!myProblem.input) {



                rowView = inflater.inflate(R.layout.problem_eq_row, parent, false);

                final EquationView equationView = (EquationView) rowView.findViewById(R.id.problem_eq_view);
                final TextView text = (TextView)rowView.findViewById(R.id.problem_eq_text);
                ((Activity)context).runOnUiThread(new Runnable() {
                         public void run() {
                             text.setText(myProblem.text);
                             text.setTextColor(Mathilda.getApp().getGreyTextColor());//0xff000000
                             text.setTypeface(Mathilda.getApp().getDJVL());
                             equationView.setEquation(myProblem.equation);
                             equationView.setColor(0xff000000);//Mathilda.getApp().getGreyTextColor()
                             equationView.setFont(Mathilda.getApp().getDJVL());
                         }
                     });



            } else {

                rowView = inflater.inflate(R.layout.problem_row, parent, false);

//                int MAX_LEN=130;
//
//                while (MAX_LEN < myProblem.text.length() && myProblem.text.charAt(MAX_LEN) != ' '){
//                    MAX_LEN++;
//                }
//
//                String str =  myProblem.text.substring(0,Math.min(MAX_LEN,myProblem.text.length()));
//                if (str.endsWith(" ")){
//                    str = str.substring(0,str.length()-1);
//                }
//                if (MAX_LEN<myProblem.text.length()){
//                    str = str + "...";
//                }
                final String fstr = myProblem.text;//str;//

                final TextView title = (TextView) rowView.findViewById(R.id.problem_name);
                final TextView text = (TextView) rowView.findViewById(R.id.row_subtitle);
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        text.setTextColor(Mathilda.getApp().getGreyTextColor());//0xff000000
                        text.setTypeface(Mathilda.getApp().getDJVL());
                        text.setText(fstr);
                        title.setTypeface(Mathilda.getApp().getDJVL());
                        title.setText(myProblem.name);
                    }
                });


            }
        } else {
            // 2. Get rowView from inflater
            rowView = inflater.inflate(R.layout.problem_row, parent, false);

            // 3. Get the two text view from the rowView
            TextView title = (TextView) rowView.findViewById(R.id.problem_name);

            Typeface dj = Typeface.createFromAsset(context.getAssets(),
                    "fonts/DejaVuSans-ExtraLight.ttf");
            title.setTypeface(dj);
            title.setText(this.title);
        }


        CircleView cir = (CircleView) rowView.findViewById(R.id.problem_circle);
        int p = i + 1;
        cir.circleDrawer.setColors(getCircleText(), CircleView.getBkgColor(p,size), CircleView.getTextColor(p,size));

        // donate rows have no myProblem
        if (myProblem != null) {
            cir.circleDrawer.setPrecent((myProblem.getSolved() ? 1 : 0));
            if (myProblem.getSolved()){
                cir.circleDrawer.setSubText("SOLVED");
            }
        }
        return rowView;
    }

    @Override
    public void updatePrecent() {
        // sometimes we have not vet inflated the row
        // in perticular this happens when you first create the activity
        // since onresume is called before the views are made
        // it is ok becuase precent is updated when we make the view
        if (rowView != null) {
            CircleView cir = (CircleView) rowView.findViewById(R.id.problem_circle);
            cir.circleDrawer.setPrecent((myProblem.getSolved() ? 1 : 0));
            if (myProblem.getSolved()){
                cir.circleDrawer.setSubText("SOLVED");
            }
        }
    }
}
