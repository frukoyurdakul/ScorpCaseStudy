package com.scorp.casestudy.furkanyurdakul.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.scorp.casestudy.furkanyurdakul.util.withLifecycle

abstract class BaseActivity<VDB: ViewDataBinding>: AppCompatActivity()
{
    protected abstract val layoutId: Int

    private var _binding: VDB? = null
    protected val binding: VDB get() = _binding!!

    protected abstract suspend fun setupUi()

    final override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)

        withLifecycle {
            setupUi()
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        _binding = null
    }
}