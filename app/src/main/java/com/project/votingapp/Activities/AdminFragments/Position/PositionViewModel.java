package com.project.votingapp.Activities.AdminFragments.Position;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PositionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PositionViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is About fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}