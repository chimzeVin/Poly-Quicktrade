package com.example.polyquicktrade;

import com.example.polyquicktrade.pojo.Enquiry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<Enquiry> getEnquiry() {
        return enquiryMutableLiveData;
    }

    private MutableLiveData<Enquiry>  enquiryMutableLiveData;

    public HomeViewModel() {
        this.enquiryMutableLiveData = new MutableLiveData<>();
    }

    public void uploadEnquireToFirebase(FirebaseFirestore db, final Enquiry enquiry){

        Date date = new Date(enquiry.getCreationTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);


        //Database Storage: Enquiries C-> Month D-> Week C -> Enquiry D
        DocumentReference enqRef = db
                .collection("Enquiry")
                .document("Month_" + month)
                .collection("enquiry_documents")
                .document();

        enquiry.setId(enqRef.getId());
        enqRef.set(enquiry).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                enquiryMutableLiveData.setValue(enquiry);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }
}
