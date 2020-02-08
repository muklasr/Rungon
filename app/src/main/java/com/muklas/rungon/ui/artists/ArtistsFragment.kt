package com.muklas.rungon.ui.artists

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.muklas.rungon.R

class ArtistsFragment : Fragment() {

    companion object {
        fun newInstance() = ArtistsFragment()
    }

    private lateinit var viewModel: ArtistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.artists_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArtistsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
