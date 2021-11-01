package com.example.polyquicktrade

import com.example.polyquicktrade.pojo.Enquiry.creationTime
import com.example.polyquicktrade.pojo.Enquiry.id
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyquicktrade.pojo.Enquiry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import java.util.*

class HomeViewModel : ViewModel() {
    val enquiry: MutableLiveData<Enquiry>
    fun uploadEnquireToFirebase(db: FirebaseFirestore, enquiry: Enquiry) {
        val date = Date(enquiry.creationTime)
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        val week = cal[Calendar.WEEK_OF_MONTH]
        val day = cal[Calendar.DAY_OF_WEEK]


        //Database Storage: Enquiries C-> Month D-> Week C -> Enquiry D
        val enqRef = db
            .collection("Enquiry")
            .document("Month_$month")
            .collection("enquiry_documents")
            .document()
        enquiry.id = enqRef.id
        enqRef.set(enquiry).addOnSuccessListener { enquiry.setValue(enquiry) }
            .addOnFailureListener { }
    }

    init {
        enquiry = MutableLiveData()
    }
}