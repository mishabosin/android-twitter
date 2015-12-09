package com.codepath.apps.tweeterclient.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.tweeterclient.R;
import com.codepath.apps.tweeterclient.listeners.NewTweetDialogListener;

public class NewTweetDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText etTweetText;

    public NewTweetDialogFragment() {}

    public static NewTweetDialogFragment newWeetDialogFragmentInstance() {
        return new NewTweetDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(getContext().getString(R.string.new_tweet_header));

        // Submit button
        Button btnAddTask = (Button) view.findViewById(R.id.btnTweet);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnInputToActivity();
            }
        });

        // Get field from view
        etTweetText = (EditText) view.findViewById(R.id.etTweetText);
        // Show soft keyboard automatically and request focus to field
        etTweetText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    // Fires whenever the textfield has an action performed
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Check if the "Done" button is pressed
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            returnInputToActivity();
            return true;
        }
        return false;
    }

    private void returnInputToActivity() {
        NewTweetDialogListener listener = (NewTweetDialogListener) getActivity();
        String taskText = etTweetText.getText().toString().trim();
        // ignore blank input
        if (taskText.equals("")) {
            dismiss();
            return;
        }

        listener.onFinishNewDialog(taskText);

        dismiss();
    }
}
