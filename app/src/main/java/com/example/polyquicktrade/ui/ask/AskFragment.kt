package com.example.polyquicktrade.ui.ask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.RecyclerClickListener
import com.example.polyquicktrade.SharedViewModel
import com.example.polyquicktrade.databinding.FragmentAskBinding
import com.example.polyquicktrade.pojo.Enquiry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class AskFragment : Fragment(), RecyclerClickListener<Enquiry?> {
    private var askViewModel: AskViewModel? = null
    private var sharedViewModel: SharedViewModel? = null
    private var enquireFab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var askAdapter: AskAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var db: FirebaseFirestore? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAskBinding.inflate(inflater)
        val db = FirebaseFirestore.getInstance()
        val askViewModel = ViewModelProviders.of(this).get(AskViewModel::class.java)
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(
            SharedViewModel::class.java
        )
//        enquireFab = binding.findViewById(R.id.fad_add_enquiry)
//        recyclerView = binding.findViewById(R.id.askRecyclerView)
        val askRecyclerView = binding.included.askRecyclerView
        askRecyclerView.isNestedScrollingEnabled = false
        binding.fadAddEnquiry.setOnClickListener{
            val askDialogFragment = AskDialogFragment()
            askDialogFragment.show(requireFragmentManager(), "Ask")
        }
        askViewModel.getEnquiriesFromFirebase(db)
        askViewModel.enquiriesLiveData?.observe(viewLifecycleOwner, Observer { enquiries ->
            askAdapter = AskAdapter(enquiries, this@AskFragment)
            askRecyclerView.adapter = askAdapter
        })
        return binding.root
    }

    override fun onClick(item: Enquiry?, view: View?) {
        item?.let { sharedViewModel!!.setEnquiry(it)
            val action = AskFragmentDirections.actionNavigationAskToEnquiryAndResponsesFragment()
            NavHostFragment.findNavController(this@AskFragment).navigate(action)
        }

    }

    companion object {
        fun newInstance(): AskFragment {
            return AskFragment()
        }
    }
}