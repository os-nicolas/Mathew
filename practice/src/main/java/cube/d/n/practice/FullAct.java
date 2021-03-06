package cube.d.n.practice;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;

/**
 * Created by Colin_000 on 7/13/2015.
 */
public class FullAct extends Activity {


    @Override
    public void onResume(){
        super.onResume();
        hideStatusBar();
    }

    protected void hideStatusBar() {
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
