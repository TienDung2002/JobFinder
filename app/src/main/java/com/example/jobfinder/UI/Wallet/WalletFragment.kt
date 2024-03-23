import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.WalletRowModel
import com.example.jobfinder.Datas.Model.walletAmountModel
import com.example.jobfinder.databinding.FragmentWalletBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class WalletFragment : Fragment() {
    private lateinit var binding: FragmentWalletBinding
    private lateinit var walletAdapter: WalletAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dataLoadListener: DataLoadListener

    // onAttach được gọi đầu tiên khi kết nối Fragment với Activity chứa nó
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataLoadListener = context as? DataLoadListener ?: throw RuntimeException("$context must implement DataLoadListener")
    }


    // interface này cho phép WalletFragment gừi thông diệp cho WalletActivity khi load xong data
    interface DataLoadListener {
        fun onDataLoaded()
        fun onDataLoadedEmpty(isListEmpty: Boolean)
    }

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

        FirebaseDatabase.getInstance()
            .getReference("WalletAmount")
            .child(uid.toString())
            .get()
            .addOnSuccessListener { data ->
                if (data.exists()) {
                    val amount = data.child("amount").getValue(String::class.java)
                        binding.amountInWalletAmount.text = amount.toString()
                } else {
                    val walletAmount = walletAmountModel("0.0")
                    FirebaseDatabase.getInstance()
                        .getReference("WalletAmount")
                        .child(uid.toString())
                        .setValue(walletAmount)
                        .addOnSuccessListener {
                            // Xử lý khi tạo giá trị mới thành công
                            binding.amountInWalletAmount.text= "0.0"
                        }
                        .addOnFailureListener { exception ->
                            // Xử lý khi tạo giá trị mới không thành công
                        }
                }
                dataLoadListener.onDataLoaded()
            }
            .addOnFailureListener { exception ->
                // Xử lý khi có lỗi xảy ra khi truy vấn dữ liệu từ Firebase
            }

        FirebaseDatabase.getInstance()
            .getReference("Wallet")
            .child(uid.toString()).get()
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

                    // gửi sự kiện qua dataLoadListener => thông qua interface ở bên trên => kích hoạt onDataLoaded() trong WalletActivity
                    dataLoadListener.onDataLoaded()
                }

                // Khởi tạo adapter và thiết lập RecyclerView
                walletAdapter = WalletAdapter(walletList, requireContext(), binding.noWalletCard) { newAmount ->
                    binding.amountInWalletAmount.text = newAmount
                }
                binding.recyclerWalletList.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerWalletList.adapter = walletAdapter
                // Gọi sự kiện đối với list empty tương tự như khi adapter có data
                dataLoadListener.onDataLoadedEmpty(walletList.isEmpty())
            }
            .addOnFailureListener { exception ->
                Log.e("Wallet data", "Error getting data from Firebase", exception)
            }

    }

    fun updateNoWalletCardVisibility(visibility: Int) {
        binding.noWalletCard.visibility = visibility
    }

//    fun setDataLoadListener(listener: DataLoadListener) {
//        dataLoadListener = listener
//    }

}
