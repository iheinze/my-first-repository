package de.isah.vocabtrainer.dictionary.word;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordState;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;
import de.isah.vocabtrainer.dictionary.word.state.WordStateFactory;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateInitial;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

/**
 * @author isa.heinze
 */
public class Word implements Serializable {

    private String key;
    private String swedish;
    private String[] german;
    private String[] grammar;
    private String remark;
    private WordPrefix prefix;

    private WordState state;

    public Word() {
        this.state = new WordStateInitial();
    }

    public Word(String serializedString) {
        this.state = new WordStateInitial();

        String[] wordStrings = serializedString.split(";");
        boolean isValid = validateStrings(wordStrings[0], wordStrings[1], wordStrings[2]);
        if (!isValid) {
            throw new IllegalArgumentException("word contains illegal characters: " + serializedString);
        }

        if (wordStrings.length == FileConstants.WORD_STRINGARRAY_LENGTH) {
            this.prefix = mapPrefix(wordStrings[0]);
            this.swedish = wordStrings[1];
            this.key = createKey(this.swedish, this.prefix);
            this.german = wordStrings[2].split(",");
            this.grammar = wordStrings[3].split(",");
            this.remark = wordStrings[4];

            WordStateFactory factory = new WordStateFactory();
            this.state = factory.create(wordStrings[5]);
        } else {
            throw new IllegalArgumentException("word has wrong format: --" + serializedString+"--");
        }
    }

    private WordPrefix mapPrefix(String wordString) {
        switch (wordString) {
            case "en":
                return WordPrefix.EN;
            case "ett":
                return WordPrefix.ETT;
            case "att":
                return WordPrefix.ATT;
            case "none":
                return WordPrefix.NONE;
            default:
                return WordPrefix.NONE;
        }
    }

    void setGerman(String... german) {
        if (!validateStrings(german)) {
            throw new IllegalArgumentException();
        }
        this.german = german;
    }

    void setSwedish(String swedish, WordPrefix prefix) {
        if (!validateStrings(swedish)) {
            throw new IllegalArgumentException();
        }
        this.swedish = swedish.trim();
        this.prefix = prefix;
        this.key = createKey(this.swedish, this.prefix);
    }

    public String getSwedish() {
        return this.swedish.trim().replaceFirst(prefix.start, "");
    }

    public WordPrefix getPrefix() {
        return this.prefix;
    }

    void setGrammar(String... grammar) {
        if (!validateStrings(grammar)) {
            throw new IllegalArgumentException();
        }
        this.grammar = grammar;
    }

    void setRemark(String remark) {
        this.remark = remark;
    }

    public String printSwedishAndGrammar() {

        StringBuilder builder = new StringBuilder();
        builder.append(prefix.replace);
        builder.append(this.swedish.trim());

        if (grammar != null && grammar.length > 0) {
            for (String grammarPart : grammar){
                builder.append("\n");
                if (!WordPrefix.NONE.equals(this.prefix)) {
                    builder.append("\t");
                }
                builder.append(grammarPart);
            }
        }

        return builder.toString().trim();
    }

    public String printGrammar() {
        StringBuilder builder = new StringBuilder();
        if (grammar != null && grammar.length > 0) {
            builder.append(grammar[0]);
            for (int i = 1; i < grammar.length; i++) {
                builder.append(", ").append(grammar[i]);
            }
        }
        return builder.toString().trim();
    }

    public String printGerman() {

        StringBuilder builder = new StringBuilder();

        if (german == null || german.length == 0) {
            builder.append("null");
        } else {
            builder.append(german[0]);
            for (int i = 1; i < german.length; i++) {
                builder.append(", ").append(german[i]);
            }
        }

        return builder.toString();
    }

    public String printRemark() {
        StringBuilder builder = new StringBuilder();
        if (remark != null) {
            builder.append(remark);
        }
        return builder.toString();
    }

    public String printLists() {
        return this.state.getWordListsNames();
    }

    public String printWholeWord() {
        StringBuilder builder = new StringBuilder();
        builder.append(printSwedishAndGrammar());
        builder.append("\n\n");
        builder.append(printGerman());
        builder.append("\n\n");
        builder.append(printRemark());
        builder.append("\n\n");
        builder.append(printLists());
        return builder.toString();
    }

    public void setState(WordState newState) throws IllegalStateTransitionException {
        if (stateTransitionIsValid(this.state, newState)) {
            this.state = newState;
        } else {
            String newClassName = null;
            if (newState != null) {
                newClassName = newState.getClass().getName();
            }
            throw new IllegalStateTransitionException("The transition from " + this.state.getClass().getName() + " to " + newClassName + " is not allowed");
        }
    }

