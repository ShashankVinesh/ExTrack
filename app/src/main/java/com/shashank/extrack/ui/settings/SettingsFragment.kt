package com.shashank.extrack.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.shashank.extrack.R
import com.shashank.extrack.data.local.worker.AutoDeleteScheduler
import com.shashank.extrack.data.local.worker.AutoDeleteWorker
import com.shashank.extrack.databinding.FragmentSettingsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: AutoDeletePrefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        prefs = AutoDeletePrefs(requireContext())

        // Load current setting and check correct radio
        viewLifecycleOwner.lifecycleScope.launch {
            prefs.policyFlow.collectLatest { policy ->
                when (policy) {
                    AutoDeletePolicy.NEVER -> binding.rbNever.isChecked = true
                    AutoDeletePolicy.DAY -> binding.rbDay.isChecked = true
                    AutoDeletePolicy.WEEK -> binding.rbWeek.isChecked = true
                    AutoDeletePolicy.MONTH -> binding.rbMonth.isChecked = true
                }
            }
        }


        // Save setting + schedule/cancel worker
        binding.rgAutoDelete.setOnCheckedChangeListener { _, checkedId ->
            val selected = when (checkedId) {
                binding.rbDay.id -> AutoDeletePolicy.DAY
                binding.rbWeek.id -> AutoDeletePolicy.WEEK
                binding.rbMonth.id -> AutoDeletePolicy.MONTH
                else -> AutoDeletePolicy.NEVER
            }

            viewLifecycleOwner.lifecycleScope.launch {
                prefs.setPolicy(selected)

                if (selected == AutoDeletePolicy.NEVER) {
                    AutoDeleteScheduler.cancel(requireContext())
                } else {
                    AutoDeleteScheduler.scheduleDaily(requireContext())
                }
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}