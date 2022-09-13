package com.project.votingapp.Activities.AdminFragments.Voters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VotersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VotersViewModel() {
//        mText = new MutableLiveData<>();
////        mText.setValue("This is voters fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}