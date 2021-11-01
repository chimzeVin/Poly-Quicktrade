package com.example.polyquicktrade.ui

import android.Manifest
import com.example.polyquicktrade.pojo.Product.photoURI
import com.example.polyquicktrade.pojo.Product.toString
import com.example.polyquicktrade.pojo.Product.getPictureURI
import com.example.polyquicktrade.pojo.Product.compressProductImage
import com.example.polyquicktrade.pojo.Product.name
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.ui.AddProductViewModel
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.os.Bundle
import com.example.polyquicktrade.R
import androidx.lifecycle.ViewModelProviders
import com.example.polyquicktrade.ui.AddProductActivity
import android.content.Intent
import android.provider.MediaStore
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import java.io.File
import java.util.*

class AddProductActivity : AppCompatActivity(), View.OnClickListener {
    private var nameET: EditText? = null
    private var priceET: EditText? = null
    private var descET: EditText? = null
    private var locationET: EditText? = null
    private var phoneNumberET: EditText? = null
    private var categorySpinner: Spinner? = null
    private var axaCheckBox: CheckBox? = null
    private var speedCheckBox: CheckBox? = null
    private var kweezyCheckBox: CheckBox? = null
    private var inPersonCheckBox: CheckBox? = null
    private var visaCheckBox: CheckBox? = null
    private var hardCashCheckBox: CheckBox? = null
    private var mobileBankingCheckBox: CheckBox? = null
    private var mobileWalletCheckBox: CheckBox? = null
    private var paypalCheckBox: CheckBox? = null
    private var imageView: ImageView? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var db: FirebaseFirestore? = null
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    private var progressBar: ProgressBar? = null
    private var mProduct: Product? = null
    private var addProductViewModel: AddProductViewModel? = null
    private var enquiryID: String? = null// Permission is not granted
    // Should we show an explanation?

