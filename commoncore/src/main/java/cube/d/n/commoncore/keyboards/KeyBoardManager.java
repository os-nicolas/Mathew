package cube.d.n.commoncore.keyboards;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import cube.d.n.commoncore.GS;

/**
* Created by Colin_000 on 5/7/2015.
*/
public class KeyBoardManager extends GS<KeyBoard> {

   private long nextKeyboadAddAt =-1l;
   private long switchTime = 500;
   private GS<KeyBoard> nextKeyboard = new GS<KeyBoard>();

    @Override
   public void set(KeyBoard k){
        if (k!= null) {
            nextKeyboard.set(k);
            nextKeyboadAddAt = System.currentTimeMillis();
        }
    }
    public KeyBoard getNextKeyboard(){
        return nextKeyboard.get();
    }

    public void swtich(){
        value = nextKeyboard.get();
        nextKeyboard.set(null);
        Log.i("switch","to: "+ get());
    }

    public void hardSet(KeyBoard k){
        super.set(k);
        Log.i("hardSet","to: "+ get());
    }

    public void draw(Canvas canvas, float top, float left, Paint paint) {
        Paint p = new Paint();
        KeyBoard target = get();

        if (nextKeyboard.get() != null) {
            long timePassed = System.currentTimeMillis() - nextKeyboadAddAt;
            if (timePassed < switchTime / 2f && !get().getClass().equals(nextKeyboard.get().getClass())) {
                int a = (int) ((switchTime / 2f - timePassed) / (switchTime / 2f) * 0xff);
                p.setAlpha(a);
                target= get();
            } else if (timePassed < switchTime && !get().getClass().equals(nextKeyboard.get().getClass())) {
                int a = (int) ((timePassed - switchTime / 2f) / (switchTime / 2f) * 0xff);
                p.setAlpha(a);
                target = nextKeyboard.get();
            } else {
                swtich();
                target= get();
            }
        }
        target.draw(canvas, top, left, p);
    }
}
