package com.project.votingapp.Activities.AdminFragments.Candidates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CandidatesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CandidatesViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is Candidates fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}