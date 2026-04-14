package com.shashank.extrack.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.extrack.R
import com.shashank.extrack.data.local.db.TransactionDatabase
import com.shashank.extrack.data.local.repository.TransactionRepository
import com.shashank.extrack.databinding.FragmentHomeBinding
import com.shashank.extrack.ui.home.adapter.TransactionAdapter
import com.shashank.extrack.util.formatINR


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: TransactionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        setupViewModel()
        setupRecyclerView()
        observeData()
    }

    private fun setupViewModel() {
        val dao = TransactionDatabase.getDatabase(requireContext()).transactionDao()
        val repository = TransactionRepository(dao)
        val factory = HomeViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter()

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter
    }

    private fun observeData() {

        // List
        viewModel.transactions.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.transactions.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmptyHome.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.totalIncome.observe(viewLifecycleOwner) {
            binding.tvIncome.text = formatINR(it)
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) {
            binding.tvExpense.text = formatINR(it)
        }

        viewModel.totalProfit.observe(viewLifecycleOwner) {
            binding.tvProfit.text = formatINR(it)
        }


        binding.fabAdd.setOnClickListener {

            val bottomSheet = AddTransactionBottomSheet { transaction ->
                viewModel.insert(transaction)
            }

            bottomSheet.show(parentFragmentManager, "AddTransaction")
        }

        adapter.onItemClick = { transaction ->

            val bundle = Bundle().apply {
                putParcelable("transaction", transaction)
            }

            findNavController().navigate(
                R.id.editTransactionFragment,
                bundle
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}