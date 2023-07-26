package com.faadev.ceria.adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> refreshData = new MutableLiveData<>();

    public void refresh() {
        refreshData.setValue(true);
    }

    public LiveData<Boolean> getRefreshData() {
        return refreshData;
    }

    public void doneRefreshing() {
        refreshData.setValue(false);
    }
}