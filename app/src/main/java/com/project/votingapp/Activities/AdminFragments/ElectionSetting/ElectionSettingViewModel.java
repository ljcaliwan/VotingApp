package com.project.votingapp.Activities.AdminFragments.ElectionSetting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ElectionSettingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ElectionSettingViewModel() {
        //mText = new MutableLiveData<>();
        //mText.setValue("This is Election Setting fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}