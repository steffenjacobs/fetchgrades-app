package me.steffenjacobs.fetchgrades.backgroundservice;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.steffenjacobs.fetchgrades.gradedisplay.StorageService;
import me.steffenjacobs.fetchgrades.gradefetcher.FetchGrades;
import me.steffenjacobs.fetchgrades.gradefetcher.Module;

public class BackgroundService {
    private final StorageService storageService;
    private final Context context;
    private final String username, password;

    private List<Module> cacheDownloadedModules, cacheStoredModules;

    private static final String FILE_NAME = "stored-modules.lst";

    public void enableNotifications(long intervalMillis){
        NotificationScheduler.setBackgroundService(this);
        NotificationScheduler.setReminder(context, intervalMillis);
    }

    private void disableNotifications(){
        NotificationScheduler.cancelReminder(context);
    }

    public void refresh(){
        try {
            updateCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Module> getModules() {
        if (cacheDownloadedModules == null) {
            try {
                updateCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cacheDownloadedModules;
    }

    private void updateCache() throws IOException {
        cacheDownloadedModules = new FetchGrades(username, password).fetchGrades();
        try {
            cacheStoredModules = storageService.load(FILE_NAME, context);
        } catch (FileNotFoundException e) {
            cacheStoredModules = new ArrayList<>();
        }
    }

    public BackgroundService(Context context, String username, String password) {
        storageService = new StorageService();
        this.context = context;
        this.username = username;
        this.password = password;
    }

    public boolean hasNewGrades() {
        return cacheStoredModules.size() != getModules().size();
    }

    public List<Module> getNewGrades() {
        List<Module> result = new ArrayList<>();
        result.addAll(getModules());
        result.removeAll(cacheStoredModules);
        return result;
    }

    public void updateStorage(List<Module> updatedList) {
        try {
            storageService.store(updatedList, FILE_NAME, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateNewGradeMessage(Module m) {
        return m.getGrade() + " in " + m.getModuleName() + " received!";
    }
}