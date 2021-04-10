package com.elli0tt.rpg_life.presentation.screen.character

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
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_character.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CharacterFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: CharacterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)
        navController = NavHostFragment.findNavController(this)

        initViews()
        subscribeToViewModel()
        setHasOptionsMenu(true)
    }

    private fun initViews() {
        setListeners()
    }

    fun navigateToAddCharacteristicFragment() {
        navController.navigate(R.id.action_character_screen_to_add_characteristic_fragment)
    }

    private fun subscribeToViewModel() {
        viewModel.apply {
            user.observe(viewLifecycleOwner) {
                coinsTextView.text = getString(R.string.character_coins, it.coinsCount.toString())
                levelTextView.text = getString(R.string.character_level, it.level.toString())
                levelProgressBar.max = it.xpToNextLevel
                levelProgressBar.progress = it.xpToNextLevel - it.xpLeftToNextLevel
                completedQuestsTextView.text = getString(
                        R.string.character_completed_quests,
                        it.completedQuestsCount.toString()
                )
                earnedCoinsTextView.text = getString(
                        R.string.character_earned_coins,
                        it.earnedCoinsCount.toString()
                )
            }
        }

    }

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
                .setFixAspectRatio(true)
                .setMinCropResultSize(characterImageView.width, characterImageView.height)
                //.setMaxCropResultSize(levelProgressBar.radius * 2, levelProgressBar.radius * 2)
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
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
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
        if (path.exists()) {
            characterImageView.setImageBitmap(BitmapFactory.decodeStream(FileInputStream(path)))
        }
    }

    private fun setListeners() {
        characterImageView.setOnClickListener(onLevelProgressBarClickListener)
        increaseCoinsFab.setOnClickListener(onIncreaseCoinsFabClickListener)
        decreaseCoinsFab.setOnClickListener(onDecreaseCoinsFabClickListener)
    }

    private val onLevelProgressBarClickListener = View.OnClickListener {
        pickImage()
    }

    private val onIncreaseCoinsFabClickListener = View.OnClickListener {
        viewModel.addOneCoin()
    }

    private val onDecreaseCoinsFabClickListener = View.OnClickListener {
        viewModel.takeOneCoin()
    }
}