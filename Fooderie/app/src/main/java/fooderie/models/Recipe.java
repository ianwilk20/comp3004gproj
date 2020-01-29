package fooderie.models;

import java.net.URL;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "table_Recipe")
public class Recipe {
    @PrimaryKey
    private int id;
}
