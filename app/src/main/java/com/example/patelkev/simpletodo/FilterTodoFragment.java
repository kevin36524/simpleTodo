package com.example.patelkev.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by patelkev on 9/28/16.
 */


public class FilterTodoFragment extends AppCompatDialogFragment implements View.OnClickListener {

    public interface FilterTodoFragmentDelegate {
        void filterFragmentDismissedWithFilterState(MainActivity.FilterState filterState);
    }

    private MainActivity.FilterState filterState;
    private FilterTodoFragmentDelegate delegate;

    public FilterTodoFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    private String getFragmentTitle() {
        return "Choose your title";
    }

    public static FilterTodoFragment newInstance(MainActivity.FilterState filterState, FilterTodoFragmentDelegate delegate) {
        FilterTodoFragment frag =  new FilterTodoFragment();
        Bundle args = new Bundle();
        args.putString("title", frag.getFragmentTitle());
        frag.setArguments(args);
        frag.delegate = delegate;
        frag.filterState = filterState;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioButton radioFilterAll = (RadioButton) view.findViewById(R.id.radio_filter_all);
        RadioButton radioFilterDone = (RadioButton) view.findViewById(R.id.radio_filter_done);
        RadioButton radioFilterPending = (RadioButton) view.findViewById(R.id.radio_filter_pending);
        Button dismissButton = (Button) view.findViewById(R.id.dismiss_button);

        radioFilterAll.setOnClickListener(this);
        radioFilterDone.setOnClickListener(this);
        radioFilterPending.setOnClickListener(this);
        dismissButton.setOnClickListener(this);


        RadioButton selectedRadioButton = radioFilterAll;

        switch (filterState) {
            case FILTER_STATE_ALL:
                selectedRadioButton = radioFilterAll;
                break;
            case FILTER_STATE_DONE:
                selectedRadioButton = radioFilterDone;
                break;
            case FILTER_STATE_PENDING:
                selectedRadioButton = radioFilterPending;
                break;
        }

        selectedRadioButton.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.radio_filter_all:
            case R.id.radio_filter_done:
            case R.id.radio_filter_pending:
                onRadioButtonClicked(v);
                break;
            case R.id.dismiss_button:
                onDismissButtonClicked(v);
                break;
        }
    }

    public void onDismissButtonClicked(View v) {
        Log.d("KevinDebug", "pressing the dismiss button and the state is " + filterState.name());
        delegate.filterFragmentDismissedWithFilterState(filterState);
        dismiss();
    }

    public void onRadioButtonClicked(View button) {
        RadioButton radioButton = (RadioButton) button;
        switch (radioButton.getId()) {
            case R.id.radio_filter_all:
                filterState = MainActivity.FilterState.FILTER_STATE_ALL;
                break;
            case R.id.radio_filter_done:
                filterState = MainActivity.FilterState.FILTER_STATE_DONE;
                break;
            case R.id.radio_filter_pending:
                filterState = MainActivity.FilterState.FILTER_STATE_PENDING;
                break;
        }

        Log.d("KevinDebug", String.valueOf(radioButton.getImeActionId()));
    }
}
