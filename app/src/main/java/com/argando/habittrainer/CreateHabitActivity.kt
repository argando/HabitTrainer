package com.argando.habittrainer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import com.argando.habittrainer.databinding.ActivityCreateHabitBinding
import com.argando.habittrainer.db.HabitDBTable
import java.io.IOException

class CreateHabitActivity : AppCompatActivity() {
    private val TAG = AppCompatActivity::class.java.simpleName
    private val requestCodeChooseImage = 4200
    private lateinit var binding: ActivityCreateHabitBinding
    private lateinit var habitBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chooseHabitImage.setOnClickListener {
            Log.i(TAG, "Add image on click")

            Intent().let {
                it.type = "image/*"
                it.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(it, getString(R.string.choose_image_title)),
                    requestCodeChooseImage
                )
                Log.i(TAG, "Intent to choose image for habit")

            }
        }

        binding.saveHabit.setOnClickListener {
            storeHabit()
        }
    }

    private fun storeHabit() {
        if (binding.editTitle.isBlack() &&
            binding.editDescription.isBlack()
        ) {
            displayErrorMessage(getString(R.string.descr_error))
            return
        }


        val title = binding.editTitle.text.toString()
        val desc = binding.editDescription.text.toString()
        val habit = Habit(title, desc, habitBitmap)
        val id = HabitDBTable(this).store(habit)
        if (id == -1L) {
            Log.e(TAG, getString(R.string.habit_store_error))
            displayErrorMessage(getString(R.string.habit_store_error))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun displayErrorMessage(message: String) {
        binding.errorSave.text = message
        binding.errorSave.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeChooseImage && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                setHabitImage(it)
            }
        }
    }

    private fun setHabitImage(data: Uri) {
        val bitmap: Bitmap? = readUriToBitmap(data)
        bitmap?.let {
            binding.habitImage.setImageBitmap(bitmap)
            binding.saveHabit.isEnabled = true
            habitBitmap = bitmap
        }
    }

    private fun readUriToBitmap(data: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(contentResolver, data)
        } catch (e: IOException) {
            return null
        }
    }
}

fun EditText.isBlack(): Boolean = this.text.isBlank()