    public void setCorrectStateSG() throws IllegalStateTransitionException {
        WordState newState;
        if(this.state instanceof WordStateGSCorrect){
            newState = new WordStateCorrect();
        } else {
            newState = new WordStateSGCorrect();
        }
        this.setState(newState);
    }

    public void setCorrectStateGS() throws IllegalStateTransitionException {
        WordState newState;
        if(this.state instanceof WordStateSGCorrect){
            newState = new WordStateCorrect();
        } else {
            newState = new WordStateGSCorrect();
        }
        this.setState(newState);
    }

    private boolean stateTransitionIsValid(WordState oldState, WordState newState) {

        // setting to Incomplete is always possible
        if (newState instanceof WordStateIncomplete) {
            return true;
        }

        if (newState == null) {
            return false;
        }

        if (oldState.getClass().equals(newState.getClass())) {
            return true;
        }

        if (oldState instanceof WordStateInitial && newState instanceof WordStateNew) {
            return true;
        }
        if (oldState instanceof WordStateInitial && newState instanceof WordStateDictionary) {
            return true;
        }
        if (oldState instanceof WordStateIncomplete && newState instanceof WordStateNew) {
            return true;
        }
        if (oldState instanceof WordStateNew && newState instanceof WordStateLearn) {
            return true;
        }
        if (oldState instanceof WordStateNew && newState instanceof WordStateDictionary) {
            return true;
        }
        if (oldState instanceof WordStateLearn && newState instanceof WordStateGSCorrect) {
            return true;
        }
        if (oldState instanceof WordStateLearn && newState instanceof WordStateSGCorrect) {
            return true;
        }
        if (oldState instanceof WordStateGSCorrect && newState instanceof WordStateCorrect) {
            return true;
        }
        if (oldState instanceof WordStateGSCorrect && newState instanceof WordStateLearn) {
            return true;
        }
        if (oldState instanceof WordStateSGCorrect && newState instanceof WordStateCorrect) {
            return true;
        }
        if (oldState instanceof WordStateSGCorrect && newState instanceof WordStateLearn) {
            return true;
        }
        if (oldState instanceof WordStateCorrect && newState instanceof WordStateLearn) {
            return true;
        }
        if (oldState instanceof WordStateCorrect && newState instanceof WordStateDictionary) {
            return true;
        }
        if (oldState instanceof WordStateDictionary && newState instanceof WordStateLearn) {
            return true;
        }
        if (oldState instanceof WordStateDictionary && newState instanceof WordStateNew) {
            return true;
        }

        return false;
    }

    public WordState getState() {
        return this.state;
    }

    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.prefix.string).append(";");
        builder.append(this.swedish).append(";");

        builder.append(german[0]);
        for (int i = 1; i < german.length; i++) {
            builder.append(",").append(german[i]);
        }

        builder.append(";");

        if (grammar != null && grammar.length > 0) {
            builder.append(grammar[0]);
            for (int i = 1; i < grammar.length; i++) {
                builder.append(",").append(grammar[i]);
            }
        } else {
            builder.append("");
        }

        builder.append(";");

        if (remark != null && !"".equals(remark)) {
            builder.append(remark);
        } else {
            builder.append("");
        }

        builder.append(";");

        builder.append(this.state.getName());

        builder.append("--");

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.key).append(" = ");
        builder.append(german[0]);
        for (int i = 1; i < german.length; i++) {
            builder.append(",").append(german[i]);
        }
        builder.append(" ");
        builder.append(this.state.getWordListSymbol());
        return builder.toString();
    }

    public String getKey(){
        return this.key;
    }

    private String createKey(String swedish, WordPrefix p) {
        return swedish.trim() + p.end;
    }

    private boolean validateStrings(String... strings) {

        for (String s : strings) {
            // allowed are letters, numbers, comma, fullstop
            if (!s.matches("[A-ZÅÄÖÜa-zåäöüß0-9\\-/\\s,.()]*")) {
                return false;
            }
        }
        return true;
    }

    public class WordComparator implements Comparator<Word> {
        Locale localeSE = Locale.forLanguageTag("sv-SE");
        @Override
        public int compare(Word w1, Word w2) {
            Collator collator = Collator.getInstance(localeSE);
            collator.setStrength(Collator.PRIMARY);
            return collator.compare(w1.toString(), w2.toString());
        }
    }
}
