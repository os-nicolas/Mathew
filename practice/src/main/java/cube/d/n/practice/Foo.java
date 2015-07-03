package cube.d.n.practice;

import java.util.concurrent.Callable;

/**
 * Created by Colin_000 on 7/2/2015.
 */
public class Foo {
    public boolean fooIsEnabled = false;

    public final FunctionWrapper bar = new FunctionWrapper(this){
        @Override
        protected void innerCall() {
            // do whatever
        }
    };

    public final FunctionWrapper baz = new FunctionWrapper(this){
        @Override
        protected void innerCall() {
            // do whatever
        }
    };

    public final FunctionWrapper bat = new FunctionWrapper(this){
        @Override
        protected void innerCall() {
            // do whatever
        }
    };

    public abstract class FunctionWrapper {
        Foo owner;
        public FunctionWrapper(Foo f){
            this.owner = f;
        }
        public boolean isEnabled(){
            return owner.fooIsEnabled;
        }

        public final void call(){
            if (!isEnabled()){
                return;
            }
            innerCall();
        }

        protected abstract void innerCall();
    }
}

