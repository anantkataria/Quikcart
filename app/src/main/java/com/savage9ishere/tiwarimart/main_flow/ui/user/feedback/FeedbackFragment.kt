package com.savage9ishere.tiwarimart.main_flow.ui.user.feedback

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.FeedbackFragmentBinding

class FeedbackFragment : Fragment() {

    private lateinit var viewModel: FeedbackViewModel

    private var uri1 : Uri? = null
    private var uri2 : Uri? = null
    private var uri3 : Uri? = null

    private val REQUEST_IMAGE3_GET = 3
    private val REQUEST_IMAGE2_GET = 2
    private val REQUEST_IMAGE1_GET = 1

    private lateinit var imageView1 : ImageView
    private lateinit var imageView2 : ImageView
    private lateinit var imageView3 : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FeedbackFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(FeedbackViewModel::class.java)

        imageView1 = binding.image1ImageView
        imageView2 = binding.image2ImageView
        imageView3 = binding.image3ImageView

        imageView1.setOnClickListener {
            selectImage1()
        }

        imageView2.setOnClickListener {
            selectImage2()
        }

        imageView3.setOnClickListener {
            selectImage3()
        }

        imageView1.setOnLongClickListener {
            if (uri1 != null){
                uri1 = null
                imageView1.setImageResource(R.drawable.ic_add2)
                true
            }
            else {
                false
            }
        }

        imageView2.setOnLongClickListener{
            if (uri2 != null){
                uri2 = null
                imageView2.setImageResource(R.drawable.ic_add2)
                true
            }
            else {
                false
            }
        }

        imageView3.setOnLongClickListener{
            if (uri3 != null){
                uri3 = null
                imageView3.setImageResource(R.drawable.ic_add2)
                true
            }
            else {
                false
            }
        }

        binding.nextButton.setOnClickListener {
            val concern = binding.concernEditText.text.toString()

            when {
                concern.isEmpty() -> {
                    Toast.makeText(context, "Enter your concern first", Toast.LENGTH_SHORT).show()
                }
                concern.length <= 7 -> {
                    Toast.makeText(context, "Write more", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideKeyboard(requireActivity())

                    val auth = Firebase.auth
                    val user = auth.currentUser
                    val phoneNumber = user!!.phoneNumber

                    if(phoneNumber == null) {
                        Toast.makeText(context, "Sign in to write a feedback", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val emails = arrayOf("feedback.tiwarimart@gmail.com")
                        val subject = "Tiwarimart Customer feedback"
                        val body = concern + "\n\n" + phoneNumber
                        composeEmail(emails, subject, body, uri1, uri2, uri3)
                    }

                }
            }
        }

        return binding.root
    }

    private fun composeEmail(emails: Array<String>, subject: String, body: String, uri1: Uri?, uri2: Uri?, uri3: Uri?) {
        val uris = arrayListOf<Uri>()

        if(uri1 != null){
            uris.add(uri1)
        }
        if(uri2 != null){
            uris.add(uri2)
        }
        if(uri3 != null){
            uris.add(uri3)
        }

        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")

        val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emails)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        emailIntent.selector = selectorIntent

        if (emailIntent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(emailIntent, 69)
        }
        else {
            Toast.makeText(context, "No email client app found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard(requireActivity: FragmentActivity) {
        val imm = requireActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity.currentFocus
        if (view == null){
            view = View(requireActivity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun selectImage3() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type ="image/*"
        if (intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE3_GET)
        }
        else {
            Toast.makeText(context, "No application to perform this task", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage2() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type ="image/*"
        if (intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE2_GET)
        }
        else {
            Toast.makeText(context, "No application to perform this task", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage1() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type ="image/*"
        if (intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE1_GET)
        }
        else {
            Toast.makeText(context, "No application to perform this task", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE1_GET -> {
                if (resultCode == RESULT_OK){
                    uri1 = data!!.data
                    imageView1.setImageURI(uri1)
                }
                else if (resultCode == RESULT_CANCELED){
                    Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show()
                }
            }

            REQUEST_IMAGE2_GET -> {
                if (resultCode == RESULT_OK){
                    uri2 = data!!.data
                    imageView1.setImageURI(uri2)
                }
                else if (resultCode == RESULT_CANCELED){
                    Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show()
                }
            }

            REQUEST_IMAGE3_GET -> {
                if (resultCode == RESULT_OK){
                    uri3 = data!!.data
                    imageView1.setImageURI(uri3)
                }
                else if (resultCode == RESULT_CANCELED){
                    Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show()
                }
            }

            69 -> {
                findNavController().popBackStack()
            }
        }
    }
}