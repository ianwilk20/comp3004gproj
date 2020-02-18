package fooderie.groceryList;

import android.content.Context;
import android.content.DialogInterface;
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

import com.example.fooderie.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list;
    private Context appContext;

    public CustomAdapter(ArrayList<String> strings, Context aContext){
        list = strings;
        appContext = aContext;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public String getItem(int item){
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
        groceryItem.setText(list.get(position));

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
                //Toast.makeText(v.getContext(), "Edit Button Was Clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Edit Item");

                v = LayoutInflater.from(v.getContext()).inflate(R.layout.edit_grocery_item, null, false);
                builder.setView(v);

                EditText item = (EditText) v.findViewById(R.id.itemName);
                item.setText(list.get(position));
                EditText itemQuantity = (EditText) v.findViewById(R.id.itemQuantity);
                EditText itemNotes = (EditText) v.findViewById(R.id.itemNotes);
                EditText itemDepartment = (EditText) v.findViewById(R.id.itemDepartment);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                String itemSelected = ((TextView) v).getText().toString();
                list.remove(itemSelected);
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
