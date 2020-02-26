package fooderie.recipeBrowser.models;

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

    //function to convert unit and quantity
    public void setUnits(String newUnit){
        //ounces to grams
        if((this.unit.equals("oz"))&&(newUnit.equals("g"))){
            this.quantity = (this.quantity*28.3495);
        }
        //grams to ounces
        if((this.unit.equals("g"))&&(newUnit.equals("oz"))){
            this.quantity = (this.quantity*0.035274);
        }
        this.unit = newUnit;
    }

    //function to round quantity and convert to string
    public String round(){
        String quantityString = df.format(quantity);
        return quantityString;
    }
}
