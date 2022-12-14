package com.scorp.casestudy.furkanyurdakul.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.scorp.casestudy.furkanyurdakul.util.withLifecycle

abstract class BaseFragment<VDB: ViewDataBinding>: Fragment()
{
    protected abstract val layoutId: Int

    private var _binding: VDB? = null
    protected val binding: VDB get() = _binding!!

    protected abstract suspend fun setupUi()

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        withLifecycle {
            setupUi()
        }
    }

    /**
     * Called when navigation bar back button or toolbar back button is
     * pressed.
     *
     * @return true to override default behavior
     */
    open fun onBackPress() = false

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}