package com.example.rbcproject.transaction

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rbcproject.R
import com.example.rbcproject.account.AccountAdapter
import com.example.rbcproject.network.model.AccountData
import com.example.rbcproject.network.viewmodel.TransactionViewModel

class TransactionActivity : AppCompatActivity() {

    companion object{
        val TRANSACTION_ACCOUNT = "account"
    }
    lateinit var transactionAdapter: TransactionAdapter
    var accountData: AccountData? = null
    lateinit var recyclerView: RecyclerView
    val transactionViewModel by viewModels<TransactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_layout)
        accountData = intent.getParcelableExtra(TRANSACTION_ACCOUNT)
        transactionAdapter = TransactionAdapter()
        recyclerView = findViewById(R.id.transaction_rv)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TransactionActivity)
            adapter = transactionAdapter

        }
        transactionViewModel.transactionLiveData.observe(this) { newData ->
            Log.d("Transactions", newData.toString())
            transactionAdapter.data.clear()
            transactionAdapter.data.addAll(newData)
            transactionAdapter.notifyDataSetChanged()
        }
        accountData?.let {
            transactionViewModel.getAccountTransactions(it)
        }

        transactionViewModel.stateLiveData.observe(this){state ->
            when(state){
                is TransactionViewModel.State.Error -> {
                    findViewById<RelativeLayout>(R.id.transaction_error).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.transaction_loading_msg).text = "Error Loading Data"
                }
                is TransactionViewModel.State.Loading ->{
                    findViewById<RelativeLayout>(R.id.transaction_error).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.transaction_loading_msg).text = "Loading"
                }
                is TransactionViewModel.State.Completed -> {
                    findViewById<RelativeLayout>(R.id.transaction_error).visibility = View.GONE

                }

            }
        }

    }


}