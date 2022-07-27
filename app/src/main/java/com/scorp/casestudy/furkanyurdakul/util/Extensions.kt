package com.scorp.casestudy.furkanyurdakul.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.withLifecycle(block: suspend CoroutineScope.() -> Unit)
{
    if (isAdded)
    {
        // Checked for getView() first since fragments can throw exceptions
        // if onDestroyView was called while accessing viewLifecycleOwner,
        // since they're both tied to the same frame on nullification.
        view?.let {
            viewLifecycleOwner.lifecycleScope.launch(block = block)
        }
    }
}