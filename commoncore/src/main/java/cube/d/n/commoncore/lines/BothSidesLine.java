package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.SovleScreen.BothSidesMode;
import cube.d.n.commoncore.HasHeaderLine;
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
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.BothSidesKeyBoard;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 5/11/2015.
 */
public class BothSidesLine extends InputLine implements HasHeaderLine {

    BothSidesKeyBoard myKeyboard;
    private BothSidesMode myBothSidesMode;
    private Equation OGmodie;
    Equation demo;

    public BothSidesLine(Main owner) {
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


    private ArrayList<Equation> getAddToBothSides() {
        ArrayList<Equation> toBothSides = new ArrayList<>();
        toBothSides.add(convert(stupid.get().copy()));
        toBothSides.add(convert(stupid.get().copy()));
        for (Equation e : toBothSides) {
            if (myBothSidesMode == BothSidesMode.MULTI &&
                    e.get(0) instanceof WritingPraEquation && ((WritingPraEquation)e.get(0)).left &&
                    e.get(e.size() -1) instanceof WritingPraEquation && !((WritingPraEquation)e.get(e.size() -1)).left  ){
                e.remove(0);
                e.remove(e.size() -1);
            }
//            e.demo = true;
//            e.bkgAlpha = 0xff;
        }
        return toBothSides;
    }

    private Equation convert(Equation eq) {
        // we want to remove |
//        if (eq instanceof WritingEquation && eq.size() ==1){
//            return null;
//        }else{
        Equation toReturn = removePlaceHolder(eq);
        Log.i("what we got?", toReturn.toString());
        if ((toReturn instanceof WritingEquation && toReturn.size() != 1) && (myBothSidesMode == BothSidesMode.MULTI || myBothSidesMode == BothSidesMode.SUB)) {
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

    public Equation makeModie(ArrayList<Equation> toBothSides) {
        Equation modie = OGmodie.copy();


        if (myBothSidesMode == BothSidesMode.ADD) {

            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    if (!(modie.get(i) instanceof AddEquation)) {
                        Equation oldEq = modie.get(i);
                        Equation toAdd = new AddEquation(this);
                        modie.get(i).replace(toAdd);
                        toAdd.add(oldEq);
                    }
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                if (!(modie instanceof AddEquation)) {
                    Equation oldEq = modie;
                    modie = new AddEquation(this);
                    modie.add(oldEq);
                }
                modie.add(toBothSides.get(0));
            }


        } else if (myBothSidesMode == BothSidesMode.SUB) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    if (!(modie.get(i) instanceof AddEquation)) {
                        Equation oldEq = modie.get(i);
                        Equation toAdd = new AddEquation(this);
                        modie.get(i).replace(toAdd);
                        toAdd.add(oldEq);
                    }
                    modie.get(i).add(toBothSides.get(i).negate());
                }
            } else {
                if (!(modie instanceof AddEquation)) {
                    Equation oldEq = modie;
                    modie = new AddEquation(this);
                    modie.add(oldEq);
                }
                modie.add(toBothSides.get(0).negate());
            }


        } else if (myBothSidesMode == BothSidesMode.MULTI) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    if (!(modie.get(i) instanceof MultiEquation)) {
                        Equation oldEq = modie.get(i);
                        Equation toAdd = new MultiEquation(this);
                        modie.get(i).replace(toAdd);
                        toAdd.add(oldEq);
                    }
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                if (!(modie instanceof MultiEquation)) {
                    Equation oldEq = modie;
                    modie = new MultiEquation(this);
                    modie.add(oldEq);
                }
                modie.add(toBothSides.get(0));
            }


        } else if (myBothSidesMode == BothSidesMode.DIV) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {

                    Equation oldEq = modie.get(i);
                    Equation toAdd = new DivEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                Equation oldEq = modie;
                modie = new DivEquation(this);
                modie.add(oldEq);
                modie.add(toBothSides.get(0));
            }
        } else if (myBothSidesMode == BothSidesMode.POWER) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    Equation oldEq = modie.get(i);
                    Equation toAdd = new PowerEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                Equation oldEq = modie;
                modie = new PowerEquation(this);
                modie.add(oldEq);
                modie.add(toBothSides.get(0));
            }
        }


        return modie;
    }

    private Equation setModie(Equation newModie) {

        return demo = newModie;
    }

    public void setUp(BothSidesMode myBothSidesMode, Equation mine) {
        this.myBothSidesMode  =myBothSidesMode;
        setOGmodie(mine);
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
