package cube.d.n.commoncore;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Colin on 4/24/2015.
 */
public interface CanTrackChanges {
    public void changed();

    public boolean hasChanged();

    ArrayList<Animation> getAfterAnimations();
}
