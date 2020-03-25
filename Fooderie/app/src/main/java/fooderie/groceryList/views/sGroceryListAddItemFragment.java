package fooderie.groceryList.views;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.fooderie.R;

public class sGroceryListAddItemFragment extends Fragment {

    public sGroceryListAddItemFragment(){}

    @SuppressWarnings("unused")
    public static sGroceryListAddItemFragment newInstance(int columnCount) {
        sGroceryListAddItemFragment fragment = new sGroceryListAddItemFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_addgrocitem, container, false);

        ImageView imageButton = (ImageView) v.findViewById(R.id.addGroceryItemFromFragment);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroceryListView.class);
                int value = 1;
                intent.putExtra("bClick", 1);
                startActivity(intent);
            }
        });
        return v;
    }
}
