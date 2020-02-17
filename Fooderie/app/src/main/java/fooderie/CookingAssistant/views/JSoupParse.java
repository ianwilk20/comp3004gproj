package fooderie.CookingAssistant.views;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class JSoupParse
{
    private static final String TAG = CookingAssistantViewer.class.getSimpleName();

    public void test()
    {
        try
        {
            String url = "http://webcode.me";

            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
            System.out.println(title);
        }
        catch(Exception e)
        {

        }
    }

    public ArrayList<String> JSoupParse(String url)
    {

        ArrayList<String> steps = new ArrayList<>();
        String val = "";

        try
        {
            Document doc = Jsoup.connect(url).get();

            if (url.contains("food52"))
            {
                Elements recipeList = doc.select("ol");
                int count = 0;
                for (Element recipeL : recipeList)
                {
                    for(int i = 1; i < recipeL.childNodeSize(); i += 2) //Skip over the empty child nodes
                    {
                        count++;

                        Node step1 = recipeL.childNode(i);
                        Node step2 = step1.childNode(1);
                        Node step3 = step2.childNode(0);
                        val = step3.toString();
                        steps.add(val);
                    }
                }
            }
            else if (url.contains("foodnetwork"))
            {
                Elements recipeList = doc.select("ol");
                int count = 0;
                for (Element recipeL : recipeList)
                {
                    for(int i = 1; i < recipeL.childNodeSize(); i += 2) //Skip over the empty child nodes
                    {
                        count++;

                        Node step1 = recipeL.childNode(i);
                        Node step3 = step1.childNode(0);
                        val = step3.toString();
                        steps.add(val);
                    }
                }
            }

            else if (url.contains("seriouseats"))
            {
                Elements recipeList = doc.select("ol");
                int count = 0;

                for (Element recipeL : recipeList)
                {
                    for(int i = 1; i < recipeL.childNodeSize(); i += 2) //Skip over the empty child nodes
                    {
                        count++;

                        Node step1 = recipeL.childNode(i);
                        Node step2 = step1.childNode(3).childNode(2).childNode(0);
                        if (step2.toString().contains("<strong>"))
                        {
                            Node step3 = step2.childNode(0); //Get actual step info
                            Node step4 = step1.childNode(3).childNode(2).childNode(1); //Get actual step info

                            val = step3.toString() + ":" + step4.toString();
                        }
                        else
                        {
                            Node step4 = step1.childNode(3).childNode(2).childNode(0); //Get actual step info

                            val = step4.toString();
                        }

                        steps.add(val);
                    }
                }
            }
            else
            {
                Log.d(TAG, "Non parsable website, do something here...");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //Log.d(TAG, e.getMessage());
        }

        return steps;
    }
}
