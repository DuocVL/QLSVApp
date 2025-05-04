package com.example.qlsvapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    private lateinit var etHoTen: EditText
    private lateinit var etMssv: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSdt: EditText
    private lateinit var btnSave: Button

    private var existingStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        etHoTen = findViewById(R.id.name)
        etMssv = findViewById(R.id.mssv)
        etEmail = findViewById(R.id.email)
        etSdt = findViewById(R.id.phoneNumber)
        btnSave = findViewById(R.id.buttonAdd)

        // Kiểm tra xem có dữ liệu sinh viên được truyền đến không (cho chức năng cập nhật)
        existingStudent = intent.getSerializableExtra("Student") as Student?
        existingStudent?.let { student ->
            etHoTen.setText(student.name)
            etMssv.setText(student.mssv)
            etEmail.setText(student.email)
            etSdt.setText(student.phone)
        }

        btnSave.setOnClickListener {
            val hoTen = etHoTen.text.toString().trim()
            val mssv = etMssv.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val sdt = etSdt.text.toString().trim()

            if (hoTen.isEmpty() || mssv.isEmpty()) {
                Toast.makeText(this, "Họ tên và MSSV không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent()
            val newId = existingStudent?.id ?: intent.getIntExtra("NewId", 0)
            val student = Student(
                newId, // Giữ nguyên ID nếu là cập nhật, 0 nếu là thêm mới
                hoTen,
                mssv,
                email,
                sdt
            )
            resultIntent.putExtra("Student", student)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}