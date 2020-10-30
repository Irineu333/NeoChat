package com.neu.neochat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neu.neochat.R

/**
 * Activity única, responsável por inicializar e gerenciar o NavHostFragment,
 * seguindo o padrão Single Activity, usando Navigation Fragment
 * @author Irineu A. Silva
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //iniciar fragments
        setContentView(R.layout.activity_main)
    }
}