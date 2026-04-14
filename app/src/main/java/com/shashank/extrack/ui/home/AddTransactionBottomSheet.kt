package com.shashank.extrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashank.extrack.data.local.entity.TransactionEntity
import com.shashank.extrack.databinding.BottomSheetAddBinding

class AddTransactionBottomSheet(
    private val onAddClick: (TransactionEntity) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var isIncome = true

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            isIncome = checkedId == binding.rbIncome.id
        }

        binding.btnAdd.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val amountText = binding.etAmount.text.toString()

            if (title.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transaction = TransactionEntity(
                title = title,
                amount = amountText.toDouble(),
                isIncome = isIncome,
                date = System.currentTimeMillis()
            )

            onAddClick(transaction)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}