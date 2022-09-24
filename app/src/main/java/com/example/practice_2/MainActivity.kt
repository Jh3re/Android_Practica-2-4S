package com.example.practice_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.practice_2.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myHandler: Handler
    private lateinit var tts: TextToSpeech
    private var code: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tts = TextToSpeech(this, this)
        myHandler = Handler(mainLooper)
        binding.btnProcess.setOnClickListener {
            startParallelProcess()
        }
    }

    private fun startParallelProcess() {
        tts.speak("Procesando", TextToSpeech.QUEUE_FLUSH, null,"")
        Thread {
            try {
                Thread.sleep(1000)
                for (i in 0 .. 10){
                    Thread.sleep(500)
                    myHandler.post {
                        binding.apply {
                            txtCounting.text = "$i ... "
                            pbLoad.progress = i * 10
                        }
                    }
                }
                runOnUiThread {
                    Thread.sleep(2000)
                    tts.speak("Listo... Cargando segunda pantalla", TextToSpeech.QUEUE_FLUSH, null,"")
                    binding.txtCounting.text = "Listo..."
                    randomCode()
                }
                Thread.sleep(3500)
                passScreen()

            } catch (e: InterruptedException){
                e.printStackTrace()
            }
        }.start()
    }

    private fun randomCode (){
        val letter_1 = ('A'..'Z').random()
        val letter_2 = ('A'..'Z').random()

        val number_1 = (0..10).random()
        var number_2 = (0..10).random()

        while (number_1 == number_2){
            number_2 = (0..10).random()
        }
        code = "$letter_1$letter_2-$number_1$number_2"
        binding.txtCode.text = code
    }

    private fun passScreen(){
        val cod = code
        val intent = Intent(this, SecondActivity::class.java)
        intent.apply {
            putExtra("code",cod)
        }
        startActivity(intent)
    }

    override fun onInit(status: Int) {
        val result = if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("es")
            "Suba el volumen para el uso de voz"
        } else "Algo salio mal."
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }
}