package de.isah.vocabtrainer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by isa.heinze on 17.01.2018.
 */

public class HelpActivity extends VocabTrainerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        WebView webView = (WebView) findViewById(R.id.webViewHelp);
        webView.loadUrl("file:///android_asset/VocabAppHelp.html");
    }
}
