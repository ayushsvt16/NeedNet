package com.example.neednet

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FindHelpActivity : AppCompatActivity() {

    private lateinit var helpList: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var requestHelpBtn: Button
    private val dummyHelpRequests = listOf(
        HelpRequest("Need groceries delivered", "Food", "1 km away"),
        HelpRequest("Need tuition for class 10th", "Education", "2.5 km away"),
        HelpRequest("Looking for blood donor", "Medical", "Nearby")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_help)

        helpList = findViewById(R.id.recyclerHelpRequests)
        searchBar = findViewById(R.id.searchBar)
        requestHelpBtn = findViewById(R.id.btnRequestHelp)

        helpList.layoutManager = LinearLayoutManager(this)
        helpList.adapter = HelpRequestAdapter(this, dummyHelpRequests)

        requestHelpBtn.setOnClickListener {
            // TODO: Navigate to RequestHelpActivity
        }
    }
}
