package com.example.rbcproject.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rbcproject.R
import com.example.rbcproject.account.AccountAdapter
import com.example.rbcproject.network.model.TransactionViews
import org.w3c.dom.Text

class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    var data = mutableListOf<TransactionViews>()

    val HEADER = 0
    val ROW = 1

    class TransactionHeading(itemView: View): RecyclerView.ViewHolder(itemView){
        val transactionTitle: TextView
        init{
            transactionTitle = itemView.findViewById(R.id.transaction_header_title)
        }
    }

    class TransactionDetails(itemView: View): RecyclerView.ViewHolder(itemView){
        val transactionDetails: TextView
        val transactionAmount: TextView
        init{
            transactionDetails = itemView.findViewById(R.id.transaction_description)
            transactionAmount = itemView.findViewById(R.id.transaction_amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            HEADER -> TransactionHeading(
                LayoutInflater.from(parent.context).inflate(R.layout.transaction_header, parent, false))
            else -> TransactionDetails(LayoutInflater.from(parent.context).inflate(R.layout.transaction_details, parent, false))
        }
    }

    override fun getItemCount(): Int {
       return if (data.isEmpty()) 0 else data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rowData = data.get(position)

        when (rowData) {
            is TransactionViews.TransactionHeader -> {
                val row = holder as TransactionHeading
                row.transactionTitle.text = rowData.date.toString()
            }
            is TransactionViews.AccountTransactions -> {
                val row = holder as TransactionDetails
                row.transactionDetails.text = rowData.description
                row.transactionAmount.text = "$ " + rowData.amount
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data.get(position)){
            is TransactionViews.TransactionHeader -> HEADER
            is TransactionViews.AccountTransactions -> ROW
        }
    }



}