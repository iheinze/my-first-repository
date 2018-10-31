package de.isah.vocabtrainer;

import android.os.Bundle;
import android.webkit.WebView;

import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

/**
 * Created by isa.heinze on 17.01.2018.
 */

public class HelpActivity extends VocabTrainerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwedishVocabAppLogger.log("on create", HelpActivity.class, isDebug);
        setContentView(R.layout.activity_help);

        WebView webView = findViewById(R.id.webViewHelp);
        webView.loadUrl("file:///android_asset/VocabAppHelp.html");
    }
}
