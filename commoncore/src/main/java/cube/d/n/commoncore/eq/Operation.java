package cube.d.n.commoncore.eq;


import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.lines.Line;

public abstract class Operation extends Equation {

	public Operation(Line owner2) {
		super(owner2);
	}

    public Operation(Line owner2, Operation op) {
        super(owner2, op);
    }
}
