package com.example.polyquicktrade;

import com.example.polyquicktrade.pojo.Enquiry;
import com.example.polyquicktrade.pojo.Product;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<Product> productMutableLiveData;
    private MutableLiveData<String> categoryMutableLiveData;
    private MutableLiveData<Enquiry> enquiry;


    public MutableLiveData<String> getCategory() {
        return categoryMutableLiveData;
    }

    public SharedViewModel() {
        this.productMutableLiveData = new MutableLiveData<>();
        this.categoryMutableLiveData = new MutableLiveData<>();
        this.enquiry = new MutableLiveData<>();

    }

    public MutableLiveData<Product> getProduct() {
        return productMutableLiveData;
    }

    public void setProduct(Product product) {
        this.productMutableLiveData.setValue(product);
    }


    public void setCategory(String item) {
        categoryMutableLiveData.setValue(item);

    }

    public void uploadWishlistItemToFirebase(
    ){

    }

    public void setEnquiry(Enquiry item) {
        enquiry.setValue(item);
    }

    public MutableLiveData<Enquiry> getEnquiry() {
        return enquiry;
    }

}
