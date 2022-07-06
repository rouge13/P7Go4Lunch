package com.julienhammer.go4lunch.data.workmate;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.julienhammer.go4lunch.models.Workmate;
import com.julienhammer.go4lunch.ui.MainApplication;

import java.util.Objects;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateRepository {

    Context context = MainApplication.getApplication();

    private static volatile WorkmateRepository instance;
    private static final String COLLECTION_NAME = "workmates";
    private static final String WKM_ID_FIELD = "wkmID";
    private static final String WKM_NAME_FIELD = "wkmName";
    private static final String WKM_EMAIL_FIELD = "wkmEmail";
    private static final String WKM_PLACE_ID = "wkmPlaceId";
    private static final String WKM_PHOTO_URL = "wkmPhotoUrl";
    private String uid;

    // Get the Collection Reference
    private CollectionReference getWkmCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create Workmate in Firestore
    public void createWorkmate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            String workmateName = user.getDisplayName();
            String workmateEmail = user.getEmail();
            String workmatePlaceId = "Not set";
            String workmatePhotoUrl = Objects.requireNonNull(user.getPhotoUrl()).toString();

            Workmate workmateToCreate = new Workmate(
                    uid,
                    workmateName,
                    workmateEmail,
                    workmatePlaceId,
                    workmatePhotoUrl
            );

            Task<DocumentSnapshot> workmateData = getWorkmateData();
            // If the user already exist in Firestore, we get his data (isMentor)
            workmateData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(WKM_PLACE_ID)){
                    workmateToCreate.setWkmPlaceId((String) documentSnapshot.get(WKM_PLACE_ID));
                }
                this.getWkmCollection().document(uid).set(workmateToCreate);
            });
        }
    }

    // Get Workmate Data from Firestore
    public Task<DocumentSnapshot> getWorkmateData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return this.getWkmCollection().document(user.getUid()).get();
        }else{
            return null;
        }
    }

    public Task<QuerySnapshot> getAllWorkmates(String workmate){
        return this.getWkmCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (task.getResult() != null){
                                    Workmate workmate = document.toObject(Workmate.class);
                                }

                            }
                        } else {
                            Toast.makeText(context, "Error getting documents: "+ task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    private WorkmateRepository(){}

    public static WorkmateRepository getInstance() {
        WorkmateRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (WorkmateRepository.class){
            if (instance == null){
                instance = new WorkmateRepository();
            }
            return instance;
        }
    }
}
