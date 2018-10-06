package de.isah.vocabtrainer.dictionary.word;

/**
 * @author isa.heinze
 */

public class WordBuilder {

    private Word word;

    public WordBuilder(){
        word = new Word();
    }

    public WordBuilder(Word w){
        this.word = w;
    }

    public WordBuilder addSwedish(String swedish, WordPrefix prefix){
        word.setSwedish(swedish.trim(), prefix);
        return this;
    }

    public WordBuilder addGerman(String ... german){

        for(int i = 0; i< german.length; i++){
            german[i] = german[i].trim();
        }

        word.setGerman(german);
        return this;
    }

    public WordBuilder addGrammar(String ... grammar){

        for(int i = 0; i< grammar.length; i++){
            grammar[i] = grammar[i].trim();
        }

        word.setGrammar(grammar);
        return this;
    }

    public WordBuilder addRemark(String remark){
        word.setRemark(remark.trim());
        return this;
    }

    public Word build(){
        return this.word;
    }
}
