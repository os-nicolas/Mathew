package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

import cube.d.n.commoncore.eq.BinaryOperator;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.any.MultiDivSuperEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.NullLine;

/**
 * Created by Colin_000 on 6/26/2015.
 */
public class Util {

    public static Equation stringEquation(String[] inpus) {
        int at = 0;
        Equation res;

        String db = "";
        for (String s : inpus) {
            db += s + ",";
        }
        Log.d("stringEquation", db);

        if (inpus[at].equals("+")) {
            res = new AddEquation(new NullLine());
        } else if (inpus[at].equals("-")) {
            res = new MinusEquation(new NullLine());
        } else if (inpus[at].equals("/")) {
            res = new DivEquation(new NullLine());
        } else if (inpus[at].equals("^")) {
            res = new PowerEquation(new NullLine());
        } else if (inpus[at].equals("*")) {
            res = new MultiEquation(new NullLine());
        } else if (inpus[at].equals("±")) {
            res = new PlusMinusEquation(new NullLine());
        } else if (inpus[at].equals("=")) {
            res = new EqualsEquation(new NullLine());
        } else {
            res = getEquation(inpus[at]);
        }
        at++;
        while (at < inpus.length) {
            if (inpus[at].equals("(")) {
                int end = closeAt(inpus, at);
                String[] inner = new String[end - (at + 1)];
                for (int i = at + 1; i < end; i++) {
                    inner[i - (at + 1)] = inpus[i];
                }

                res.add(stringEquation(inner));
                at = end + 1;
            } else {
                res.add(getEquation(inpus[at]));
                at++;
            }
        }
        return res;
    }

    private static Equation getEquation(String inpu) {
        Equation res;
        boolean canConvert = false;
        try {
            new BigDecimal(inpu);
            canConvert = true;
        } catch (Exception e) {
        }

        if (canConvert) {
            res = NumConstEquation.create(new BigDecimal(inpu), new NullLine(), true);
        } else {
            res = new VarEquation(inpu, new NullLine());
        }
        return res;
    }

    private static int closeAt(String[] str, int start) {
        int at = start;
        int count = 0;
        while (at < str.length) {
            if (str[at].equals("(")) {
                count++;
            } else if (str[at].equals(")")) {
                count--;
            }
            if (count == 0) {
                return at;
            }
            at++;
        }
        Log.e("closeAt", "match not found");
        return -1;
    }

    public static void reduce(GS<Equation> stupid) {

        Equation outerLast = null;
        int startAt = 0;
        while (outerLast == null || !outerLast.reallySame(stupid.get())) {
            Log.d("reduce outerloop",stupid.get().toString());
            int at = startAt;
            outerLast = stupid.get().copy();

            while (at < stupid.get().size()) {
                Log.d("reduce innerloop",stupid.get().toString() + " "+at);
                reduce(stupid.get(), at);

                stupid.get().fixIntegrety();
                if (at + 1 < stupid.get().size()) {
                    reduce(stupid.get(), at + 1);
                    stupid.get().fixIntegrety();
                }

                if (shouldOperate(stupid.get(), at)) {
                    stupid.get().tryOperator(at);
                }
                stupid.get().fixIntegrety();
                if (at < stupid.get().size()) {
                    // we want to move things we can't evaluate to the 0 position
                    if (
                            cantReduce(stupid.get(), at)
                            ) {
                        Equation oldEq = stupid.get().get(at);
                        stupid.get().get(at).justRemove();
                        stupid.get().add(0, oldEq);
                        startAt++;
                    }
                }
                at++;
            }
        }
    }

    private static boolean cantReduce(Equation eq, int at) {
        return (// we can't reduce (*&@^$(#@)/0
                eq.get(at) instanceof DivEquation &&
                        Operations.sortaNumber(eq.get(at).get(1)) &&
                        Operations.getValue(eq.get(at).get(1)).doubleValue() == 0
        ) || ( // or +-(*%()#@$%*&#)
                !(eq instanceof DivEquation) &&
                        eq.get(at) instanceof PlusMinusEquation &&
                        eq.get(at).get(0).size() == 0);
    }

    private static Equation reduce(Equation parent, int index) {
        Equation outerLast = null;
        Equation newEq = parent.get(index);
        int startAt = 0;
        while (outerLast == null || !outerLast.reallySame(newEq)) {
            Log.d("reduce outerloop",parent.toString());
            int at = startAt;
            outerLast = newEq.copy();

            while (at < newEq.size()) {
                Log.d("reduce innerloop",parent.toString()  + " at:"+at);
                reduce(newEq, at);
                newEq = parent.get(index);
                newEq.fixIntegrety();
                if (at + 1 < newEq.size()) {
                    reduce(newEq, at + 1);
                    newEq.fixIntegrety();
                }
                if (shouldOperate(newEq, at)) {
                    newEq.tryOperator(at);
                }
                newEq = parent.get(index);
                newEq.fixIntegrety();

                if (at < newEq.size()) {
                    if (cantReduce(newEq, at)) {
                        Equation oldEq = newEq.get(at);
                        newEq.get(at).justRemove();
                        newEq.add(0, oldEq);
                        startAt++;
                    }
                }
                at++;
            }
        }

        return newEq;
    }

