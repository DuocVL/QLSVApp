package com.example.qlsvapp
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanager.adapter.StudentAdapter


class MainActivity : AppCompatActivity(), StudentAdapter.OnItemClickListener {

    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private val studentList = mutableListOf(
        Student(1, "Nguyễn Văn A", "SV001", "a.nguyen@example.com", "0901234567"),
        Student(2, "Trần Thị B", "SV002", "b.tran@example.com", "0987654321")
    )
    private var selectedStudent: Student? = null
    private var selectedPosition: Int = -1

    private lateinit var addStudentLauncher: ActivityResultLauncher<Intent>
    private lateinit var updateStudentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentRecyclerView = findViewById(R.id.studentRecyclerView)
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(studentList, this)
        studentRecyclerView.adapter = studentAdapter

        // Đăng ký RecyclerView cho Context Menu
        registerForContextMenu(studentRecyclerView)



        // Khởi tạo ActivityResultLauncher cho thêm sinh viên
        addStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getSerializableExtra("Student")?.let { newStudent  ->
                    studentList.add(newStudent as Student)
                    studentAdapter.notifyItemInserted(studentList.size - 1)
                }
            }
        }

        // Khởi tạo ActivityResultLauncher cho cập nhật sinh viên
        updateStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getSerializableExtra("Student")?.let { updatedStudent ->
                    val updatedStudent = updatedStudent as Student
                    val index = studentList.indexOfFirst { it.id == updatedStudent.id }
                    if (index != -1) {
                        studentList[index] = updatedStudent
                        studentAdapter.notifyItemChanged(index)
                    }
                }
            }
        }
    }

    // Option Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                val newId = (studentList.maxOfOrNull { it.id } ?: 0) + 1
                intent.putExtra("NewId", newId)
                addStudentLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Context Menu
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == R.id.studentRecyclerView && selectedPosition in studentList.indices) {
            menuInflater.inflate(R.menu.menu_context, menu)
        }

    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.capNhat -> {
                selectedStudent?.let { student ->
                    val intent = Intent(this, AddStudentActivity::class.java)
                    intent.putExtra("Student", student)
                    updateStudentLauncher.launch(intent)
                }
                true
            }
            R.id.xoa -> {
                showConfirmationDialog(selectedPosition)
                true
            }
            R.id.phone -> {
                selectedStudent?.let { student ->
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${student.phone}"))
                    startActivity(intent)
                }
                true
            }
            R.id.email-> {
                selectedStudent?.let { student ->
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${student.email}")
                    }
                    startActivity(intent)
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên này?")
            .setPositiveButton("Xóa") { _, _ ->
                studentAdapter.removeItem(position)
                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    override fun onItemLongClick(position: Int, student: Student) {
        selectedPosition = position
        selectedStudent = student
    }


}