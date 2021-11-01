package com.example.polyquicktrade.ui.ask

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.os.Bundle
import android.view.LayoutInflater
import com.example.polyquicktrade.R
import android.content.DialogInterface
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.polyquicktrade.databinding.DialogAskBinding
import com.example.polyquicktrade.ui.ask.AskDialogFragment.AskDialogListener
import java.lang.ClassCastException

class AskDialogFragment : DialogFragment() {
//    private var prodName: EditText? = null
//    private var prodDescription: EditText? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        val view =  DialogAskBinding.inflate(inflater)
//        val view = inflater.inflate(R.layout.dialog_ask, null)
        val prodName: EditText = view.editTextProdName

        val prodDescription = view.editTextProdDesc
        builder.setView(view.root)
            .setPositiveButton(R.string.add) { _,_ ->
                val name = prodName.text.toString()
                val desc = prodDescription.text.toString()
                listener!!.onDialogPositiveClick(this@AskDialogFragment, name, desc)
            }
            .setNegativeButton(R.string.cancel) { _,_ -> this@AskDialogFragment.dialog!!.cancel() }
        return builder.create()
    }

    interface AskDialogListener {
        fun onDialogPositiveClick(
            dialog: DialogFragment?,
            productName: String?,
            productDesc: String?
        ) //TODO no need to pass in the dialog
    }

    // Use this instance of the interface to deliver action events
    //This interface is implemented in the parent activity, which is the home activity
    var listener: AskDialogListener? = null

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        listener = try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            context as AskDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                activity.toString()
                        + " must implement AskDialogListener"
            )
        }
    }
}