package com.zwt.superandroid.api;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/12/27
 * Time: 10:52
 */
public class TestDatabindingData extends BaseObservable {
    @Bindable
    private String name;

    private String password;

    public TestDatabindingData() {
    }

    public TestDatabindingData(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(com.zwt.superandroid.BR.name);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyChange();
    }
}
