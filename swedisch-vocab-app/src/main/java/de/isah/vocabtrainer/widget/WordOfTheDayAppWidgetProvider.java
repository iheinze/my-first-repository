package de.isah.vocabtrainer.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import de.isah.vocabtrainer.MainActivity;
import de.isah.vocabtrainer.R;
import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.WordOfTheDay;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

public class WordOfTheDayAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            // Get proper intent for Main activity
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, MainActivity.getWordOfTheDayFragmentIntent(context), 0);

            // Get the layout for the App Widget and attach an on-click listener to the frame layout
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_wordoftheday);
            views.setTextViewText(R.id.widgetTextViewWordOfTheDay, WordOfTheDay.toStringWidget());
            views.setOnClickPendingIntent(R.id.widgetButtonView, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

            context.startService(new Intent(context, UpdateWordOfTheDayService.class));
        }
    }

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        // TODO: implement this to make widget look pretty at all times, also think about disallowing changes in size in wordoftheday_appwidget_info.xml
        //  more infos: https://developer.android.com/guide/topics/appwidgets#AppWidgetProvider
        /*
        This is called when the widget is first placed and any time the widget is resized. You can use this callback to show or hide content based on the widget's size ranges. You get the size ranges by calling getAppWidgetOptions(), which returns a Bundle that includes the following:

    OPTION_APPWIDGET_MIN_WIDTH—Contains the lower bound on the current width, in dp units, of a widget instance.
    OPTION_APPWIDGET_MIN_HEIGHT—Contains the lower bound on the current height, in dp units, of a widget instance.
    OPTION_APPWIDGET_MAX_WIDTH—Contains the upper bound on the current width, in dp units, of a widget instance.
    OPTION_APPWIDGET_MAX_HEIGHT—Contains the upper bound on the current height, in dp units, of a widget instance.

This callback was introduced in API Level 16 (Android 4.1). If you implement this callback, make sure that your app doesn't depend on it since it won't be called on older devices.
         */
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), WordOfTheDayAppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_wordoftheday);
            views.setTextViewText(R.id.widgetTextViewWordOfTheDay, WordOfTheDay.toStringWidget());
            for (int appWidgetId : appWidgetIds) {
                appWidgetManager.updateAppWidget(appWidgetId, views);

                if(intent.getAction() == null) {
                    context.startService(new Intent(context, UpdateWordOfTheDayService.class));
                } else {
                    super.onReceive(context, intent);
                }
            }
            //onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    public static class UpdateWordOfTheDayService extends IntentService {

        public UpdateWordOfTheDayService() {
            super("WordOfTheDayAppWidgetProvider$UpdateWordOfTheDayService");
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            System.out.println("i am here");
            ComponentName me=new ComponentName(this,
                    WordOfTheDayAppWidgetProvider.class);
            AppWidgetManager mgr=AppWidgetManager.getInstance(this);

            mgr.updateAppWidget(me, update(this));
        }

        private RemoteViews update(Context context){
            RemoteViews updateViews=new RemoteViews(context.getPackageName(), R.layout.widget_wordoftheday);

            SwedishVocabAppLogger.log("calling update", WordOfTheDayAppWidgetProvider.class, true);

            final Dictionary dictionary = DictionaryCache.getCachedDictionary();
            if(dictionary != null && dictionary.getNWordsInDict() > 0) {
                WordOfTheDay.setWordOfTheDay(dictionary.getRandomWord());
                context.sendBroadcast(new Intent("newWordOfTheDay"));
            }
            updateViews.setTextViewText(R.id.widgetTextViewWordOfTheDay, WordOfTheDay.toStringWidget());
            Intent intent = new Intent(context, WordOfTheDayAppWidgetProvider.class);
            PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, 0, intent, 0);
            updateViews.setOnClickPendingIntent(R.id.widgetButtonUpdate, pendingIntent);
            return updateViews;
        }
    }
}
