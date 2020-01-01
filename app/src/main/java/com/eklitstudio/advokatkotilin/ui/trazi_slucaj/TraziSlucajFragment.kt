package com.eklitstudio.advokatkotilin.ui.trazi_slucaj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj
import com.eklitstudio.advokatkotilin.databinding.TraziSlucajFragmentBinding
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.TraziSlucajViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.trazi_slucaj_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TraziSlucajFragment: ScopedFragment(), KodeinAware{

    override val kodein by closestKodein()

    private val viewModelFactory: CustomViewModelFactory by instance()

    private lateinit var binding: TraziSlucajFragmentBinding

    private lateinit var viewModel: TraziSlucajViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TraziSlucajFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TraziSlucajViewModel::class.java)

        binding.traziSlucajViewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.slucajevi
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()
    }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private fun bindUI() = launch(Dispatchers.Main){

        var displaylist: MutableList<Slucaj> = ArrayList()
        viewModel.slucajevi.await().observe(viewLifecycleOwner, Observer { listaSlucajeva ->

            if(listaSlucajeva == null) return@Observer
            group_loading.visibility = View.GONE

            trazi_slucaj_edit_text.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    displaylist.clear()
                    if(newText!!.isNotEmpty()){
                        val search = newText.toLowerCase()
                        listaSlucajeva.forEach{
                            if(it.sifra_slucaja.toString().contains(search)){
                                displaylist.add(it)
                                initRecyclerView(displaylist.toSlucajItems())
                            }
                        }
                    }else{
                        displaylist.addAll(listaSlucajeva)
                        initRecyclerView(displaylist.toSlucajItems())
                    }
                    return true
                }
            })

            initRecyclerView(listaSlucajeva.toSlucajItems())

        })
    }

    private fun List<Slucaj>.toSlucajItems() : List<SlucajItem>{
        return this.map {
            SlucajItem(it)
        }
    }

    private fun initRecyclerView(slucajevi: List<SlucajItem>){

        val groupAdapter= GroupAdapter<GroupieViewHolder>().apply {
            addAll(slucajevi)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TraziSlucajFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener{ item, view ->
            if(item is SlucajItem) {
                Navigation.findNavController(view).navigate(
                    TraziSlucajFragmentDirections
                        .actionTraziSlucajFragmentToDodavanjeRadnjeFragment(item.slucaj.id, item.slucaj.postupak_id.toLong())
                )
            }
        }
    }

}
