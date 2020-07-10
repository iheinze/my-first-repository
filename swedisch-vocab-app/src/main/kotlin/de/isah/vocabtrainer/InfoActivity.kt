package de.isah.vocabtrainer

import android.os.Bundle
import android.widget.TextView
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger

class InfoActivity : VocabTrainerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        SwedishVocabAppLogger.log("on create", InfoActivity::class.java)

        val textView1 = findViewById<TextView>(R.id.textViewAppInfoName)
        textView1.text = getString(R.string.app_name)

        val textView2 = findViewById<TextView>(R.id.textViewAppInfoVersion)
        textView2.text = getString(R.string.app_version)
    }
}