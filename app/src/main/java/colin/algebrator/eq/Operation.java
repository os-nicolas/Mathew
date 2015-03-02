package colin.algebrator.eq;

import colin.example.algebrator.SuperView;

public abstract class Operation extends Equation {

	public Operation(SuperView owner2) {
		super(owner2);
	}

    public Operation(SuperView owner2, Operation op) {
        super(owner2, op);
    }
}
