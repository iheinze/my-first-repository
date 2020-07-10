package de.isah.vocabtrainer

import android.os.Bundle
import android.webkit.WebView
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger

class HelpActivity : VocabTrainerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        SwedishVocabAppLogger.log("on create", HelpActivity::class.java)
        val editText = findViewById<WebView>(R.id.webViewHelp)
        editText.loadUrl("file:///android_asset/VocabAppHelp.html")
    }

}