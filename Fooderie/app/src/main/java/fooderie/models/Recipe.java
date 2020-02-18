package fooderie.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "table_Recipe",
         indices = {@Index("recipe_id")})
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="recipe_id")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
