package fooderie.models;

public class Nutrients {
    public double ENERC_KCAL;
    public double PROCNT;
    public double FAT;
    public double CHOCDF;

    public Nutrients(){
        ENERC_KCAL = 0.00;
        PROCNT = 0.00;
        FAT = 0.00;
        CHOCDF = 0.00;
    }

    public Nutrients(double kcal, double proc, double fat_cont, double carbs){
        ENERC_KCAL = kcal;
        PROCNT = proc;
        FAT = fat_cont;
        CHOCDF = carbs;
    }
}
