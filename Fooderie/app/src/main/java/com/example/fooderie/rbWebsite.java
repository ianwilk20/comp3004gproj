package com.example.fooderie;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import fooderie.models.Recipe;

public class rbWebsite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb_website);

        //Get selected recipe from rbActivity
        Intent intent = getIntent();
        Recipe selected = (Recipe)intent.getSerializableExtra("RECIPE");

        //Load selected recipe url into webview
        WebView myWebView = findViewById(R.id.webview);
        myWebView.loadUrl(selected.url);
    }
}
