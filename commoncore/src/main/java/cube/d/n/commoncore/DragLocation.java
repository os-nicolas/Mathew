package cube.d.n.commoncore;


import android.util.Log;

import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.lines.AlgebraLine;


public class DragLocation implements Comparable<DragLocation> {
    public float x;
    public float y;
    public Equation myStupid;
    public AlgebraLine owner;
    public Equation myDemo;
    public boolean og = false;

    public DragLocation(Equation.Op op, Equation dragging, Equation equation, boolean right) {
        Log.d("dragLocation-input", op + ", " + dragging.toString() + ", " + equation + ", " + right);

        if (dragging.equals(equation)) {
            oGinit(equation);
        } else {
            this.owner = (AlgebraLine)equation.owner;
            myStupid = equation.owner.stupid.get().copy();

            Equation ownerStupid = owner.stupid.get();

            Equation ourEquation = Util.getSimilarEquation(ownerStupid, equation, myStupid);

            //let's follow the path down
            Equation at = owner.stupid.get();
            myDemo = Util.getSimilarEquation(ownerStupid, dragging, myStupid);

            // try op with our copies
            int lookingForId = myDemo.hashCode();
            myDemo = ourEquation.tryOp(myDemo, right, op);
            myStupid = myDemo.root();

            // at this point myDemo can point someplace stupid
            // so we need to find my demo again


            myStupid.x = 0;
            myStupid.y = 0;

            myStupid.updateLocation();

            if (myStupid instanceof EqualsEquation) {
                this.x = myDemo.x - ((EqualsEquation)myStupid).getCenter();
                this.y = myDemo.y - myStupid.getDrawnAtY();
            } else {
                this.x = myDemo.x - myStupid.getX();
                this.y = myDemo.y - myStupid.getDrawnAtY();
            }

            myDemo.demo = true;

            Log.d("dragLocation-result", myStupid.toString() + " x: " + x + " y: " + y);
            Log.d("dragLocation-result", " Dx: " + myDemo.x + " Dy: " + myDemo.y);
        }

    }

    private void oGinit(Equation equation) {
        this.owner = (AlgebraLine)equation.owner;
        og = true;
        myStupid = equation.owner.stupid.get();
        myDemo = equation;

        //myStupid.updateLocation();
        if (myStupid instanceof EqualsEquation) {
            this.x = equation.x - ((EqualsEquation)myStupid).getCenter();
            this.y = equation.y - myStupid.getDrawnAtY();
        } else {
            this.x = equation.x - myStupid.getX();
            this.y = equation.y - myStupid.getDrawnAtY();
        }

        myDemo.demo = true;
    }

    public float dis = 0;

    public DragLocation(Equation equation) {
        oGinit(equation);
    }

    public void updateDis(float eventX, float eventY) {
        if (myStupid instanceof EqualsEquation) {
            this.dis = (float) Math.sqrt((x + ((EqualsEquation)owner.stupid.get()).getCenter() - eventX) *
                    (x + ((EqualsEquation)owner.stupid.get()).getCenter() - eventX) +
                    (y + owner.stupid.get().getDrawnAtY() - eventY) *
                            (y + owner.stupid.get().getDrawnAtY() - eventY));
        } else {
            this.dis = (float) Math.sqrt((x + owner.stupid.get().getX() - eventX) *
                    (x + owner.stupid.get().getX() - eventX) +
                    (y + owner.stupid.get().getDrawnAtY() - eventY) *
                            (y + owner.stupid.get().getDrawnAtY()- eventY));
        }

    }

    @Override
    public int compareTo(DragLocation other) {
        float otherDis = other.dis;
        if (otherDis > dis) {
            return -1;
        } else if (otherDis < dis) {
            return 1;
        }
        return 0;
    }

    public boolean isOG() {
        return og;
    }

    public void select() {
        owner.stupid.set(myStupid);
        if (!isOG()) {
            if (owner instanceof AlgebraLine) {
                ((AlgebraLine) owner).changed();
                ((AlgebraLine) owner).updateHistory();
            }
        } else {
            myDemo.setSelected(true);
        }
        myStupid.deDemo();
    }
}
