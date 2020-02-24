package fooderie.CookingAssistant.views.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.fooderie.R;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> steps;
    
    public SliderAdapter(Context context, ArrayList<String> instructions)
    {
        this.context = context;
        this.steps = instructions;
    }

    @Override
    public int getCount()
    {
        return steps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o)
    {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        //Get our layout
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_cooking_assistant_slide, container, false);

        //Get our elements
        TextView slideTitle = (TextView) view.findViewById(R.id.slideStepNumber);
        TextView slideInstructions = (TextView) view.findViewById(R.id.slideInstructions);

        //Set element values
        //slideTitle.setText(headings[position]);
        slideTitle.setText((position + 1) + ".");
        //slideInstructions.setText(instructionSteps[position]);
        slideInstructions.setText(steps.get(position));

        //Add view and return
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        //Remove defined object from instantiation
        container.removeView((RelativeLayout)object);
    }
}
