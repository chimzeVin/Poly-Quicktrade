package com.example.polyquicktrade.ui.ask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.polyquicktrade.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AskDialogFragment extends DialogFragment {

    private EditText prodName, prodDescription;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ask, null);
        prodName = view.findViewById(R.id.editTextProdName);
        prodDescription = view.findViewById(R.id.editTextProdDesc);


        builder.setView(view)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String prodName = AskDialogFragment.this.prodName.getText().toString();
                        String prodDesc = AskDialogFragment.this.prodDescription.getText().toString();
                        listener.onDialogPositiveClick(AskDialogFragment.this,prodName, prodDesc);

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AskDialogFragment.this.getDialog().cancel();

            }
        });
        return builder.create();
    }



    public interface AskDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String productName, String productDesc);//TODO no need to pass in the dialog

    }

    // Use this instance of the interface to deliver action events
    //This interface is implemented in the parent activity, which is the home activity
    AskDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (AskDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement AskDialogListener");
        }
    }
}
