package de.isah.vocabtrainer

import android.os.Bundle
import android.webkit.WebView
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger

class GrammarActivity : VocabTrainerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar)
        SwedishVocabAppLogger.log("on create", GrammarActivity::class.java)
        val editText = findViewById<WebView>(R.id.webViewGrammar)
        editText.loadUrl("file:///android_asset/VocabAppGrammar.html")

    }

}