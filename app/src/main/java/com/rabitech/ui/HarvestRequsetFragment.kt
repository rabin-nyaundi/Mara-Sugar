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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rabitech.R
import com.rabitech.dataModels.CustomLoading
import com.rabitech.dataModels.FarmDetails
import com.rabitech.dataModels.LocationDetails
import com.rabitech.dataModels.UserDetails
import com.rabitech.databinding.FragmentHarvestRequsetBinding
import kotlinx.android.synthetic.main.fragment_harvest_requset.*
import java.io.IOException


class HarvestRequsetFragment : Fragment() {

    private lateinit var binding: FragmentHarvestRequsetBinding
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    private val REQ_CODE_IMAGE_GALLERY = 0
    private var selectedImageUri: Uri? = null

    //personal details
    private var firstname = ""
    private var lastname = ""
    private var phone = ""
    private var email = ""
    private var id_number = ""
    private var time_request_sent = ""


    //location details
    private var constituency = ""
    private var ward = ""
    private var landmark = ""

    //farm details
    private var farmSize = ""
    private var downloadUrl = ""
    private var status_of_request ="Awaiting Approval"

    val progressBar = CustomLoading()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_harvest_requset, container, false)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_harvest_requset, container, false)

        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        retrievePersonalData()
        retrieveLocation()

        binding.submitRequest.setOnClickListener {
            progressBar.show(this.context!!,"Sending request")
            validateUserInput()
            uploadImage()
            progressBar.loadingDialog.dismiss()

        }
        binding.imageFarm.setOnClickListener {
            showSelectedPictureDialog()
        }

        binding.btnEdit.setOnClickListener {
            editField()
        }

        return binding.root
    }


// retrieve details from the database

    private fun retrievePersonalData() {

        val userId = mAuth.currentUser!!.uid
        val user_email = mAuth.currentUser!!.email

        val ref = mDatabase.collection("UserDetails").document(userId)
        ref.get()
            .addOnSuccessListener { documentReference ->
                if (documentReference != null) {

                    //personal details
                    firstname = documentReference.getString("user_fname").toString()
                    lastname = documentReference.getString("user_lname").toString()
                    phone = documentReference.getString("user_phone").toString()
                    email = user_email.toString()
                    id_number = documentReference.getString("user_National_id").toString()

                    binding.textFname.setText(firstname)
                    binding.textLname.setText(lastname)
                    binding.textPhone.setText(phone)
                    binding.textEmail.setText(email)
                    binding.textIdcard.setText(id_number)

                    disableField()


                    Log.d(
                        TAG,
                        "@@@@@@@@@@@@@DocumentSnapshot data@@@@@@@@@@@@: ${documentReference.data}"
                    )
                    Log.d(
                        TAG,
                        "@@@@@@@@@@@@@DocumentSnapshot data@@@@@@@@@@@@: ${documentReference.data}" + firstname
                    )
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
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

    //Disable and enable edit text fields
    private fun editField() {

        binding.textFname.setFocusableInTouchMode(true)
        binding.textLname.setFocusableInTouchMode(true)
        binding.textPhone.setFocusableInTouchMode(true)
        binding.textEmail.setFocusableInTouchMode(true)
        binding.textIdcard.setFocusableInTouchMode(true)
    }

    private fun disableField() {

        binding.textFname.setFocusable(false)
        binding.textLname.setFocusable(false)
        binding.textPhone.setFocusable(false)
        binding.textEmail.setFocusable(false)
        binding.textIdcard.setFocusable(false)

    }

    //select and upload image from gallery

    //Validate user inputs
    private fun validateUserInput() {

        //farm details
        firstname = binding.textFname.text.toString()
        lastname = binding.textLname.text.toString()
        phone = binding.textPhone.text.toString()
        email = binding.textEmail.text.toString()
        id_number = binding.textIdcard.text.toString()
//        time_request_sent = Timestamp.now()

        //location details
        constituency = binding.textConstituency.text.toString()
        ward = binding.textWard.text.toString()
        landmark = binding.landmark.text.toString()

        //farm details
        farmSize = binding.textFarmSize.text.toString()
//        downloadUrl = binding.imageFarm.transitionName.toString()

        if (firstname.isEmpty()) {
            text_fname.error = "Please enter the first name"
            text_fname.requestFocus()
            return
        }
        if (lastname.isEmpty()) {
            text_lname.error = "Please enter the last name"
            text_lname.requestFocus()
            return
        }
        if (phone.isEmpty()) {
            text_phone.error = "Please enter the phone number"
            text_phone.requestFocus()
            return
        }
        if (email.isEmpty()) {
            text_email.error = "Please enter the email address"
            text_email.requestFocus()
            return
        }
        if (id_number.isEmpty()) {
            text_idcard.error = "Please enter the National ID Number"
            text_idcard.requestFocus()
            return
        }
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

        insertDetails()
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

    // insert personal , location and farm details to firestore
    private fun insertDetails() {

        val userId = mAuth.currentUser!!.uid

        val userdetails = UserDetails(
            firstname,
            lastname,
            phone,
            email,
            id_number,
            Timestamp.now(),
            false

        )
        mDatabase.collection("UserDetails").document(userId).set(userdetails)
            .addOnSuccessListener { document ->

                Toast.makeText(activity, "Personal details inserted sccessflly", Toast.LENGTH_LONG)

                Log.d("@@@@insertion@@@@", "details added Successfully ${document}")

            }.addOnFailureListener { exception ->

                Toast.makeText(activity, "Faled to insert personal details", Toast.LENGTH_LONG)
                    .show()

                Log.d(
                    "@@@@insertion failed@",
                    "User details insertion failed ${exception.localizedMessage}"
                )

            }
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
            downloadUrl

        )

        mDatabase.collection("FarmDetails").document().set(farmDetails)
            .addOnSuccessListener { document ->
                Toast.makeText(activity, "Farm details inserted successfully", Toast.LENGTH_LONG)
                    .show()

            }.addOnFailureListener { exception ->

                Toast.makeText(activity, "Failed to insert farm details", Toast.LENGTH_LONG).show()

            }
    }

}
