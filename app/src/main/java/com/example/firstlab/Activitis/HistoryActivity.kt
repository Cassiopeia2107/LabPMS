package com.example.firstlab.Activitis

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.firstlab.Domains.FirebaseFiles.FirestoreUtils
import com.example.firstlab.R

class HistoryActivity : AppCompatActivity() {

    private lateinit var textItem: TextView
    private lateinit var historyListView: ListView
    private lateinit var btnClose: ImageButton
    private lateinit var btnClearHistory: Button
    private lateinit var firestoreUtils: FirestoreUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        btnClose = findViewById(R.id.btnClose)
        btnClearHistory = findViewById(R.id.btnClearHistory)
        textItem = findViewById(R.id.textItem)
        historyListView = findViewById(R.id.historyListView)
        firestoreUtils = FirestoreUtils(this)
        val historyList = intent.getStringArrayListExtra("historyList")


        val adapter = historyList?.let {
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                it.toList()
            )
        }
        historyListView.adapter = adapter
        if (historyList != null && historyList.isNotEmpty()) {
            val indexedHistory = historyList.mapIndexed { index, line -> "${index + 1}) $line" }
            textItem.text = indexedHistory.joinToString("\n")
        } else {
            textItem.text = "No history available"
        }

        btnClose.setOnClickListener {
            finish()
        }

        btnClearHistory.setOnClickListener {
            clearHistory()
        }
    }

    private fun clearHistory() {
        textItem.text = "No history available"
        firestoreUtils.clearHistoryInFirestore { success ->
            if (success as Boolean) {
                setResult(RESULT_OK)
                finish()
            } else {
                Log.e("HistoryActivity", "Failed to clear history in Firestore")
            }
        }
    }
}
