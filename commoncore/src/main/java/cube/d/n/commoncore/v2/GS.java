package cube.d.n.commoncore.v2;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class GS<T> {
    protected T value;

    public GS(){}

    public GS(T inValue){
        set(inValue);
    }


    public T get(){
        return value;
    }

    public void set(T newValue){
        value = newValue;
    }
}
