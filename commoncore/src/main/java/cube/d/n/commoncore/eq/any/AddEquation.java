package cube.d.n.commoncore.eq.any;

import java.util.ArrayList;

import android.util.Log;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.FlexOperation;
import cube.d.n.commoncore.eq.MultiCountData;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.v2.lines.Line;

public class AddEquation extends FlexOperation {

    @Override
    public void integrityCheck(){
        if (size() < 2){
            Log.e("ic","this should be at least size 2");
        }
    }

    public boolean canFlatten(Equation a, Equation b){
        Equation lcc = a.lowestCommonContainer(b);
        return (lcc.addContain(a) && lcc.addContain(b));
    }

    public void flatten(Equation a, Equation b){
        Equation lcc = a.lowestCommonContainer(b);
        // we need to work our way up from a to lcc
        // and on each step pull everthing from the level we are at up a level
        for (Equation x: new Equation[]{a,b}) {
            Equation at = x;
            while (!at.equals(lcc)) {
                if (at instanceof AddEquation){
                    Equation oldEq = at.parent;
                    int loc = oldEq.indexOf(at);
                    at.justRemove();
                    for (Equation e : at) {
                        oldEq.add(loc++, e);
                    }
                }
                at = at.parent;
            }
        }
    }

    public AddEquation(Line owner, AddEquation addEq){
        super(owner, addEq);
        init();
        this.display = addEq.getDisplay(-1);

    }

	@Override
	public Equation copy() {
		Equation result = new AddEquation(this.owner,this);
		return result;
	}

	public String getDisplay(int pos) {
        if (pos ==-1){
            return display;
        }
        Equation at = get(pos);
        while(at.size()>1){
            at = at.get(0);
        }
        if (at instanceof MonaryEquation && !((MonaryEquation) at).drawSign()) {
            if (at instanceof MinusEquation) {
                return "-";
            }
            if (at instanceof PlusMinusEquation) {
                return "\u00B1";
            }
        }
		return display;
	}

	public AddEquation(Line owner) {
		super(owner);
        init();
	}

    private void init() {
        display = "+";
    }

    // addition is a little more spread out
    @Override
    protected float myWidthAdd() {
        return (float) (16* BaseApp.getApp().getDpi()*BaseApp.getApp().zoom);
    }

    public void tryOperator(ArrayList<Equation> eqs) {
        //TODO handle inbeddedness
        Equation a = eqs.get(0);
        Equation b = eqs.get(1);
		int at = Math.min(indexOf(a), indexOf(b));
        if (indexOf(a)> indexOf(b)){
            Equation temp =a;
            a=b;
            b=temp;
        }

        operateRemove(eqs);

        Equation result = Operations.Add(new MultiCountData(a), new MultiCountData(b), owner);
		 if (result instanceof AddEquation){
             int i=0;
             for (Equation e: result){
                 if ((this.size()==0 && result.indexOf(e) == result.size()-1 ) || !Operations.sortaNumber(result) || Operations.getValue(result).doubleValue() != 0) {
                     add(at + i, e);
                     i++;
                 }
             }
         }else if (this.size()==0 || !Operations.sortaNumber(result) || Operations.getValue(result).doubleValue() != 0){
             add(at, result);
         }
        if (this.size() ==1){
            // if this is a zero
            if (Operations.sortaNumber(this.get(0)) && Operations.getValue(this.get(0)).doubleValue() == 0 && this.parent instanceof AddEquation){
                this.remove();
            }else {
                this.replace(this.get(0));
            }
        }
	}



//    private CountData updateCounts(Equation e, HashSet<CountData> counts) {
//		CountData cd = new CountData(e);
//
//		boolean hasMatch = false;
//		for (CountData countData : counts) {
//			if (countData.matches(cd)) {
//				hasMatch = true;
//				countData.value += cd.value;
//			}
//		}
//		if (!hasMatch) {
//			counts.add(cd);
//		}
//		return cd;
//
//	}

}

