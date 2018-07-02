package me.steffenjacobs.fetchgrades.backgroundservice;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.steffenjacobs.fetchgrades.R;
import me.steffenjacobs.fetchgrades.gradedisplay.StorageService;
import me.steffenjacobs.fetchgrades.web.Module;
import me.steffenjacobs.fetchgrades.web.MyRegisteredModule;
import me.steffenjacobs.fetchgrades.web.Session;
import me.steffenjacobs.fetchgrades.web.User;

public class BackgroundService {
    private final StorageService storageService;
    private final Context context;
    private final String username, password;

    private List<Module> cacheDownloadedModules, cacheStoredModules;
    private List<MyRegisteredModule> cacheDownloadedMyRegisteredModules, cacheStoredMyRegisteredModules;

    private User user;

    private static final String FILE_NAME_MODULES = "stored-modules.lst";
    private static final String FILE_NAME_MYREGISTEREDMODULES = "stored-registered-modules.lst";

    public void enableNotifications(long intervalMillis) {
        NotificationScheduler.setBackgroundService(this);
        NotificationScheduler.setReminder(context, intervalMillis);
    }

    public void disableNotifications() {
        NotificationScheduler.cancelReminder(context);
    }

    public void refresh() {
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

    public List<MyRegisteredModule> getMyRegisteredModules(){
        if(cacheDownloadedMyRegisteredModules == null){
            try{
                updateCache();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return cacheDownloadedMyRegisteredModules;
    }

    private void updateCache() throws IOException {
        if(Session.performLogin(username, password)){
            cacheDownloadedModules = Session.fetchGrades();
            Object[] data = Session.fetchMyRegisteredModules();
            this.user = (User) data[0];
            cacheDownloadedMyRegisteredModules = (List<MyRegisteredModule>) data[1];
        }

        try {
            cacheStoredModules = storageService.loadModules(FILE_NAME_MODULES, context);
            cacheStoredMyRegisteredModules = storageService.loadMyRegisteredModules(FILE_NAME_MYREGISTEREDMODULES, context);
        } catch (FileNotFoundException e) {
            cacheStoredModules = new ArrayList<>();
            cacheStoredMyRegisteredModules = new ArrayList<>();
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

    public boolean hasNewRegisteredModules(){
        return cacheStoredMyRegisteredModules.size() != getMyRegisteredModules().size();
    }

    public List<Module> getNewGrades() {
        List<Module> result = new ArrayList<>();
        result.addAll(getModules());
        result.removeAll(cacheStoredModules);
        return result;
    }

    public List<MyRegisteredModule> getNewMyRegisteredModules(){
        List<MyRegisteredModule> result = new ArrayList<>();
        result.addAll(getMyRegisteredModules());
        result.removeAll(cacheStoredMyRegisteredModules);
        return result;
    }

    public void updateModuleStorage(List<Module> updatedList) {
        try {
            storageService.storeModules(updatedList, FILE_NAME_MODULES, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMyRegisteredModulesStorage(List<MyRegisteredModule> updatedList){
        try {
            storageService.storeMyRegisteredModules(updatedList, FILE_NAME_MYREGISTEREDMODULES, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateNewGradeMessage(Module m) {
        return String.format(context.getString(R.string.grade_received), "" + m.getGrade(), m.getModuleName());
    }
}
