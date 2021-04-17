package com.savage9ishere.tiwarimart.fragment_container.particular_item.review

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ReviewFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import javax.security.auth.login.LoginException

const val REQUEST_IMAGE_GET = 11

class ReviewFragment : Fragment() {

    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ReviewFragmentBinding.inflate(inflater)

        val item : Item? = requireArguments().getParcelable("item")
        val categoryName : String? = requireArguments().getString("category_name")

        val viewModelFactory = ReviewViewModelFactory(item!!, categoryName!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReviewViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val photosAdapter = ItemPhotosAdapter {
            viewModel.removeItem(it)
        }
        binding.itemPhotosRecyclerView.adapter = photosAdapter
        binding.itemPhotosRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        binding.addImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if(intent.resolveActivity(requireActivity().packageManager) != null){
                startActivityForResult(intent, REQUEST_IMAGE_GET)
            }
        }

        binding.submitReviewButton.setOnClickListener {
            val review = binding.reviewTextInput.editText?.text.toString()
            val title = binding.titleTextInput.editText?.text.toString()
            val rating = binding.productRatingBar.rating
            val ratingInt = rating.toInt()
            if(review.isNotEmpty() && title.isNotEmpty() && ratingInt != 0){
                viewModel.addNewReview(review, title, rating)
            }
            else {
                Toast.makeText(context, "Something empty", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.itemImage.observe(viewLifecycleOwner, {
            it?.let {
                Glide.with(binding.productImageView.context)
                    .load(it.toUri().buildUpon().scheme("https").build())
                    .into(binding.productImageView)
                viewModel.donePuttingImage()
            }
        })

        viewModel.itemPhotos.observe(viewLifecycleOwner, {
            it?.let {
                photosAdapter.submitList(it.toList())
            }
        })

        viewModel.uploadComplete.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    Toast.makeText(context, "Review Uploaded successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.doneUploadComplete()
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK){
            //put image uri to arraylist stored in the newItemDetailsViewModel
            if(data != null){
                val imageUri = data.data
                viewModel.addPhotoToArrayList(imageUri.toString())
            }
        }
    }
}