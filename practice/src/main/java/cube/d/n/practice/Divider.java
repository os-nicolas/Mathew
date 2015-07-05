package cube.d.n.practice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Colin_000 on 7/4/2015.
 */
public class Divider implements Row{

    @Override
    public View makeView(Context context,ViewGroup parent,int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_view_divider, parent, false);
        rowView.setFocusable(false);
        rowView.setClickable(false);
        return rowView;
    }
}
