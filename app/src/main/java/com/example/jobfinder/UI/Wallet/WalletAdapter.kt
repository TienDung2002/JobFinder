import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.WalletRowModel
import com.example.jobfinder.R
import com.example.jobfinder.databinding.RowWalletCardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class WalletAdapter(private val walletList: MutableList<WalletRowModel>,
                    private val context: Context,
                    private val noWalletLayout: ConstraintLayout
) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

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
                // Binding dữ liệu từ wallet vào các view tương ứng
                txtWalletBankName.text = wallet.bankName
                walletAmount.text ="$"+ wallet.amount
                walletId.text = wallet.cardNumber
                imageMainWalletImage.setImageResource(randomMaskGroup())
                rowWallet.setBackgroundResource(getGradientDrawable(wallet.cardColor))

                // Thiết lập onClickListener cho thẻ
                rowWallet.setOnClickListener {
                    showOptionsDialog(wallet)
                }


            }
        }

        fun removeWallet(position: Int) {
            if (position in 0 until walletList.size) {
                val removedWallet = walletList[position]

                walletList.removeAt(position)
                // Thông báo cho RecyclerView biết rằng có một mục đã bị xóa
                notifyItemRemoved(position)
                // Cập nhật lại các vị trí của các mục sau mục bị xóa
                notifyItemRangeChanged(position, walletList.size - position)

                // Kiểm tra xem RecyclerView có rỗng không và hiển thị layout không có thẻ nếu cần
                checkEmptyAdapter()

                // Xóa thẻ từ danh sách thành công
            }
        }

        private fun checkEmptyAdapter() {
            if (walletList.isEmpty()) {
                // Ẩn RecyclerView và hiển thị layout không có thẻ
                noWalletLayout.visibility = View.VISIBLE
            } else {
                // Hiển thị RecyclerView và ẩn layout không có thẻ
                noWalletLayout.visibility = View.GONE
            }
        }

        // Hiển thị dialog với các chức năng
        private fun showOptionsDialog(wallet: WalletRowModel) {
            val dialog = Dialog(binding.root.context)
            dialog.setContentView(R.layout.dialog_wallet_data)

            // Tìm kiếm các nút trong dialog
            val deleteButton = dialog.findViewById<Button>(R.id.delete_card_button)
            val addButton = dialog.findViewById<Button>(R.id.add_cash_button)
            val cancelButton = dialog.findViewById<Button>(R.id.button_cancel)

            // Xử lý khi nhấn vào nút Xóa
            deleteButton.setOnClickListener {
                // Xóa thẻ
                deleteWallet(wallet)
                dialog.dismiss() // Đóng dialog
            }

            // Xử lý khi nhấn vào nút Hủy
            cancelButton.setOnClickListener {
                dialog.dismiss() // Đóng dialog
            }

            // Xử lý khi nhấn vào nút Thêm
            addButton.setOnClickListener {
                // Thêm 10000 vào amount của thẻ
                addMoney(wallet)
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                uid?.let { userId ->
                    FirebaseDatabase.getInstance().getReference("Wallet").child(userId)
                        .child(wallet.cardId ?: "")
                        .child("amount")
                        .setValue(wallet.amount)
                        .addOnSuccessListener {
                            Toast.makeText(context, getString(context,R.string.add_cash_success), Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            // Xử lý khi thêm thất bại
                            Log.e("Add Money", "Error adding money to Firebase", exception)
                        }
                }

                dialog.dismiss() // Đóng dialog // Đóng dialog
            }

            dialog.show() // Hiển thị dialog
        }

        private fun deleteWallet(wallet: WalletRowModel) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Remove wallet from RecyclerView
                walletList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, walletList.size - position)
                checkEmptyAdapter()

                // Remove wallet from Firebase
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                uid?.let { userId ->
                    FirebaseDatabase.getInstance().getReference("Wallet").child(userId)
                        .child(wallet.cardId ?: "")
                        .removeValue()
                        .addOnSuccessListener {
                            // Handle success
                            Toast.makeText(context, getString(context,R.string.delete_card_success), Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            // Handle failure
                            Log.e("WalletAdapter", "Error deleting wallet from Firebase", exception)
                        }
                }
            }
        }
        // Hàm thêm tiền vào thẻ
        private fun addMoney(wallet: WalletRowModel) {
            // Chuyển đổi giá trị amount từ string thành kiểu float
            val currentAmount = wallet.amount?.toFloatOrNull() ?: 0f
            // Thêm 10000 vào giá trị hiện tại
            val newAmount = currentAmount + 10000f
            // Cập nhật giá trị amount của thẻ thành chuỗi mới
            wallet.amount = newAmount.toString()
            // Cập nhật lại giao diện cho thẻ
            bind(wallet)
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