package cube.d.n.commoncore.keyboards;

import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Action.WriteScreen.ClearAction;
import cube.d.n.commoncore.Action.helper.HelperEnterAction;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.PopUpButton;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 9/11/2015.
 */
public class HelperInputKeyboard extends InputKeyboard {
    public HelperInputKeyboard(Main owner, InputLine inputLine) {
        super(owner,inputLine);
    }

    @Override
    protected SuperAction getEnter(InputLine line) {
        return new HelperEnterAction(line);
    }

    @Override
    protected void addButtons() {
        super.addButtons();
        popUpButtons.add((PopUpButton)(new PopUpButton("clear", new ClearAction((InputLine) line)).withColor(BaseApp.getApp().lightLightColor)));

    }

}
