import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.WalletRowModel
import com.example.jobfinder.databinding.FragmentWalletBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue

class WalletFragment : Fragment() {
    private lateinit var binding: FragmentWalletBinding
    private lateinit var walletAdapter: WalletAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val walletList = mutableListOf<WalletRowModel>()

        FirebaseDatabase.getInstance().getReference("Wallet").child(uid.toString()).get()
            .addOnSuccessListener { dataSnapshot ->
                for (cardSnapshot in dataSnapshot.children) {
                    // Truy cập trực tiếp các thuộc tính của WalletRowModel từ cardSnapshot
                    val cardId = cardSnapshot.child("cardId").getValue(String::class.java)
                    val bankName = cardSnapshot.child("bankName").getValue(String::class.java)
                    val amount = cardSnapshot.child("amount").getValue(String::class.java)
                    val cardNumber = cardSnapshot.child("cardNumber").getValue(String::class.java)
                    val expDate = cardSnapshot.child("expDate").getValue(String::class.java)
                    val cardColor = cardSnapshot.child("cardColor").getValue(String::class.java)

                    // Tạo một đối tượng WalletRowModel từ các thuộc tính lấy được
                    val wallet = WalletRowModel(cardId, bankName, amount, cardNumber, expDate, cardColor)
                    walletList.add(wallet)
                }

                // Khởi tạo adapter và thiết lập RecyclerView
                walletAdapter = WalletAdapter(walletList, requireContext(), binding.noWalletCard)
                binding.recyclerWalletList.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerWalletList.adapter = walletAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("Wallet data", "Error getting data from Firebase", exception)
            }

    }

}
