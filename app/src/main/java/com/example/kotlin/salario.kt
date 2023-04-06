package com.example.kotlin
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class salario : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var  drawer: DrawerLayout
    private lateinit var  toogle: ActionBarDrawerToggle
    private lateinit var  dbRef: DatabaseReference
    private lateinit var  nombre :EditText
    private lateinit var  salario : EditText
    private lateinit var btn : Button
    private lateinit var iss : EditText
    private lateinit var afp :EditText
    private lateinit var Renta: EditText
    private lateinit var totalapagar: EditText
    private lateinit var saveddata: Button
    private lateinit var fetching : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario)
        //inicializacion de kotlin
        val firebase :DatabaseReference = FirebaseDatabase.getInstance().getReference()

        //finalizacion de inicializacion
        val toolbar: androidx.appcompat.widget.Toolbar= findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer=findViewById(R.id.drawer_layout)
        toogle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener (this)

         nombre = findViewById(R.id.txt_sal_nombre)
         salario = findViewById(R.id.txt_sal_salario)
         btn = findViewById(R.id.btn_sal_calcular)
         iss = findViewById(R.id.txt_sal_salarioISS)
         afp =findViewById(R.id.txt_sal_salarioAFP)
         Renta = findViewById(R.id.txt_sal_renta)
         totalapagar = findViewById(R.id.txt_sal_salariototal)
         saveddata = findViewById(R.id.btn_sal_savedata)
         fetching = findViewById(R.id.btn_sal_mostrardata)

        btn.setOnClickListener {
            val salariobtn = salario.text.toString().toFloat()
            val RestaISS = (salariobtn * 0.03)
            val RestaAFP = (salariobtn * 0.04)
            val RestaRenta = (salariobtn *0.05)
            val promedioDescuento = (RestaISS + RestaAFP +RestaRenta)
            val total = salariobtn - promedioDescuento
            iss.setText(RestaISS.toString())
            afp.setText(RestaAFP.toString())
            Renta.setText(RestaRenta.toString())
            totalapagar.setText(total.toString())
        }
        //firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")
        saveddata.setOnClickListener {
            saveEmployeeData()
        }
        fetching.setOnClickListener {
            val intent = Intent(this,SalaryFetchingActivity::class.java)
            startActivity(intent)
        }
    }
    private fun saveEmployeeData() {
        //gettin values
        val empName = nombre.text.toString()
        val empSalario =salario.text.toString()
        val empIss =  iss.text.toString()
        val  empAfp = afp.text.toString()
        val empRenta = Renta.text.toString()
        val empTotal = totalapagar.text.toString()
        if(empName.isEmpty()||empSalario.isEmpty()||empIss.isEmpty()||empAfp.isEmpty()||empRenta.isEmpty()||empTotal.isEmpty()){
            Toast.makeText(this,"Asegurate de llenar los campos o haz el calculo primero",Toast.LENGTH_LONG).show()
        }
        //firebase
        val empID = dbRef.push().key!!
        val employee =EmployeeModel(empID,empName,empSalario,empIss,empAfp,empRenta,empTotal)
        dbRef.child(empID).setValue(employee)
            .addOnCompleteListener{
                Toast.makeText(this,"Se agrego correctamente",Toast.LENGTH_LONG).show()
                nombre.text.clear()
                salario.text.clear()
                iss.text.clear()
                afp.text.clear()
                Renta.text.clear()
                totalapagar.text.clear()
            }.addOnFailureListener{
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
