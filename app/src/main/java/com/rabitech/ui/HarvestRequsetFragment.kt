package com.rabitech.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rabitech.R
import com.rabitech.dataModels.LocationDetails
import com.rabitech.dataModels.UserDetails
import com.rabitech.databinding.FragmentHarvestRequsetBinding
import kotlinx.android.synthetic.main.fragment_harvest_requset.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class HarvestRequsetFragment : Fragment() {

    private lateinit var binding: FragmentHarvestRequsetBinding
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth


    private var firstname = ""
    private var lastname = ""
    private var phone = ""
    private var email = ""
    private var id_number = ""

    private var constituency = ""
    private var ward = ""
    private var landmark = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_harvest_requset, container, false)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_harvest_requset, container, false)

        mDatabase = FirebaseFirestore.getInstance()

        mAuth = FirebaseAuth.getInstance()

        binding.submitRequest.setOnClickListener {
            insertPersonalDetals()

        }
        binding.imageFarm.setOnClickListener {
            showSelectedPictureDialog()
        }

        return binding.root

    }

    private fun showSelectedPictureDialog() {
        val pictureDialog = context?.let { AlertDialog.Builder(it) }
        pictureDialog?.setTitle("Select Image Location")
        val pictureDialogItems = arrayOf("Select imaeg fom galley", "Capture image")
        pictureDialog?.setItems(pictureDialogItems) { _, selectedOption ->
            {
                when (selectedOption) {
                    0 -> selectPhotoFromGallery()
                    else -> captureImage()
                }
            }
        }
    }

    private fun captureImage() {

    }

    private fun selectPhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(galleryIntent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (data != null) {
                val imageUrl = data.data
                try {
                    val bitmapImage =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUrl)
                    binding.imageFarm.setImageBitmap(bitmapImage)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context,"Failed to select image",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun insertPersonalDetals() {
        firstname = binding.textFname.text.toString()
        lastname = binding.textLname.text.toString()
        phone = binding.textPhone.text.toString()
        email = binding.textEmail.text.toString()
        id_number = binding.textIdcard.text.toString()

        constituency = binding.textConstituency.text.toString()
        ward = binding.textWard.text.toString()
        landmark = binding.textLocation.text.toString()


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
            text_email_login.requestFocus()
            return
        }
        if (id_number.isEmpty()) {
            text_idcard.error = "Please enter the National ID Number"
            text_idcard.requestFocus()
            return
        }
        if (constituency.isEmpty()) {
            text_constituency.error = "Please enter the National ID Number"
            text_idcard.requestFocus()
            return
        }
        if (ward.isEmpty()) {
            text_ward.error = "Please enter the National ID Number"
            text_idcard.requestFocus()
            return
        }
        if (landmark.isEmpty()) {
            text_location.error = "Please enter the National ID Number"
            text_idcard.requestFocus()
            return
        }

        insertDetails()
        locationDetails()
    }

    private fun locationDetails() {
        val locationDetails = LocationDetails(
            constituency,
            ward,
            landmark
        )
        mDatabase.collection("LocationDetails").add(locationDetails).addOnSuccessListener {
            Toast.makeText(activity, "Location details inserted sccessflly", Toast.LENGTH_LONG)
                .show()

        }.addOnFailureListener {
            Toast.makeText(activity, "Faled to insert location details", Toast.LENGTH_LONG).show()
        }
    }

    private fun insertDetails() {
//
//        val userdetails= mDatabase.collection("details").document()
//        val details =UserDetails(
//            mAuth.currentUser?.email.toString(),
//        firstn
//
//        )
        val userdetails = UserDetails(
            firstname,
            lastname,
            phone,
            email,
            id_number

        )
        mDatabase.collection("UserDetails").add(userdetails)
            .addOnSuccessListener { documentReference ->
//                Toast.makeText(context,"Successfully added to the database",Toast.LENGTH_LONG).show()
                Log.d("@@@@insertion@@@@", "details added Successfully ${documentReference.id}")

            }.addOnFailureListener { exception ->
//                Toast.makeText(context,"Failed to insert to database",Toast.LENGTH_LONG).show()

                Log.d(
                    "@@@@insertion failedd@",
                    "User details insertion failed ${exception.localizedMessage}"
                )

            }
    }


}
