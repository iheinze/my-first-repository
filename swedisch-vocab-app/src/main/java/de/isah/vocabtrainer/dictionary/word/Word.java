package de.isah.vocabtrainer.dictionary.word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

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
    private String pronunciation;
    private WordSource source;
    private String[] labels;

    private WordState state;

    public Word() {
        this.state = new WordStateInitial();
    }

    public Word(String serializedString) {
        this.state = new WordStateInitial();

        try {
            JSONObject wordJson = new JSONObject(serializedString);
            init(wordJson);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("word has wrong format: " + serializedString);
        }
    }

    public Word(JSONObject jsonWord) {
        this.state = new WordStateInitial();

        try {
            init(jsonWord);
        } catch (JSONException e) {
            throw new IllegalArgumentException("word has wrong format: " + jsonWord.toString());
        }
    }

    private void init(JSONObject jsonWord) throws JSONException {
        if (jsonWord.has("prefix")) {
            this.prefix = mapPrefix(jsonWord.getString("prefix"));
        } else {
            this.prefix = mapPrefix("none");
        }

        this.swedish = jsonWord.getString("swedish");
        JSONArray germanJson = jsonWord.getJSONArray("german");
        this.german = germanJson.join(",").replaceAll("\"", "").split(",");
        if (jsonWord.has("grammar")) {
            JSONArray grammarJson = jsonWord.getJSONArray("grammar");
            this.grammar = grammarJson.join(",").replaceAll("\"", "").split(",");
        } else {
            this.grammar = new String[0];
        }
        this.key = createKey(this.swedish, this.prefix);
        if (jsonWord.has("remark")) {
            this.remark = jsonWord.getString("remark");
        } else {
            this.remark = "";
        }
        if (jsonWord.has("pronunciation")) {
            this.pronunciation = jsonWord.getString("pronunciation");
        } else {
            this.pronunciation = "";
        }
        if (jsonWord.has("labels")) {
            JSONArray labelJson = jsonWord.getJSONArray("labels");
            this.labels = labelJson.join(",").replaceAll("\"", "").split(",");
        } else {
            this.labels = new String[0];
        }
        if (jsonWord.has("source")) {
            try {
                this.source = WordSource.valueOf(jsonWord.getString("source"));
            } catch (IllegalArgumentException e) {
                this.source = WordSource.UNKNOWN;
            }
        } else {
            this.source = WordSource.UNKNOWN;
        }
        WordStateFactory factory = new WordStateFactory();
        if (jsonWord.has("state")) {
            this.state = factory.create(jsonWord.getString("state"));
        }
    }

    private WordPrefix mapPrefix(String wordString) {
        System.out.println("word prefix: " + wordString);
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

    void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getPronunciation() {
        return this.pronunciation;
    }

    void setSource(WordSource source) {
        this.source = source;
    }

    public WordSource getSource() {
        return this.source;
    }

    void setLabels(String... labels) {
        if (!validateStrings(labels)) {
            throw new IllegalArgumentException();
        }
        this.labels = labels;
    }

    public String printLabels() {
        StringBuilder builder = new StringBuilder();

        if (labels != null && labels.length > 0) {
            for (String l : this.labels) {
                builder.append(l);
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public String printSwedishAndGrammar() {

        StringBuilder builder = new StringBuilder();
        builder.append(prefix.replace);
        builder.append(this.swedish.trim());

        if (grammar != null && grammar.length > 0) {
            for (String grammarPart : grammar) {
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

    String printWholeWord() {
        return printSwedishAndGrammar() + "\n\n" + printGerman() + "\n\n" + printRemark() + "\n\n" + printLists();
    }

    public void setState(WordState newState) throws IllegalStateTransitionException {
        if (this.state == null || newState == null) {
            throw new IllegalStateTransitionException("At least one of the states was null.");
        }
        if (stateTransitionIsValid(this.state, newState)) {
            this.state = newState;
        } else {
            throw new IllegalStateTransitionException("The transition from " + this.state.getClass().getName() + " to " + newState.getClass().getName() + " is not allowed");
        }
    }

    public void setCorrectStateSG() throws IllegalStateTransitionException {
        WordState newState;
        if (this.state instanceof WordStateGSCorrect) {
            newState = new WordStateCorrect();
        } else {
            newState = new WordStateSGCorrect();
        }
        this.setState(newState);
    }

    public void setCorrectStateGS() throws IllegalStateTransitionException {
        WordState newState;
        if (this.state instanceof WordStateSGCorrect) {
            newState = new WordStateCorrect();
        } else {
            newState = new WordStateGSCorrect();
        }
        this.setState(newState);
    }

    private boolean stateTransitionIsValid(WordState oldState, WordState newState) {

        if (newState == null || oldState == null) {
            return false;
        }

        // setting to Incomplete is always possible
        if (newState instanceof WordStateIncomplete) {
            return true;
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

    public String serialize() throws JSONException {
        return serializeToJsonString();
    }

    public String serializeToJsonString() throws JSONException {
        JSONObject json = toJson();
        return json.toString() + "--";
    }

    private JSONObject toJson() throws JSONException {
        JSONArray german = new JSONArray();
        for (String s : this.german) {
            german.put(s);
        }

        JSONArray grammar = new JSONArray();
        if (this.grammar != null && this.grammar.length > 0) {
            for (String s : this.grammar) {
                grammar.put(s);
            }
        }

        JSONArray labels = new JSONArray();
        if (this.labels != null && this.labels.length > 0) {
            for (String l : this.labels) {
                labels.put(l);
            }
        }

        JSONObject json = new JSONObject();
        if (this.remark != null && this.remark.length() > 0) {
            json.put("remark", this.remark);
        }
        if (this.pronunciation != null && this.pronunciation.length() > 0) {
            json.put("pronunciation", this.pronunciation);
        }
        if (this.source != null) {
            json.put("source", this.source.toString());
        }
        json.put("swedish", this.swedish);
        json.put("state", this.state.getName());
        json.put("prefix", this.prefix.string);
        if (this.grammar != null && this.grammar.length > 0) {
            json.put("grammar", grammar);
        }
        json.put("german", german);
        if (this.labels != null && this.labels.length > 0) {
            json.put("labels", labels);
        }

        return json;
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

    public String toStringMinimal() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.key).append(" = ");
        builder.append(german[0]);
        for (int i = 1; i < german.length; i++) {
            builder.append(",").append(german[i]);
        }
        return builder.toString();
    }

    public String getKey() {
        return this.key;
    }

    private String createKey(String swedish, WordPrefix p) {
        return swedish.trim() + p.end;
    }

    private boolean validateStrings(String... strings) {

        for (String s : strings) {
            // allowed are letters, numbers, comma, fullstop
            if (!s.matches("[A-ZÅÄÆÖØÜa-zåäæöøüß0-9\\-/\\s,.()]*")) {
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
