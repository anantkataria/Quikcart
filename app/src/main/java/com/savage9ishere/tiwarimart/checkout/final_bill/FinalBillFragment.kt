package com.savage9ishere.tiwarimart.checkout.final_bill

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.databinding.FinalBillFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.MainActivity
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus

const val REQUEST_CODE = 123

class FinalBillFragment : Fragment(), PaymentStatusListener {

    private lateinit var viewModel: FinalBillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FinalBillFragmentBinding.inflate(inflater)

        (activity as AppCompatActivity).supportActionBar?.title = "Checkout"


        val itemsList : ArrayList<CartItems>? = requireArguments().getParcelableArrayList("itemsList")
        val address : AddressItem? = requireArguments().getParcelable("address")
        val paymentMethod : String? = requireArguments().getString("paymentMethod")

        val application = requireNotNull(this.activity).application
        val cartDataSource = CartItemsDatabase.getInstance(application).cartItemDao

        val viewModelFactory = FinalBillViewModelFactory(itemsList!!, address!!, paymentMethod!!, cartDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FinalBillViewModel::class.java)

        binding.viewModel = viewModel

        val adapter = FinalBillAdapter()
        binding.itemsRecyclerView.adapter = adapter
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.placeOrderButton.setOnClickListener {
//            viewModel.placeOrder()
            if (paymentMethod == "upi"){
                val transactionId = "TID" + System.currentTimeMillis()
                var s = ""
                var total = 0
                for (item in itemsList) {
                    s += "${item.name}, ${item.price} \n"
                    total += item.price.toInt()*item.qty
                }
                if(total < 500){
                    total += 40
                }

//                try {
//                    val easyUpiPayment = EasyUpiPayment(requireActivity()){
//                        this.paymentApp = PaymentApp.ALL
//                        this.payeeVpa = "katariaanant4@oksbi"
//                        this.payeeName = "Tiwari Mart"
//                        this.transactionId = transactionId
//                        this.transactionRefId = transactionId
//                        this.payeeMerchantCode = ""
//                        this.description = s
//                        this.amount = "$total.00"
//                    }
//
//                    easyUpiPayment.setPaymentStatusListener(this)
//
//                    easyUpiPayment.startPayment()
//
//                } catch (e : Exception){
//                    e.printStackTrace()
//                    Toast.makeText(context, "Error : ${e.message}", Toast.LENGTH_SHORT).show()
//                }

                val uri = Uri.Builder()
                    .scheme("upi")
                    .authority("pay")
                    .appendQueryParameter("pa", "katariaanant4@oksbi")
                    .appendQueryParameter("pn", "Tiwari Mart")
                    .appendQueryParameter("tr", transactionId)
                    .appendQueryParameter("tn", transactionId)
                    .appendQueryParameter("am", total.toString())
                    .appendQueryParameter("cu", "INR")
                    .build()

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = uri

                val chooser = Intent.createChooser(intent, "Pay with")
                startActivityForResult(chooser, REQUEST_CODE)
            }
            else {
                viewModel.placeOrder()
            }
        }

        viewModel.orderPlacedSuccessfully.observe(viewLifecycleOwner, {
            it?.let {
                if(it){
                    Toast.makeText(context, "Order Placed Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
                viewModel.doneOrderPlaced()
            }
        })

        viewModel.cartItems.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                val status = data!!.getStringExtra("Status")
                if (status.equals("SUCCESS")){
                    viewModel.placeOrder()
                }
                else if(status.equals("FAILURE")){
                    Toast.makeText(context, "Payment cancelled, Try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onTransactionCancelled() {
        Toast.makeText(context, "Payment Cancelled", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        when(transactionDetails.transactionStatus){
            TransactionStatus.FAILURE -> onTransactionFailed()
            TransactionStatus.SUBMITTED -> onTransactionSubmitted()
            TransactionStatus.SUCCESS -> onTransactionSuccess()
        }
    }

    private fun onTransactionSuccess() {
        viewModel.placeOrder()
    }

    private fun onTransactionSubmitted() {
        viewModel.placeOrder()
    }

    private fun onTransactionFailed() {
        Toast.makeText(context, "Transaction failed", Toast.LENGTH_SHORT).show()
    }



}