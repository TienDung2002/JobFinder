package com.example.jobfinder.UI.Wallet

import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.WalletRowModel

class WalletCardListViewModel: ViewModel() {

    private val cardList: MutableList<WalletRowModel> = mutableListOf()
    var isDataLoaded: Boolean = false

    // Thêm dữ liệu vào JobsList
    fun addCardData(cardData: WalletRowModel) {
        cardList.add(cardData)
    }

    // Lấy danh sách dữ liệu cho adapter.
    fun getCardList(): MutableList<WalletRowModel> {
        return cardList
    }

    fun removeCardData(wallet: WalletRowModel) {
        cardList.remove(wallet)
    }

}