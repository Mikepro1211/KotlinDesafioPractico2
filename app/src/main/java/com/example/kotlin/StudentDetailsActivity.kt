package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class StudentDetailsActivity : AppCompatActivity() {
    private lateinit var tvStuId:TextView
    private lateinit var tvStuName:TextView
    private lateinit var tvStuNota1:TextView
    private lateinit var tvStuNota2:TextView
    private lateinit var tvStuNota3:TextView
    private lateinit var tvStuNota4:TextView
    private lateinit var tvStuNota5:TextView
    private lateinit var tvStuPromedio:TextView
    private lateinit var btnUpdate:Button
    private lateinit var btnDelete:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)
        initView()
        setValuesToViews()
        btnUpdate.setOnClickListener {
            openUpdateDialog(
               intent.getStringExtra("stId").toString(),
                intent.getStringExtra("stNombre").toString()

            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("stId").toString(),
            )
        }
    }

    private fun openUpdateDialog(stId:String, stNombre: String) {
        val mDialog  = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val  mDialogView = inflater.inflate(R.layout.updated_dialog_student, null)
        mDialog.setView(mDialogView)
        val etStuName = mDialogView.findViewById<EditText>(R.id.etStuName)
        val etStuNota1 =mDialogView.findViewById<EditText>(R.id.etStuNota1)
        val etStuNota2 =mDialogView.findViewById<EditText>(R.id.etStuNota2)
        val etStuNota3 =mDialogView.findViewById<EditText>(R.id.etStuNota3)
        val etStuNota4 =mDialogView.findViewById<EditText>(R.id.etStuNota4)
        val etStuNota5 =mDialogView.findViewById<EditText>(R.id.etStuNota5)
        val etPromedio =mDialogView.findViewById<EditText>(R.id.etStuPromedio)

        val btnUpdateData =mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etStuName.setText(intent.getStringExtra("stNombre").toString())
        etStuNota1.setText(intent.getStringExtra("stNota1").toString())
        etStuNota2.setText(intent.getStringExtra("stNota2").toString())
        etStuNota3.setText(intent.getStringExtra("stNota3").toString())
        etStuNota4.setText(intent.getStringExtra("stNota4").toString())
        etStuNota5.setText(intent.getStringExtra("stNota5").toString())
        etPromedio.setText(intent.getStringExtra("stPromedio").toString())
        mDialog.setTitle("Updating $stNombre Record")
        val alertDialog =mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateStuData(
                stId,
                etStuName.text.toString(),
                etStuNota1.text.toString(),
                etStuNota2.text.toString(),
                etStuNota3.text.toString(),
                etStuNota4.text.toString(),
                etStuNota5.text.toString(),
                etPromedio.text.toString()
            )
            Toast.makeText(applicationContext,"Student Data updated",Toast.LENGTH_LONG).show()
            //we are setting updated
            tvStuName. text =etStuName.text.toString()
            tvStuNota1.text =etStuNota1.text.toString()
            tvStuNota2.text =etStuNota2.text.toString()
            tvStuNota3.text =etStuNota3.text.toString()
            tvStuNota4.text =etStuNota4.text.toString()
            tvStuNota5.text =etStuNota5.text.toString()
            tvStuPromedio.text=etPromedio.text.toString()
            alertDialog.dismiss()
        }


    }

    private fun deleteRecord(id: String) {
         val dbRef= FirebaseDatabase.getInstance().getReference("Student").child(id)
         val mTask = dbRef.removeValue()
        mTask.addOnSuccessListener {
            Toast.makeText(this,"Se borro con exito",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,AverageFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{
            error->Toast.makeText(this,"Error ${error.message}",Toast.LENGTH_LONG).show()
        }
    }

    private fun updateStuData(
        id:String , name:String, nota1:String,nota2:String,nota3:String,nota4:String,nota5:String,promedio:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Student").child(id)
        val StuInfo = StudentModel(id,name,nota1,nota2,nota3,nota4,nota5,promedio)
        dbRef.setValue(StuInfo)
    }


    private fun initView() {
        tvStuId = findViewById(R.id.tvStuId)
        tvStuName = findViewById(R.id.tvStuName)
        tvStuNota1 = findViewById(R.id.tvStuNota1)
        tvStuNota2= findViewById(R.id.tvStuNota2)
        tvStuNota3= findViewById(R.id.tvStuNota3)
        tvStuNota4= findViewById(R.id.tvStuNota4)
        tvStuNota5= findViewById(R.id.tvStuNota5)
        tvStuPromedio = findViewById(R.id.tvStuPromedio)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews(){
        tvStuId.text = intent.getStringExtra("stId")
        tvStuName.text=intent.getStringExtra("stNombre")
        tvStuNota1.text=intent.getStringExtra("stNota1")
        tvStuNota2.text=intent.getStringExtra("stNota2")
        tvStuNota3.text= intent.getStringExtra("stNota3")
        tvStuNota4.text=intent.getStringExtra("stNota4")
        tvStuNota5.text=intent.getStringExtra("stNota5")
        tvStuPromedio.text=intent.getStringExtra("stPromedio")
    }
}