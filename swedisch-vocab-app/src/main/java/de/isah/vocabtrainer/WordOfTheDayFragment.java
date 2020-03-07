package de.isah.vocabtrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.WordOfTheDay;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

public class WordOfTheDayFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public WordOfTheDayFragment() {
    }

    public static WordOfTheDayFragment newInstance(int sectionNumber) {
        WordOfTheDayFragment fragment = new WordOfTheDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_word_of_the_day, container, false);

        final Dictionary dictionary = DictionaryCache.getCachedDictionary();

        // Get debug preferences. If for some reason it does not work, debug mode should be on because there is something wrong.
        //disable add word button if needed.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());

        SwedishVocabAppLogger.log("on create view", WordOfTheDayFragment.class, sharedPref.getBoolean("pref_debug_mode", false));

        TextView textViewSwedish = rootView.findViewById(R.id.textViewWordOftTheDaySwedish);
        textViewSwedish.setText(WordOfTheDay.printSwedishAndGrammar());

        TextView textViewGerman = rootView.findViewById(R.id.textViewWordOftTheDayGerman);
        textViewGerman.setText(WordOfTheDay.printGerman());

        TextView textViewRemark = rootView.findViewById(R.id.textViewWordOftTheDayRemarks);
        textViewRemark.setText(WordOfTheDay.printRemark());


        Button updateButton = rootView.findViewById(R.id.buttonUpdateWordOfTheDay);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordOfTheDay.setWordOfTheDay(dictionary.getRandomWord());
                reloadTextViews(rootView);
                rootView.getContext().sendBroadcast(new Intent("newWordOfTheDay"));
            }
        });


        return rootView;
    }

    static void reloadTextViews(View rootView) {

        TextView textViewSwedish = rootView.findViewById(R.id.textViewWordOftTheDaySwedish);
        textViewSwedish.setText(WordOfTheDay.printSwedishAndGrammar());

        TextView textViewGerman = rootView.findViewById(R.id.textViewWordOftTheDayGerman);
        textViewGerman.setText(WordOfTheDay.printGerman());

        TextView textViewRemark = rootView.findViewById(R.id.textViewWordOftTheDayRemarks);
        textViewRemark.setText(WordOfTheDay.printRemark());
    }
}
