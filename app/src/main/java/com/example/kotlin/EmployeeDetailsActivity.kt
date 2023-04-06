package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.*
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmpSalario: TextView
    private lateinit var tvIss : TextView
    private lateinit var tvAFP : TextView
    private lateinit var tvRenta: TextView
    private lateinit var tvSalarioNeto : TextView
    private lateinit var btnUpdate : Button
    private lateinit var btnDelete: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)
        initView()
        setValuesToView()
        btnUpdate.setOnClickListener {
            openUpdatedDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString(),
                intent.getStringExtra("empSalario").toString(),
                intent.getStringExtra("empIss").toString(),
                intent.getStringExtra("empAfp").toString(),
                intent.getStringExtra("empRenta").toString(),
                intent.getStringExtra("empTotal").toString()
            )
        }
        btnDelete.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }
    }
    private fun initView() {
        tvEmpId =findViewById(R.id.tvEmpId)
        tvEmpName= findViewById(R.id.tvEmpName)
        tvEmpSalario= findViewById(R.id.tvEmpAge)
        tvIss= findViewById(R.id.tvEmpISS)
        tvAFP = findViewById(R.id.tvEmpAFP)
        tvRenta = findViewById(R.id.tvEmpRenta)
        tvSalarioNeto = findViewById(R.id.tvEmpNeto)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }
    private fun setValuesToView(){
        tvEmpId.text =intent.getStringExtra("empId")
        tvEmpName.text=intent.getStringExtra("empName")
        tvEmpSalario.text=intent.getStringExtra("empSalario")
        tvIss.text =intent.getStringExtra("empIss")
        tvAFP.text =intent.getStringExtra("empAfp")
        tvRenta.text = intent.getStringExtra("empRenta")
        tvSalarioNeto.text =intent.getStringExtra("empTotal")
    }
    private fun openUpdatedDialog(
        empId: String,
        empName:String,
        empSalario: String,
        empIss: String,
        empAfp:String,
        empRenta: String,
        empTotal: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater =layoutInflater
        val mDialogView =inflater.inflate(R.layout.updated_dialog,null)
        mDialog.setView(mDialogView)
        val etEmpName =  mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpSalario =mDialogView.findViewById<EditText>(R.id.etEmpSalario)
        val etEmpIss =mDialogView.findViewById<EditText>(R.id.etEmpIss)
        val etEmpAfp =mDialogView.findViewById<EditText>(R.id.etEmpAFP)
        val etEmpRenta = mDialogView.findViewById<EditText>(R.id.etEmpRenta)
        val etEmpTotal = mDialogView.findViewById<EditText>(R.id.etEmpTotal)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)
        etEmpName.setText(intent.getStringExtra("empName").toString())
        etEmpSalario.setText(intent.getStringExtra("empSalario").toString())
        etEmpIss.setText(intent.getStringExtra("empIss").toString())
        etEmpAfp.setText(intent.getStringExtra("empAfp").toString())
        etEmpRenta.setText(intent.getStringExtra("empRenta").toString())
        etEmpTotal.setText(intent.getStringExtra("empTotal").toString())
        mDialog.setTitle("Updating $empName Record")
        val alertDialog = mDialog.create()
        alertDialog.show()
        btnUpdateData.setOnClickListener {
            updateEmpData(
                empId,
                etEmpName.text.toString(),
                etEmpSalario.text.toString(),
                etEmpIss.text.toString(),
                etEmpAfp.text.toString(),
                etEmpRenta.text.toString(),
                etEmpTotal.text.toString()
            )
            Toast.makeText(applicationContext,"Informacion modificada",Toast.LENGTH_LONG).show()
            //setting updated data
            tvEmpName.text= etEmpName.text.toString()
            alertDialog.dismiss()
        }
    }
    private fun deleteRecord(id: String){
        val dbRef =FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()
        mTask.addOnSuccessListener {
            Toast.makeText(this,"Informacion borrada",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,SalaryFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{
            error ->Toast.makeText(this,"Informacion no ha sido borrada",Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateEmpData( id : String, name:String,salario: String, iss:String,afp:String,renta:String,total:String){
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo =EmployeeModel(id, name,salario,iss, afp,renta, total)
        dbRef.setValue(empInfo)
    }
}