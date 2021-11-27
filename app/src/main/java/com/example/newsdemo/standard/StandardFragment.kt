package com.example.newsdemo.standard

import com.example.newsdemo.common.base.BaseFragment
import com.example.newsdemo.databinding.FragmentStandardBinding

class StandardFragment : BaseFragment<FragmentStandardBinding>() {

    override fun setBinding(): FragmentStandardBinding =
        FragmentStandardBinding.inflate(layoutInflater)
}