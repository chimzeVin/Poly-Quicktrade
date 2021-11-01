package com.example.polyquicktrade.ui.ask

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyquicktrade.pojo.Enquiry
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AskViewModel : ViewModel() {


    var enquiriesLiveData: MutableLiveData<ArrayList<Enquiry>>? = MutableLiveData()

    init {
        enquiriesLiveData?.value = arrayListOf()
//        getEnquiriesFromFirebase()
    }

    fun getEnquiriesMutableLiveData(): MutableLiveData<ArrayList<Enquiry>> {
        if (enquiriesLiveData == null) enquiriesLiveData = MutableLiveData()
        return enquiriesLiveData as MutableLiveData<ArrayList<Enquiry>>
    }

    fun getEnquiriesFromFirebase(db: FirebaseFirestore) {
        val enquiries = ArrayList<Enquiry>()
        db.collectionGroup("enquiry_documents").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in Objects.requireNonNull(task.result)) {
                        Log.d("MY_TAG", document.id + " => " + document.data)
                        enquiries.add(document.toObject(Enquiry::class.java))
                        if (enquiriesLiveData == null) enquiriesLiveData =
                            MutableLiveData()
                        enquiriesLiveData!!.value = enquiries
                    }
                } else {
                    Log.d("MY_TAG", "AskViewModel task not successful: ")
                }
            }.addOnFailureListener { e ->
                Log.d("MY_TAG", "Message: " + e.message)
                Log.d("MY_TAG", "eString: $e")
            }
    }
}