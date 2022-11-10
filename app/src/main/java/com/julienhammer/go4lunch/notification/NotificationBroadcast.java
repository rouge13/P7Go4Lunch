package com.julienhammer.go4lunch.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.julienhammer.go4lunch.R;

import static android.content.Context.MODE_PRIVATE;

public class NotificationBroadcast extends BroadcastReceiver {

    private static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static String RESTAURANT_NAME = "nameRes";
    private static String RESTAURANT_ADDRESS = "addressRes";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
        SharedPreferences prefsWorkmates = context.getSharedPreferences(context.getString(R.string.shared_workmates), MODE_PRIVATE);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(
                context, context.getString(R.string.channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.content_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.content_name)
                                + " : "
                                + prefs.getString(RESTAURANT_NAME,"")
                                + ". "
                                + context.getString(R.string.Address)
                                + " : "
                                + prefs.getString(RESTAURANT_ADDRESS,"")
                                + ". "
                                + context.getString(R.string.title_workmates_view)
                                + " : "
                                + prefsWorkmates.getString(context.getString(R.string.shared_workmates), "")
                                + "."
                ))



                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, notificationCompat.build());
    }




//    private static final String TAG = "MyBroadcastReceiver";
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Action: " + intent.getAction() + "\n");
//        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
//        String log = sb.toString();
//        Log.d(TAG, log);
//        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
//    }


//    private static final String TAG = "MyBroadcastReceiver";
//
//    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        final PendingResult pendingResult = goAsync();
//
//        backgroundExecutor.execute(() -> {
//            try {
//                // Do some background work
//                StringBuilder sb = new StringBuilder();
//                sb.append("Action: ").append(intent.getAction()).append("\n");
//                sb.append("URI: ").append(intent.toUri(Intent.URI_INTENT_SCHEME)).append("\n");
//                String log = sb.toString();
//                Log.d(TAG, log);
//            } finally {
//                // Must call finish() so the BroadcastReceiver can be recycled
//                pendingResult.finish();
//            }
//        });
//    }


}
