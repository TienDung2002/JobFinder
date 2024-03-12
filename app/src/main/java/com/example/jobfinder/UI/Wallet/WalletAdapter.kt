import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.WalletRowModel
import com.example.jobfinder.R
import com.example.jobfinder.databinding.RowWalletCardBinding
import kotlin.random.Random

class WalletAdapter(private val walletList: List<WalletRowModel>) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val binding = RowWalletCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val wallet = walletList[position]
        holder.bind(wallet)
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    inner class WalletViewHolder(private val binding: RowWalletCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wallet: WalletRowModel) {
            binding.apply {
                txtWalletBankName.text = wallet.bankName
                walletAmount.text = wallet.amount
                walletId.text = wallet.cardNumber
                imageMainWalletImage.setImageResource(randomMaskGroup())
                rowWallet.setBackgroundResource(getGradientDrawable(wallet.cardColor))
            }
        }
        private fun getGradientDrawable(cardColor: String?): Int {
            return when (cardColor) {
                "red" -> R.drawable.wallet_red_bg
                "green" -> R.drawable.wallet_green_bg
                "blue" -> R.drawable.wallet_blue_bg
                else -> R.drawable.wallet_blue_bg
            }
        }

        private fun randomMaskGroup(): Int {
            val num = Random.nextInt(1, 4)
            return when (num) {
                1-> R.drawable.img_mask_group
                2-> R.drawable.img_mask_group_white_a700
                3-> R.drawable.img_mask_group_white_a700_170x319
                else -> {R.drawable.img_mask_group}
            }
        }

    }
}
