package fr.maximedubost.digikofyapp.ui.preparation

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.maximedubost.digikofyapp.MainActivity
import fr.maximedubost.digikofyapp.adapters.PreparationAdapter
import fr.maximedubost.digikofyapp.databinding.PreparationFragmentBinding
import fr.maximedubost.digikofyapp.oldrepositories.PreparationRepository

class PreparationFragment : Fragment() {

    companion object {
        fun newInstance() = PreparationFragment()
    }

    private lateinit var viewModel: PreparationViewModel
    private lateinit var binding: PreparationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding  = PreparationFragmentBinding.inflate(inflater)

        val lavNoPreparation = binding.lavNoPreparation
        val tvNoPreparation = binding.tvNoPreparation
        lavNoPreparation.visibility = if (PreparationRepository.Singleton.preparationList.size == 0) View.VISIBLE else View.GONE
        tvNoPreparation.visibility = if (PreparationRepository.Singleton.preparationList.size == 0) View.VISIBLE else View.GONE
        val rvPreparations = binding.rvPreparations
        rvPreparations.adapter = PreparationAdapter(
            PreparationRepository.Singleton.preparationList.filter { it.saved },
            true,
            false,
            false,
            this.activity as MainActivity
        )

        initNavBar(rvPreparations)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PreparationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    /**
     * initNavBar
     */
    fun initNavBar(rvPreparations: RecyclerView) {
        var isSavedPreparationsPage = true
        var isNextPreparationsPage = false
        var isPastPreparationsPage = false

        val tvSavedPreparationsTab: TextView = binding.tvSavedPreparationsTab
        val tvNextPreparationsTab: TextView = binding.tvNextPreparationsTab
        val tvPastPreparationsTab: TextView = binding.tvPastPreparationsTab

        tvSavedPreparationsTab.setTextColor(Color.BLACK)

        tvSavedPreparationsTab.setOnClickListener {
            tvSavedPreparationsTab.setTextColor(Color.BLACK)
            tvNextPreparationsTab.setTextColor(Color.GRAY)
            tvPastPreparationsTab.setTextColor(Color.GRAY)
            isSavedPreparationsPage = true
            rvPreparations.adapter = PreparationAdapter(
                PreparationRepository
                    .Singleton
                    .preparationList
                    .filter { it.saved }
                    .sortedBy { it.name },
                isSavedPreparationsPage = isSavedPreparationsPage,
                context = this.activity as MainActivity
            )
        }

        tvNextPreparationsTab.setOnClickListener {
            tvSavedPreparationsTab.setTextColor(Color.GRAY)
            tvNextPreparationsTab.setTextColor(Color.BLACK)
            tvPastPreparationsTab.setTextColor(Color.GRAY)
            isNextPreparationsPage = true
            rvPreparations.adapter = PreparationAdapter(
                PreparationRepository
                    .Singleton
                    .preparationList
                    .filter { it.isFuturePreparation() }
                    .sortedBy { it.nextTime },
                isNextPreparationsPage = isNextPreparationsPage,
                context = this.activity as MainActivity
            )
        }

        tvPastPreparationsTab.setOnClickListener {
            tvSavedPreparationsTab.setTextColor(Color.GRAY)
            tvNextPreparationsTab.setTextColor(Color.GRAY)
            tvPastPreparationsTab.setTextColor(Color.BLACK)
            isPastPreparationsPage = true
            rvPreparations.adapter = PreparationAdapter(
                PreparationRepository
                    .Singleton
                    .preparationList
                    .filter { it.lastTime != null }
                    .sortedByDescending { it.lastTime },
                isPastPreparationsPage = isPastPreparationsPage,
                context = this.activity as MainActivity
            )
        }

    }

}