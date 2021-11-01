package com.example.polyquicktrade.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static java.util.Calendar.getInstance;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MY_TAG";
    private EditText nameET;
    private EditText priceET;
    private EditText descET;
    private EditText locationET;
    private EditText phoneNumberET;

    private Spinner categorySpinner;

    private CheckBox axaCheckBox;
    private CheckBox speedCheckBox;
    private CheckBox kweezyCheckBox;
    private CheckBox inPersonCheckBox;

    private CheckBox visaCheckBox;
    private CheckBox hardCashCheckBox;
    private CheckBox mobileBankingCheckBox;
    private CheckBox mobileWalletCheckBox;
    private CheckBox paypalCheckBox;

    private ImageView imageView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ProgressBar progressBar;

    private Product mProduct;

    private AddProductViewModel addProductViewModel;
    private String enquiryID;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;


    private void getPermissions() {
        // Here, thisActivity is the current activity
        // Permission is not granted
        // Should we show an explanation?
        // No explanation needed; request the permission
        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                234);
        // Permission has already been granted

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    234);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }
    //TODO save image locally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar myToolbar = findViewById(R.id.toolbar_add_prod);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_activity_add_prod);
        }


        if (getIntent().getExtras() != null){
            enquiryID = AddProductActivityArgs.fromBundle(getIntent().getExtras()).getAddProductToEnquiry();
        }

        getPermissions();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mProduct = new Product();

        addProductViewModel = ViewModelProviders.of(this).get(AddProductViewModel.class);

//        MyViewModel model = new ViewModelProvider(this).get(MyViewModel.class);

        initViews();//binding of views


    }



    private Product createProductFromForm() {

        String name = nameET.getText().toString();
        double price = Double.parseDouble(priceET.getText().toString());
        String description = descET.getText().toString();
        String location = locationET.getText().toString();
        String phoneNumber = phoneNumberET.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();


        ArrayList<String> deliveryOptions = new ArrayList<>();

        if(axaCheckBox.isChecked()){//TODO all these should be converted to CONSTANTS
            deliveryOptions.add("AXA");
        } if(speedCheckBox.isChecked()){
            deliveryOptions.add("Speed");
        } if(kweezyCheckBox.isChecked()){
            deliveryOptions.add("Kweezy");
        } if(inPersonCheckBox.isChecked()){
            deliveryOptions.add("In-person");
        }

        ArrayList<String> paymentOptions = new ArrayList<>();

        if (visaCheckBox.isChecked()){
            paymentOptions.add("Visa");
        } if (hardCashCheckBox.isChecked()){
            paymentOptions.add("Hard Cash");
        } if (mobileBankingCheckBox.isChecked()){
            paymentOptions.add("Mobile Bank Transfer");
        } if(mobileWalletCheckBox.isChecked()){
            paymentOptions.add("Mobile Wallet");
        } if(paypalCheckBox.isChecked()){
            paymentOptions.add("PayPal");
        }

        long uploadTime = getInstance().getTime().getTime();
        String prodID = String.valueOf(uploadTime);

        Product newProduct = new Product(name, description, location, phoneNumber,
                category, price, deliveryOptions, paymentOptions, "", user.getUid(), prodID, uploadTime);//TODO add photo location

        newProduct.setPhotoURI(mProduct.getPhotoURI());
        Log.d(TAG, newProduct.toString());
        return newProduct;
    }

    private void initViews() {

        nameET = findViewById(R.id.nameEditText);
        priceET = findViewById(R.id.priceEditText);
        descET = findViewById(R.id.decriptionEditText);
        locationET = findViewById(R.id.locationEditText);
        phoneNumberET = findViewById(R.id.phoneNumberEdtText);

        categorySpinner = findViewById(R.id.categorySpinner);

        axaCheckBox = findViewById(R.id.axaCheckBox);
        speedCheckBox = findViewById(R.id.speedCheckBox);
        kweezyCheckBox = findViewById(R.id.kweezyCheckBox);
        inPersonCheckBox = findViewById(R.id.inPersonCheckBox);

        visaCheckBox = findViewById(R.id.visaCheckBox);
        hardCashCheckBox = findViewById(R.id.hardCashCheckBox);
        mobileBankingCheckBox = findViewById(R.id.mobileBankingCheckBox);
        mobileWalletCheckBox = findViewById(R.id.mobileWalletCheckBox);
        paypalCheckBox = findViewById(R.id.payPalCheckBox);

        Button addButton = findViewById(R.id.addButton);
        progressBar= findViewById(R.id.progressBar);
        imageView = findViewById(R.id.prodImageView);

        addButton.setOnClickListener(this);
        imageView.setOnClickListener(this);
        findViewById(R.id.clickImageLabel).setOnClickListener(this);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Option");

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            Uri photoURI = mProduct.getPictureURI(this );
            if (photoURI != null){

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePictureIntent});

            }
            else
                Log.d(TAG, "Could not create image URI");
        }

        startActivityForResult(chooserIntent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data!=null){
                Uri selectedImage = data.getData();
                assert selectedImage != null;

                String filePath = getRealPathFromUri(this, selectedImage);
//                Log.d(TAG, "File path: " + filePath);
                mProduct.setPhotoURI(filePath);
                File tempFile = new File(filePath);
//                if (!tempFile.getAbsolutePath().isEmpty())
//                    Log.i("MY_TAG", "Absolute path: " + tempFile.getAbsolutePath());
//
                mProduct.compressProductImage(this, Product.SELECTED_FROM_GALLERY);
                String productURI = mProduct.getPhotoURI();
                updatePicUI(productURI);
                galleryAddPic();

            }else{
                mProduct.compressProductImage(this, Product.CREATED_FILE);
                String productURI = mProduct.getPhotoURI();
                updatePicUI(productURI);
                galleryAddPic();
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mProduct.getPhotoURI());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);

        this.sendBroadcast(mediaScanIntent);

    }

    private void updatePicUI(String productURI) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(productURI, bmOptions);
        Log.d(TAG,mProduct.getPhotoURI());

        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();

        user = firebaseAuth.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.clickImageLabel || view.getId() == R.id.prodImageView)
            dispatchTakePictureIntent();

        if (view.getId() == R.id.addButton){
            Product mProduct = createProductFromForm();
            progressBar.setVisibility(View.VISIBLE);

            if (enquiryID != null){
                addProductViewModel.uploadToFirebase(storageRef, mProduct, db, enquiryID);

            }else {

                addProductViewModel.uploadToFirebase(storageRef, mProduct, db);
            }

            addProductViewModel.getmProduct().observe(this, new Observer<Product>() {
                @Override
                public void onChanged(Product product) {
                    Toast.makeText(AddProductActivity.this, product.getName(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
            });
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
