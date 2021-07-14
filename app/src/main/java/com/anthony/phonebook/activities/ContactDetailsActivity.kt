package com.anthony.phonebook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anthony.phonebook.databinding.ActivityContactDetailsBinding

class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            //id.text = intent.getStringExtra("id").toString()
            name.text = intent.getStringExtra("name")
            phonenumber.text = intent.getStringExtra("number").toString()
        }
    }
}