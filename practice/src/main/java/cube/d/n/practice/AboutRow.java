package cube.d.n.practice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Colin_000 on 7/9/2015.
 */
public class AboutRow implements Row {
    String about;

    public AboutRow(String about) {
        this.about = about;
    }

    @Override
    public View makeView(Context context, ViewGroup parent, int i, int size) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.about_row, parent, false);
        ((TextView)rowView.findViewById(R.id.about_text)).setText(about);
        rowView.setFocusable(false);
        rowView.setClickable(false);
        return rowView;
    }
}
