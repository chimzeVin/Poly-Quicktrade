package com.example.polyquicktrade.pojo;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import id.zelory.compressor.Compressor;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Product {

    public static final int CREATED_FILE = 23;
    private static final int IMAGE_COMPRESS_QUALITY = 60;
    public static int SELECTED_FROM_GALLERY = 12;

    public Product(String name, String description, String location, String phoneNumber, String category, double price, ArrayList<String> deliveryOptions, ArrayList<String> paymentOptions, String photoURI, String seller, String id, long uploadTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.price = price;
        this.deliveryOptions = deliveryOptions;
        this.paymentOptions = paymentOptions;
        this.photoURI = photoURI;
        this.seller = seller;
        this.id = id;
        this.uploadTime = uploadTime;
    }

    private String name;//Done
    private double price;//Done
    private String description;//Done
    private long uploadTime;//Done


    private ArrayList<String> deliveryOptions;
    private ArrayList<String> paymentOptions;

    private String location;//Done
    ;
    private String seller;//Done
    private String phoneNumber;//Done0
    private String category;
    private String photoURI;
    private String id;

    public Product() {
        this.name = "";
        this.description = "";
        this.location = "";
        this.phoneNumber = "";
        this.category = "";
        this.price = 0D;
        this.deliveryOptions = new ArrayList<>();
        this.paymentOptions = new ArrayList<>();
        this.photoURI = "";
        this.seller = "";
        this.id = "";
        this.uploadTime = 0L;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " +
                description + " " +
                location + " " +
                phoneNumber + " " +
                category + " " +
                price + " " +
                photoURI + " " +
                seller + " " +
                id + " " +
                deliveryOptions.toString() + " " +
                paymentOptions.toString();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<String> getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(ArrayList<String> deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public ArrayList<String> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(ArrayList<String> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String imageFileName = "QT_" + timeStamp;

        File tempfile = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        File storageDir = new File(tempfile, "QuickTrade");

        if (storageDir.isDirectory()) {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            this.photoURI = image.getAbsolutePath();
            return image;
        } else {
            try {

                if (storageDir.mkdirs())
                    Log.i("MY_TAG", "Success");
            } catch (Exception ex) {

                Log.i("MY_TAG", "Failed", ex);
            }
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        this.photoURI =image.getAbsolutePath();
        Log.i("MY_TAG", this.photoURI);
        return image;
    }

    public Uri getPictureURI(Context context){

        File photoFile = null;
        try {

            photoFile = this.createImageFile();
        } catch (IOException ex) {

            // Error occurred while creating the File
            Log.d(TAG,"Error occurred: ", ex);
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context,
                    "com.example.polyquicktrade.provider",
                    photoFile);
            Log.d(TAG, "Product Class getpictureuri(): " + photoURI.toString());
            return photoURI;

        }

        return null;
    }

    public void compressProductImage(Context context, int fileOrigin){


        FileOutputStream fos = null;
        File compressedImageFile;
        File originalFile = new File(this.photoURI);
        Log.d("MY_TAG", "File size before compression: " + originalFile.length());


        try {
                compressedImageFile = this.createImageFile();
//                Bitmap compressedImageBitmap = new Compressor(context).compressToBitmap(originalFile);
                fos = new FileOutputStream(originalFile);
//                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESS_QUALITY, fos);

            if (fileOrigin == CREATED_FILE){
                if (originalFile.delete())//deletes created file and keeps compressed file
                    Log.d("MY_TAG", "From Product Class: File deleted");
            }else
                Log.d("MY_TAG", "Gallery File: " + originalFile.getAbsolutePath());


                this.photoURI = compressedImageFile.getAbsolutePath();
            Log.d("MY_TAG", "Compressed image: " + this.photoURI);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {

                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


    }
}
