package com.example.stocksviewer.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.stocksviewer.App
import com.example.stocksviewer.R
import com.example.stocksviewer.databinding.FragmentDetailsBinding
import com.example.stocksviewer.model.Resource
import com.example.stocksviewer.viewmodels.DetailsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import com.example.stocksviewer.model.entity.HistoricItemWithCurrency
import com.example.stocksviewer.utils.*
import com.example.stocksviewer.utils.onclick.BuyClick
import com.example.stocksviewer.utils.onclick.OnGoBackClick
import com.example.stocksviewer.utils.onclick.OnSetIntervalClick

class DetailsFragment : Fragment(), OnGoBackClick, BuyClick, OnSetIntervalClick {
    private lateinit var binding: FragmentDetailsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailsViewModel
    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[DetailsViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_details,
            container,
            false
        )

        initChart(binding.root)

        return binding.root
    }


    override fun buyClick() {
        val uriUrl: Uri = Uri.parse("https://bitcoin.org/ru/buy")
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    override fun setInterval(interval: Interval) {
        viewModel.setInterval(interval.description)
    }

    override fun goBack() {
        findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("symbol")?.let { viewModel.start(it) }
        setupObservers()
        binding.buy = this
        binding.setInterval = this
    }

    private fun setupObservers() {
        viewModel.quote.observe(viewLifecycleOwner, {
            binding.quote = it
            binding.goBack = this
            binding.detailsViewModel = viewModel
        })

        viewModel.data.observe(viewLifecycleOwner, {
            if(it.status == Resource.Status.LOADING){

            }
            if (it.status == Resource.Status.SUCCESS) {
                val data = it.data!!.firstOrNull()?.items?.items?.map { x ->
                    val date = SimpleDateFormat(getString(R.string.date_pattern)).parse(x.date)
                    val item = HistoricItemWithCurrency(x, viewModel.quote.value?.info?.currency)
                    Entry(
                        date.time.toFloat(),
                        x.open.toFloat(),
                        item
                    )
                }?.toList()
                if (data != null)
                    setData(data)
            }
        })
    }

    private fun initChart(view: View) {
        chart = view.findViewById(R.id.chart)
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawBorders(false)
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setDrawGridBackground(false)
        val mv = MyMarkerView(view.context, R.layout.custom_marker_view)
        mv.chartView = chart
        chart.marker = mv
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.animateX(1500)
        chart.axisRight.setDrawLabels(false)
        chart.axisLeft.setDrawLabels(false)
        chart.xAxis.setDrawLabels(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.setDrawGridLines(false)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.isEnabled = false
        chart.minOffset = 0f
    }

    private fun setData(values: List<Entry>) {
        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        } else {
            set1 = LineDataSet(values, "DataSet 1")
            set1.setDrawIcons(false)
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)
            set1.lineWidth = 2f
            set1.circleRadius = 4f
            set1.setDrawCircleHole(false)
            set1.formLineWidth = 1f
            set1.formSize = 15f
            set1.valueTextSize = 9f
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
            if (Utils.getSDKInt() >= 18) {
                val drawable = ContextCompat.getDrawable(this.context!!, R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)
            val data = LineData(dataSets)
            chart.data = data
        }
    }


}

