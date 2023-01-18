package com.example.rbcproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rbcproject.account.AccountAdapter
import com.example.rbcproject.network.model.AccountData
import com.example.rbcproject.network.viewmodel.AccountViewModel
import com.example.rbcproject.transaction.TransactionActivity

class MainActivity : AppCompatActivity() {
    private lateinit var accountAdapter: AccountAdapter
    private lateinit var recyclerView : RecyclerView
    private val accountViewModel : AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_layout)
        accountAdapter = AccountAdapter(AccountAdapter.OnClickListener{account:AccountData -> getAccountTransactions(account)})
        recyclerView = findViewById(R.id.accounts_rv)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            accountAdapter.data = accountViewModel.getAccounts()
            adapter = accountAdapter
        }

    }

    private fun getAccountTransactions(accountData: AccountData){

        val intent = Intent(this@MainActivity, TransactionActivity::class.java)
        intent.putExtra(TransactionActivity.TRANSACTION_ACCOUNT, accountData)
        startActivity(intent)

    }


}