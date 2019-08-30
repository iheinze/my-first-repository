package de.isah.vocabtrainer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.util.Calendar;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.WordOfTheDay;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.dictionary.persist.filehandling.AbstractFileHandler;

import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;
import de.isah.vocabtrainer.widget.WordOfTheDayAppWidgetProvider;

/**
 * @author isa.heinze
 */
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREVIOUS_FRAGMENT = "previousFragment";
    private static Dictionary dictionary;
    private String persistenceType = "";
    private AbstractFileHandler fileHandler;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        FileConstants.setFilePath(this.getFilesDir().getAbsolutePath());
        FileConstants.setExternalFilePath(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());


        SwedishVocabAppLogger.log("on create", MainActivity.class, sharedPref.getBoolean("pref_debug_mode", false));

        this.persistenceType = sharedPref.getString("pref_persist_method", "no selection");

        this.fileHandler = FileHandlerFactory.create(this.persistenceType);
        this.fileHandler.openIniDictionaryFile(getAssets());

        dictionary = DictionaryCache.getCachedDictionary(persistenceType);

        this.fileHandler.closeIniDictionary();


        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(this.getIntent().getIntExtra(PREVIOUS_FRAGMENT, 0));

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (sharedPref.getBoolean("pref_word_of_the_day", false)) {
            Calendar time = createRepeatTime(sharedPref);

            WordOfTheDayAlarm alarm = new WordOfTheDayAlarm(this, time);

            // TODO: move this to own class
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    WordOfTheDayFragment.reloadTestViews(findViewById(android.R.id.content));
                }
            };
            // TODO: do the registration differently, can I set intent filter in AndroidManifest.xml?
            WordOfTheDayAppWidgetProvider widgetProvider = new WordOfTheDayAppWidgetProvider();
            registerReceiver(receiver, new IntentFilter("newWordOfTheDay"));
            registerReceiver(widgetProvider, new IntentFilter("newWordOfTheDay"));
        }
    }

    //TODO: move this method to a utility class in a proper package and write JUnit tests
    @NonNull
    private Calendar createRepeatTime(SharedPreferences sharedPref) {
        String timeString = sharedPref.getString("pref_word_of_the_day_time", "00:00");
        int configuredHour = Integer.parseInt(timeString.split(":")[0]);
        int configuredMinute = Integer.parseInt(timeString.split(":")[1]);

        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, configuredHour);
        time.set(Calendar.MINUTE, configuredMinute);
        time.set(Calendar.SECOND, 0);
        return time;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_add).setVisible(!dictionary.isDisableAddWord());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                addWord();
                return true;
            case R.id.action_settings:
                showSettings();
                return true;
            case R.id.action_help:
                showHelp();
                return true;
            case R.id.action_info:
                showInfo();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addWord() {
        Intent intent = new Intent(this, AddWordActivity.class);
        addFragmentToIntent(intent);
        startActivity(intent);
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        addFragmentToIntent(intent);
        startActivity(intent);
    }

    private void showHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        addFragmentToIntent(intent);
        startActivity(intent);
    }

    private void showInfo() {
        Intent intent = new Intent(this, InfoActivity.class);
        addFragmentToIntent(intent);
        startActivity(intent);
    }

    public void showDebugInfo(View view) {
        Intent intent = new Intent(this, DebugInfoActivity.class);
        addFragmentToIntent(intent);
        startActivity(intent);
    }

    private void addFragmentToIntent(Intent intent) {
        intent.putExtra(PREVIOUS_FRAGMENT, mViewPager.getCurrentItem());
    }

    public static Intent getWorfOfTheDayFragmentIntent(Context context) {
        return new Intent(context, MainActivity.class).putExtra(PREVIOUS_FRAGMENT, 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dictionary.persist();
    }

    @Override
    protected void onResume() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("pref_persist_method".equals(key)) {
            SwedishVocabAppLogger.log("on shared preferences changed: "+key, MainActivity.class, sharedPreferences.getBoolean("pref_debug_mode", false));
            this.persistenceType = sharedPreferences.getString("pref_persist_method", "no selection");
            reloadDictionaryWithoutNotification();
        }
        if ("pref_word_of_the_day".equals(key)){
            SwedishVocabAppLogger.log("on shared preferences changed: "+key, MainActivity.class, sharedPreferences.getBoolean("pref_debug_mode", false));
            //TODO: implement this, figure out how the repeat can be unset
        }
        if ("pref_word_of_the_day_time".equals(key)){
            SwedishVocabAppLogger.log("on shared preferences changed: "+key, MainActivity.class, sharedPreferences.getBoolean("pref_debug_mode", false));
            Calendar time = createRepeatTime(sharedPreferences);
            new WordOfTheDayAlarm(this, time);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ShowWordsFragment.newInstance(position + 1);
                case 1:
                    return WordOfTheDayFragment.newInstance(position + 1);
                case 2:
                    return VocabTrainingFragment.newInstance(position + 1);
                case 3:
                    return OtherFragment.newInstance(position + 1);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return MainActivity.this.getString(R.string.activity_title_show_words);
                case 1:
                    return MainActivity.this.getString(R.string.activity_title_word_of_the_day);
                case 2:
                    return MainActivity.this.getString(R.string.activity_title_vocab_training);
                case 3:
                    return MainActivity.this.getString(R.string.activity_title_dictionary);
            }
            return null;
        }
    }

    /*
     * Start Other stuff
     */

    public boolean reloadDictionaryWithoutNotification() {
        this.fileHandler = FileHandlerFactory.create(this.persistenceType);
        this.fileHandler.openIniDictionaryFile(getAssets());
        boolean reload = DictionaryCache.reloadDictionary(this.persistenceType);
        dictionary = DictionaryCache.getCachedDictionary(this.persistenceType);
        this.fileHandler.closeIniDictionary();
        return reload;
    }

    public void reloadDictionary(View view) {
        boolean reload = reloadDictionaryWithoutNotification();

        String reloadMessage;
        if (reload) {
            reloadMessage = "Dictionary was reloaded.";
        } else {
            reloadMessage = "Dictionary could not be reloaded!";
        }

        Snackbar.make(view, reloadMessage, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    public void exportWords(View view) {
        boolean export = dictionary.export();

        String exportMessage;
        if (export) {
            exportMessage = "Dictionary was exported.";
        } else {
            exportMessage = "Dictionary could not be exported!";
        }

        Snackbar.make(view, exportMessage, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    public void importWords(View view) {
        boolean importResult = dictionary.importDir();

        String importMessage;
        if (importResult) {
            importMessage = "Dictionary was imported.";
        } else {
            importMessage = "Dictionary could not be imported!";
        }

        Snackbar.make(view, importMessage, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    /*
     * End Other stuff
     */
}
