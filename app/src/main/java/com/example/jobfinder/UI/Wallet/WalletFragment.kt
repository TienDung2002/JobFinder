import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.WalletRowModel
import com.example.jobfinder.databinding.FragmentWalletBinding

class WalletFragment : Fragment() {
    private lateinit var binding: FragmentWalletBinding
    private lateinit var walletAdapter: WalletAdapter
    private var isExpanded = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.walletHistoryFtTxt.visibility = View.GONE
        binding.walletHistoryFtBtn.visibility = View.GONE
        binding.addWalletFtTxt.visibility = View.GONE
        binding.addWalletFtBtn.visibility = View.GONE
        // Tạo danh sách mẫu các ví
        val walletList = mutableListOf(
            WalletRowModel("BIDV", "$45.678", "0123456789", "blue"),
            WalletRowModel("BIDV", "$45.678", "0123456789","red"),
            WalletRowModel("BIDV", "$45.678", "0123456789","green"),
            WalletRowModel("Vietcombank", "$100.000", "9876543210","green"),
            WalletRowModel("Vietcombank", "$100.000", "9876543210","blue"),
            WalletRowModel("Vietcombank", "$100.000", "9876543210","red"),
        )

        binding.mainFtBtn.setOnClickListener {
            // Đảo ngược trạng thái mở rộng và cập nhật giao diện
            isExpanded = !isExpanded
            updateFABVisibility()
        }

        // Khởi tạo adapter và gán danh sách ví vào
        walletAdapter = WalletAdapter(walletList)

        // Thiết lập layout manager và adapter cho RecyclerView
        binding.recyclerWalletList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerWalletList.adapter = walletAdapter
    }

    private fun updateFABVisibility() {
        Log.d("sadddđ","sdfdsgfhdsjgfds")
        if (isExpanded) {
            // Hiển thị hai FAB khi mở rộng
            binding.walletHistoryFtTxt.visibility= View.VISIBLE
            binding.walletHistoryFtBtn.visibility = View.VISIBLE
            binding.walletHistoryFtBtn.isClickable = true
            binding.addWalletFtTxt.visibility = View.VISIBLE
            binding.addWalletFtBtn.visibility = View.VISIBLE
            binding.addWalletFtBtn.isClickable = true
        } else {
            // Ẩn hai FAB khi thu gọn và vô hiệu hóa khả năng nhấp
            binding.walletHistoryFtTxt.visibility = View.GONE
            binding.walletHistoryFtBtn.visibility = View.GONE
            binding.walletHistoryFtBtn.isClickable = false
            binding.addWalletFtTxt.visibility = View.GONE
            binding.addWalletFtBtn.visibility = View.GONE
            binding.addWalletFtBtn.isClickable = false
        }
    }

    private fun animateView(view: View, visibility: Int) {
        val fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val fadeOutAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        view.clearAnimation()
        if (visibility == View.VISIBLE) {
            view.startAnimation(fadeInAnimation)
            view.visibility = View.VISIBLE
        } else {
            view.startAnimation(fadeOutAnimation)
            view.visibility = View.GONE
        }
    }

}
