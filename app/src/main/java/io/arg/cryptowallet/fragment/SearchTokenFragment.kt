package io.arg.cryptowallet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.arg.cryptowallet.databinding.FragmentSearchTokenBinding

class SearchTokenFragment : Fragment() {

    private lateinit var binding: FragmentSearchTokenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentSearchTokenBinding.inflate(inflater, container, false)


        return binding.root
    }

}