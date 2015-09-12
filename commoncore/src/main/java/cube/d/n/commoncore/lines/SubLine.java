package cube.d.n.commoncore.lines;

/**
 * Created by Colin_000 on 9/11/2015.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

import cube.d.n.commoncore.HasHeaderLine;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.keyboards.BothSidesKeyBoard;
import cube.d.n.commoncore.keyboards.KeyBoard;


/**
 * Created by Colin_000 on 5/11/2015.
 */
public class SubLine extends InlineInputLine implements HasHeaderLine {

    BothSidesKeyBoard myKeyboard;
    private Equation OGmodie;
    Equation demo;
    private VarEquation toReplace;

    public SubLine(Main owner) {

        super(owner);
    }

    @Override
    public KeyBoard getKeyboad() {
        if (myKeyboard == null){
            myKeyboard = new BothSidesKeyBoard(owner,this);
        }
        return myKeyboard;
    }

    @Override
    public void setKeyBoard(KeyBoard k) {
        myKeyboard = (BothSidesKeyBoard)k;
    }

    @Override
    public pm parentThesisMode() {
        return null;
    }

    @Override
    public float requestedMaxX() {
        return Math.max(0,(demo.measureWidth()+  getBuffer()*2-owner.width)/2f);
    }

    @Override
    public float requestedMinX() {
        return -Math.max(0,(demo.measureWidth()+  getBuffer()*2-owner.width)/2f);
    }


    private Equation getAddToBothSides() {
        return convert(stupid.get().copy());
    }

    private Equation convert(Equation eq) {
        // we want to remove |
//        if (eq instanceof WritingEquation && eq.size() ==1){
//            return null;
//        }else{
        Equation toReturn = removePlaceHolder(eq);
        Log.i("what we got?", toReturn.toString());
        if ((toReturn instanceof WritingEquation && toReturn.size() != 1)) {
            toReturn.add(0, new WritingPraEquation(true, this));
            toReturn.add(new WritingPraEquation(false, this));
        }

        return toReturn;
//        }
    }

    private Equation removePlaceHolder(Equation eq) {
        if (eq instanceof PlaceholderEquation) {
            if (eq.parent instanceof BinaryEquation ||
                    eq.parent.size() == 1 ||
                    (eq.left() != null && eq.left().isOpLeft()) && (eq.right() == null || eq.right().isOpRight()) ||
                    (eq.right() != null && eq.right().isOpRight()) && (eq.left() == null || eq.left().isOpLeft())) {
                eq.replace(new VarEquation("?", this));
            } else {
                eq.remove();
            }
        } else {
            // we iterate backword so we can remove
            for (int i = eq.size() - 1; -1 < i; i--) {
                removePlaceHolder(eq.get(i));
            }
        }
        return eq;
    }

    public void updateModie() {
        setModie(makeModie(getAddToBothSides()));
    }


    //U!G!L!Y!
    @Override
    public Equation makeModie(ArrayList<Equation> converted) {
        return makeModie(converted.get(0));
    }

    public Equation makeModie(Equation replaceWith) {
        Equation modie = OGmodie.copy();

        return Util.sub(modie,toReplace,replaceWith);
    }

    private Equation setModie(Equation newModie) {

        return demo = newModie;
    }

    public void setUp(Equation mine, VarEquation varEquation) {
        setOGmodie(mine);
        toReplace = (VarEquation)varEquation.copy();
        updateModie();

    }

    private void setOGmodie(Equation newOGModie) {
        OGmodie = newOGModie;
        demo = OGmodie;
    }

    @Override
    public void innerDraw(Canvas canvas, float top, float left, Paint paint) {

//        Rect r = new Rect((int)0,(int)top,(int)(0+ measureWidth()),(int)(top+measureHeight()));
//        Paint p = new Paint();
//        p.setAlpha(paint.getAlpha());
//        p.setColor(BaseApp.getApp().lightColor);
//        canvas.drawRect(r,p);

        // i should probably shade it the input color

        demo.draw(canvas, left + (measureWidth() / 2f), top + getBuffer()+ demo.measureHeightUpper());


        stupid.get().draw(canvas, left + (measureWidth() / 2f), top + getBuffer()*3+ demo.measureHeight() + stupid.get().measureHeightUpper() );


    }

    @Override
    public float measureHeight() {
        return stupid.get().measureHeight() + 2 *  getBuffer() + demo.measureHeight()+ 2* getBuffer();
    }


}

