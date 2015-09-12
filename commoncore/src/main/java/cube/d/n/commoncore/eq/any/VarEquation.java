package cube.d.n.commoncore.eq.any;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.SovleScreen.SubAction;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.eq.LegallityCheck;
import cube.d.n.commoncore.lines.AlgebraLine;
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

    @Override
    public ArrayList<SelectedRow> getSelectedRow() {
        ArrayList<SelectedRow> startWith = super.getSelectedRow();

        ArrayList<SelectedRowButtons> buttons = new ArrayList<>();

        if (BaseApp.getApp().allowsSub()){
            SelectedRowButtons srb = new SelectedRowButtons(BaseApp.getApp().getResources().getString(R.string.Sub), new SubAction((AlgebraLine)owner,(VarEquation)this.copy()));
            buttons.add(srb);
        }

        if (buttons.size() != 0) {
            SelectedRow sr = new SelectedRow(1f / 9f);
            sr.addButtonsRow(buttons, 0, 1);
            ArrayList<SelectedRow> res = new ArrayList<SelectedRow>();
            res.add(sr);
            return res;
        }else{
            return startWith;
        }
    }
}