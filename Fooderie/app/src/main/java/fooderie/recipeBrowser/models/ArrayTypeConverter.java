package fooderie.recipeBrowser.models;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Collections;

public class ArrayTypeConverter {
    @TypeConverter
    public static String arraylistToString(ArrayList<String> values) {
        if (values == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : values){
            sb.append(s);
            sb.append("\t");
        }
        return (sb.toString());
    }

    @TypeConverter
    public static ArrayList<String> stringToArrayList(String value) {
        if (value == null){
            return null;
        }
        String[] arr = value.split("\t");
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, arr);
        return list;
    }
}
