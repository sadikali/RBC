package com.example.rbcproject.network.model

import android.os.Parcelable
import com.rbc.rbcaccountlibrary.AccountType
import kotlinx.parcelize.Parcelize

sealed class DisplayAccount():Parcelable

@Parcelize
data class AccountHeader(val name: String):DisplayAccount()
@Parcelize
data class AccountData(val name: String, val number: String, val balance: String, val type: AccountType): DisplayAccount()

