package fooderie.CookingAssistant.views;

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

    //Arrays for steps (need to get from parsing still
    public String[] headings = {"1.", "2.", "3."};
    public String[] instructionSteps = {"This is a sentence of stuff", "More stuff here", "End of all of the stuffz"};

    public SliderAdapter(Context context, ArrayList<String> instructions)
    {
        this.context = context;

        this.steps = instructions;
        //this.steps.add("This is first step");
        //this.steps.add("This is second step");
        //this.steps.add("this is third step");
        //this.steps = steps;
        //Log.d(TAG, steps.toString());
    }





    @Override
    public int getCount()
    {
        return headings.length;
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
