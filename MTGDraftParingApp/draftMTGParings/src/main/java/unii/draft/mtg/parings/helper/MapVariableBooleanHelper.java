package unii.draft.mtg.parings.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by apachucy on 2016-07-22.
 */
public class MapVariableBooleanHelper {
    public static final int TRUE_INT = 1;
    public static final int FALSE_INT = 0;
    public static final String TRUE_STRING = "True";
    public static final String FALSE_STRING = "False";
    private static final String[] sStringArray = {FALSE_STRING, TRUE_STRING};

    private MapVariableBooleanHelper() {
    }

    public static String castBooleanToString(boolean valueToBeCasted) {
        return !valueToBeCasted ? FALSE_STRING : TRUE_STRING;
    }

    public static int castBooleanToInteger(boolean valueToBeCasted) {
        return !valueToBeCasted ? FALSE_INT : TRUE_INT;
    }

    public static Boolean castStringToBoolean(String valueToBeCasted) {
        return Boolean.parseBoolean(valueToBeCasted);
    }

    public static List<String> castBooleanToStringList() {
        return Arrays.asList(sStringArray);
    }
}
