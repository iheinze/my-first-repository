package de.isah.vocabtrainer.dictionary.word;

import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WordUnitTest {

    @Test
    public void testNormalChar() {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        assertEquals("pojke", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testSpecialChar() {
        Word word = new Word();
        word.setGerman("äöüßÄÖÜ");
        word.setSwedish("æøäöåÆØÄÖÅ", WordPrefix.NONE);

        assertEquals("æøäöåÆØÄÖÅ", word.printSwedishAndGrammar());
        assertEquals("äöüßÄÖÜ", word.printGerman());
    }

    @Test
    public void testIsCorrect() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        word.setState(new WordStateNew());
        word.setState(new WordStateLearn());
        word.setState(new WordStateSGCorrect());
        word.setState(new WordStateCorrect());
        assertTrue(word.getState() instanceof WordStateCorrect);
    }

    @Test
    public void testIsOnList1() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        word.setState(new WordStateNew());
        assertTrue(word.getState() instanceof WordStateNew);
        assertFalse(word.getState() instanceof WordStateLearn);
    }

    @Test
    public void testIsOnList2() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        word.setState(new WordStateNew());
        word.setState(new WordStateLearn());
        assertFalse(word.getState() instanceof WordStateNew);
        assertTrue(word.getState() instanceof WordStateLearn);
    }

    @Test
    public void testTwoGerman() {
        Word word = new Word();
        word.setGerman("german1", "german2");

        assertEquals("german1, german2", word.printGerman());
    }

    @Test
    public void testGermanNull() {
        Word word = new Word();
        assertEquals("null", word.printGerman());
    }

    @Test(expected = NullPointerException.class)
    public void testSwedishNull() {
        Word word = new Word();
        assertEquals("null", word.printSwedishAndGrammar());
    }

    @Test
    public void testGrammar() {
        Word word = new Word();
        word.setSwedish("word", WordPrefix.NONE);
        word.setGrammar("grammar1");

        assertEquals("word\ngrammar1", word.printSwedishAndGrammar());
    }

    @Test
    public void testTwoGrammar() {
        Word word = new Word();
        word.setSwedish("word", WordPrefix.NONE);
        word.setGrammar("grammar1", "grammar2");

        assertEquals("word\ngrammar1\ngrammar2", word.printSwedishAndGrammar());
    }

    @Test
    public void testRemark() {
        Word word = new Word();
        word.setSwedish("word", WordPrefix.NONE);
        word.setRemark("test remark");

        assertEquals("test remark", word.printRemark());
    }

    @Test
    public void testSerializeFullWord() throws JSONException {
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("{\"swedish\":\"swedish\",\"german\":[\"german1\",\"german2\"],\"grammar\":[\"grammar1\",\"grammar2\"],\"prefix\":\"none\",\"remark\":\"remark\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializePartialWord() throws JSONException {
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1");

        assertEquals("{\"swedish\":\"swedish\",\"german\":[\"german1\"],\"prefix\":\"none\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testDeserializeFullWord() throws JSONException {
        Word word = new Word("{\"swedish\":\"swedish\",\"german\":[\"german1\",\"german2\"],\"grammar\":[\"grammar1\",\"grammar2\"],\"prefix\":\"none\",\"remark\":\"remark\",\"state\":\"WordStateLearn\"}");
        assertEquals("{\"swedish\":\"swedish\",\"german\":[\"german1\",\"german2\"],\"grammar\":[\"grammar1\",\"grammar2\"],\"prefix\":\"none\",\"remark\":\"remark\",\"state\":\"WordStateLearn\"}--",word.serialize());
        assertTrue(word.getState() instanceof WordStateLearn);
    }

    @Test
    public void testDeserializePartialWord() throws JSONException {
        Word word = new Word("{\"swedish\":\"swedish\",\"german\":[\"german1\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}");
        assertEquals("{\"swedish\":\"swedish\",\"german\":[\"german1\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--",word.serialize());
    }

    @Test
    public void testTabsEn() {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.EN);
        word.setGrammar("pojken","pojkar","pojkarna");

        assertEquals("(en)\n\tpojke\n\tpojken\n\tpojkar\n\tpojkarna", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testTabsEtt() {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.ETT);
        word.setGrammar("pojken","pojkar","pojkarna");

        assertEquals("(ett)\n\tpojke\n\tpojken\n\tpojkar\n\tpojkarna", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testTabsAtt() {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.ATT);
        word.setGrammar("pojken","pojkar","pojkarna");

        assertEquals("(att)\n\tpojke\n\tpojken\n\tpojkar\n\tpojkarna", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testToString(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testPrintWholeWord() throws IllegalStateTransitionException{
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");
        word.setState(new WordStateDictionary());

        assertEquals("swedish\ngrammar1\ngrammar2\n\ngerman1, german2\n\nremark\n\ndictionary list",word.printWholeWord());
    }

    @Test
    public void testToStringEn(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.EN);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish, en = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testToStringEtt(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.ETT);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish, ett = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testToStringAtt(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.ATT);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish, att = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testSwedishWithPrefix() {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.EN);

        assertEquals("(en)\n\tpojke", word.printSwedishAndGrammar());
    }

    @Test
    public void testComparatorOne(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("aaa", WordPrefix.NONE);
        w1.setGerman("aaa");
        Word w2 = new Word();
        w2.setSwedish("bbb", WordPrefix.NONE);
        w2.setGerman("bbb");
        int result = comparator.compare(w1,w2);
        assertTrue(result < 0);
    }

    @Test
    public void testComparatorTwo(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("eee", WordPrefix.NONE);
        w1.setGerman("eee");
        Word w2 = new Word();
        w2.setSwedish("aaa", WordPrefix.NONE);
        w2.setGerman("aaa");
        int result = comparator.compare(w1,w2);
        assertTrue(result > 0);
    }

    @Test
    public void testComparatorThree(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("xxx", WordPrefix.NONE);
        w1.setGerman("xxx");
        Word w2 = new Word();
        w2.setSwedish("xxx", WordPrefix.NONE);
        w2.setGerman("xxx");
        int result = comparator.compare(w1,w2);
        assertEquals(0, result);
    }

    @Test
    public void testComparatorFour(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("ååå", WordPrefix.NONE);
        w1.setGerman("aaa");
        Word w2 = new Word();
        w2.setSwedish("äää", WordPrefix.NONE);
        w2.setGerman("bbb");
        int result = comparator.compare(w1,w2);
        assertTrue(result < 0);
    }

    @Test
    public void testValidateValid() {
        Word word = new Word();
        word.setRemark("The chars -()/., are allowed");
        // no exception is expected, no assert needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateGerman() {
        Word word = new Word();
        word.setGerman("&<>$%§");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateSwedisch1() {
        Word word = new Word();
        word.setSwedish("&<>$%§", WordPrefix.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateSwedisch2() {
        Word word = new Word();
        word.setSwedish("&<>$%§", WordPrefix.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateGrammar() {
        Word word = new Word();
        word.setGrammar("&<>$%§");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateConstructor(){
        new Word("&;<,>;$,%;$remark remark;;");
    }

    @Test
    public void testSetStateInitialToNew() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateInitialInvalid() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        assertTrue(w.getState() instanceof WordStateDictionary);
    }

    @Test
    public void testSetStateIncompleteToNew() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateIncomplete());
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateNewToNew()throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateNewToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateLearnToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateLearnToCorrectGS() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        assertTrue(w.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testSetStateLearnToCorrectSG() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        assertTrue(w.getState() instanceof WordStateSGCorrect);
    }

    @Test
    public void testSetStateCorrectGSToCorrect() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateCorrect());
        assertTrue(w.getState() instanceof WordStateCorrect);
    }

    @Test
    public void testSetStateCorrectGSToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateCorrectGSToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateCorrectSGToCorrect() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateCorrect());
        assertTrue(w.getState() instanceof WordStateCorrect);
    }

    @Test
    public void testSetStateCorrectSGToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateCorrectSGToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateCorrectToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateCorrect());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateCorrectToDictionary() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateCorrect());
        w.setState(new WordStateDictionary());
        assertTrue(w.getState() instanceof WordStateDictionary);
    }

    @Test
    public void testSetStateCorrectToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateCorrect());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateDictionaryToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateDictionaryToNew() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateDictionaryToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testJsonFull() throws JSONException {

        JSONArray grammar = new JSONArray();
        grammar.put("pojken");
        grammar.put("pojkar");
        grammar.put("pojkarna");

        JSONArray german = new JSONArray();
        german.put("Junge");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "en");
        wordJson.put("swedish", "pojke");
        wordJson.put("german", german);
        wordJson.put("grammar", grammar);
        wordJson.put("remark", "test");
        wordJson.put("state", "WordStateDictionary");

        Word word = new Word(wordJson);
        assertEquals(WordPrefix.EN, word.getPrefix());
        assertEquals("pojke", word.getSwedish());
        assertEquals("WordStateDictionary", word.getState().getName());
        assertEquals("Junge", word.printGerman());
        assertEquals("pojken, pojkar, pojkarna", word.printGrammar());
        assertEquals("test", word.printRemark());

    }

    @Test
    public void testJsonMin() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("Junge");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "en");
        wordJson.put("swedish", "pojke");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals(WordPrefix.EN, word.getPrefix());
        assertEquals("pojke", word.getSwedish());
        assertEquals("WordStateInitial", word.getState().getName());
        assertEquals("Junge", word.printGerman());
        assertEquals("", word.printGrammar());
        assertEquals("", word.printRemark());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonSwedishMissing() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("Junge");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "en");
        wordJson.put("german", german);

        new Word(wordJson);
    }

    @Test
    public void testSerializeToJsonFull() throws JSONException {
        JSONArray grammar = new JSONArray();
        grammar.put("pojken");
        grammar.put("pojkar");
        grammar.put("pojkarna");

        JSONArray german = new JSONArray();
        german.put("Junge");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "en");
        wordJson.put("swedish", "pojke");
        wordJson.put("german", german);
        wordJson.put("grammar", grammar);
        wordJson.put("remark", "test");
        wordJson.put("state", "WordStateDictionary");

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"pojke\",\"german\":[\"Junge\"],\"grammar\":[\"pojken\",\"pojkar\",\"pojkarna\"],\"prefix\":\"en\",\"remark\":\"test\",\"state\":\"WordStateDictionary\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonMin() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("Junge");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "en");
        wordJson.put("swedish", "pojke");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"pojke\",\"german\":[\"Junge\"],\"prefix\":\"en\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonMin2() throws JSONException {
        JSONObject wordJson = new JSONObject("{\"swedish\":\"och\",\"german\":[\"und\"],\"prefix\":\"none\",\"state\":\"WordStateLearn\"}");

        Word word = new Word(wordJson);
        //assertEquals("{\"swedish\":\"och\",\"german\":[\"und\"],\"grammar\":[],\"prefix\":\"none\",\"remark\":\"\",\"state\":\"WordStateLearn\"}", word.serializeToJsonString());
        assertEquals("{\"swedish\":\"och\",\"german\":[\"und\"],\"prefix\":\"none\",\"state\":\"WordStateLearn\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonPrefixEtt() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("Apfel");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "ett");
        wordJson.put("swedish", "äpple");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"äpple\",\"german\":[\"Apfel\"],\"prefix\":\"ett\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonPrefixAtt() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("heißen");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "att");
        wordJson.put("swedish", "heta");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"heta\",\"german\":[\"heißen\"],\"prefix\":\"att\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonPrefixNone() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("mit");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "none");
        wordJson.put("swedish", "med");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonPrefixEmpty() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("mit");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "");
        wordJson.put("swedish", "med");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonPrefixOther() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("mit");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", "something");
        wordJson.put("swedish", "med");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonPrefixNull() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("mit");

        JSONObject wordJson = new JSONObject();
        wordJson.put("prefix", null);
        wordJson.put("swedish", "med");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test
    public void testSerializeToJsonPrefixNotSet() throws JSONException {

        JSONArray german = new JSONArray();
        german.put("mit");

        JSONObject wordJson = new JSONObject();
        wordJson.put("swedish", "med");
        wordJson.put("german", german);

        Word word = new Word(wordJson);
        assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateInitial\"}--", word.serializeToJsonString());
    }

    @Test(expected = IllegalStateTransitionException.class)
    public void setStateNull() throws IllegalStateTransitionException {
        Word word = new Word();
        word.setState(null);
    }

    @Test(expected = IllegalStateTransitionException.class)
    public void setStateWrongTransition() throws IllegalStateTransitionException {
        Word word = new Word();
        word.setState(new WordStateLearn());
    }

    @Test
    public void setStateGSCorrect1() throws IllegalStateTransitionException {
        Word word = new Word();
        word.setState(new WordStateDictionary());
        word.setState(new WordStateLearn());
        word.setCorrectStateGS();

        assertTrue(word.getState() instanceof  WordStateGSCorrect);
    }

    @Test
    public void setStateGSCorrect2() throws IllegalStateTransitionException {
        Word word = new Word();
        word.setState(new WordStateDictionary());
        word.setState(new WordStateLearn());
        word.setState(new WordStateSGCorrect());
        word.setCorrectStateGS();

        assertTrue(word.getState() instanceof  WordStateCorrect);
    }

    @Test
    public void setStateSGCorrect1() throws IllegalStateTransitionException {
        Word word = new Word();
        word.setState(new WordStateDictionary());
        word.setState(new WordStateLearn());
        word.setCorrectStateSG();

        assertTrue(word.getState() instanceof  WordStateSGCorrect);
    }

    @Test
    public void setStateSGCorrect2() throws IllegalStateTransitionException {
        Word word = new Word();
        word.setState(new WordStateDictionary());
        word.setState(new WordStateLearn());
        word.setState(new WordStateGSCorrect());
        word.setCorrectStateSG();

        assertTrue(word.getState() instanceof  WordStateCorrect);
    }
}