package com.example.a390project.Model.TaskClasses;

import android.util.Log;

import java.util.List;

public class PackagingTask {

    private String description;
    private long hours;
    private List<String> materialIDs;

    private static final String TAG = "PackagingTask";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public List<String> getMaterialIDs() {
        return materialIDs;
    }

    public void setMaterialIDs(List<String> materialIDs) {
        Log.d(TAG, "setMaterialIDs: material");
        for (String material : materialIDs)
            Log.d(TAG, "setMaterialIDs: material: " + material);
        this.materialIDs = materialIDs;
    }
}
