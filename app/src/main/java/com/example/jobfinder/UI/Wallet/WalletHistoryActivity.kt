package com.example.jobfinder.UI.Wallet

import WalletHistoryAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.databinding.ActivityWalletHistoryBinding
import com.example.jobfinder.Datas.Model.walletHistoryModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class WalletHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletHistoryBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        FirebaseDatabase.getInstance()
            .getReference("WalletHistory")
            .child(uid.toString())
            .get()
            .addOnSuccessListener { dataSnapshot ->
                // Chuyển đổi dataSnapshot thành danh sách walletHistoryModel
                val walletHistoryList = dataSnapshot.children.flatMap { cardId ->
                    cardId.children.map { historySnapshot ->
                        val historyId = historySnapshot.child("historyId").getValue(String::class.java)
                        val amount = historySnapshot.child("amount").getValue(String::class.java)
                        val cardId = historySnapshot.child("cardId").getValue(String::class.java)
                        val bankName = historySnapshot.child("bankName").getValue(String::class.java)
                        val cardNum = historySnapshot.child("cardNum").getValue(String::class.java)
                        val date = historySnapshot.child("date").getValue(String::class.java)
                        val type = historySnapshot.child("type").getValue(String::class.java)

                        walletHistoryModel(historyId, amount, cardId, bankName, cardNum, date, type)
                    }
                }
                if(!walletHistoryList.isEmpty()){
                    val sortedList = walletHistoryList.sortedByDescending { GetData.convertStringToDate( it.date.toString()) }

                    // Khởi tạo và cài đặt adapter cho RecyclerView

                    val adapter = WalletHistoryAdapter(sortedList)
                    binding.recyclerWalletHistoryList.layoutManager = LinearLayoutManager(this)
                    binding.recyclerWalletHistoryList.adapter = adapter
                }else{
                    binding.noWalletHistory.visibility= View.VISIBLE
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Wallet data", "Error getting data from Firebase", exception)
            }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
            finish()
        }
    }

}