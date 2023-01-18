package com.example.rbcproject.network.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rbcproject.network.Repo
import com.example.rbcproject.network.model.AccountData
import com.example.rbcproject.network.model.TransactionViews
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import kotlinx.coroutines.*

class TransactionViewModel :ViewModel() {

    sealed class State{
        class Loading:State()
        class Error:State()
        class Completed:State()
    }

    val repo = Repo()
    val transactionLiveData = MutableLiveData<List<TransactionViews>>()
    val stateLiveData = MutableLiveData<State>()

    private fun getCCTransactions(account: AccountData): List<Transaction>{
        return if (account.type == AccountType.CREDIT_CARD) repo.accountProvider.getAdditionalCreditCardTransactions(account.number) else listOf()
    }

    private fun getAllTransactions(accountNumber: String): List<Transaction>{
        Log.d("Account#", "Getting account transactions from " + accountNumber)
        return repo.accountProvider.getTransactions(accountNumber)
    }

    fun getAccountTransactions(account: AccountData) {
        stateLiveData.postValue(State.Loading())
        val handler = CoroutineExceptionHandler { _, exception ->
            stateLiveData.postValue(State.Error())
        }
        viewModelScope.launch { withContext(Dispatchers.IO) {

            val generalTransactions = async(handler) { getAllTransactions(account.number).map { transaction: Transaction ->
                TransactionViews.AccountTransactions(
                    transaction.amount,
                    transaction.date,
                    transaction.description
                )
            }}


            val creditCardTransactions = async(handler){getCCTransactions(account).map { transaction: Transaction ->
                TransactionViews.AccountTransactions(
                    transaction.amount,
                    transaction.date,
                    transaction.description
                )
            }}

            val combinedData = mutableListOf<TransactionViews.AccountTransactions>()
            try {
                combinedData.addAll(generalTransactions.await())
                combinedData.addAll(creditCardTransactions.await())
                val mappedData = combinedData.groupBy {it.date }
                val rtn = mutableListOf<TransactionViews>()
                for (k in mappedData.keys.sortedByDescending{it}){
                    rtn.add(TransactionViews.TransactionHeader(k))
                    rtn.addAll(mappedData.get(k).orEmpty())
                }
                transactionLiveData.postValue(rtn)
                stateLiveData.postValue(State.Completed())
        } catch (e: Exception){
            Log.wtf("EXCEPTION CAUGHT", "Didn't fetch data" )
            stateLiveData.postValue(State.Error())
        }

        }}

    }



}