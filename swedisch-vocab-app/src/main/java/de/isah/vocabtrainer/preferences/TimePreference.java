package de.isah.vocabtrainer.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import de.isah.vocabtrainer.R;

// code taken from here: https://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen
public class TimePreference extends DialogPreference {

    private int lastHour=0;
    private int lastMinute=0;
    private TimePicker picker=null;

    public static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView() {
        picker=new TimePicker(getContext());

        return(picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour=picker.getCurrentHour();
            lastMinute=picker.getCurrentMinute();

            String time = getTimeString();
            setSummary(getContext().getString(R.string.prefs_title_word_of_day_time_summary)+"\nCurrent Time: "+getTimeString());

            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @NonNull
    private String getTimeString() {
        String hour = Integer.toString(lastHour);
        if (lastHour < 10) {
            hour = "0" + hour;
        }
        String minute = Integer.toString(lastMinute);
        if (lastMinute < 10) {
            minute = "0" + minute;
        }
        return hour+":"+minute;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time=null;

        if (restoreValue) {
            if (defaultValue==null) {
                time=getPersistedString("00:00");
            }
            else {
                time=getPersistedString(defaultValue.toString());
            }
        }
        else {
            time=defaultValue.toString();
        }

        lastHour=getHour(time);
        lastMinute=getMinute(time);
        setSummary(getContext().getString(R.string.prefs_title_word_of_day_time_summary)+"\nCurrent Time: "+getTimeString());
    }
}
