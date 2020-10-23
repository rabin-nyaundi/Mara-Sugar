package com.rabitech.ui.harvestRequest

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rabitech.R
import com.rabitech.dataModels.CustomLoading
import com.rabitech.dataModels.FarmDetails
import com.rabitech.dataModels.HarvestRequest
import com.rabitech.dataModels.LocationDetails
import com.rabitech.databinding.FragmentHarvestRequsetBinding
import com.rabitech.util.HarvestStatus
import kotlinx.android.synthetic.main.fragment_harvest_requset.*
import java.io.IOException
import java.util.*


class HarvestRequestFragment : Fragment() {

    private lateinit var binding: FragmentHarvestRequsetBinding
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    private val viewModel: HarvestRequestViewModel by viewModels()

    private val REQ_CODE_IMAGE_GALLERY = 0

    private var selectedImageUri: Uri? = null


    val progressBar = CustomLoading()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_harvest_requset, container, false)

        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        //retrieveLocation()

        binding.submitRequest.setOnClickListener {

            if (validateUserInput()) {
                uploadImage()
            }
            /*validateUserInput()
            uploadImage()*/

        }
        binding.imageFarmBtn.setOnClickListener {
            showSelectedPictureDialog()
        }

        binding.btnEdit.setOnClickListener {
//            editField()
        }

        return binding.root
    }


    private fun validateUserInput(): Boolean {
        var isValid = false
        //location details
        viewModel.constituency = binding.textConstituency.text.toString()
        viewModel.ward = binding.textWard.text.toString()
        viewModel.landmark = binding.landmark.text.toString()

        viewModel.farmSize = binding.textFarmSize.text.toString()
        viewModel.datePlanted = binding.inputPlantDate.toString()

        if (viewModel.constituency.isEmpty()) {
            text_constituency.error = "Please enter the the constituency"
            text_constituency.requestFocus()
            isValid = false
        }
        if (viewModel.ward.isEmpty()) {
            text_ward.error = "Please enter the ward"
            text_ward.requestFocus()
            isValid = false
        }
        if (viewModel.landmark.isEmpty()) {
            landmark.error = "Please enter the nearest landmark"
            landmark.requestFocus()
            isValid = false
        }
        if (viewModel.farmSize == "") {
            text_farm_size.error = "Please enter the farm size"
            text_farm_size.requestFocus()
            isValid = false
        }

        if (viewModel.datePlanted.isEmpty()) {
            text_date_planted.error = "Please enter the date planted"
            text_date_planted.requestFocus()
            isValid = false
        }

        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Select Farm Image", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (viewModel.constituency.isNotEmpty() && viewModel.ward.isNotEmpty() && viewModel.landmark.isNotEmpty() && viewModel.farmSize.isNotEmpty()
            && viewModel.datePlanted.isNotEmpty() && selectedImageUri != null
        ) {
            isValid = true
        }

        return isValid
        //sendRequest()
    }

    /*private fun retrieveLocation() {
        val userId = mAuth.currentUser!!.uid

        val ref = mDatabase.collection("LocationDetails").document(userId)
        ref.get().addOnSuccessListener { document ->

            if (document != null) {

                constituency = document.getString("constituency").toString()
                ward = document.getString("ward").toString()
                landmark = document.getString("location").toString()

                binding.textConstituency.setText(constituency)
                binding.textWard.setText(ward)
                binding.landmark.setText(landmark)

                Log.d(
                    TAG,
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@data@@@@@@@@@@@@: ${document.data}"
                )
            }
        }
    }*/


    private fun uploadImage() {
        val uuid = UUID.randomUUID()

        val reference = storageReference.child("LandImages/$uuid")
        val uploadTask = reference.putFile(selectedImageUri!!)

        val urlTask = uploadTask.continueWith { task ->
            if (!task.isSuccessful) {
                task.exception?.let { exception ->
                    throw exception
                }
            }
            reference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()

                sendRequest(downloadUrl)
            }
        }

    }

    private fun showSelectedPictureDialog() {
        val pictureDialog = context?.let { AlertDialog.Builder(it) }
        pictureDialog?.setTitle("Select Image Location")
        val pictureDialogItems = arrayOf("Select Image from Galley", "Capture image")
        pictureDialog?.setItems(pictureDialogItems) { _, selectedOption ->
            when (selectedOption) {
                0 -> selectPhotoFromGallery()
                1 -> captureImage()
            }

        }
        pictureDialog?.show()
    }


    private fun selectPhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (galleryIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(galleryIntent, REQ_CODE_IMAGE_GALLERY)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_IMAGE_GALLERY) {
            if (data != null) {
                val imageUri = data.data
                selectedImageUri = imageUri
                try {
                    val bitmapImage =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
                    binding.imageFarm.setImageBitmap(bitmapImage)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to select image", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun captureImage() {
//        capture image using camera intent
    }


    private fun sendRequest(imageUrl: String) {
        val userId = mAuth.currentUser!!.uid

        val locationDetails = LocationDetails(
            viewModel.constituency,
            viewModel.ward,
            viewModel.landmark
        )

        val farmDetails = FarmDetails(
            viewModel.farmSize.toInt(),
            viewModel.datePlanted
        )

        val harvestRequest = HarvestRequest(
            locationDetails,
            farmDetails,
            imageUrl,
            HarvestStatus.WAITING_APPROVAL.toString(),
            userId,
            ""
        )

        /* val harvest = Harvest_data_model(
             userId,
             constituency,
             ward,
             landmark,
             farmSize,
             downloadUrl,
             plantDate = Date()
         )*/

        progressBar.show(requireContext(), "Updating your details...")

        val harvestRequestReference = mDatabase.collection("harvestRequests").document()
        harvestRequest.requestId = harvestRequestReference.id

        harvestRequestReference.set(harvestRequest).addOnSuccessListener {
            Toast.makeText(
                activity,
                "Request sent successfully",
                Toast.LENGTH_LONG
            )
                .show()

            progressBar.loadingDialog.dismiss()

        }.addOnFailureListener {
            Toast.makeText(
                activity,
                "Request not sent. Check the details and send again.",
                Toast.LENGTH_LONG
            )
                .show()
        }


        /* mDatabase.collection("Harvest Request").document(userId).collection("harvest" + Date())
             .add(harvest)
             .addOnSuccessListener { document ->
                 Toast.makeText(
                     activity,
                     "Request sent successfully",
                     Toast.LENGTH_LONG
                 )
                     .show()

                 progressBar.loadingDialog.dismiss()

             }.addOnFailureListener { exception ->
                 Toast.makeText(
                     activity,
                     "Request not sent. Check the details and send again.",
                     Toast.LENGTH_LONG
                 )
                     .show()
             }*/

        mDatabase.collection("harvestRequests").document()
    }
}
