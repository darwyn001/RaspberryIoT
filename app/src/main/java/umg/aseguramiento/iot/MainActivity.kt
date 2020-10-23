package umg.aseguramiento.iot

import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val refhome = database.getReference("home")

    private lateinit var refLights: DatabaseReference
    private lateinit var refLivingRoomLight: DatabaseReference

    private lateinit var refButtons: DatabaseReference
    private lateinit var refPulsadorA: DatabaseReference

    private lateinit var btnToggle: ToggleButton

    private lateinit var tvEstadoPulsador: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refLights = refhome.child("light")
        refLivingRoomLight = refLights.child("living_room")

        refButtons = refhome.child("button")
        refPulsadorA = refButtons.child("pulsador_a")


        btnToggle = toggleButton
        btnToggle.textOn = "Luz encendida"
        btnToggle.textOff = "Luz apagada"

        tvEstadoPulsador = textViewPulsador

        controlLed(refLivingRoomLight, btnToggle)

        estadoPulsador(refPulsadorA, tvEstadoPulsador)
    }

    private fun controlLed(refLivingRoomLight: DatabaseReference, btnToggle: ToggleButton) {
        btnToggle.setOnCheckedChangeListener{ _, isChecked->
            refLivingRoomLight.setValue(isChecked)
        }

        refLivingRoomLight.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val estadoLed: Boolean = snapshot.value as Boolean
                btnToggle.isChecked = estadoLed
                if (estadoLed) {
                    btnToggle.textOn = "Luz encendida"
                } else {
                    btnToggle.textOff = "Luz apagada"
                }
            }

        })
    }

    private fun estadoPulsador(refPulsadorA: DatabaseReference, tvEstadoPulsador: TextView) {
        refPulsadorA.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val estadoLed: Boolean = snapshot.value as Boolean
                val text = if(estadoLed) "Luz encendida" else "Luz apagada"
                tvEstadoPulsador.text = text
            }
        })
    }
}