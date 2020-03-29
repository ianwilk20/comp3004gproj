package fooderie.groceryList.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import com.example.fooderie.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fooderie.groceryList.models.Food;
import fooderie.groceryList.models.UserGroceryListItem;
import fooderie.groceryList.viewModels.GroceryListViewModel;

public class CustomAdapter extends BaseAdapter implements ListAdapter {

    private List<UserGroceryListItem> list;
    private Context appContext;
    private GroceryListViewModel groceryListViewModel;
    private Activity parentAppActivty;


    public CustomAdapter(List<UserGroceryListItem> strings, Context aContext, GroceryListViewModel groceryListViewModel, Activity activity){
        list = strings;
        appContext = aContext;
        this.groceryListViewModel = groceryListViewModel;
        parentAppActivty = activity;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public UserGroceryListItem getItem(int item){
        return list.get(item);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null){
            LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.edit_delete_layout, null);
        }

        //The grocery item displayed - as food_name
        TextView groceryItem = (TextView) v.findViewById(R.id.groceryItem);

        groceryItem.setText(list.get(position).getFood_name());

        //The grocery item's additional information - as quantity, notes, and department
        TextView groceryItemInfo = (TextView) v.findViewById(R.id.groceryItemInfo);

        String quantity = list.get(position).getQuantity();
        String notes = list.get(position).getNotes();
        String dept = list.get(position).getDepartment();
        String food_id = list.get(position).getFood_id();
        String infoStr = "";

        if (quantity != null && !quantity.isEmpty()) { infoStr += "Quantity: " + quantity; }
        if (notes != null && !notes.isEmpty()) { infoStr += "  Notes: " + notes; }
        if (dept != null && !dept.isEmpty()) { infoStr += "  Department: " + dept; }
        groceryItemInfo.setText(infoStr);

        if (list.get(position).getInPantry() == true) {
            groceryItem.setPaintFlags(groceryItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            groceryItemInfo.setPaintFlags((groceryItemInfo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG));
        } else if (list.get(position).getInPantry() == false){
            groceryItem.setPaintFlags(0);
            groceryItemInfo.setPaintFlags(0);
        }

        ImageButton editButton = (ImageButton) v.findViewById(R.id.editButton);
        ImageButton deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);

        groceryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((groceryItem.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                    groceryListViewModel.updateInPantryStatus(list.get(position).getFood_id(), false);
                } else {
                    groceryListViewModel.updateInPantryStatus(list.get(position).getFood_id(), true);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentAppActivty);
                builder.setTitle("Edit Item");

                String ogItemName = list.get(position).getFood_name();
                String foodIDForChosenItem = list.get(position).getFood_id();

                v = LayoutInflater.from(v.getContext()).inflate(R.layout.edit_grocery_item, null, false);
                builder.setView(v);

                LiveData<Food> foodFromDB = groceryListViewModel.getFoodByID(food_id);
                Food foodObject = foodFromDB.getValue();


                final EditText item = (EditText) v.findViewById(R.id.itemName);
                item.setText(list.get(position).getFood_name());
                final EditText itemQuantity = (EditText) v.findViewById(R.id.itemQuantity);
                itemQuantity.setText(list.get(position).getQuantity());
                final EditText itemNotes = (EditText) v.findViewById(R.id.itemNotes);
                itemNotes.setText(list.get(position).getNotes());
                final EditText itemDepartment = (EditText) v.findViewById(R.id.itemDepartment);
                itemDepartment.setText(list.get(position).getDepartment());

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String userItem = item.getText().toString();
                        String userQuantity = itemQuantity.getText().toString();
                        String userNotes = itemNotes.getText().toString();
                        String userDepartment = itemDepartment.getText().toString();

                        //No SQL Check, maybe check fields before updating blindly
                        groceryListViewModel.updateGroceryItemAttributes(foodIDForChosenItem, userItem, userQuantity, userNotes, userDepartment, false);
                        notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idForItem = list.get(position).getFood_id();
                groceryListViewModel.deleteGroceryItemByID(idForItem);
                notifyDataSetChanged();
            }
        });

        return v;
    }

    public void clearCrossedOffItems(){
        List<UserGroceryListItem> inPantryItems = FetchIngredientsWithInPantryStatus();
        if (inPantryItems != null && inPantryItems.size() != 0){
            for (UserGroceryListItem i : inPantryItems){
                if (i.getInPantry() == true){
                    groceryListViewModel.delete(i);
                }
            }
        }
    }

    public void clearAllItems(){
        //Toast.makeText(this.appContext, "Clearing All items", Toast.LENGTH_SHORT).show();
        groceryListViewModel.deleteAllGroceryItems();
    }

    public interface OnDataChangeListener{
        public void onDataChanged(String item);
    }

    OnDataChangeListener myOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        myOnDataChangeListener = onDataChangeListener;
    }

    private class GetItemsInPantryAsyncTask extends AsyncTask<Void, Void, List<UserGroceryListItem>> {

        @Override
        protected List<UserGroceryListItem> doInBackground(Void... voids) {
            List<UserGroceryListItem> ingredientWithPantryStatus = null;
            ingredientWithPantryStatus = groceryListViewModel.getItemsInPantry();
            return ingredientWithPantryStatus;
        }
    }

    public List<UserGroceryListItem> FetchIngredientsWithInPantryStatus(){
        GetItemsInPantryAsyncTask getGroceryItemsInPantry = new GetItemsInPantryAsyncTask();
        getGroceryItemsInPantry.execute();
        List<UserGroceryListItem> itemsInPantry = null;
        try {
            itemsInPantry = getGroceryItemsInPantry.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (itemsInPantry == null){
            return null;
        } else {
            return itemsInPantry;
        }
    }
}
