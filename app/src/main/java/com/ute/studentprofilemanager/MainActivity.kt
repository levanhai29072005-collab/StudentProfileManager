package com.ute.studentprofilemanager

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ute.studentprofilemanager.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private val TAG = "Lifecycle_MainActivity"
    private lateinit var binding: ActivityMainBinding

    private var student = Student(
        id = "2415053122316",
        name = "Lê Văn Hải",
        className = "24t3",
        email = "vanhai@ute.udn.vn",
        gpa = 3.8
    )

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    // 1. Launcher chinh sua ho so
    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == RESULT_OK) {
            val updatedStudent = res.data?.serializable<Student>("UPDATED")
            updatedStudent?.let {
                student = it
                bindData(student)
                Toast.makeText(this, "Đã cập nhật hồ sơ!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Hủy thao tác chỉnh sửa", Toast.LENGTH_SHORT).show()
        }
    }

    // 2. Launcher lay anh tu thu vien
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.imgAvatar.setImageURI(it)
            Toast.makeText(this, "Đã đổi ảnh đại diện!", Toast.LENGTH_SHORT).show()
        }
    }

    // 3. Launcher xin quyen Camera
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Đã cấp quyền Camera thành công!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bị từ chối quyền Camera!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate duoc goi")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindData(student)

        // Nut 1: Chinh sua ho so
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java).apply {
                putExtra("STUDENT", student)
            }
            editLauncher.launch(intent)
        }

        // Nut 2: Doi anh avatar
        binding.btnChangeAvatar.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        // Nut 3: Goi dien thoai
        binding.btnCallHotline.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:02363822571")
            }
            startActivity(dialIntent)
        }

        // Nut 4: Kiem tra quyen Camera
        binding.btnRequestCamera.setOnClickListener {
            cameraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun bindData(student: Student) {
        binding.tvName.text = student.name
        binding.tvDetails.text = "MSSV: ${student.id} | Lớp: ${student.className}\nEmail: ${student.email}"
        val rank = when {
            student.gpa >= 3.6 -> "Xuất sắc"
            student.gpa >= 3.2 -> "Giỏi"
            student.gpa >= 2.5 -> "Khá"
            else -> "Trung bình"
        }
        binding.tvGpaBadge.text = "GPA: ${student.gpa} ($rank)"
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart duoc goi") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume duoc goi") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause duoc goi") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop duoc goi") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy duoc goi") }
}