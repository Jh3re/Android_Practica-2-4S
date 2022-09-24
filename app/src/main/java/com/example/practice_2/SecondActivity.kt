package com.example.practice_2

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice_2.databinding.ActivitySecondBinding
import java.util.*

class SecondActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var tts: TextToSpeech
    private var firstCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tts = TextToSpeech(this, this)

        val cod = "${intent.getStringExtra("code")}"
        firstCode = cod

        binding.swEstate.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.swEstate.isChecked){
                Toast.makeText(this, "Casado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Soltero", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnProcessCode.setOnClickListener {
            processFinalCode()
        }
    }

    private fun processFinalCode (){

        val firstLetterName = binding.etName.text.replace("\\s".toRegex(), "")
        val lastLetterLastName = binding.etLastName.text.replace("\\s".toRegex(), "")


        if (firstLetterName.length < 3 || firstLetterName.isEmpty() || lastLetterLastName.length < 3 || lastLetterLastName.isEmpty()){
            binding.txtResultCode.text = "CODIGO"
            Toast.makeText(this, "Nombre o apellido incorrecto", Toast.LENGTH_SHORT).show()
            tts.speak("Nombre o apellido incorrecto", TextToSpeech.QUEUE_FLUSH, null,"")
        } else {
            binding.apply {
                if (swEstate.isChecked){
                    txtResultCode.text = "${firstLetterName.first().uppercase()}${lastLetterLastName.last().uppercase()}-C-$firstCode"
                }
                else{
                    txtResultCode.text = "${firstLetterName.first().uppercase()}${lastLetterLastName.last().uppercase()}-S-$firstCode"
                }
            }
            Toast.makeText(this, "Listo, código procesado", Toast.LENGTH_SHORT).show()
            tts.speak("Listo, código procesado", TextToSpeech.QUEUE_FLUSH, null,"")
        }


    }

    override fun onInit(status: Int) {
        var result = if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("es")
            "Suba el volumen para el uso de voz"
        } else "Algo salio mal"
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }
}
