package com.project.votingapp.Activities.AdminFragments.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("Election Result");
    }

    public LiveData<String> getText() {
        return mText;
    }
}