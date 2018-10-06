package de.isah.vocabtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.constants.ListConstants;
import de.isah.vocabtrainer.dictionary.word.Word;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by isa.heinze on 17.06.2018.
 */

public class ShowWordsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Dictionary dictionary;
    private View rootView;
    private static ArrayAdapter adapter;

    public ShowWordsFragment() {
    }

    public static ShowWordsFragment newInstance(int sectionNumber) {
        ShowWordsFragment fragment = new ShowWordsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_show_words, container, false);

        dictionary = DictionaryCache.getCachedDictionary();

        ListView listView = rootView.findViewById(R.id.listViewAll);
        ArrayList<Word> list = (ArrayList<Word>) this.dictionary.getAllWordsList().getOriginalList();
        this.adapter = new ArrayAdapter(rootView.getContext(), android.R.layout.simple_list_item_1, list);

        Word word = new Word();
        adapter.sort(word.new WordComparator());
        if(!StringUtils.isEmpty(ListConstants.getConstraint())) {
            adapter.getFilter().filter(ListConstants.getConstraint());
        }
        listView.setAdapter(adapter);
        listView.setSelection(ListConstants.getPosition());
        listView.setOnItemClickListener(new WordOnItemClickListener());
        listView.setOnScrollListener(new WordListOnScrollListener());

        SearchView searchView = rootView.findViewById(R.id.searchView);
        if(!StringUtils.isEmpty(ListConstants.getConstraint())) {
            searchView.setQuery(ListConstants.getConstraint(), false);
            searchView.setIconified(false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                ListConstants.setConstraint(newText);
                return false;
            }
        });

        return rootView;
    }

    class WordOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putLong("id", id);
            bundle.putString("word", ((Word)parent.getItemAtPosition(position)).serialize());
            bundle.putString("wordkey", ((Word) parent.getItemAtPosition(position)).getKey());

            Intent intent = new Intent(rootView.getContext(), ShowWordDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    class WordListOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            ListConstants.setPosition(firstVisibleItem);
        }
    }

    static void reloadWordList(){
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}

