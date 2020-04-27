package com.rabitech.ui

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rabitech.R
import com.rabitech.dataModels.CustomLoading
import com.rabitech.dataModels.FarmDetails
import com.rabitech.dataModels.LocationDetails
import com.rabitech.databinding.FragmentHarvestRequsetBinding
import kotlinx.android.synthetic.main.fragment_harvest_requset.*
import kotlinx.android.synthetic.main.fragment_harvest_requset.view.*
import java.io.IOException
import java.util.*


class HarvestRequsetFragment : Fragment() {

    private lateinit var binding: FragmentHarvestRequsetBinding
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    private val REQ_CODE_IMAGE_GALLERY = 0
    private var selectedImageUri: Uri? = null


    //location details
    private var constituency = ""
    private var ward = ""
    private var landmark = ""

    //farm details
    private var farmSize = ""
    private var downloadUrl = ""
    private var date = ""
    private var status_of_request ="Awaiting Approval"

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

        retrieveLocation()

        binding.submitRequest.setOnClickListener {
            validateUserInput()
            uploadImage()

        }
        binding.imageFarmBtn.setOnClickListener {
            showSelectedPictureDialog()
        }

        binding.btnEdit.setOnClickListener {
//            editField()
        }

        return binding.root
    }

    private fun retrieveLocation() {
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
    }

    //Validate user inputs
    private fun validateUserInput() {
        //location details
        constituency = binding.textConstituency.text.toString()
        ward = binding.textWard.text.toString()
        landmark = binding.landmark.text.toString()

        //farm details
        farmSize = binding.textFarmSize.text.toString()
        date = binding.inputPlantDate.text_date_planted.toString()


        if (constituency.isEmpty()) {
            text_constituency.error = "Please enter the the constituency"
            text_constituency.requestFocus()
            return
        }
        if (ward.isEmpty()) {
            text_ward.error = "Please enter the ward"
            text_ward.requestFocus()
            return
        }
        if (landmark.isEmpty()) {
            text_location.error = "Please enter the nearest landmark"
            text_location.requestFocus()
            return
        }
        if (farmSize.isEmpty()) {
            text_farm_size.error = "Please enter the farm size"
            text_farm_size.requestFocus()
            return
        }

        locationDetails()
        insertFarmDetails()
    }



    private fun uploadImage() {
        if (selectedImageUri == null) {
            return
        } else {
            val imageId = mAuth.currentUser?.uid
            val reference = storageReference.child("LandImages/$imageId")
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
                    val downloadUrl = task.result
                }
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
        if (galleryIntent.resolveActivity(activity!!.packageManager) != null) {
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

    }

    private fun locationDetails() {

        val userId = mAuth.currentUser!!.uid

        val locationDetails = LocationDetails(
            constituency,
            ward,
            landmark

        )

        mDatabase.collection("LocationDetails").document(userId).set(locationDetails)
            .addOnSuccessListener { document ->
                Toast.makeText(
                        activity,
                        "Location details inserted successfully",
                        Toast.LENGTH_LONG
                    )
                    .show()

            }.addOnFailureListener { exception ->
                Toast.makeText(activity, "Failed to insert location details", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun insertFarmDetails() {

        val farmDetails = FarmDetails(
            farmSize,
            downloadUrl,
            plantDate = Date()

        )

        progressBar.show(this.context!!, "Updating your details...")
        mDatabase.collection("FarmDetails").document().set(farmDetails)
            .addOnSuccessListener { document ->

                Toast.makeText(activity, "Farm details inserted successfully", Toast.LENGTH_LONG)
                    .show()
                progressBar.loadingDialog.dismiss()

            }.addOnFailureListener { exception ->

                Toast.makeText(activity, "Failed to insert farm details", Toast.LENGTH_LONG).show()

            }
    }

}
