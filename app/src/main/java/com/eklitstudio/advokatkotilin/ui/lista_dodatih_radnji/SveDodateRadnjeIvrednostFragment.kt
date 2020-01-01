package com.eklitstudio.advokatkotilin.ui.lista_dodatih_radnji

import android.content.Context
import android.os.Bundle
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.IzracunatTrosakRadnje
import com.eklitstudio.advokatkotilin.databinding.FragmentSveDodateRadnjeBinding
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.ui.konacni_trosak.MyPrintDocumentAdapter
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.SveDodateRadnjeiNJihoveVrednostiViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_sve_dodate_radnje.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class SveDodateRadnjeIvrednostFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CustomViewModelFactory by instance()
    private lateinit var binding: FragmentSveDodateRadnjeBinding
    private lateinit var viewModel: SveDodateRadnjeiNJihoveVrednostiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSveDodateRadnjeBinding.inflate(
            inflater,
            container,
            false
        )


        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SveDodateRadnjeiNJihoveVrednostiViewModel::class.java)
        if (arguments != null){
            var args = SveDodateRadnjeIvrednostFragmentArgs.fromBundle(arguments!!)
            viewModel.setSlucajID(args.idSlucaja)
        }

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_make_pdf.setOnClickListener{

                val printManager = activity!!
                    .getSystemService(Context.PRINT_SERVICE) as PrintManager
                val jobName = this.getString(R.string.app_name) + " Document"

            launch(Dispatchers.Main){
                var slucajSaIzracunatimTroskovimaRadnje = viewModel.slucajSaIzracunatimTroskovimaRadnje.await()
                var slucajSaStrankama = viewModel.slucajSaStrankama.await()
                printManager.print(jobName, MyPrintDocumentAdapter(requireContext(), slucajSaStrankama, slucajSaIzracunatimTroskovimaRadnje, ukupno), null)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()

    }

    /**
     * Ukupna izracunata vrednost svih radnji
     */
    private var ukupno = 0
    private fun bindUI() = launch(Dispatchers.Main){


        viewModel._slucajSaIzracunatimTroskovimaRadnje.await().observe(viewLifecycleOwner, Observer {
           if(it == null) return@Observer
           group_loading_dodate_radnje.visibility = View.GONE
           initRecyclerView(it.izracunatiTroskoviRadnja.toIzvrsenaRadnjaItem())
            ukupno = it.izracunatiTroskoviRadnja.sumBy{
               it.cena!!.toInt()
           }
           text_view_ukupnasvota.text = ukupno.toString()
       })
    }


    private fun List<IzracunatTrosakRadnje>.toIzvrsenaRadnjaItem() : List<IzvrsenaRadnjaItem>{
        return this.map {
            IzvrsenaRadnjaItem(it)
        }
    }

    private fun initRecyclerView(radnje: List<IzvrsenaRadnjaItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(radnje)
        }

        recyclerViewZaDodateRadnje.apply {
            layoutManager = LinearLayoutManager(this@SveDodateRadnjeIvrednostFragment.context)
            adapter = groupAdapter
        }

        //todo mozda dodati brisanje! onclick ...

    }

}