package de.isah.vocabtrainer;

import de.isah.vocabtrainer.dictionary.word.WordPrefix;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 30.04.2018.
 */
public class AndroidSpinnerItemWordPrefixMapperTest {

    @Test
    public void testSpinnerItemToWordPrefixNone() {
        WordPrefix prefix = AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix("none");
        assertEquals(WordPrefix.NONE.string, prefix.string);
    }

    @Test
    public void testSpinnerItemToWordPrefixAtt() {
        WordPrefix prefix = AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix("att");
        assertEquals(WordPrefix.ATT.string, prefix.string);
    }

    @Test
    public void testSpinnerItemToWordPrefixEtt() {
        WordPrefix prefix = AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix("ett");
        assertEquals(WordPrefix.ETT.string, prefix.string);
    }

    @Test
    public void testSpinnerItemToWordPrefixEn() {
        WordPrefix prefix = AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix("en");
        assertEquals(WordPrefix.EN.string, prefix.string);
    }

    @Test
    public void testSpinnerItemToWordPrefixDefault() {
        WordPrefix prefix = AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix("default");
        assertEquals(WordPrefix.NONE.string, prefix.string);
    }

    @Test
    public void testWordPrefixToSpinnerItemNone() {
        int item = AndroidSpinnerItemWordPrefixMapper.mapWordPrefixToSpinnerItem(WordPrefix.NONE);
        assertEquals(1, item);
    }

    @Test
    public void testWordPrefixToSpinnerItemAtt() {
        int item = AndroidSpinnerItemWordPrefixMapper.mapWordPrefixToSpinnerItem(WordPrefix.ATT);
        assertEquals(2, item);
    }

    @Test
    public void testWordPrefixToSpinnerItemEtt() {
        int item = AndroidSpinnerItemWordPrefixMapper.mapWordPrefixToSpinnerItem(WordPrefix.ETT);
        assertEquals(4, item);
    }

    @Test
    public void testWordPrefixToSpinnerItemEn() {
        int item = AndroidSpinnerItemWordPrefixMapper.mapWordPrefixToSpinnerItem(WordPrefix.EN);
        assertEquals(3, item);
    }

}