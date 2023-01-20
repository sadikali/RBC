package com.example.rbcproject.network.model

import java.util.Calendar

sealed class TransactionViews {
    data class TransactionHeader(val date: String) : TransactionViews()
    data class AccountTransactions(
        val amount: String,
        val date: Calendar,
        val description: String
    ) : TransactionViews()
}
