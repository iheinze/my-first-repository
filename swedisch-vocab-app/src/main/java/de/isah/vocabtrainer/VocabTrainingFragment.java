package de.isah.vocabtrainer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.LearnWordList;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;

/**
 * Created by isa.heinze on 22.06.2018.
 */

public class VocabTrainingFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private Dictionary dictionary;
    private LearnWordList toLearnList;

    public VocabTrainingFragment() {

    }

    public static VocabTrainingFragment newInstance(int sectionNumber) {
        VocabTrainingFragment fragment = new VocabTrainingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vocab_training, container, false);

        dictionary = DictionaryCache.getCachedDictionary();

        updateLearningSummary();

        Button resetButton = (Button) rootView.findViewById(R.id.buttonResetLearningProgress);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dictionary.resetProgress();
                updateLearningSummary();
                Snackbar.make(rootView, "Learning Progress was reset.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        Button updateButton = (Button) rootView.findViewById(R.id.buttonUpdateVocabList);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
                String prefStackSize = sharedPref.getString("pref_stack_size", "0");
                String summary = dictionary.createToLearnList(Integer.valueOf(prefStackSize));

                String updateVocabListMessage;
                if (dictionary.getToLearnList().size() > 0) {
                    updateVocabListMessage = "Learn list was updated.\n" + summary;
                } else {
                    updateVocabListMessage = "Learn list could not be updated.\n" + summary;
                }
                ShowWordsFragment.reloadWordList();
                updateLearningSummary();
                Snackbar.make(rootView, updateVocabListMessage, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });


        Button learnSGButton = rootView.findViewById(R.id.buttonTrainingSwedishGerman);
        learnSGButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // this shuffles the list also
                toLearnList = dictionary.getToLearnList();
                int size = toLearnList.size();
                if (size == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("Error");
                    builder.setMessage("No Words in toLearnList.\nUpdate toLearnList first.");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                // show first word
                showWord(size, 0, "sg");
            }
        });

        Button learnGSButton = rootView.findViewById(R.id.buttonTrainingGermanSwedish);
        learnGSButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // this shuffles the list also
                toLearnList = dictionary.getToLearnList();
                int size = toLearnList.size();
                if (size == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("Error");
                    builder.setMessage("No Words in toLearnList.\nUpdate toLearnList first.");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                // show first word
                showWord(size, 0, "gs");
            }
        });

        return rootView;
    }


    private void showWord(final int size, final int counter, String direction) {
        Word w = this.toLearnList.getWord(counter);
        String title = "Word " + Integer.valueOf(counter + 1) + "/" + size;

        // Show swedish
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setTitle(title);
        builder.setIcon(R.mipmap.ic_launcher);
        switch (direction) {
            case "sg":
                builder.setMessage(AndroidTools.addEmptyLines(w.printSwedishAndGrammar()));
                builder.setNeutralButton("Show German", new VocabTrainingFragment.OtherDirectionDialogInterfaceMethod(this.toLearnList, size, counter, direction, rootView.getContext()));
                break;
            case "gs":
                builder.setMessage(AndroidTools.addEmptyLines(w.printGerman()));
                builder.setNeutralButton("Show Swedish", new VocabTrainingFragment.OtherDirectionDialogInterfaceMethod(this.toLearnList, size, counter, direction, rootView.getContext()));
                break;
            default:
                break;
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private class OtherDirectionDialogInterfaceMethod implements DialogInterface.OnClickListener {

        private int size;
        private int counter;
        private String direction;
        private LearnWordList words;
        private Context context;

        OtherDirectionDialogInterfaceMethod(LearnWordList words, int size, int counter, String direction, @NonNull Context context) {
            this.words = words;
            this.size = size;
            this.counter = counter;
            this.direction = direction;
            this.context = context;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();

            // show german
            Word w = words.getWord(counter);
            String title = "Word " + Integer.valueOf(counter + 1) + "/" + size;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
            switch (direction) {
                case "sg":
                    builder.setMessage(AndroidTools.addEmptyLines(w.printGerman()));
                    break;
                case "gs":
                    builder.setMessage(AndroidTools.addEmptyLines(w.printSwedishAndGrammar()));
                    break;
                default:
                    break;
            }
            builder.setPositiveButton("Correct", new VocabTrainingFragment.CorrectDialogInterfaceMethod(words, size, counter, direction));
            builder.setNegativeButton("False", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    int newcounter = counter + 1;
                    if (newcounter < size) {
                        showWord(size, newcounter, direction);
                    } else {
                        showSummary();
                        updateLearningSummary();
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private class CorrectDialogInterfaceMethod implements DialogInterface.OnClickListener {

        private int size;
        private int counter;
        private String direction;
        private LearnWordList words;

        CorrectDialogInterfaceMethod(LearnWordList words, int size, int counter, String direction) {
            this.words = words;
            this.size = size;
            this.counter = counter;
            this.direction = direction;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            Word w = words.getWord(counter);

            switch (direction) {
                case "sg":
                    try {
                        w.setCorrectStateSG();
                    } catch (IllegalStateTransitionException e) {
                        // do nothing
                    } finally {
                        break;
                    }
                case "gs":
                    try {
                        w.setCorrectStateGS();
                    } catch (IllegalStateTransitionException e) {
                        // do nothing
                    } finally {
                        break;
                    }
                default:
                    break;
            }

            dialog.dismiss();

            int newcounter = counter + 1;
            if (newcounter < size) {
                showWord(size, newcounter, direction);
            } else {
                showSummary();
                updateLearningSummary();
            }
        }
    }

    private void showSummary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        if(this.toLearnList != null) {
            builder.setTitle("Summary");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(this.toLearnList.printResults());
        } else {
            builder.setTitle("Error");
            builder.setMessage("No Words in toLearnList.\nUpdate toLearnList first.");
        }
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void updateLearningSummary() {
        System.out.println("calling updateLearningSummary()");
        TextView learnSummaryTextView = (TextView) rootView.findViewById(R.id.textViewLearningSummary);
        if (dictionary.getToLearnList() != null && dictionary.getToLearnList().size() > 0) {
            learnSummaryTextView.setText(dictionary.getToLearnList().printResults());
        } else {
            learnSummaryTextView.setText("There are no words no the learn list.");
        }
    }
}