//class CountData {
//	public HashSet<Equation> rem;
//	public Double value = 1.0;
//	public HashSet<Equation> key = new HashSet<Equation>();
//	public CountData over = null;
//
//	public CountData() {
//	}
//
//	public Equation remToEquation(SuperView owner) {
//		if (rem.size() == 0) {
//            if (value <0){
//                Equation minus = new MinusEquation(owner);
//                minus.add(new NumConstEquation(-value, owner));
//                return minus;
//            }else{
//                return new NumConstEquation(value, owner);
//            }
//		} else if (value == 0){
//            return new NumConstEquation(0, owner);
//        }else if (value == 1 && rem.size() == 1) {
//			return (Equation) rem.toArray()[0];
//		} else if (value == 1) {
//			Equation result = new MultiEquation(owner);
//			for (Equation e : rem) {
//				result.add(e);
//			}
//			return result;
//		} else {
//			Equation result = new MultiEquation(owner);
//			result.add(new NumConstEquation(value , owner));
//			for (Equation e : rem) {
//				result.add(e);
//			}
//			return result;
//		}
//	}
//
//	public Equation toEquation(SuperView owner) {
//        if (value ==0){
//            return new NumConstEquation(0,owner);
//        }else if (key.size() == 0) {
//			return new NumConstEquation(value, owner);
//		} else if (value == 1 && key.size() == 1) {
//			return (Equation) key.toArray()[0];
//		} else if (value == 1) {
//			Equation result = new MultiEquation(owner);
//			for (Equation e : key) {
//				result.add(e);
//			}
//			return result;
//		} else {
//			Equation result = new MultiEquation(owner);
//			result.add(new NumConstEquation(value , owner));
//			for (Equation e : key) {
//				result.add(e);
//			}
//			return result;
//		}
//	}
//
//	public CountData(Equation e) {
//        over = new CountData();
//		updateKey(e);
//	}
//
//	public void updateKey(Equation e) {
//        while (e instanceof MinusEquation){
//            value *= -1;
//            e = e.get(0);
//        }
//		if (e instanceof MultiEquation) {
//			for (Equation ee : e) {
//				this.updateKey(ee);
//			}
//		} else if (e instanceof LeafEquation) {
//			if (e instanceof NumConstEquation) {
//				NumConstEquation ee = (NumConstEquation) e;
//				this.value *= ee.getValue();
//			} else {
//				this.key.add(e);
//			}
//		} else if (e instanceof DivEquation) {
//			this.updateKey(e.get(0));
//			this.over.updateKey(e.get(1));
//		} else {
//			this.key.add(e);
//		}
//	}

	/**
	 * things in key that are not in common
	 * 
	 * @param common
	 * @return
	 */
//	public void remainder(HashSet<Equation> common) {
//        HashSet<Equation> commonClone = new HashSet<Equation>();
//		HashSet<Equation> result = new HashSet<Equation>();
//		for (Equation e : key) {
//			boolean pass = true;
//			for (Equation e2 : commonClone) {
//				if (e.same(e2)) {
//					pass = false;
//                    commonClone.remove(e2);
//					break;
//				}
//			}
//			if (pass) {
//				result.add(e);
//			}
//		}
//		rem = result;
//	}
//
//	/**
//	 * things in this.key also in cd2.key
//	 *
//	 * @param cd2
//	 * @return
//	 */
//	public HashSet<Equation> common(CountData cd2) {
//		HashSet<Equation> result = new HashSet<Equation>();
//		for (Equation e : key) {
//			for (Equation e2 : cd2.key) {
//				if (e.same(e2)) {
//					result.add(e);
//					break;
//				}
//			}
//		}
//		return result;
//	}
//
//	public boolean matches(CountData cd) {
//		// if everything in this is the same as something in the other
//		if (key.size() != cd.key.size()) {
//			return false;
//		}
//		for (Equation e : key) {
//			boolean any = false;
//			for (Equation ee : cd.key) {
//				if (ee.same(e)) {
//					any = true;
//					break;
//				}
//			}
//			if (!any) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//
//}
