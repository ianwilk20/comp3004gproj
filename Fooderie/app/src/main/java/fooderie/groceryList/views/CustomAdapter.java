package fooderie.groceryList.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import com.example.fooderie.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

        TextView groceryItem = (TextView) v.findViewById(R.id.groceryItem);
        groceryItem.setText(list.get(position).getFood_name());
        TextView groceryItemInfo = (TextView) v.findViewById(R.id.groceryItemInfo);
        groceryItemInfo.setText("Quantity: " + list.get(position).getQuantity() + " Notes: " + list.get(position).getNotes() + " Department: " + list.get(position).getDepartment());

        ImageButton editButton = (ImageButton) v.findViewById(R.id.editButton);
        ImageButton deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);

        groceryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelected = ((TextView) v).getText().toString();
                list.remove(itemSelected);
                notifyDataSetChanged();
                if (myOnDataChangeListener != null){
                    myOnDataChangeListener.onDataChanged(itemSelected);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Edit Button Was Clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(parentAppActivty);
                builder.setTitle("Edit Item");

                String ogItemName = list.get(position).getFood_name();

                v = LayoutInflater.from(v.getContext()).inflate(R.layout.edit_grocery_item, null, false);
                builder.setView(v);

                final EditText item = (EditText) v.findViewById(R.id.itemName);
                item.setText(list.get(position).getFood_name());
                final EditText itemQuantity = (EditText) v.findViewById(R.id.itemQuantity);
                final EditText itemNotes = (EditText) v.findViewById(R.id.itemNotes);
                final EditText itemDepartment = (EditText) v.findViewById(R.id.itemDepartment);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userItem = item.getText().toString();
                        String userQuantity = itemQuantity.getText().toString();
                        String userNotes = itemNotes.getText().toString();
                        String userDepartment = itemDepartment.getText().toString();

                        //No SQL Check, maybe check fields before updating blindly
                        groceryListViewModel.updateGroceryItemAttributes(ogItemName, userItem, userQuantity, userNotes, userDepartment);
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
                String itemSelected = groceryItem.getText().toString();
                groceryListViewModel.deleteGroceryItemByName(itemSelected);
                notifyDataSetChanged();
            }
        });

        return v;
    }

    public interface OnDataChangeListener{
        public void onDataChanged(String item);
    }

    OnDataChangeListener myOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        myOnDataChangeListener = onDataChangeListener;
    }
}
