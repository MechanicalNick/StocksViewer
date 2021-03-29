package com.example.stocksviewer.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.stocksviewer.App
import com.example.stocksviewer.R
import com.example.stocksviewer.databinding.TabsFragmentBinding
import com.example.stocksviewer.di.ViewModelFactory
import com.example.stocksviewer.model.Resource
import com.example.stocksviewer.model.entity.QuoteWithInfoAndData
import com.example.stocksviewer.utils.onclick.OnQuoteItemClick
import com.example.stocksviewer.view.adapter.RecyclerAdapter
import com.example.stocksviewer.viewmodels.MainViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

class TabsFragment : Fragment(), OnQuoteItemClick {
    var counter by Delegates.notNull<Int>()
    private lateinit var binding: TabsFragmentBinding
    private lateinit var recyclerAdapter: RecyclerAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel


    init {
        App.component.inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counter = requireArguments().getInt(ARG_COUNT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.tabs_fragment,
            container,
            false
        )
        binding.mainViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView != null) {
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerAdapter = RecyclerAdapter(viewModel, this)
            recyclerView.adapter = recyclerAdapter
            setupObservers()
        }
    }

    private fun setupObservers() {
        viewModel.quotes.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        viewModel.filtredQuotes = viewModel.getItems(counter)
                        viewModel.filtredQuotes.observe(viewLifecycleOwner, { recyclerAdapter.updateData(it) })
                    }
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val ARG_COUNT = "param1"
        fun newInstance(counter: Int): TabsFragment {
            val fragment = TabsFragment()
            val args = Bundle()
            args.putInt(ARG_COUNT, counter)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(quoteWithInfoAndData: QuoteWithInfoAndData) {
        findNavController().navigate(
            R.id.action_charactersFragment_to_characterDetailFragment,
            bundleOf("symbol" to quoteWithInfoAndData.quote.symbol)
        )
    }
}