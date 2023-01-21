package com.example.rbcproject.network.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rbcproject.network.Repo
import com.example.rbcproject.network.model.AccountData
import com.example.rbcproject.network.model.TransactionViews
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TransactionViewModel :ViewModel() {

    sealed class State{
        class Loading:State()
        class Error:State()
        class Completed:State()
    }

    val repo = Repo()
    val transactionLiveData = MutableLiveData<List<TransactionViews>>()
    val stateLiveData = MutableLiveData<State>()
    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.IO + job)


    private fun getCCTransactions(account: AccountData): List<Transaction>{
        return if (account.type == AccountType.CREDIT_CARD) repo.accountProvider.getAdditionalCreditCardTransactions(account.number) else listOf()
    }

    private fun getAllTransactions(accountNumber: String): List<Transaction>{
        Log.d("Account#", "Getting account transactions from " + accountNumber)
        return repo.accountProvider.getTransactions(accountNumber)
    }

    suspend fun getAccountTransactionsRepo(account: AccountData): List<TransactionViews.AccountTransactions> {
        val myFormat = "M-d-y"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        val newFormat = DateTimeFormatter.ofPattern(myFormat)

        val generalTransactions = coroutineScope {

            withContext(Dispatchers.IO) {
                getAllTransactions(account.number).map { transaction: Transaction ->
                    TransactionViews.AccountTransactions(
                        transaction.amount,
                        LocalDate.parse(dateFormat.format(transaction.date.time), newFormat),
                        transaction.description
                    )
                }
            }
        }

        val creditCardTransactions = coroutineScope {
            withContext(Dispatchers.IO) {
                getCCTransactions(account).map { transaction: Transaction ->
                    TransactionViews.AccountTransactions(
                        transaction.amount,
                        LocalDate.parse(dateFormat.format(transaction.date.time), newFormat),
                        transaction.description
                    )
                }
            }
        }


        val combinedData = mutableListOf<TransactionViews.AccountTransactions>()
        combinedData.addAll(creditCardTransactions)
        combinedData.addAll(generalTransactions)
        if (combinedData.isNotEmpty()) {
            val mappedData = combinedData.groupBy { it.date }
            val rtn = mutableListOf<TransactionViews>()
            for (k in mappedData.keys.sortedByDescending { it }) {
                rtn.add(TransactionViews.TransactionHeader(k))
                rtn.addAll(mappedData.get(k).orEmpty())
            }
            transactionLiveData.postValue(rtn)
            stateLiveData.postValue(State.Completed())
        } else {
            stateLiveData.postValue(State.Error())
        }

        return combinedData
    }

    fun getAccountTransactions(account: AccountData): List<TransactionViews.AccountTransactions> {
        val data = mutableListOf<TransactionViews.AccountTransactions>()
        stateLiveData.postValue(State.Loading())
        scope.launch{
            try {
                 data.addAll(getAccountTransactionsRepo(account))
            } catch (e: Exception){
                Log.e("EXCEPTION **", "***** EXCEPTION ***** " +  e.message )
                stateLiveData.postValue(State.Error())
            }
        }.start()
        return data
    }


}