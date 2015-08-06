package cube.d.n.commoncore.eq;


import java.util.ArrayList;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.GS;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.SeletedRowEquationButton;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.lines.EquationLine;

public abstract class Operation extends Equation {

	public Operation(EquationLine owner2) {
		super(owner2);
	}

    public Operation(EquationLine owner2, Operation op) {
        super(owner2, op);
    }


}
