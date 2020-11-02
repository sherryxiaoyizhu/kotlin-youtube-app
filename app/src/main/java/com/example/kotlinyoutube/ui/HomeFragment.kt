package com.example.kotlinyoutube.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlinyoutube.MainActivity
import com.example.kotlinyoutube.R

class HomeFragment: Fragment() {

    private lateinit var swipe: SwipeRefreshLayout
    private val favoritesFragTag = "favoritesFragTag"
    private val videosFragTag = "videosFragTag"

    private val viewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_rv, container, false)
        initAdapter(root)
        initTitleObservers()
        initSwipeLayout(root)
        actionSearch()
        actionFavorite()
        return root
    }

    private fun initAdapter(root: View) {
        val viewAdapter = HomeAdapter(viewModel)
        root.findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        // observe data change
        viewModel.observeVideos().observe(viewLifecycleOwner,
            Observer {
                viewAdapter.submitList(it)
                viewAdapter.notifyDataSetChanged()
                swipe.isRefreshing = false
            })
    }

    private fun setTitle(newTitle: String) {
        requireActivity().findViewById<TextView>(R.id.actionTitle).text = newTitle
    }

    private fun initTitleObservers() {
        viewModel.observeTitle().observe(viewLifecycleOwner, Observer { title: String ->
            setTitle(title)
        })
    }

    private fun initSwipeLayout(root: View) {
        swipe = root.findViewById(R.id.swipeRefreshLayout)
        swipe.isRefreshing = true
        swipe.setOnRefreshListener {
            swipe.isRefreshing = true
            viewModel.repoFetch()
        }
    }

    private fun actionSearch() {
        requireActivity().findViewById<EditText>(R.id.actionSearch)
            .addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        if (s.isEmpty()) {
                            (activity as MainActivity).hideKeyboard()
                        }
                    }
                    viewModel.setSearchTerm(s.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
    }

    private fun actionFavorite() {
        requireActivity().findViewById<ImageView>(R.id.actionFavorite)
            .setOnClickListener {
                val newFavFrag = Favorites.newInstance()
                parentFragmentManager.apply {
                    if (backStackEntryCount == 0) { // // only enables actionFavorite in home fragment
                        beginTransaction()
                            .add(R.id.main_frame, newFavFrag)
                            .addToBackStack(favoritesFragTag)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit()
                    }
                }
            }
    }
}
