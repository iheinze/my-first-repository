package de.isah.vocabtrainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;

/**
 * Created by isa.heinze on 22.06.2018.
 */

public class OtherFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private static Dictionary dictionary;

    public OtherFragment(){

    }

    public static OtherFragment newInstance(int sectionNumber) {
        OtherFragment fragment = new OtherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_other, container, false);

        dictionary = DictionaryCache.getCachedDictionary();

        // Get debug preferences. If for some reason it does not work, debug mode should be on because there is something wrong.
        //disable add word button if needed.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        if(!sharedPref.getBoolean("pref_debug_mode", false)){
            (rootView.findViewById(R.id.buttonShowDebugInfo)).setVisibility(View.GONE);
            (rootView.findViewById(R.id.viewSpace)).setVisibility(View.GONE);
        } else {
            (rootView.findViewById(R.id.buttonShowDebugInfo)).setVisibility(View.VISIBLE);
            (rootView.findViewById(R.id.viewSpace)).setVisibility(View.VISIBLE);
        }

        if(!this.dictionary.isDisableImportExport()) {
            rootView.findViewById(R.id.buttonDictExport).setEnabled(true);
            rootView.findViewById(R.id.buttonDictImport).setEnabled(true);
        } else {
            rootView.findViewById(R.id.buttonDictExport).setEnabled(false);
            rootView.findViewById(R.id.buttonDictImport).setEnabled(false);
        }
        return rootView;
    }

}
