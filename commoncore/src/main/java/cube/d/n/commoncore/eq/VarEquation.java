package cube.d.n.commoncore.eq;

import cube.d.n.commoncore.BaseView;

public class VarEquation extends LeafEquation implements LegallityCheck{
		
	public VarEquation(String display, BaseView owner) {
		super(owner);
        init(display);

    }

    private void init(String display) {
        this.display = display;
    }

    public VarEquation(String display, BaseView owner, VarEquation equations) {
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