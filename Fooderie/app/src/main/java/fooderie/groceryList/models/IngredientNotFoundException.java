package fooderie.groceryList.models;

public class IngredientNotFoundException extends Exception {

    public IngredientNotFoundException(String ex_message){
        super(ex_message);
    }

}
