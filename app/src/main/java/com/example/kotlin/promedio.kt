package com.example.kotlin

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class promedio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var  drawer: DrawerLayout
    private lateinit var  toogle: ActionBarDrawerToggle
    private lateinit var  dbRef :DatabaseReference
    private lateinit var  nombre: EditText
    private lateinit var  nota1 : EditText
    private lateinit var  nota2 : EditText
    private lateinit var  nota3 : EditText
    private lateinit var  nota4 : EditText
    private lateinit var  nota5 : EditText
    private lateinit var  promedio :EditText
    private lateinit var estado:EditText
    private lateinit var btn: Button
    private lateinit var saveddata: Button
    private lateinit var fetching:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promedio)
        /*Inicializacion de FireBase
        * */
        val firebase:DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val toolbar: androidx.appcompat.widget.Toolbar= findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer=findViewById(R.id.drawer_layout)
        toogle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener (this)

          nombre = findViewById(R.id.txt_pro_nombre)
          nota1  = findViewById(R.id.txt_pro_Nota1)
          nota2  = findViewById(R.id.txt_pro_nota2)
          nota3  = findViewById(R.id.txt_pro_nota3)
          nota4  = findViewById(R.id.txt_pro_nota4)
          nota5  = findViewById(R.id.txt_pro_nota5)
          promedio = findViewById(R.id.txt_pro_promedio)
          estado = findViewById(R.id.txt_pro_estado)
          btn =  findViewById(R.id.btn_pro_calcular)
          saveddata = findViewById(R.id.btn_pro_savedata)
          fetching = findViewById(R.id.btn_pro_mostrardata)

         btn.setOnClickListener {
              val score1  = nota1.text.toString().toFloat()
              val score2  = nota2.text.toString().toFloat()
              val score3  = nota3.text.toString().toFloat()
              val score4  = nota4.text.toString().toFloat()
              val score5  = nota5.text.toString().toFloat()
              val  average = (score1+score2+score3+score4+score5)/5
              nota1.setText(score1.toString())
              nota2.setText(score2.toString())
              nota3.setText(score3.toString())
              nota4.setText(score4.toString())
              nota5.setText(score5.toString())
              promedio.setText(average.toString())
         }
        fetching.setOnClickListener {
            val intent = Intent(this,AverageFetchingActivity::class.java)
            startActivity(intent)
        }
        //firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Student")
        saveddata.setOnClickListener {
            savedStudenData()
        }

    }

    private fun savedStudenData() {
        //getting values
        val stName = nombre.text.toString()
        val stNota1 = nota1.text.toString()
        val stNota2 = nota2.text.toString()
        val stNota3 = nota3.text.toString()
        val stNota4 = nota4.text.toString()
        val stNota5 = nota5.text.toString()
        val stPromedio = promedio.text.toString()
        if(stName.isEmpty()||stNota1.isEmpty()||stNota2.isEmpty()||stNota3.isEmpty()||stNota4.isEmpty()||stNota5.isEmpty()||stPromedio.isEmpty()){
            Toast.makeText(this,"Asegurate de llenar los campos o haz el calculo primero",Toast.LENGTH_LONG).show()
        }
        //firebase
         val stId =dbRef.push().key!!
        val student = StudentModel(stId,stName,stNota1,stNota2,stNota3,stNota4,stNota5,stPromedio)
        dbRef.child(stId).setValue(student)
            .addOnCompleteListener{
                Toast.makeText(this,"Se agrego correctamente",Toast.LENGTH_LONG).show()
                nombre.text.clear()
                nota1.text.clear()
                nota2.text.clear()
                nota3.text.clear()
                nota4.text.clear()
                nota5.text.clear()
                promedio.text.clear()
            }.addOnFailureListener {
                    error->Toast.makeText(this,"No fueron agregados correctamente ",Toast.LENGTH_LONG).show()
            }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_one -> {
                Toast.makeText(this,"Voy para promedio", Toast.LENGTH_LONG).show()
                val intent = Intent(this,promedio::class.java)
                startActivity(intent)
            }
            R.id.nav_item_two -> {
                Toast.makeText(this,"Voy para deducciones", Toast.LENGTH_LONG).show()
                val intent = Intent(this,salario::class.java)
                startActivity(intent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onPostCreate(savedInstanceState: Bundle?){
        super.onPostCreate(savedInstanceState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}