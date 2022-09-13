package com.project.votingapp.Activities.AdminFragments.TallyBoard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TallyBoardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TallyBoardViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is tally board fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}