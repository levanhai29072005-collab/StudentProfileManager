package com.ute.studentprofilemanager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ute.studentprofilemanager.databinding.ActivityEditProfileBinding
import java.io.Serializable

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var originalStudent: Student? = null

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhan du lieu sinh vien cu va hien thi len form
        originalStudent = intent.serializable<Student>("STUDENT")
        originalStudent?.let {
            binding.edtName.setText(it.name)
            binding.edtClass.setText(it.className)
            binding.edtGpa.setText(it.gpa.toString())
        }

        // Nut Luu va Phan Hoi
        binding.btnSave.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val className = binding.edtClass.text.toString().trim()
            val gpa = binding.edtGpa.text.toString().toDoubleOrNull()

            if (name.isEmpty() || className.isEmpty() || gpa == null || gpa !in 0.0..4.0) {
                Toast.makeText(this, "Vui lòng nhập tên, lớp và GPA từ 0.0 đến 4.0!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = originalStudent?.copy(name = name, className = className, gpa = gpa)
                ?: Student("2415053122316", name, className, "vanhai@ute.udn.vn", gpa)

            val resultIntent = Intent().apply {
                putExtra("UPDATED", updatedStudent)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Nut Huy Bo
        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}