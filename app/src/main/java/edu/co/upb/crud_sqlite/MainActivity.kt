package edu.co.upb.crud_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.E

class MainActivity : AppCompatActivity() {
    private lateinit var etNombreEst: EditText
    private lateinit var etCorreoEst: EditText
    private lateinit var etCursoEst: EditText
    private lateinit var btnListar: Button
    private lateinit var btnActualizar: Button

    private lateinit var SQLiteHelper: SQLHelper
    private lateinit var estModel: EstModel
    private var adapter: EstAdapter?=null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initView()
        initRecyclerView()
        SQLiteHelper = SQLHelper(this)

        findViewById<Button>(R.id.buttonAgregar).setOnClickListener{addEst()}
        btnListar.setOnClickListener{getStudents()}

        btnActualizar.setOnClickListener{ updateStudent()}

        adapter?.setOnClickItem {
            Toast.makeText(this, it.nombre, Toast.LENGTH_SHORT).show()

            etNombreEst.setText(it.nombre)
            etCorreoEst.setText(it.correo)
            etCursoEst.setText(it.curso)
            estModel = it

        }

        adapter?.setOnClickDeleteItem {
            it.id?.let{it1-> deleteStudent(it1) }
        }



    }

    private fun getStudents() {
        val estList = SQLiteHelper.getListEst()
        adapter?.addItems(estList)

    }
    private fun clearText(){
        etNombreEst.setText("")
        etCorreoEst.setText("")
        etCursoEst.setText("")
        etNombreEst.requestFocus()
    }

    private fun deleteStudent(id: Int){
        if (id == null ) return

        val builder= AlertDialog.Builder(this)
        builder.setMessage("Desea Eliminar Permanentemente Esta Informacion")
        builder.setCancelable(true)
        builder.setPositiveButton("Si"){ dialog,_ ->
            SQLiteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()

        }
        builder.setNegativeButton("No"){dialog,_ ->
            dialog.dismiss()


        }

        val alert = builder.create()
        alert.show()

    }

    private fun updateStudent(){
        val nombre = etNombreEst.text.toString()
        val correo = etCorreoEst.text.toString()
        val curso = etCursoEst.text.toString()

        if (nombre == estModel?.nombre && correo == estModel?.correo && curso == estModel?.curso){
            Toast.makeText(this, "VALORES IGUALES, NO SE ACTUALIZARA", Toast.LENGTH_SHORT).show()
            return
        }
        if (estModel== null) return

        val estAct = EstModel(estModel!!.id,nombre, correo, curso)
        val status = SQLiteHelper.actualizarEst(estAct)
        if (status >- 1){
            clearText()
            getStudents()

        }else{
            Toast.makeText(this, "ERROR AL ACTUALIZAR", Toast.LENGTH_SHORT).show()

        }


    }

    private fun addEst(){
        val nombre = findViewById<EditText>(R.id.etNombreEst).text.toString()
        val correo = findViewById<EditText>(R.id.etCorreoEst).text.toString()
        val curso = findViewById<EditText>(R.id.etCursoEst).text.toString()

        if (nombre.isEmpty() || curso.isEmpty()){
            Toast.makeText(this, "Por Favor Ingrese Los Campos Requeridos", Toast.LENGTH_SHORT).show()

        }else {
            val est = EstModel( null, nombre, correo, curso)
            val status = SQLiteHelper.insertEst(est)
            if (status >-1){
                Toast.makeText(this, "Se Agrego Correctamente El Estudiante", Toast.LENGTH_SHORT).show()
                clearText()

            }else
                Toast.makeText(this, "Ocurrio Un Error Al Guardar El Estudiante", Toast.LENGTH_SHORT).show()

        }

    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EstAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        etNombreEst = findViewById(R.id.etNombreEst)
        etCorreoEst = findViewById(R.id.etCorreoEst)
        etCursoEst = findViewById(R.id.etCursoEst)
        btnListar = findViewById(R.id.buttonListar)
        btnActualizar = findViewById(R.id.buttonActualizar)
        recyclerView = findViewById(R.id.recyclerView)

    }
}