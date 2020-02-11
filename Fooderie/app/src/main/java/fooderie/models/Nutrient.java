package fooderie.models;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Nutrient implements Serializable {

    private static DecimalFormat df = new DecimalFormat("0.00");

    public String label;
    public double quantity;
    public String unit;

    public Nutrient(String l, double q, String u){
        label = l;
        quantity = q;
        unit = u;
    }

    public String round(){
        String quantityString = df.format(quantity);
        return quantityString;
    }
}
