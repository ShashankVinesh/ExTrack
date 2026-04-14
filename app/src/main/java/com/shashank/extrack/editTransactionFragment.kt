package com.shashank.extrack

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shashank.extrack.data.local.db.TransactionDatabase
import com.shashank.extrack.data.local.entity.TransactionEntity
import com.shashank.extrack.data.local.repository.TransactionRepository
import com.shashank.extrack.databinding.FragmentEditTransactionBinding
import com.shashank.extrack.ui.home.HomeViewModel
import com.shashank.extrack.ui.home.HomeViewModelFactory

class editTransactionFragment : Fragment(R.layout.fragment_edit_transaction) {

    private var _binding: FragmentEditTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var transaction: TransactionEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditTransactionBinding.bind(view)

        setupViewModel()

        transaction = requireArguments().getParcelable("transaction")!!

        setupUI()
        setupActions()
    }

    private fun setupViewModel() {
        val dao = TransactionDatabase.getDatabase(requireContext()).transactionDao()
        val repository = TransactionRepository(dao)
        val factory = HomeViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setupUI() {
        binding.etTitle.setText(transaction.title)
        binding.etAmount.setText(transaction.amount.toString())

        if (transaction.isIncome) {
            binding.rbIncome.isChecked = true
        } else {
            binding.rbExpense.isChecked = true
        }
    }

    private fun setupActions() {

        // UPDATE
        binding.btnUpdate.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString()

            if (title.isEmpty() || amount.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updated = transaction.copy(
                title = title,
                amount = amount.toDouble(),
                isIncome = binding.rbIncome.isChecked
            )

            viewModel.insert(updated)
            findNavController().popBackStack()
        }

        // DELETE
        binding.btnDelete.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.softDelete(transaction.id)
                    requireActivity().onBackPressed()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}