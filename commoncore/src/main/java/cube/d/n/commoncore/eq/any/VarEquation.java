package cube.d.n.commoncore.eq.any;

import cube.d.n.commoncore.eq.LegallityCheck;
import cube.d.n.commoncore.lines.EquationLine;

public class VarEquation extends LeafEquation implements LegallityCheck {
		
	public VarEquation(String display, EquationLine owner) {
		super(owner);
        init(display);

    }

    private void init(String display) {
        this.display = display;
    }

    public VarEquation(String display, EquationLine owner, VarEquation equations) {
        super(owner, equations);
        init(display);
    }

    //TODO
    public boolean illegal() {
        return true;
    }
	
	@Override
	public Equation copy() {
		Equation result = new VarEquation(this.display, this.owner,this);

		return result;
	}
	
	@Override
	public boolean same(Equation eq){
		if (!(eq instanceof LeafEquation))
			return false;
		LeafEquation e = (LeafEquation)eq;
		return display.equals(e.display);
	}
}