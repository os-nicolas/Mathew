package cube.d.n.commoncore.eq;


import cube.d.n.commoncore.BaseView;

public abstract class Operation extends Equation {

	public Operation(BaseView owner2) {
		super(owner2);
	}

    public Operation(BaseView owner2, Operation op) {
        super(owner2, op);
    }
}
