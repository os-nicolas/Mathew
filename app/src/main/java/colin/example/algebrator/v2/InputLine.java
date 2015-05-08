//package colin.example.algebrator.v2;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.view.MotionEvent;
//
//import java.util.ArrayList;
//
//import colin.example.algebrator.Algebrator;
//import cube.d.n.commoncore.eq.Equation;
//import cube.d.n.commoncore.eq.EquationDis;
//import cube.d.n.commoncore.eq.PlaceholderEquation;
//import cube.d.n.commoncore.eq.WritingEquation;
//
///**
// * Created by Colin_000 on 5/7/2015.
// */
//public class InputLine extends Line {
//
//    final PlaceholderEquation selected;
//
//    public InputLine(Main owner) {
//        super(owner);
//        selected = new PlaceholderEquation(this);
//        stupid.set(new WritingEquation(this));
//        stupid.get().add(selected);
//    }
//
//    TouchMode myMode;
//
//    @Override
//    public boolean onTouch(MotionEvent event) {
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (in(event)) {
//                if (stupid.get().nearAny(event.getX(), event.getY())) {
//                    resolveSelected(event);
//                    myMode = TouchMode.SELECT;
//                } else {
//                    myMode = TouchMode.MOVE;
//                }
//                return true;
//            } else {
//                myMode = TouchMode.NOPE;
//            }
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            if (myMode == TouchMode.SELECT) {
//                resolveSelected(event);
//                selected.goDark();
//            }
//            if (myMode == TouchMode.MOVE) {
//                //TODO scrolling
//            }
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (myMode == TouchMode.SELECT) {
//                resolveSelected(event);
//            }
//            // if we were dragging everything around
//            if (myMode == TouchMode.MOVE) {
//                //TODO scrolling
//            }
//
//        }
//        return false;
//    }
//
//    private boolean in(MotionEvent event) {
//        return event.getY() < getY() + measureHeight()/2f && event.getY() > getY() - measureHeight()/2f;
//    }
//
//
//    private void updateVelocity(MotionEvent event) {
//        //TODO
//    }
//
//    private void removeSelected() {
//        if (selected instanceof PlaceholderEquation) {
//            if (!(selected.parent.size() == 1 && selected.parent != null)) {
//                Equation oldEq = selected;
//                oldEq.remove();
//            }
//        }
//    }
//
//
//    private void resolveSelected(MotionEvent event) {
//// now we need to figure out what we are selecting
//        // find the least commond parent
//
//        int currentBkgAlpha = 0x00;
//        if (selected != null) {
//            currentBkgAlpha = selected.bkgAlpha;
//        }
//
//        removeSelected();
//
//        Equation lcp = null;
//        // if it's an action up
//        // and it was near the left of stupid
//        ArrayList<EquationDis> closest = stupid.get().closest(
//                event.getX(), event.getY());
//
//        lcp = closest.get(0).equation;
//
//        // TODO 100 to var scale by dpi
//        //float minDis = 100 * Algebrator.getAlgebrator().getDpi();
//        //if (Math.abs(event.getY() - lcp.y) < minDis) {
//        if (lcp instanceof PlaceholderEquation) {
//            lcp.setSelected(true);
//        } else {
//            // the the lcp is the left or right end of something we might want to select it's parent
//            Equation current = lcp;
//
//            boolean left = event.getX() < lcp.x;
//            int depth = 0;
//            // find how many layor deep we are
//            while (current.parent != null && current.parent.indexOf(current) == (left ? 0 : current.parent.size() - 1)) {
//                // we don't count binary eq because they are fixed size
//                //if (!(current.parent instanceof BinaryEquation)) {
//                depth++;
//                //}
//                current = current.parent;
//            }
//            // we really should figure how much space we have to work with so we can divide it up
//
//
//            if (depth != 0) {
//                float distance = 100 * Algebrator.getAlgebrator().getDpi() + (lcp.measureWidth() / 2f);
//
//                // this means use left or right
//                Equation next = (left ? lcp.left() : lcp.right());
//                if (next != null) {
//                    distance = Math.abs(lcp.x - next.x) / 2;
//                }
//
//                float each = distance / ((float) (depth + 1));
//                int num = (int) Math.floor(Math.min(Math.abs(lcp.x - event.getX()), distance) / each);
//
//                if (num == depth + 1) {
//                    num = depth;
//                }
//
//                current = lcp;
//                while (num != 0) {
//                    //if (!(current.parent instanceof BinaryEquation)) {
//                    num--;
//                    //}
//                    current = current.parent;
//                }
//                lcp = current;
//            }
//
//
//            // insert a Placeholder to the left of everything
//            Equation toSelect = selected;
//            toSelect.bkgAlpha = currentBkgAlpha;
//            toSelect.x = event.getX();
//            toSelect.y = event.getY();
//            // add toSelect left of lcp
//            if (lcp instanceof WritingEquation) {
//                lcp.add((left ? 0 : lcp.size()), toSelect);
//            } else if (lcp.parent instanceof WritingEquation) {
//                int at = lcp.parent.indexOf(lcp);
//                lcp.parent.add(at + (left ? 0 : 1), toSelect);
//            } else {
//                Equation oldEq = lcp;
//                Equation holder = new WritingEquation(this);
//                oldEq.replace(holder);
//                if (left) {
//                    holder.add(toSelect);
//                    holder.add(oldEq);
//                } else {
//                    holder.add(oldEq);
//                    holder.add(toSelect);
//                }
//                toSelect.setSelected(true);
//            }
//
//        }
//        //}
//        if (selected != null) {
//            if (event.getAction() == MotionEvent.ACTION_UP || event.getPointerCount() != 1) {
//                selected.drawBkg = false;
//            } else {
//                selected.drawBkg = true;
//                selected.goDark();
//            }
//        }
//
//        return;
//    }
//
//
//    @Override
//    public float measureHeight() {
//        return stupid.get().measureHeight() + 2 * buffer;
//    }
//
//    @Override
//    public void innerDraw(Canvas canvas, float top, float left, Paint paint) {
//        stupid.get().draw(canvas, top + buffer + stupid.get().measureHeightUpper(), left + measureWidth() / 2f);
//    }
//}
