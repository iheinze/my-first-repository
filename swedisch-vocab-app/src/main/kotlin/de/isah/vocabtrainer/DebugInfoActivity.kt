package de.isah.vocabtrainer

import android.os.Bundle
import android.widget.TextView
import de.isah.vocabtrainer.dictionary.Dictionary
import de.isah.vocabtrainer.dictionary.DictionaryCache
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger

class DebugInfoActivity : VocabTrainerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_info)
        SwedishVocabAppLogger.log("on create", DebugInfoActivity::class.java)
        val text = getDebugInfo()
        val textView = findViewById<TextView>(R.id.textViewDebugInfo)
        textView.text = text
    }

    fun getDebugInfo(dictionary: Dictionary = DictionaryCache.getCachedDictionary(), handler: DebugInfoHandler = DebugInfoHandler()): String{
        return handler.getDebugInfo(dictionary, resources.getString(R.string.app_name), resources.getString(R.string.app_version))
    }
}