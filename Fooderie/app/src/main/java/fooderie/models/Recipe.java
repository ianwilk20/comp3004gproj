package fooderie.models;

import java.net.URL;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "table_Recipe",
         indices = {@Index("recipe_id")})
public class Recipe {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="recipe_id")
    private int id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
