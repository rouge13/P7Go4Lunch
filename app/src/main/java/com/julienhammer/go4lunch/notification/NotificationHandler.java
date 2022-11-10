//package com.julienhammer.go4lunch.notification;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.work.*;
//import com.google.android.gms.maps.model.LatLng;
//import com.julienhammer.go4lunch.models.RestaurantDetails;
//import org.jetbrains.annotations.NotNull;
//import static android.content.Context.MODE_PRIVATE;
//
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//import com.julienhammer.go4lunch.R;
//
//public class NotificationHandler extends Worker {
//    AppCompatActivity activity;
//    private static String PLACE_ID = "placeId";
//    private static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
//    private static String RESTAURANT_NAME = "nameRes";
//    private static String RESTAURANT_ADDRESS = "addressRes";
//
//    public NotificationHandler(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public Result doWork() {
//        showNotification();
//        return null;
//    }
//
////    public static void periodRequest(){
////        PeriodicWorkRequest notifPeriodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationHandler.class, 1, TimeUnit.DAYS)
////                .setInitialDelay(5, TimeUnit.SECONDS)
////                .setConstraints(setNotificationConstraint())
////                .addTag("notificationPeriodic")
////                .build();
////        WorkManager.getInstance().enqueueUniquePeriodicWork("notificationPeriodic", ExistingPeriodicWorkPolicy.REPLACE, notifPeriodicWorkRequest);
////
////    }
//
//    // Create notification
//    public void showNotification(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "RestaurantReminderChannel";
//            String description = "Channel for Go4Lunch application";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(getApplicationContext().getString(R.string.app_name), name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        SharedPreferences prefs = activity.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getApplicationContext().getString(R.string.app_name))
//                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
//                .setContentTitle("Restaurant du jour : ")
//                .setContentText(activity.getString(R.string.name)
//                        + ": "
//                        + prefs.getString(RESTAURANT_NAME,"")
//                        + activity.getString(R.string.Address)
//                        + ": "
//                        + prefs.getString(RESTAURANT_ADDRESS,"")
//
//                )
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(1, builder.build());
//
////        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
////        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(
////                getApplicationContext(),
////                "14")
////                .setSmallIcon(R.mipmap.ic_launcher)
////                .setContentTitle("Restaurant du jour.")
////                .setContentText("PlaceId")
////                .setContentIntent(pendingIntent)
////                .setAutoCancel(true);
////        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
////
////        notificationManagerCompat.notify(4, notificationCompat.build());
//    }
//
//
//}
