package com.abdeveloper.library;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class MultiSelectModel {
    private Integer id;
    private String name;
    private int resource;
    private Boolean isSelected;

    public MultiSelectModel(Integer id, @NonNull String name) {
        this(id, name, 0);
    }

    public MultiSelectModel(Integer id, @NonNull String name, @DrawableRes int resId) {
        this.id = id;
        this.name = name;
        this.resource = resId;
    }


    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getImageResource() {
        return resource;
    }

    public void setImageResource(@DrawableRes int resId) {
        this.resource = resId;
    }

    Boolean getSelected() {
        return isSelected;
    }

    void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
