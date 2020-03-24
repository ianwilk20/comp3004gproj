package fooderie.groceryList.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity (tableName = "table_userGroceryList",
         indices = {@Index("food_id")})
public class UserGroceryListItem {
    @PrimaryKey
    @NonNull
    private String food_id;

    private String food_name;
    private String quantity; //String because user can enter 4 cups or 4 bottles etc..
    private String notes;
    @ColumnInfo(name="department", defaultValue = "Other")
    private String department;
    @ColumnInfo(name="inPantry", defaultValue = "false")
    private boolean inPantry;

    @NonNull
    public String getFood_id(){return food_id;}

    public String getFood_name(){return food_name;}
    public String getQuantity(){return quantity;}
    public String getNotes(){return notes;}
    public String getDepartment(){return department;}
    public boolean getInPantry(){return inPantry;}

    public void setFood_id(String food_id){this.food_id = food_id;}
    public void setFood_name(String food_name){this.food_name = food_name;}
    public void setQuantity(String quantity){this.quantity = quantity;}
    public void setNotes(String notes){this.notes = notes;}
    public void setDepartment(String department){this.department = department;}
    public void setInPantry(Boolean inPantry){this.inPantry = inPantry;}

    public UserGroceryListItem(){
        department = "Other";
    }

    @Ignore
    public UserGroceryListItem(String food_id, String food_name){
        this.food_id = food_id;
        this.food_name = food_name;
    }

    @Ignore
    public UserGroceryListItem(String food_id, String food_name, String quantity, String notes, String department){
        this.food_id = food_id;
        this.food_name = food_name;
        this.quantity = quantity;
        this.notes = notes;
        this.department = department;
        this.inPantry = false;
    }
}
