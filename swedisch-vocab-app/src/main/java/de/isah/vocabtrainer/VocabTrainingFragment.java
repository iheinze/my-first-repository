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
import de.isah.vocabtrainer.dictionary.learn.LearnDirection;
import de.isah.vocabtrainer.dictionary.learn.VocabTrainingMessenger;

/**
 * Created by isa.heinze on 22.06.2018.
 */

public class VocabTrainingFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private Dictionary dictionary;
    private LearnWordList toLearnList;
    private VocabTrainingMessenger vocabTrainingMessenger;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vocab_training, container, false);

        dictionary = DictionaryCache.getCachedDictionary();

        vocabTrainingMessenger = new VocabTrainingMessenger();

        updateLearningSummary();

        Button resetButton = rootView.findViewById(R.id.buttonResetLearningProgress);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dictionary.resetProgress();
                updateLearningSummary();
                Snackbar.make(rootView, "Learning Progress was reset.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        Button updateButton = rootView.findViewById(R.id.buttonUpdateVocabList);
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
                if (vocabTrainingMessenger.giveMeLearnListLength() == 0) {
                    showEmptyLearnListAlertDialog();
                    return;
                }
                // show first word
                showWord(LearnDirection.SG);
            }
        });

        Button learnGSButton = rootView.findViewById(R.id.buttonTrainingGermanSwedish);
        learnGSButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // this shuffles the list also
                toLearnList = dictionary.getToLearnList();
                if (vocabTrainingMessenger.giveMeLearnListLength() == 0) {
                    showEmptyLearnListAlertDialog();
                    return;
                }
                // show first word
                showWord(LearnDirection.GS);
            }
        });

        return rootView;
    }

    private void showEmptyLearnListAlertDialog() {
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
    }


    private void showWord(LearnDirection direction) {
        try {
            this.vocabTrainingMessenger.giveMeNextWord();
            int newCount = this.vocabTrainingMessenger.giveMeCurrentCounter();
            String title = "Word " + newCount + "/" + this.vocabTrainingMessenger.giveMeLearnListLength();

            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(AndroidTools.addEmptyLines(this.vocabTrainingMessenger.giveMeFirstSide(direction)));
            builder.setNeutralButton(this.vocabTrainingMessenger.giveMeOtherSideButtonName(direction), new VocabTrainingFragment.OtherDirectionDialogInterfaceMethod(direction, rootView.getContext()));
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } catch (IndexOutOfBoundsException e) {
            // no words left on learn list
            showSummary();
            updateLearningSummary();
            vocabTrainingMessenger.reset();
        }
    }

    private class OtherDirectionDialogInterfaceMethod implements DialogInterface.OnClickListener {

        private LearnDirection direction;
        private Context context;

        OtherDirectionDialogInterfaceMethod(LearnDirection direction, @NonNull Context context) {
            this.direction = direction;
            this.context = context;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            int newCount = vocabTrainingMessenger.giveMeCurrentCounter();
            String title = "Word " + newCount + "/" + vocabTrainingMessenger.giveMeLearnListLength();
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(AndroidTools.addEmptyLines(vocabTrainingMessenger.giveMeSecondSide(this.direction)));
            builder.setPositiveButton("Correct", new VocabTrainingFragment.CorrectDialogInterfaceMethod(this.direction));
            builder.setNegativeButton("False", new VocabTrainingFragment.NotCorrectDialogInterfaceMethod(this.direction));
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private class CorrectDialogInterfaceMethod implements DialogInterface.OnClickListener {

        private LearnDirection direction;

        CorrectDialogInterfaceMethod(LearnDirection direction) {
            this.direction = direction;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            vocabTrainingMessenger.setCorrectState(direction);
            dialog.dismiss();
            showWord(direction);
        }
    }

    private class NotCorrectDialogInterfaceMethod implements DialogInterface.OnClickListener {

        private LearnDirection direction;

        NotCorrectDialogInterfaceMethod(LearnDirection direction) {
            this.direction = direction;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            showWord(direction);
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
        TextView learnSummaryTextView = rootView.findViewById(R.id.textViewLearningSummary);
        if (dictionary.getToLearnList() != null && dictionary.getToLearnList().size() > 0) {
            learnSummaryTextView.setText(dictionary.getToLearnList().printResults());
        } else {
            String text = "There are no words on the learn list.";
            learnSummaryTextView.setText(text);
        }
    }
}
