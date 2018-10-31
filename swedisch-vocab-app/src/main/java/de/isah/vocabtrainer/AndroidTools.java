package de.isah.vocabtrainer;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import org.apache.commons.lang3.StringUtils;

class AndroidTools {

    private AndroidTools(){}

    static Point getDisplayDimensions(WindowManager wManager){
        Display display = wManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    static String addEmptyLines (String input){
        int newLineCounter = StringUtils.countMatches(input, "\n");
        StringBuilder builder = new StringBuilder(input);
        for(int i = newLineCounter; i<4; i++){
            builder.append("\n ");
        }
        return builder.toString();
    }
}
