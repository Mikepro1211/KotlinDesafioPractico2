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

class AverageFetchingActivity : AppCompatActivity() {
    private lateinit var  stRecycleView : RecyclerView
    private lateinit var  tvLoadingData : TextView
    private lateinit var stuList: ArrayList<StudentModel>
    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_average_fetching)
        stRecycleView = findViewById(R.id.rvStudent)
        stRecycleView.layoutManager =LinearLayoutManager(this)
        stRecycleView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        stuList = arrayListOf<StudentModel>()
        getStudentData()

    }

    private fun getStudentData() {
        stRecycleView.visibility = View.GONE
        tvLoadingData.visibility= View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("Student")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                stuList.clear()
                if(snapshot.exists()){
                    for(stuSnap in snapshot.children){
                        val stuData =stuSnap.getValue(StudentModel::class.java)
                        stuList.add(stuData!!)
                    }
                    val mAdapter =stuAdapter(stuList)
                    stRecycleView.adapter =mAdapter
                    mAdapter.setOnItemClickListener(object :stuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@AverageFetchingActivity, StudentDetailsActivity::class.java)
                            //put extras
                            intent.putExtra("stId",stuList[position].stId)
                            intent.putExtra("stNombre",stuList[position].stNombre)
                            intent.putExtra("stNota1",stuList[position].stNota1)
                            intent.putExtra("stNota2",stuList[position].stNota2)
                            intent.putExtra("stNota3",stuList[position].stNota3)
                            intent.putExtra("stNota4",stuList[position].stNota4)
                            intent.putExtra("stNota5",stuList[position].stNota5)
                            intent.putExtra("stPromedio",stuList[position].stPromedio)
                            startActivity(intent)

                        }


                    })
                    stRecycleView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }
}