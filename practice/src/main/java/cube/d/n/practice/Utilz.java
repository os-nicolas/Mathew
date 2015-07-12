package cube.d.n.practice;

import java.util.ArrayList;

/**
 * Created by Colin_000 on 7/9/2015.
 */
public class Utilz {

    public static ArrayList<Row> asRowList(ArrayList<ProblemRow> problems) {
        ArrayList<Row> res = new ArrayList<Row>();
        for (ProblemRow p: problems){
            res.add(p);
        }
        return res;
    }
}
