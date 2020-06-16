package com.elli0tt.rpg_life.presentation.character

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.custom_view.CustomLevelProgressBar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ir.alirezaiyan.progressbar.utils.getBoundRectF
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CharacterFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: CharacteristicsViewModel
    private lateinit var levelProgressBar: CustomLevelProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CharacteristicsViewModel::class.java)
        navController = NavHostFragment.findNavController(this)
        levelProgressBar = view.findViewById(R.id.level_progress_bar)
        levelProgressBar.setOnClickListener {
            pickImage()
        }
        subscribeToViewModel()
        setHasOptionsMenu(true)
    }

    fun navigateToAddCharacteristicFragment() {
        navController.navigate(R.id.action_character_screen_to_add_characteristic_fragment)
    }

    private fun subscribeToViewModel() {}
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.character_toolbar_menu, menu)
    }

    override fun onStart() {
        super.onStart()
        loadProfileImageFromInternalStorage()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.character_toolbar_item_populate_with_samples -> {
                viewModel.populateWithSamples()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun pickImage() {

        CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMinCropResultSize(levelProgressBar.radius * 2, levelProgressBar.radius * 2)
                .setMaxCropResultSize(levelProgressBar.radius * 2, levelProgressBar.radius * 2)
                .start(requireContext(), this)
//        val pickImageIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pickImageIntent.type = "image/*"
//        pickImageIntent.putExtra("crop", "true")
//                .putExtra("aspectX", 1)
//                .putExtra("aspectY", 1)
//        startActivityForResult(Intent.createChooser(pickImageIntent, "Select picture"), Constants.PICK_IMAGE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK){
                    saveProfileImageToInternalStorage(result.uri)
                    loadProfileImageFromInternalStorage()
                }
            }
        }
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                Constants.PICK_IMAGE_REQUEST_CODE -> {
//
//                    saveProfileImageToInternalStorage(data!!.data)
//                    loadProfileImageFromInternalStorage()
//                }
//            }
//        }
    }

    private fun saveProfileImageToInternalStorage(imageUri: Uri) {
        val imageStream = requireContext().contentResolver.openInputStream(imageUri)
        val path = File(requireContext().filesDir, getString(R.string.user_images_directory_name))
        if (!path.exists()) {
            path.mkdir()
        }
        val bitmap = BitmapFactory.decodeStream(imageStream)
        val outFile = File(path, requireContext().getString(R.string.profile_image_file_name) + ".jpeg")
        val outputStream = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        imageStream?.close()
        outputStream.close()
    }

    private fun loadProfileImageFromInternalStorage() {
        val path = File(requireContext().filesDir,
                getString(R.string.user_images_directory_name) +
                        "/" +
                        getString(R.string.profile_image_file_name) +
                        ".jpeg")
        if (path.exists()){
            levelProgressBar.setImage(BitmapFactory.decodeStream(FileInputStream(path)))
        }
    }
}