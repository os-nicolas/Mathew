package cube.d.n.practice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cube.d.n.commoncore.CircleView;
import cube.d.n.commoncore.EquationView;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.NullLine;

/**
 * Created by Colin_000 on 7/1/2015.
 */
public class MainRow implements Row,Goable {


    private Class<?> targetActivity;
    public String title;
    public String subtitle;

    public MainRow(String title, String subtitle, Class<?> targetActivity){
        this(title,subtitle);
        this.targetActivity = targetActivity;

    }

    // this is for sub classes that override go
    protected MainRow(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public void go(Activity that) {
        Intent intent = new Intent(that,targetActivity);
        that.startActivity(intent);
    }

    View rowView;

    @Override
    public View makeView(Context context, ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        rowView = inflater.inflate(R.layout.topic_row, parent, false);

        TextView title = (TextView) rowView.findViewById(R.id.row_title);

        Typeface djLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        title.setTypeface(djLight);


        EquationView equationView = (EquationView) rowView.findViewById(R.id.row_subtitle);

        int at = i+1;
        CircleView cir = (CircleView) rowView.findViewById(R.id.topic_circle);
        String inCircle;

        if (this instanceof TopicRow) {
            TopicRow myTopic= (TopicRow)this;

            if (myTopic.equation == null) {
                equationView.setEquation(new VarEquation(myTopic.subtitle, new NullLine()), .5f);//,
            } else {
                equationView.setEquation(myTopic.equation, .5f);
            }

            inCircle = (myTopic.myId + 1 <= 9 ? "0" : "") +(myTopic.myId + 1) ;


        }else{
            MainRow myRow= this;
            equationView.setEquation(new VarEquation(myRow.subtitle, new NullLine()), .5f);//,
            inCircle=myRow.title.substring(0,2);
        }

        cir.circleDrawer.setColors(inCircle, CircleView.getBkgColor(at), CircleView.getTextColor(at));
        if (this instanceof TopicRow){
            cir.circleDrawer.setPrecent(((TopicRow)this).getPrecent());
            if (((TopicRow)this).getPrecent()==1){
                cir.circleDrawer.setSubText("COMPLETE");
            }
        }
        equationView.setFont(Mathilda.getMathilda().getDJVL());
        equationView.setColor(Mathilda.getApp().getGreyTextColor());
        title.setText(this.title);

        return rowView;

    }
}


