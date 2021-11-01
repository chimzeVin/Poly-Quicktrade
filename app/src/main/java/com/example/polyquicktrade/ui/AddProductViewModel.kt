package com.example.polyquicktrade.ui

import android.net.Uri
import android.util.Log
import com.example.polyquicktrade.pojo.Product.uploadTime
import com.example.polyquicktrade.pojo.Product.category
import com.example.polyquicktrade.pojo.Product.id
import com.example.polyquicktrade.pojo.Enquiry.creationTime
import com.example.polyquicktrade.pojo.Enquiry.id
import com.example.polyquicktrade.pojo.Enquiry.productResponses
import com.example.polyquicktrade.pojo.Product.photoURI
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyquicktrade.pojo.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.example.polyquicktrade.pojo.Enquiry
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.util.*

class AddProductViewModel : ViewModel() {
    fun getmProduct(): MutableLiveData<Product> {
        return mProduct
    }

    private val mProduct: MutableLiveData<Product>
    fun uploadProductToFirestone(product: Product, db: FirebaseFirestore) {
        val date = Date(product.uploadTime)
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        val week = cal[Calendar.WEEK_OF_MONTH]
        val day = cal[Calendar.DAY_OF_WEEK]


        //Database storage: Products C-> Category D-> Month C-> Week D-> Day C-> Product D
        val newProdRef = db
            .collection("Products")
            .document(product.category)
            .collection("Month_$month")
            .document("Week_$week")
            .collection("product_documents")
            .document()
        product.id = newProdRef.id
        newProdRef
            .set(product)
            .addOnSuccessListener {
                Log.d("MY_TAG", "Product Succesfully Written!")
                mProduct.value = product
            }
            .addOnFailureListener { e -> Log.w("MY_TAG", "Error adding Product", e) }
    }

    fun uploadProductToFirestone(product: Product, db: FirebaseFirestore, enquiryID: String) {
        val date = Date(product.uploadTime)
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        val week = cal[Calendar.WEEK_OF_MONTH]
        val day = cal[Calendar.DAY_OF_WEEK]


        //Database storage: Products C-> Category D-> Month C-> Week D-> Day C-> Product D
        val newProdRef = db
            .collection("Products")
            .document(product.category)
            .collection("Month_$month")
            .document("Week_$week")
            .collection("product_documents")
            .document()
        product.id = newProdRef.id
        newProdRef
            .set(product)
            .addOnSuccessListener {
                Log.d("MY_TAG", "Product Succesfully Written!")
                mProduct.value = product
                addProductIDtoEnquiry(enquiryID, product.id, db)
            }
            .addOnFailureListener { e -> Log.w("MY_TAG", "Error adding Product", e) }
    }

    private fun addProductIDtoEnquiry(enquiryID: String, productID: String, db: FirebaseFirestore) {
        val enquiries = ArrayList<Enquiry>()
        db.collectionGroup("enquiry_documents").whereEqualTo("id", enquiryID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in Objects.requireNonNull(task.result)) {
                        Log.d("MY_TAG", document.id + " => " + document.data)
                        enquiries.add(document.toObject(Enquiry::class.java))
                    }
                    updateEnquiryProducts(enquiries[0], productID, db)
                } else {
                    Log.d("MY_TAG", "AskViewModel task not successful: ")
                }
            }.addOnFailureListener { e ->
                Log.d("MY_TAG", "Message: " + e.message)
                Log.d("MY_TAG", "eString: $e")
            }
    }

    private fun updateEnquiryProducts(enquiry: Enquiry, productID: String, db: FirebaseFirestore) {
        val date = Date(enquiry.creationTime)
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]


        //Database Storage: Enquiries C-> Month D-> Week C -> Enquiry D
        val enqRef = db
            .collection("Enquiry")
            .document("Month_$month")
            .collection("enquiry_documents")
            .document(enquiry.id!!)
        if (enquiry.productResponses != null) {
            val productIDs = enquiry.productResponses
            productIDs!!.add(productID)
            enquiry.productResponses = productIDs
        } else {
            val productIDs = ArrayList<String>()
            productIDs.add(productID)
            enquiry.productResponses = productIDs
        }
        enqRef.set(enquiry).addOnSuccessListener { Log.d("MY_TAG", "Enquiry Updated") }
            .addOnFailureListener { }
    }

    fun uploadToFirebase(storageRef: StorageReference, product: Product, db: FirebaseFirestore) {
        val date = Date(product.uploadTime)
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        val week = cal[Calendar.WEEK_OF_MONTH]
        val day = cal[Calendar.DAY_OF_WEEK]
        val file = Uri.fromFile(File(product.photoURI))
        val imageRef =
            storageRef.child("Product").child(product.category).child(month.toString() + "")
                .child(week.toString() + "")
                .child(day.toString() + "")
                .child(file.lastPathSegment!!)
        val uploadTask = imageRef.putFile(file)
        //
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return imageRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                } else {
//                    // Handle failures
//                    // ...
//                }
//            }
//        });

//        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                product.setPhotoURI(
//                        task.getResult().toString());
//
//
//                uploadProductToFirestone(product, db);
//                Log.w("MY_TAG", "succesfully added image to cloud storage");
//                Log.w("MY_TAG", task.getResult();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w("MY_TAG", "Error adding image to cloud storage", e);
//
//            }
//        });
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                product.photoURI = uri.toString()
                uploadProductToFirestone(product, db)
                Log.w("MY_TAG", "succesfully added image to cloud storage")
                Log.w("MY_TAG", uri.toString())
            }
        }.addOnFailureListener { e -> Log.w("MY_TAG", "Error adding image to cloud storage", e) }
    }

    fun uploadToFirebase(
        storageRef: StorageReference,
        product: Product,
        db: FirebaseFirestore,
        enquiryID: String
    ) {
        val date = Date(product.uploadTime)
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        val week = cal[Calendar.WEEK_OF_MONTH]
        val day = cal[Calendar.DAY_OF_WEEK]
        val file = Uri.fromFile(File(product.photoURI))
        val imageRef =
            storageRef.child("Product").child(product.category).child(month.toString() + "")
                .child(week.toString() + "")
                .child(day.toString() + "")
                .child(file.lastPathSegment!!)
        val uploadTask = imageRef.putFile(file)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                product.photoURI = uri.toString()
                uploadProductToFirestone(product, db, enquiryID)
                Log.w("MY_TAG", "succesfully added image to cloud storage")
                Log.w("MY_TAG", uri.toString())
            }
        }.addOnFailureListener { e -> Log.w("MY_TAG", "Error adding image to cloud storage", e) }
    }

    init {
        mProduct = MutableLiveData()
    }
}