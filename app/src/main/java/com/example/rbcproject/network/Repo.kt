package com.example.rbcproject.network

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction


class Repo {
    val accountProvider = AccountProvider

    fun getAccounts(): List<Account> {
        return AccountProvider.getAccountsList()
    }

    fun getTransaction(account: Account): List<Transaction>{
        return AccountProvider.getTransactions(account.number)
    }

    fun getCCTransaction(account: Account):List<Transaction>{
        require(account.type == AccountType.CREDIT_CARD)
        return AccountProvider.getAdditionalCreditCardTransactions(account.number)
    }
}