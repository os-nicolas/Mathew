package cube.d.n.commoncore.keyboards;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 5/9/2015.
 */
public class EnptyKeyboard extends KeyBoard {
    public EnptyKeyboard(Main owner) {
        super(owner,null);
    }

    public EnptyKeyboard(Main owner,EquationLine l ) {
        super(owner,l);
    }

    @Override
    protected void addButtons() {    }

    @Override
    public float getBaseButtonsPercent() {
        return 0;
    }
}
