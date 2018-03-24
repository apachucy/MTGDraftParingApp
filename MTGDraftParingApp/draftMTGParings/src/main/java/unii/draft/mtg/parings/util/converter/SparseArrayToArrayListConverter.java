package unii.draft.mtg.parings.util.converter;


import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class SparseArrayToArrayListConverter {

    private SparseArrayToArrayListConverter() {
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }
}
