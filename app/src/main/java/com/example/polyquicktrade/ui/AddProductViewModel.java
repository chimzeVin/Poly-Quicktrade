package com.example.polyquicktrade.ui;

import android.net.Uri;
import android.util.Log;

import com.example.polyquicktrade.pojo.Enquiry;
import com.example.polyquicktrade.pojo.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddProductViewModel extends ViewModel {

    public MutableLiveData<Product> getmProduct() {
        return mProduct;
    }

    private MutableLiveData<Product> mProduct;

    public AddProductViewModel(){
        mProduct = new MutableLiveData<>();


    }

    public void uploadProductToFirestone(final Product product, FirebaseFirestore db) {


        Date date = new Date(product.getUploadTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);


        //Database storage: Products C-> Category D-> Month C-> Week D-> Day C-> Product D
        DocumentReference newProdRef = db
                .collection("Products")
                .document(product.getCategory())
                .collection("Month_" + month)
                .document("Week_" + week)
                .collection("product_documents")
                .document();

        product.setId(newProdRef.getId());

        newProdRef
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MY_TAG", "Product Succesfully Written!");
                        AddProductViewModel.this.mProduct.setValue(product);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MY_TAG", "Error adding Product", e);

                    }
                });

    }
    public void uploadProductToFirestone(final Product product, final FirebaseFirestore db, final String enquiryID) {


        Date date = new Date(product.getUploadTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);


        //Database storage: Products C-> Category D-> Month C-> Week D-> Day C-> Product D
        DocumentReference newProdRef = db
                .collection("Products")
                .document(product.getCategory())
                .collection("Month_" + month)
                .document("Week_" + week)
                .collection("product_documents")
                .document();

        product.setId(newProdRef.getId());

        newProdRef
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MY_TAG", "Product Succesfully Written!");
                        AddProductViewModel.this.mProduct.setValue(product);

                        addProductIDtoEnquiry(enquiryID, product.getId(), db);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MY_TAG", "Error adding Product", e);

                    }
                });

    }

    private void addProductIDtoEnquiry(String enquiryID, final String productID, final FirebaseFirestore db) {

        final ArrayList<Enquiry> enquiries = new ArrayList<>();
        db.collectionGroup("enquiry_documents").whereEqualTo("id", enquiryID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d("MY_TAG", document.getId() + " => " + document.getData());
                                enquiries.add(document.toObject(Enquiry.class));

                            }
                            updateEnquiryProducts(enquiries.get(0), productID, db);

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

    private void updateEnquiryProducts(Enquiry enquiry, String productID, FirebaseFirestore db) {


            Date date = new Date(enquiry.getCreationTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);


            int month = cal.get(Calendar.MONTH);


        //Database Storage: Enquiries C-> Month D-> Week C -> Enquiry D
            DocumentReference enqRef = db
                    .collection("Enquiry")
                    .document("Month_" + month)
                    .collection("enquiry_documents")
                    .document(enquiry.getId());


            if (            enquiry.getProductResponses() !=null){
                ArrayList<String> productIDs = enquiry.getProductResponses();
                productIDs.add(productID);
                enquiry.setProductResponses(productIDs);

            }else {
                ArrayList<String> productIDs = new ArrayList<>();
                productIDs.add(productID);
                enquiry.setProductResponses(productIDs);
            }

            enqRef.set(enquiry).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                   Log.d("MY_TAG", "Enquiry Updated");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });






    }

    public void uploadToFirebase(StorageReference storageRef, final Product product, final FirebaseFirestore db){

        Date date = new Date(product.getUploadTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        Uri file = Uri.fromFile(new File(product.getPhotoURI()));


        final StorageReference imageRef = storageRef.
                child("Product").
                child(product.getCategory()).
                child(month + "")
                .child(week + "")
                .child(day + "")
                .child(Objects.requireNonNull(file.getLastPathSegment()));

        UploadTask uploadTask = imageRef.putFile(file);
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

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        product.setPhotoURI(uri.toString());


                        uploadProductToFirestone(product, db);
                        Log.w("MY_TAG", "succesfully added image to cloud storage");
                        Log.w("MY_TAG", uri.toString());
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("MY_TAG", "Error adding image to cloud storage", e);
            }
        });



    }
    public void uploadToFirebase(StorageReference storageRef, final Product product, final FirebaseFirestore db, final String enquiryID){

        Date date = new Date(product.getUploadTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        Uri file = Uri.fromFile(new File(product.getPhotoURI()));


        final StorageReference imageRef = storageRef.
                child("Product").
                child(product.getCategory()).
                child(month + "")
                .child(week + "")
                .child(day + "")
                .child(file.getLastPathSegment());

        UploadTask uploadTask = imageRef.putFile(file);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        product.setPhotoURI(uri.toString());


                        uploadProductToFirestone(product, db, enquiryID);
                        Log.w("MY_TAG", "succesfully added image to cloud storage");
                        Log.w("MY_TAG", uri.toString());
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("MY_TAG", "Error adding image to cloud storage", e);
            }
        });



    }


}
