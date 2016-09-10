package com.example.abhinav.bucketdrop.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import com.example.abhinav.bucketdrop.MainActivity;
import com.example.abhinav.bucketdrop.R;
import com.example.abhinav.bucketdrop.beans.Drop;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Drop> results = realm.where(Drop.class).equalTo("completed", false).findAll();//find all becoz we are using in services

            for (Drop current : results) {
                if (isNotificationNeeded(current.getAdded(), current.getWhen())) {
                   // Log.d(TAG, "onHandleIntent: notifcation needed");
                   fireNotification(current);
                }
            }

        } finally {
            if (realm != null) {
                realm.close();
            }
        }

            }

    private void fireNotification(Drop drop) {
        String message=getString(R.string.notif_message)+"\""+drop.getWhat()+"\"";
        PugNotification.with(this)
                .load()
                .title("Achievement")
                .message(message)
                .bigTextStyle("Congratulations,you are on verge of accomplishing your goal")
                .smallIcon(R.drawable.pugnotification_ic_launcher)
                .largeIcon(R.drawable.pugnotification_ic_launcher)
                .flags(Notification.DEFAULT_ALL)
                .color(R.color.purple)
                .autoCancel(true)
                .click(MainActivity.class)
                .simple()
                .build();
    }

    private boolean isNotificationNeeded(long added, long when) {
        long now = System.currentTimeMillis();
        if (now > when) {
            return false;
        } else {
            long difference90 = (long) (0.9 * (when - added));
            return (now > (added + difference90)) ? true : false;
        }

    }
}


