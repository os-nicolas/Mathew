package cube.d.n.practice;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;

import cube.d.n.commoncore.EquationView;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemRow {
    final public String name;
    public Equation equation;

    public ProblemRow(String name){
        this.name = name;
    }

    public ProblemRow(Equation equation){
        this.equation = equation;
        this.name="";
    }
}
