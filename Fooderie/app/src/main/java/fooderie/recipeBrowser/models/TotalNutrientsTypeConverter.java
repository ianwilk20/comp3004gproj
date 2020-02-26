package fooderie.recipeBrowser.models;

import androidx.room.TypeConverter;

public class TotalNutrientsTypeConverter {
    @TypeConverter
    public static String totalNutrientsToString(TotalNutrients value) {
        if (value == null){
            return null;
        }

        StringBuilder sb = new StringBuilder();

        if(value.ENERC_KCAL!= null) {
            sb.append(value.ENERC_KCAL.label);
            sb.append("&");
            sb.append(value.ENERC_KCAL.quantity);
            sb.append("&");
            sb.append(value.ENERC_KCAL.unit);
        }
        else{
            sb.append("nill");
        }
        sb.append("\t");

        if(value.FAT!= null) {
            sb.append(value.FAT.label);
            sb.append("&");
            sb.append(value.FAT.quantity);
            sb.append("&");
            sb.append(value.FAT.unit);
        }
        else{
            sb.append("nill");
        }
        sb.append("\t");

        if(value.CHOCDF!= null) {
            sb.append(value.CHOCDF.label);
            sb.append("&");
            sb.append(value.CHOCDF.quantity);
            sb.append("&");
            sb.append(value.CHOCDF.unit);
        }
        else{
            sb.append("nill");
        }
        sb.append("\t");

        if(value.FIBTG!= null) {
            sb.append(value.FIBTG.label);
            sb.append("&");
            sb.append(value.FIBTG.quantity);
            sb.append("&");
            sb.append(value.FIBTG.unit);
        }
        else{
            sb.append("nill");
        }
        sb.append("\t");

        if(value.SUGAR!= null) {
            sb.append(value.SUGAR.label);
            sb.append("&");
            sb.append(value.SUGAR.quantity);
            sb.append("&");
            sb.append(value.SUGAR.unit);
        }
        else{
            sb.append("nill");
        }
        sb.append("\t");

        if(value.PROCNT!= null) {
            sb.append(value.PROCNT.label);
            sb.append("&");
            sb.append(value.PROCNT.quantity);
            sb.append("&");
            sb.append(value.PROCNT.unit);
        }
        else{
            sb.append("nill");
        }

        return (sb.toString());
    }

    @TypeConverter
    public static TotalNutrients stringToTotalNutrients(String value) {
        if (value == null){
            return null;
        }

        Nutrient constrArray[] = new Nutrient[6];

        String[] arr = value.split("\t");
        for(int i=0; i<6; ++i)
        {
            if(arr[i].equals("nill")){
                constrArray[i] = null;
            }
            else{
                String[] a = arr[i].split("&");
                constrArray[i] = new Nutrient(a[0],Double.valueOf(a[1]),a[2]);
            }
        }

        TotalNutrients tn = new TotalNutrients(constrArray[0], constrArray[1], constrArray[2], constrArray[3], constrArray[4], constrArray[5]);
        return tn;
    }
}
