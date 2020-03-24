package fooderie.cookingAssistant.models;

import java.util.Random;

public class generateUrl
{
    public String getUrl()
    {
        Random rng = new Random();

        String[] urls = {
                "https://food52.com/recipes/22633-strawberry-basil-lemonade",
                "https://www.foodnetwork.com/recipes/food-network-kitchen/strawberries-with-basil-granita-recipe-1928457",
                "https://www.seriouseats.com/recipes/2013/06/whole-wheat-oatmeal-pancakes-maple-roast-rhubarb-recipe.html",
                "https://www.seriouseats.com/recipes/2015/06/grilled-scallion-pancake-recipe.html"
        };

        return urls[rng.nextInt(urls.length)];
    }
}
