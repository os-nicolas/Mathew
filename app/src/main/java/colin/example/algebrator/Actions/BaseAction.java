package colin.example.algebrator.Actions;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public abstract class BaseAction<T> {

    public T myView;

    public BaseAction(T myView){
        this.myView =myView;
    }

        public boolean canAct(){
            return true;
        }

        public void act(){
            if (canAct()){
                privateAct();
            }
        }

        protected abstract void privateAct();

}
