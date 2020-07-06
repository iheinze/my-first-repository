package de.isah.vocabtrainer.dictionary.persist;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;

import de.isah.vocabtrainer.dictionary.LearnWordList;
import de.isah.vocabtrainer.dictionary.WordList;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

/**
 * A persistence type which takes user defined words only. It saves them in JSON format in a file which can be exported.
 *
 * @author isa.heinze
 */
public class UserFilePersistenceJson implements Persistence {

    private File file;
    private String filename;

    private WordList allWords;
    private LearnWordList toLearnWords;
    private Queue<Word> newWords;
    private WordList incompleteList;

    UserFilePersistenceJson(String filename) throws IOException {
        this.allWords = new WordList();
        this.toLearnWords = new LearnWordList();
        this.newWords = new LinkedList<>();
        this.incompleteList = new WordList();
        this.filename = filename;
        init();
        getWords(this.file);
    }

    private void init() throws IOException {
        if (FileConstants.getFilePath() != null) {
            this.file = new File(FileConstants.getFilePath() + "/"+this.filename);
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
        }
    }

    private void getWords(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                String[] splitLine = line.split("--");
                //for (int i = 0; i < splitLine.length; i++) {
                for (String splitPart : splitLine) {
                    //verify that the word has proper structure

                    // check for comment line
                    if (splitPart.startsWith("#")) {
                        continue;
                    }

                    JSONObject wordJson = new JSONObject(splitPart);
                    Word word = new Word(wordJson);

                    this.allWords.addWord(word);
                    if (word.getState() instanceof WordStateNew) {
                        this.newWords.add(word);
                    }
                    if (word.getState() instanceof WordStateIncomplete) {
                        this.incompleteList.addWord(word);
                    }
                    if (word.getState() instanceof WordStateLearn) {
                        this.toLearnWords.addWord(word);
                    }
                    if (word.getState() instanceof WordStateSGCorrect) {
                        this.toLearnWords.addWord(word);
                    }
                    if (word.getState() instanceof WordStateGSCorrect) {
                        this.toLearnWords.addWord(word);
                    }
                    if (word.getState() instanceof WordStateCorrect) {
                        this.toLearnWords.addWord(word);
                    }

                }
            } catch (WordAlreadyExistsException e){
                // do nothing
            } catch (JSONException e) {
                // do nothing
            }
        }
    }

    @Override
    public WordList getAllWords() {
        return this.allWords;
    }

    @Override
    public Queue<Word> getNewWords() {
        return this.newWords;
    }

    @Override
    public LearnWordList getToLearnWords() {
        return this.toLearnWords;
    }

    @Override
    public WordList getIncompleteList() {
        return this.incompleteList;
    }

    @Override
    public boolean addWord(Word w) {
        boolean added;
        try {
            appendToFile(w.serializeToJsonString(), this.file);
            added = true;
        } catch (IOException e) {
            // just do not write anything
            e.printStackTrace();
            added = false;
        } catch (JSONException e) {
            // just do not write anything
            e.printStackTrace();
            added = false;
        }
        return added;
    }

    @Override
    public boolean persistAll(WordList list) {
        boolean returnValue;
        try {
            // delete current content from file
            PrintWriter writer = new PrintWriter(this.file);
            writer.print("");
            writer.close();

            for (int i = 0; i < list.size(); i++) {
                appendToFile(list.getWord(i).serializeToJsonString(), this.file);
            }

            returnValue = true;
        } catch (IOException e) {
            // just do not write anything
            e.printStackTrace();
            returnValue = false;
        } catch (JSONException e) {
            // just do not write anything
            e.printStackTrace();
            returnValue = false;
        }
        return returnValue;
    }

    private void appendToFile(String line, File f) throws IOException {
        Writer writer = new BufferedWriter(new FileWriter(f, true));
        writer.append(line).append("\n");
        writer.close();
    }

    @Override
    public boolean exportAll(WordList list) {
        File exportFile = new File(FileConstants.getExternalFilePath() + "/dictionary-export-json.txt");
        boolean returnValue;
        try {
            exportFile.createNewFile();
            // delete current content from file
            if (exportFile.exists()) {
                PrintWriter writer = new PrintWriter(exportFile);
                writer.print("");
                writer.close();
            }

            for (int i = 0; i < list.size(); i++) {
                appendToFile(list.getWord(i).serializeToJsonString(), exportFile);
            }
            returnValue = true;
        } catch (IOException e) {
            // just do not write anything
            e.printStackTrace();
            returnValue = false;
        } catch (JSONException e) {
            // just do not write anything
            e.printStackTrace();
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean importFile() {
        File importFile = new File(FileConstants.getExternalFilePath() + "/dictionary-export-json.txt");

        if (!importFile.exists()) {
            return false;
        }

        boolean returnValue;

        try {
            // clear all word lists
            this.allWords = new WordList();
            this.toLearnWords = new LearnWordList();
            this.newWords = new LinkedList<>();
            this.incompleteList = new WordList();

            getWords(importFile);
            returnValue = persistAll(this.allWords);
        } catch (IOException e) {
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean disableAddWords() {
        return false;
    }

    @Override
    public boolean disableImportExport() {
        return false;
    }

    @Override
    public boolean disableDeleteWords() {
        return false;
    }

    @Override
    public boolean disableEditWords() {
        return false;
    }
}
