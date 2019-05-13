package de.isah.vocabtrainer;

import android.support.annotation.NonNull;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;

public class DebugInfoHandler {

    @NonNull
    public String getDebugInfo(Dictionary dictionary, String appName, String appVersion) {
        return "---DEBUG---"
                + "\n" +"\n"
                + "App Name: "
                + appName
                + "\n"
                + "App Version: "
                + appVersion
                + "\n" +"\n"
                + "Persistence type: "
                + dictionary.getPersistenceType()
                + "\n" +"\n"
                + "Number of words in dictionary: "
                + dictionary.getNWordsInDict()
                + "\n"
                +"Number of new words: "
                + dictionary.getNWordsNew()
                +"\n"
                + "Number of words in toLearn List: "
                + dictionary.getToLearnList().size()
                + "\n"
                + "Number of words in incomplete List: "
                + dictionary.getIncompleteList().size()
                + "\n" + "\n"
                + "File Path: "
                + FileConstants.getFilePath()
                + "\n"
                + "External File Path: "
                + FileConstants.getExternalFilePath()
                + "\n";
    }
}
