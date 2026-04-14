package com.shashank.extrack.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.extrack.R
import com.shashank.extrack.data.local.db.TransactionDatabase
import com.shashank.extrack.data.local.repository.TransactionRepository
import com.shashank.extrack.databinding.FragmentHistoryBinding
import com.shashank.extrack.ui.home.adapter.TransactionAdapter


class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: TransactionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistoryBinding.bind(view)

        setupViewModel()
        setupRecycler()
        setupActions()
        observe()
    }

    private fun setupViewModel() {
        val dao = TransactionDatabase.getDatabase(requireContext()).transactionDao()
        val repo = TransactionRepository(dao)
        val factory = HistoryViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
    }

    private fun setupRecycler() {
        adapter = TransactionAdapter(showDeletedAt = true)
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter
    }

    private fun setupActions() {
        binding.btnClearAll.setOnClickListener {
            viewModel.clearDeleted()
        }

        binding.tvFilter.setOnClickListener { anchor ->
            val popup = PopupMenu(requireContext(), anchor)
            popup.menuInflater.inflate(R.menu.menu_filter, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.filter_all -> {
                        binding.tvFilter.text = "All Time ▼"
                        viewModel.setFilter("ALL")
                    }
                    R.id.filter_day -> {
                        binding.tvFilter.text = "Day ▼"
                        viewModel.setFilter("DAY")
                    }
                    R.id.filter_week -> {
                        binding.tvFilter.text = "Week ▼"
                        viewModel.setFilter("WEEK")
                    }
                    R.id.filter_month -> {
                        binding.tvFilter.text = "Month ▼"
                        viewModel.setFilter("MONTH")
                    }
                }
                true
            }
            popup.show()
        }
    }

    private fun observe() {
        viewModel.transactions.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.transactions.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmptyHistory.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}