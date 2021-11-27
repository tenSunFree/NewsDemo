package com.example.newsdemo.mine

import com.example.newsdemo.common.base.BaseFragment
import com.example.newsdemo.databinding.FragmentMineBinding

class MineFragment : BaseFragment<FragmentMineBinding>() {

    override fun setBinding(): FragmentMineBinding =
        FragmentMineBinding.inflate(layoutInflater)
}