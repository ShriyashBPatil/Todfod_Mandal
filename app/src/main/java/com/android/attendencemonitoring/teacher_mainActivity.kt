package com.android.attendencemonitoring

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

// ✅ Renamed from Student to Student2 to avoid conflicts
data class Student2(
    val name: String = "",
    val rollNo: String = "",
    val Class: String = "",
    val email: String = ""
)

// ✅ RecyclerView Adapter
class StudentAdapter(private val studentList: List<Student2>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.student_name)
        val rollNo: TextView = itemView.findViewById(R.id.student_rollno)
        val stdClass: TextView = itemView.findViewById(R.id.student_class)
        val email: TextView = itemView.findViewById(R.id.student_email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.name.text = student.name
        holder.rollNo.text = "Roll No: ${student.rollNo}"
        holder.stdClass.text = "Class: ${student.Class}"
        holder.email.text = "Email: ${student.email}"
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}

// ✅ Activity to Fetch Data and Display in RecyclerView
class teacher_mainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val studentList = mutableListOf<Student2>() // ✅ Used MutableList to prevent crash
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Initialize adapter with an empty list first
        adapter = StudentAdapter(studentList)
        recyclerView.adapter = adapter

        fetchStudents() // ✅ Now fetch data and update adapter
    }

    private fun fetchStudents() {
        firestore.collection("Students")
            .get()
            .addOnSuccessListener { documents ->
                studentList.clear()
                for (document in documents) {
                    val student = document.toObject(Student2::class.java)
                    studentList.add(student)
                }
                adapter.notifyDataSetChanged() // ✅ Notify adapter about new data
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
    }
}
