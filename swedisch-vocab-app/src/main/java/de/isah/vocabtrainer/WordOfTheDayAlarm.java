package de.isah.vocabtrainer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.WordOfTheDay;

public class WordOfTheDayAlarm extends BroadcastReceiver {

    private static AlarmManager alarmMgr;
    private String persistentType;

    public WordOfTheDayAlarm() {
    }

    public WordOfTheDayAlarm(Context context, Calendar calendar, String persistentType) {
        this.persistentType = persistentType;

        if(alarmMgr == null ) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        //TODO: move everything that will not change over time into the if(alarmMgr == null )
        Intent intent = new Intent(context, WordOfTheDayAlarm.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // TODO: understand why this line was here: calendar.setTimeInMillis(System.currentTimeMillis());
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //WordOfTheDay.setWordOfTheDay(DictionaryCache.getCachedDictionary().getRandomWord());
        WordOfTheDay.setWordOfTheDay(DictionaryCache.getCachedDictionary(this.persistentType).getRandomWord());
        context.sendBroadcast(new Intent("newWordOfTheDay"));
    }

}
