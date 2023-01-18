package com.example.rbcproject.network.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rbcproject.network.Repo
import com.example.rbcproject.network.model.AccountData
import com.example.rbcproject.network.model.AccountHeader
import com.example.rbcproject.network.model.DisplayAccount
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType

class AccountViewModel: ViewModel() {

    val repo = Repo()

    fun getAccounts():List<DisplayAccount>{
        val data = repo.getAccounts().groupBy { account -> account.type }
        val rtn = mutableListOf<DisplayAccount>()
        for (k in data.keys){
            rtn.add(AccountHeader(k.name))
            data.get(k)?.map { v -> AccountData(v.name, v.number, v.balance, v.type) }
                ?.let { rtn.addAll(it) }
        }
        return rtn
    }
}