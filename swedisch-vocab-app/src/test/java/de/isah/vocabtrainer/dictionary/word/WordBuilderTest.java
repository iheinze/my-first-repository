package de.isah.vocabtrainer.dictionary.word;

import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 21.12.2017.
 */
public class WordBuilderTest {

    @Test
    public void testAll() throws IllegalStateTransitionException{
        WordBuilder builder = new WordBuilder();
        Word w = builder.addSwedish("swedish", WordPrefix.NONE).addGerman("german1","german2").addGrammar("grammar1","grammar2","grammar3").addRemark("remark").build();
        w.setState(new WordStateDictionary());

        assertEquals("swedish\ngrammar1\ngrammar2\ngrammar3\n\ngerman1, german2\n\nremark\n\ndictionary list",w.printWholeWord());
    }

    @Test
    public void testAllTrim() throws IllegalStateTransitionException{
        WordBuilder builder = new WordBuilder();
        Word w = builder.addSwedish(" swedish ", WordPrefix.NONE).addGerman(" german1 "," german2 ").addGrammar(" grammar1 "," grammar2 "," grammar3 ").addRemark(" remark ").build();
        w.setState(new WordStateDictionary());

        assertEquals("swedish\ngrammar1\ngrammar2\ngrammar3\n\ngerman1, german2\n\nremark\n\ndictionary list",w.printWholeWord());
    }

    @Test
    public void testAllTrim2() throws IllegalStateTransitionException{
        WordBuilder builder = new WordBuilder();
        Word w = builder.addSwedish("  swedish  ", WordPrefix.NONE).addGerman("  german1  ","  german2  ").addGrammar("  grammar1  ","  grammar2  ","  grammar3  ").addRemark("  remark  ").build();
        w.setState(new WordStateDictionary());

        assertEquals("swedish\ngrammar1\ngrammar2\ngrammar3\n\ngerman1, german2\n\nremark\n\ndictionary list",w.printWholeWord());
    }

    @Test
    public void testAllTrim3() throws IllegalStateTransitionException{
        WordBuilder builder = new WordBuilder();
        Word w = builder.addSwedish("\tswedish\t", WordPrefix.NONE).addGerman("\tgerman1\t","\tgerman2\t").addGrammar("\tgrammar1\t","\tgrammar2\t","\tgrammar3\t").addRemark("\tremark\t").build();
        w.setState(new WordStateDictionary());

        assertEquals("swedish\ngrammar1\ngrammar2\ngrammar3\n\ngerman1, german2\n\nremark\n\ndictionary list",w.printWholeWord());
    }

    @Test
    public void testAllTrim4() throws IllegalStateTransitionException{
        WordBuilder builder = new WordBuilder();
        Word w = builder.addSwedish("\nswedish\n", WordPrefix.NONE).addGerman("\ngerman1\n","\ngerman2\n").addGrammar("\ngrammar1\n","\ngrammar2\n","\ngrammar3\n").addRemark("\nremark\n").build();
        w.setState(new WordStateDictionary());

        assertEquals("swedish\ngrammar1\ngrammar2\ngrammar3\n\ngerman1, german2\n\nremark\n\ndictionary list",w.printWholeWord());
    }

    @Test
    public void testPrefix(){
        WordBuilder builder = new WordBuilder();
        Word w = builder.addSwedish("swedish", WordPrefix.ETT).addGerman("german1","german2").build();

        assertEquals("(ett)\n\tswedish",w.printSwedishAndGrammar());
    }

    @Test
    public void testModifyAll() throws IllegalStateTransitionException{
        WordBuilder builder = new WordBuilder();
        Word w = builder.addSwedish("swedish", WordPrefix.NONE).addGerman("german1","german2").addGrammar("grammar1","grammar2","grammar3").addRemark("remark").build();
        w.setState(new WordStateDictionary());

        WordBuilder modifyBuilder = new WordBuilder(w);
        Word modifiedWord = modifyBuilder.addSwedish("mswedish", WordPrefix.NONE).addGerman("mgerman1","mgerman2").addGrammar("mgrammar1","mgrammar2","mgrammar3").addRemark("mremark").build();

        assertEquals("mswedish\nmgrammar1\nmgrammar2\nmgrammar3\n\nmgerman1, mgerman2\n\nmremark\n\ndictionary list",modifiedWord.printWholeWord());
    }
}