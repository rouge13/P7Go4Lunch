package com.julienhammer.go4lunch.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class NotificationBroadcast extends BroadcastReceiver {

    private static final String COLLECTION_NAME = "users";
    private static final String USER_PLACE_ID_FIELD = "userPlaceId";
    private static String PLACE_ID = "placeId";
    private static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static String RESTAURANT_NAME = "nameRes";
    private static String RESTAURANT_ADDRESS = "addressRes";
    private static String USER_ID_FIELD = "userId";


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
        if (!Objects.equals(prefs.getString(PLACE_ID,""), "")){
            FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_PLACE_ID_FIELD, prefs.getString(PLACE_ID,"")).addSnapshotListener((value, error) -> {
                List<User> workmates = new ArrayList<>();
                if (value != null){
                    for (QueryDocumentSnapshot doc : value){
                        if (doc != null){
                            workmates.add(doc.toObject(User.class));
                        }
                    }
                }
                showNotification(context, workmates, prefs);

            });
            Log.d("MyAlarm", "Alarm just fired");
        }


    }

    private void showNotification(Context context, List<User> workmates, SharedPreferences prefs) {
        StringBuilder builder = new StringBuilder();
        String stringAllWorkmates = context.getString(R.string.no_workmates);
        if (workmates.size() != 0){
            ArrayList<String> workmatesUserNameAdded = new ArrayList<>();
            // Storing the key and its value as the data fetched from edittext
            for (int i = 0; i < workmates.size(); i++){
                workmatesUserNameAdded.add(workmates.get(i).getUserName());
            }
            // create an object of StringBuilder class
//        if (workmatesUserNameAdded.iterator().hasNext()) {
            for (String workmatesUserName : workmatesUserNameAdded) {
                builder.append(workmatesUserName + " ");
            }

//        }
//        else if (!workmatesUserNameAdded.iterator().hasNext()){
//            for (String workmatesUserName : workmatesUserNameAdded) {
//                builder.append(workmatesUserName);
//            }
//        }
            // convert StringBuilder object into string
            stringAllWorkmates = builder.toString();
        }

//        SharedPreferences prefsWorkmates = context.getSharedPreferences(context.getString(R.string.shared_workmates), MODE_PRIVATE);
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
                                + stringAllWorkmates
                                + "."
                ))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, notificationCompat.build());
    }

}
