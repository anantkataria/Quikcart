package com.savage9ishere.tiwarimart.authentication.login_or_register

import android.content.Intent
import android.os.Bundle
import android.text.method.TextKeyListener
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.savage9ishere.tiwarimart.checkout.CheckoutActivity
import com.savage9ishere.tiwarimart.databinding.LoginOrRegisterFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

class LoginOrRegisterFragment : Fragment() {

    private lateinit var phoneNumberTextInput : TextInputLayout
    private lateinit var countryCode : TextInputLayout
    private lateinit var changeNumberButton : Button
    private lateinit var otpTextInput : TextInputLayout
    private lateinit var getOtpButton : Button
    private lateinit var verifyOtpButton : Button

    private lateinit var auth : FirebaseAuth
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var verificationId : String

    private var fromCart : Boolean? = null
    private var fromDirect : Boolean? = null
    private var fromProfile : Boolean? = null
    private var fromOrders : Boolean? = null

    private lateinit var itemsList : ArrayList<CartItems>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LoginOrRegisterFragmentBinding.inflate(inflater)

        val bundle = requireActivity().intent.extras
        fromCart = bundle!!.getBoolean("fromCart")
        fromDirect = bundle.getBoolean("fromDirect")
        fromProfile = bundle.getBoolean("fromProfile")
        fromOrders = bundle.getBoolean("fromOrders")
        itemsList = bundle.getParcelableArrayList("itemsList")!!


        phoneNumberTextInput = binding.phoneNumberTextInputLayout
        countryCode = binding.countryCode
        changeNumberButton = binding.changeNumberButton
        otpTextInput = binding.otpTextInputLayout
        getOtpButton = binding.getOtpButton
        verifyOtpButton = binding.verifyOtpButton

        auth = Firebase.auth

        getOtpButton.setOnClickListener {
            phoneNumberTextInput.editText?.isEnabled = false
            getOtpButton.isEnabled = false

            val phoneNumber = "+91" + phoneNumberTextInput.editText?.text.toString()

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(0L, TimeUnit.MILLISECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            getOtpButton.visibility = View.INVISIBLE
            phoneNumberTextInput.visibility = View.INVISIBLE
            countryCode.visibility = View.INVISIBLE
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                when (p0) {
                    is FirebaseNetworkException -> {
                        Toast.makeText(context, "Check your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseTooManyRequestsException -> {
                        Toast.makeText(context, "Your quota of getting otp is over because of too many requests", Toast.LENGTH_LONG).show()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(context, "Enter Valid Number", Toast.LENGTH_SHORT).show()
                        TextKeyListener.clear(phoneNumberTextInput.editText?.text)
                    }
                }

                phoneNumberTextInput.editText?.isEnabled = true
                getOtpButton.isEnabled = true
                getOtpButton.visibility = View.VISIBLE
                phoneNumberTextInput.visibility = View.VISIBLE
                countryCode.visibility = View.VISIBLE
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                verificationId = p0

                changeNumberButton.visibility = View.VISIBLE
                verifyOtpButton.visibility = View.VISIBLE
                otpTextInput.visibility = View.VISIBLE
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }
        }

        changeNumberButton.setOnClickListener {
            val fragment = this
            parentFragmentManager.beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit()
        }

        verifyOtpButton.setOnClickListener {
            val otpCode = otpTextInput.editText?.text.toString()

            when {
                otpCode.isEmpty() -> {
                    Toast.makeText(context, "Please enter the OTP sent!", Toast.LENGTH_SHORT).show()
                }
                otpCode.length < 6 -> {
                    Toast.makeText(context, "OTP should be 6 digits long!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    verifyOtpButton.isEnabled = false
                    otpTextInput.editText?.isEnabled = false

                    val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
                    signInWithPhoneAuthCredential(credential)
                }
            }
        }

        return binding.root
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    if(fromCart!! || fromDirect!!){
                        val intent = Intent(this.activity, CheckoutActivity::class.java)
                        val b = Bundle()
                        b.putParcelableArrayList("itemsList", itemsList)
                        intent.putExtras(b)
                        startActivity(intent)
                    }
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
                else{
                    if(it.exception is FirebaseNetworkException){
                        Toast.makeText(context, "Check you Internet Connection", Toast.LENGTH_SHORT).show()

                        if (verifyOtpButton.visibility != View.VISIBLE){
                            phoneNumberTextInput.editText?.isEnabled = true
                            getOtpButton.isEnabled = true
                            getOtpButton.visibility = View.VISIBLE
                            phoneNumberTextInput.visibility = View.VISIBLE
                            countryCode.visibility = View.VISIBLE
                        }
                        otpTextInput.editText?.isEnabled = true
                        verifyOtpButton.isEnabled = true
                    }
                    if (it.exception is FirebaseAuthInvalidCredentialsException){
                        Log.e("185", "otp wrong called here")
                        Toast.makeText(context, "Entered OTP is wrong!", Toast.LENGTH_SHORT).show()
                        otpTextInput.editText?.isEnabled = true
                        verifyOtpButton.isEnabled = true
                    }

                    if (it.exception is FirebaseAuthUserCollisionException){
                        auth.signInWithCredential(credential).addOnCompleteListener {itt ->
                            if (itt.isSuccessful){
                                if(fromCart!! || fromDirect!!){
                                    val intent = Intent(this.activity, CheckoutActivity::class.java)
                                    val b = Bundle()
                                    b.putParcelableArrayList("itemsList", itemsList)
                                    intent.putExtras(b)
                                    startActivity(intent)
                                }
                                requireActivity().finish()
                            }
                            else {
                                if(itt.exception is FirebaseNetworkException){
                                    Toast.makeText(context, "Check you Internet Connection", Toast.LENGTH_SHORT).show()

                                    if (verifyOtpButton.visibility != View.VISIBLE){
                                        phoneNumberTextInput.editText?.isEnabled = true
                                        getOtpButton.isEnabled = true
                                        getOtpButton.visibility = View.VISIBLE
                                        phoneNumberTextInput.visibility = View.VISIBLE
                                        countryCode.visibility = View.VISIBLE
                                    }
                                    otpTextInput.editText?.isEnabled = true
                                    verifyOtpButton.isEnabled = true
                                }
                                if (itt.exception is FirebaseAuthInvalidCredentialsException){
                                    Log.e("218", itt.exception!!.message!!)
                                    Log.e("218", "otp wrong called here")
                                    Toast.makeText(context, "Entered OTP is wrong!", Toast.LENGTH_SHORT).show()
                                    otpTextInput.editText?.isEnabled = true
                                    verifyOtpButton.isEnabled = true
                                }
                            }
                        }
                    }
                }
            }
    }

}