    public static Equation getSimilarEquation(Equation stupid1, Equation sub1, Equation stupid2) {
        Equation at1 = stupid1;
        Equation at2 = stupid2;
        while (!at1.equals(sub1)) {
            int index = at1.deepIndexOf(sub1);
            at1 = at1.get(index);
            at2 = at2.get(index);
        }
        return at2;
    }

    private static boolean shouldOperate(Equation equation, int at) {
        if (equation instanceof BinaryOperator && at + 1 < equation.size()) {
            if ((equation.get(at).removeNeg() instanceof NumConstEquation) &&
                    (equation.get(at + 1).removeNeg() instanceof NumConstEquation)) {
                return true;
            }
            //div and multi don'r care about +-
            if (equation instanceof MultiDivSuperEquation) {
                if ((equation.get(at).removeSign() instanceof NumConstEquation || equation.get(at).removeSign() instanceof DivEquation) &&
                        (equation.get(at + 1).removeSign() instanceof NumConstEquation || equation.get(at + 1).removeSign() instanceof DivEquation)) {
                    return true;
                }
            }
            // we want to handle (2+-4)/5
            if (equation instanceof DivEquation) {
                if (equation.get(at) instanceof AddEquation &&
                        equation.get(at + 1).removeSign() instanceof NumConstEquation) {
                    return true;
                }
            }
            // we want to expand tho
            if (equation instanceof MultiEquation) {
                if ((equation.get(at).removeNeg() instanceof NumConstEquation || equation.get(at).removeNeg() instanceof AddEquation) &&
                        (equation.get(at + 1).removeNeg() instanceof NumConstEquation || equation.get(at + 1).removeNeg() instanceof AddEquation)) {
                    return true;
                }
            }
            // we want to put things under a common denominator also
            if (equation instanceof AddEquation) {
                if ((equation.get(at).removeNeg() instanceof NumConstEquation || equation.get(at).removeNeg() instanceof DivEquation) &&
                        (equation.get(at + 1).removeNeg() instanceof NumConstEquation || equation.get(at + 1).removeNeg() instanceof DivEquation)) {
                    return true;
                }
            }

        }

        if (equation instanceof MonaryEquation) {
            return true;
        }
        return false;
    }

    public static ArrayList<String> getVars(Equation stupid) {
        ArrayList<String> result = new ArrayList<>();
        if (stupid instanceof VarEquation) {
            result.add(stupid.getDisplay(-1));
        }
        for (Equation e : stupid) {
            ArrayList<String> innerResult = getVars(e);
            for (String s : innerResult) {
                if (!result.contains(s)) {
                    result.add(s);
                }
            }
        }
        return result;
    }

    public static Equation sub(Equation equation, VarEquation toReplace, Equation replaceWith) {
        if (equation.same(toReplace)) {
            return replaceWith.copy();
        } else {
            for (Equation inner : equation) {
                innerSub(inner, toReplace, replaceWith);
            }
        }
        return equation;
    }

    private static void innerSub(Equation equation, VarEquation toReplace, Equation replaceWith) {
        if (equation.same(toReplace)) {
            equation.replace(replaceWith.copy());
        } else {
            for (Equation inner : equation) {
                innerSub(inner, toReplace, replaceWith);
            }
        }
    }

    public static float varWidth(int min, String display, Paint paint) {
        return (float) (min) + paint.measureText(display) - paint.measureText("A");
    }

    public static void drawShadow(Canvas canvas, int alpha, float at, float width, boolean up) {
        Paint p = new Paint();
        int color = Color.BLACK;//BaseApp.getApp().darkDarkColor;
        p.setColor(color);
        p.setAlpha(alpha);
//        for (int i=0;i<2f/Algebrator.getAlgebrator().getDpi();i++){
//            canvas.drawLine(0,at,width,at,p);
//            at--;
//        }
        p.setAlpha((int) (0x8f * (alpha / ((float) 0xff))));
        while (p.getAlpha() > 1) {
            canvas.drawLine(0, at, width, at, p);
            p.setAlpha((int) (p.getAlpha() / BaseApp.getApp().getShadowFade()));
            at += (up ? 1 : -1);
        }
    }

    public static int colorMix(int c1, int c2, float p) {
        int currentAlpha = android.graphics.Color.alpha(c1);
        int currentRed = android.graphics.Color.red(c1);
        int currentGreen = android.graphics.Color.green(c1);
        int currentBlue = android.graphics.Color.blue(c1);


        int targetAlpha = android.graphics.Color.alpha(c2);
        int targetRed = android.graphics.Color.red(c2);
        int targetGreen = android.graphics.Color.green(c2);
        int targetBlue = android.graphics.Color.blue(c2);

        int res = android.graphics.Color.argb(
                (int) (((1-p) * currentAlpha +  p*targetAlpha) ),
                (int) (((1-p) * currentRed +  p*targetRed) ),
                (int) (((1 - p) * currentGreen + p*targetGreen) ),
                (int) (((1 - p) * currentBlue + p*targetBlue) ));
        return res;
    }
}