    // No explanation needed; request the permission

    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
    // app-defined int constant. The callback method gets the
    // result of the request.
    // Here, thisActivity is the current activity
    // Permission is not granted
    // Should we show an explanation?
    // No explanation needed; request the permission
    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
    // app-defined int constant. The callback method gets the
    // result of the request.
    // Permission has already been granted
    val permissions: Unit
        private get() {
            // Here, thisActivity is the current activity
            // Permission is not granted
            // Should we show an explanation?
            // No explanation needed; request the permission
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                234
            )
            // Permission has already been granted
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                // Permission is not granted
                // Should we show an explanation?

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    234
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    //TODO save image locally
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        val myToolbar = findViewById<Toolbar>(R.id.toolbar_add_prod)
        setSupportActionBar(myToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle(R.string.title_activity_add_prod)
        }
        if (intent.extras != null) {
            enquiryID = AddProductActivityArgs.fromBundle(intent.extras).addProductToEnquiry
        }
        permissions
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage!!.reference
        mProduct = Product()
        addProductViewModel = ViewModelProviders.of(this).get(
            AddProductViewModel::class.java
        )

//        MyViewModel model = new ViewModelProvider(this).get(MyViewModel.class);
        initViews() //binding of views
    }

    private fun createProductFromForm(): Product {
        val name = nameET!!.text.toString()
        val price = priceET!!.text.toString().toDouble()
        val description = descET!!.text.toString()
        val location = locationET!!.text.toString()
        val phoneNumber = phoneNumberET!!.text.toString()
        val category = categorySpinner!!.selectedItem.toString()
        val deliveryOptions = ArrayList<String>()
        if (axaCheckBox!!.isChecked) { //TODO all these should be converted to CONSTANTS
            deliveryOptions.add("AXA")
        }
        if (speedCheckBox!!.isChecked) {
            deliveryOptions.add("Speed")
        }
        if (kweezyCheckBox!!.isChecked) {
            deliveryOptions.add("Kweezy")
        }
        if (inPersonCheckBox!!.isChecked) {
            deliveryOptions.add("In-person")
        }
        val paymentOptions = ArrayList<String>()
        if (visaCheckBox!!.isChecked) {
            paymentOptions.add("Visa")
        }
        if (hardCashCheckBox!!.isChecked) {
            paymentOptions.add("Hard Cash")
        }
        if (mobileBankingCheckBox!!.isChecked) {
            paymentOptions.add("Mobile Bank Transfer")
        }
        if (mobileWalletCheckBox!!.isChecked) {
            paymentOptions.add("Mobile Wallet")
        }
        if (paypalCheckBox!!.isChecked) {
            paymentOptions.add("PayPal")
        }
        val uploadTime = Calendar.getInstance().time.time
        val prodID = uploadTime.toString()
        val newProduct = Product(
            name, description, location, phoneNumber,
            category, price, deliveryOptions, paymentOptions, "", user!!.uid, prodID, uploadTime
        ) //TODO add photo location
        newProduct.photoURI = mProduct!!.photoURI
        Log.d(TAG, newProduct.toString())
        return newProduct
    }

    private fun initViews() {
        nameET = findViewById(R.id.nameEditText)
        priceET = findViewById(R.id.priceEditText)
        descET = findViewById(R.id.decriptionEditText)
        locationET = findViewById(R.id.locationEditText)
        phoneNumberET = findViewById(R.id.phoneNumberEdtText)
        categorySpinner = findViewById(R.id.categorySpinner)
        axaCheckBox = findViewById(R.id.axaCheckBox)
        speedCheckBox = findViewById(R.id.speedCheckBox)
        kweezyCheckBox = findViewById(R.id.kweezyCheckBox)
        inPersonCheckBox = findViewById(R.id.inPersonCheckBox)
        visaCheckBox = findViewById(R.id.visaCheckBox)
        hardCashCheckBox = findViewById(R.id.hardCashCheckBox)
        mobileBankingCheckBox = findViewById(R.id.mobileBankingCheckBox)
        mobileWalletCheckBox = findViewById(R.id.mobileWalletCheckBox)
        paypalCheckBox = findViewById(R.id.payPalCheckBox)
        val addButton = findViewById<Button>(R.id.addButton)
        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.prodImageView)
        addButton.setOnClickListener(this)
        imageView.setOnClickListener(this)
        findViewById<View>(R.id.clickImageLabel).setOnClickListener(this)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(pickIntent, "Select Option")

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoURI = mProduct!!.getPictureURI(this)
            if (photoURI != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))
            } else Log.d(TAG, "Could not create image URI")
        }
        startActivityForResult(chooserIntent, REQUEST_TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data!!
                val filePath = getRealPathFromUri(this, selectedImage)
                //                Log.d(TAG, "File path: " + filePath);
                mProduct!!.photoURI = filePath
                val tempFile = File(filePath)
                //                if (!tempFile.getAbsolutePath().isEmpty())
//                    Log.i("MY_TAG", "Absolute path: " + tempFile.getAbsolutePath());
//
                mProduct!!.compressProductImage(this, Product.SELECTED_FROM_GALLERY)
                val productURI = mProduct!!.photoURI
                updatePicUI(productURI)
                galleryAddPic()
            } else {
                mProduct!!.compressProductImage(this, Product.CREATED_FILE)
                val productURI = mProduct!!.photoURI
                updatePicUI(productURI)
                galleryAddPic()
            }
        }
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mProduct!!.photoURI)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    private fun updatePicUI(productURI: String) {
        // Get the dimensions of the View
        val targetW = imageView!!.width
        val targetH = imageView!!.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true
        val bitmap = BitmapFactory.decodeFile(productURI, bmOptions)
        Log.d(TAG, mProduct!!.photoURI)
        imageView!!.setImageBitmap(bitmap)
    }

    override fun onStart() {
        super.onStart()
        user = firebaseAuth!!.currentUser
    }

    override fun onClick(view: View) {
        if (view.id == R.id.clickImageLabel || view.id == R.id.prodImageView) dispatchTakePictureIntent()
        if (view.id == R.id.addButton) {
            val mProduct = createProductFromForm()
            progressBar!!.visibility = View.VISIBLE
            if (enquiryID != null) {
                addProductViewModel!!.uploadToFirebase(storageRef, mProduct, db, enquiryID)
            } else {
                addProductViewModel!!.uploadToFirebase(storageRef, mProduct, db)
            }
            addProductViewModel!!.getmProduct().observe(this, Observer { product ->
                Toast.makeText(this@AddProductActivity, product.name, Toast.LENGTH_LONG).show()
                progressBar!!.visibility = View.GONE
                finish()
            })
        }
    }

    companion object {
        private const val TAG = "MY_TAG"
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_TAKE_PHOTO = 1
        fun getRealPathFromUri(context: Context, contentUri: Uri?): String {
            var cursor: Cursor? = null
            return try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(column_index)
            } finally {
                cursor?.close()
            }
        }
    }
}