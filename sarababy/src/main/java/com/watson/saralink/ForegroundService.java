package com.watson.saralink;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ForegroundService extends Service {

    private static Logger LOGGER = LoggerFactory.getLogger(ForegroundService.class);

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    RequestHandler requestHandler = new RequestHandler();

    //ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

    //取消后台更新任务用
    //ScheduledFuture scheduledFuture;

    //NotificationManagerCompat notificationManager;

    int i = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        LOGGER.info("Foreground service onCreate().");
        //notificationManager.from(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("oops, not implemented yet!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(null != intent) {
            String action = intent.getAction();

            switch(action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    void connect() {
        LOGGER.info("----connect. ip:{} port:{}", LinkConfigManager.getInstance().getLinkConfig().getAnnaIp(),
                LinkConfigManager.getInstance().getLinkConfig().getAnnaPort());

    }

    void disconnect() {

    }

    Notification buildNotification() {
         // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
        //i = i + 1;
        builder.setContentTitle("sarababy");
        //builder.setContentText(String.format("%s:%s connected %s",
        //        LinkConfigManager.getInstance().getLinkConfig().getAnnaIp(),
        //        LinkConfigManager.getInstance().getLinkConfig().getAnnaPort(),
        //        i));
        builder.setContentText("hello");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setWhen(System.currentTimeMillis());
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIconBitmap);
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);

        // Build the notification.
        return builder.build();
    }

    void updateNotification() {
        Notification notification = buildNotification();

        //notificationManager.notify(1, notification);
    }

    void startForegroundService() {
        LOGGER.info("Start foreground service");

        Notification notification = buildNotification();

        // Start foreground service.
        startForeground(1, notification);

        /*
        scheduledFuture = timer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateNotification();
            }
        }, 0, 1, TimeUnit.SECONDS);
        */
        connect();
    }


    void stopForegroundService() {
        LOGGER.info("Stop foreground service.");

        stopForeground(true);

        /*
        if(null != scheduledFuture) {
            scheduledFuture.cancel(false);
        }
        */

        stopSelf();
    }
}
