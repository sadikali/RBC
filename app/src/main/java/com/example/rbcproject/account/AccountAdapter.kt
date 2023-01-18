package com.example.rbcproject.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rbcproject.R
import com.example.rbcproject.network.model.AccountData
import com.example.rbcproject.network.model.AccountHeader
import com.example.rbcproject.network.model.DisplayAccount

class AccountAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<DisplayAccount> = listOf()
    val HEADER = 0
    val ROW = 1

    class AccountHeading(itemView: View): RecyclerView.ViewHolder(itemView){
        val accountTypeTitle: TextView
        init{
            accountTypeTitle = itemView.findViewById(R.id.account_header_title)
        }
    }

    class AccountDetails(itemView: View): RecyclerView.ViewHolder(itemView){
        val accountName: TextView
        val accountNumber: TextView
        val accountBalance: TextView

        init {
            accountName = itemView.findViewById(R.id.account_name)
            accountNumber = itemView.findViewById(R.id.account_number)
            accountBalance = itemView.findViewById(R.id.account_balance)
        }
    }

    class OnClickListener(val clickListener: (accountData: AccountData) -> Unit){
        fun onClick(accountData: AccountData) = clickListener(accountData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType) {
            HEADER -> AccountHeading(LayoutInflater.from(parent.context).inflate(R.layout.account_header, parent, false))
            else -> AccountDetails(LayoutInflater.from(parent.context).inflate(R.layout.account_details, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) 0 else data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val displayAccount = data.get(position)

        when (displayAccount){
            is AccountHeader -> {
                val view = holder as AccountHeading
                view.accountTypeTitle.text = displayAccount.name
            }
            is AccountData -> {
                val view = holder as AccountDetails
                view.accountBalance.text = displayAccount.balance
                view.accountNumber.text = displayAccount.number
                view.accountName.text = displayAccount.name
                view.itemView.setOnClickListener {
                    onClickListener.onClick(displayAccount)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data.get(position)){
            is AccountHeader -> HEADER
            is AccountData -> ROW
        }
    }


}