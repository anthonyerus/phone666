package com.anthony.phonebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.anthony.phonebook.activities.ContactDetailsActivity
import com.anthony.phonebook.adapters.PhoneAdapter
import com.anthony.phonebook.databinding.ActivityMainBinding
import com.anthony.phonebook.models.Contact
import com.anthony.phonebook.models.PhoneDataBase
import com.anthony.phonebook.viewmodels.PhoneBookView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var phoneAdapter: PhoneAdapter
    private lateinit var database: PhoneDataBase
    private lateinit var viewModel: PhoneBookView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(this,
            PhoneDataBase::class.java,
            "phone_database"
        ).allowMainThreadQueries().build()

        // initializing the view model
        viewModel = ViewModelProvider(this)[PhoneBookView::class.java]
        viewModel.getContact(database)


        phoneAdapter = PhoneAdapter(database.contactDoa().getAllContacts()){
            val intent = Intent(this@MainActivity, ContactDetailsActivity::class.java)
            intent.run {
                //putExtra("id",it.id)
                putExtra("name", it.name)
                putExtra("number", it.number)
            }
            startActivity(intent)

        }
        // binding the RecyclerView
        binding.phoneRecycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = phoneAdapter
        }

        // Observing livedata
        viewModel.phonebookLiveData.observe(this,{ contacts ->
            phoneAdapter.contacts = contacts
            phoneAdapter.notifyDataSetChanged()
        })
        // binding the save button
        binding.btnSave.setOnClickListener {
            val name = binding.txNameInput.text.toString()
            val number = binding.txNumberInput.text.toString()
            saveContact(name, number)
        }
    }
    private fun saveContact(name: String, number: String){
        val contact = Contact(id = 0, name, number)
        viewModel.addContact(database,contact)
        binding.apply {
            txNameInput.text.clear()
            txNumberInput.text.clear()
        }

    }
}