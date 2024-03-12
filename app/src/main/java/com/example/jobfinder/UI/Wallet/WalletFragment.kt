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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tạo danh sách mẫu các ví
        val walletList = mutableListOf(
            WalletRowModel("BIDV", "$45.678", "0123456789", "blue"),
            WalletRowModel("BIDV", "$45.678", "0123456789","red"),
            WalletRowModel("BIDV", "$45.678", "0123456789","green"),
            WalletRowModel("Vietcombank", "$100.000", "9876543210","green"),
            WalletRowModel("Vietcombank", "$100.000", "9876543210","blue"),
            WalletRowModel("Vietcombank", "$100.000", "9876543210","red"),
        )

        // Khởi tạo adapter và gán danh sách ví vào
        walletAdapter = WalletAdapter(walletList)

        // Thiết lập layout manager và adapter cho RecyclerView
        binding.recyclerWalletList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerWalletList.adapter = walletAdapter
    }


}
