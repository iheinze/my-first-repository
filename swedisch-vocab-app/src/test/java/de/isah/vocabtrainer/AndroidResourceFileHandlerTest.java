package de.isah.vocabtrainer;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 14.04.2018.
 */
public class AndroidResourceFileHandlerTest {

    @Test
    @Ignore("This is not so easy to test, because android stuff is needed.")
    public void openIniDictionaryFile() throws Exception {

        AppCompatActivity testACA = new AppCompatActivity();
        AssetManager testAm = testACA.getAssets(); //  this line causes trouble

        AndroidResourceFileHandler arfh  = new AndroidResourceFileHandler();
        arfh.openIniDictionaryFile(testAm);

        assertNotNull(AndroidResourceFileHandler.getFileInStream());

        arfh.closeIniDictionary();

        assertNull(AndroidResourceFileHandler.getFileInStream());
    }

}