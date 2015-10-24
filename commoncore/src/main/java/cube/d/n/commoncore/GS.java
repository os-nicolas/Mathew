package cube.d.n.commoncore;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class GS<T> {

    //TODO listenrs?

    protected T value;

    public GS(){}

    public GS(T inValue){
        set(inValue);
    }

    public GS(GS<T> toCopy) {
        this(toCopy.get());
    }

    public T get(){
        return value;
    }

    public void set(T newValue){
        value = newValue;
    }
}
