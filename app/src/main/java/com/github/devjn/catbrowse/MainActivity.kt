package com.github.devjn.catbrowse

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.devjn.catbrowse.databinding.ActivityMainBinding
import com.github.devjn.catbrowse.util.IOHelper
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorites
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.btnAdd.setOnClickListener { selectImageForUpload() }
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED || resultCode != RESULT_OK || data == null) {
            return
        }
        when (requestCode) {
            REQUEST_CAMERA -> {
                val selectedImage = data.extras!!["data"] as Bitmap?
                // ToDo:
            }
            REQUEST_GAlERY -> {
                val selectedImage: Uri = data.data ?: return
                val part =
                    IOHelper.getMultipartBodyFromUri(contentResolver, selectedImage) ?: return
                Provider.service.uploadCatImage(part).subscribe(
                    {
                        Log.i("TAG", "success uploading image")
                    }, {
                        it.printStackTrace()
                    }
                )
            }
        }
    }

    //      -------------------

    private fun selectImageForUpload() = AlertDialog.Builder(this).apply {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        setTitle("Choose your profile picture")
        setItems(options) { dialog, item ->
            when (options[item]) {
                "Take Photo" -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, REQUEST_CAMERA)
                }
                "Choose from Gallery" -> {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, REQUEST_GAlERY)
                }
                "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
    }.show()

    companion object {
        const val REQUEST_CAMERA = 1
        const val REQUEST_GAlERY = 2
    }

}