package com.example.rbcproject.network.model

import java.time.LocalDate
import java.util.Calendar

sealed class TransactionViews {
    data class TransactionHeader(val date: LocalDate) : TransactionViews()
    data class AccountTransactions(
        val amount: String,
        val date: LocalDate,
        val description: String
    ) : TransactionViews(){}
}
