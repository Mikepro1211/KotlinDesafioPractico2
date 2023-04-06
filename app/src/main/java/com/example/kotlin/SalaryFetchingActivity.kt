package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SalaryFetchingActivity : AppCompatActivity() {
    private lateinit var  empRecycleView :RecyclerView
    private lateinit var  tvLoadingData :TextView
    private lateinit var empList: ArrayList<EmployeeModel>
    private lateinit var dbRef :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salary_fetching)
        empRecycleView = findViewById(R.id.rvEmployee)
        empRecycleView.layoutManager =LinearLayoutManager(this)
        empRecycleView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        empList = arrayListOf<EmployeeModel>()
        getEmployeesData()
    }

    private fun getEmployeesData() {
        empRecycleView.visibility = View.GONE
        tvLoadingData.visibility =View.VISIBLE
         dbRef = FirebaseDatabase.getInstance().getReference("Employees")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for(empSnap in snapshot.children){
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    empRecycleView.adapter =mAdapter
                    mAdapter.setOnItemClickListener(object :EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent =Intent(this@SalaryFetchingActivity,EmployeeDetailsActivity::class.java)
                            //put extras
                            intent.putExtra("empId",empList[position].empId)
                            intent.putExtra("empName",empList[position].empName)
                            intent.putExtra("empSalario",empList[position].empSalario)
                            intent.putExtra("empIss",empList[position].empIss)
                            intent.putExtra("empAfp",empList[position].empAfp)
                            intent.putExtra("empRenta",empList[position].empRenta)
                            intent.putExtra("empTotal",empList[position].empTotal)
                            startActivity(intent)
                        }

                    })
                    empRecycleView.visibility =View.VISIBLE
                    tvLoadingData.visibility= View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}