package com.example.polyquicktrade.ui.ask;

import android.util.Log;

import com.example.polyquicktrade.pojo.Enquiry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AskViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Enquiry>> enquiriesMutableLiveData;

    public MutableLiveData<ArrayList<Enquiry>> getEnquiriesMutableLiveData() {
        if (enquiriesMutableLiveData ==null)
            enquiriesMutableLiveData = new MutableLiveData<>();
        return enquiriesMutableLiveData;
    }

    public AskViewModel() {


    }

    public void getEnquiriesFromFirebase(FirebaseFirestore db){


        final ArrayList<Enquiry> enquiries = new ArrayList<>();

        db.collectionGroup("enquiry_documents").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d("MY_TAG", document.getId() + " => " + document.getData());
                                enquiries.add(document.toObject(Enquiry.class));
                                if (enquiriesMutableLiveData ==null)
                                    enquiriesMutableLiveData = new MutableLiveData<>();

                                enquiriesMutableLiveData.setValue(enquiries);

                            }

                        }
                        else {
                            Log.d("MY_TAG", "AskViewModel task not successful: ");

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("MY_TAG", "Message: "+e.getMessage());
                Log.d("MY_TAG", "eString: "+ e.toString());

            }
        });
    }


}