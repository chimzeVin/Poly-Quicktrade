package com.example.polyquicktrade.pojo

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*

class Product {
    constructor(
        name: String,
        description: String,
        location: String,
        phoneNumber: String,
        category: String,
        price: Double,
        deliveryOptions: ArrayList<String>,
        paymentOptions: ArrayList<String>,
        photoURI: String,
        seller: String,
        id: String,
        uploadTime: Long
    ) {
        this.name = name
        this.description = description
        this.location = location
        this.phoneNumber = phoneNumber
        this.category = category
        this.price = price
        this.deliveryOptions = deliveryOptions
        this.paymentOptions = paymentOptions
        this.photoURI = photoURI
        this.seller = seller
        this.id = id
        this.uploadTime = uploadTime
    }

    var name //Done
            : String
    var price //Done
            : Double
    var description //Done
            : String
    var uploadTime //Done
            : Long
    var deliveryOptions: ArrayList<String>
    var paymentOptions: ArrayList<String>
    var location //Done
            : String
    var seller //Done
            : String
    var phoneNumber //Done0
            : String
    var category: String
    var photoURI: String
    var id: String

    constructor() {
        name = ""
        description = ""
        location = ""
        phoneNumber = ""
        this.category = ""
        price = 0.0
        deliveryOptions = ArrayList()
        paymentOptions = ArrayList()
        photoURI = ""
        seller = ""
        id = ""
        uploadTime = 0L
    }

    override fun toString(): String {
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
                paymentOptions.toString()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = DateFormat.getDateTimeInstance().format(Date())
        val imageFileName = "QT_$timeStamp"
        val tempfile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val storageDir = File(tempfile, "QuickTrade")
        if (storageDir.isDirectory) {
            val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
            )

            // Save a file: path for use with ACTION_VIEW intents
            photoURI = image.absolutePath
            return image
        } else {
            try {
                if (storageDir.mkdirs()) Log.i("MY_TAG", "Success")
            } catch (ex: Exception) {
                Log.i("MY_TAG", "Failed", ex)
            }
        }
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        photoURI = image.absolutePath
        Log.i("MY_TAG", photoURI)
        return image
    }

    fun getPictureURI(context: Context?): Uri? {
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {

            // Error occurred while creating the File
            Log.d(Constraints.TAG, "Error occurred: ", ex)
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                context!!,
                "com.example.polyquicktrade.provider",
                photoFile
            )
            Log.d(Constraints.TAG, "Product Class getpictureuri(): $photoURI")
            return photoURI
        }
        return null
    }

    fun compressProductImage(context: Context?, fileOrigin: Int) {
        var fos: FileOutputStream? = null
        val compressedImageFile: File
        val originalFile = File(photoURI)
        Log.d("MY_TAG", "File size before compression: " + originalFile.length())
        try {
            compressedImageFile = createImageFile()
            //                Bitmap compressedImageBitmap = new Compressor(context).compressToBitmap(originalFile);
            fos = FileOutputStream(originalFile)
            //                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESS_QUALITY, fos);
            if (fileOrigin == CREATED_FILE) {
                if (originalFile.delete()) //deletes created file and keeps compressed file
                    Log.d("MY_TAG", "From Product Class: File deleted")
            } else Log.d("MY_TAG", "Gallery File: " + originalFile.absolutePath)
            photoURI = compressedImageFile.absolutePath
            Log.d("MY_TAG", "Compressed image: " + photoURI)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val CREATED_FILE = 23
        private const val IMAGE_COMPRESS_QUALITY = 60
        @JvmField
        var SELECTED_FROM_GALLERY = 12
    }
}