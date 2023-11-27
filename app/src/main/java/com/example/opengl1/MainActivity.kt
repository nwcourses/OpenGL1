package com.example.opengl1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val glview = findViewById<OpenGLView>(R.id.glview)

        findViewById<Button>(R.id.plusZ).setOnClickListener {
            glview.camera.translate(0f, 0f, 1f)
        }
    }
}