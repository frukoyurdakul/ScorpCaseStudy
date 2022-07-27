package com.scorp.casestudy.furkanyurdakul.util

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.scorp.casestudy.furkanyurdakul.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

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

fun ComponentActivity.withLifecycle(block: suspend CoroutineScope.() -> Unit)
{
    lifecycleScope.launch(block = block)
}

/**
 * Navigates to another fragment using a direction by using the default
 * animations, as [R.anim.fade_in] and [R.anim.fade_out].
 */
fun Fragment.navigate(navDirections: NavDirections)
{
    runCatching {

        // Check if the action exists.
        val controller = findNavController()
        val action = controller.currentDestination?.getAction(navDirections.actionId)
        action ?: return

        // Find the current navigation options.
        val sourceOptions = action.navOptions!!

        // Create a copy and override the default animations.
        val navOptions = navOptions {

            // Apply default animations to navigation if none are defined.
            anim {
                enter = if (sourceOptions.enterAnim == -1) R.anim.fade_in else sourceOptions.enterAnim
                exit = if (sourceOptions.exitAnim == -1) R.anim.fade_out else sourceOptions.exitAnim
                popEnter = if (sourceOptions.popEnterAnim == -1) R.anim.fade_in else sourceOptions.popEnterAnim
                popExit = if (sourceOptions.popExitAnim == -1) R.anim.fade_out else sourceOptions.popExitAnim
            }

            // Take source options and apply.
            sourceOptions.popUpToId.takeIf { it != -1 }?.let {
                popUpTo(it) {
                    inclusive = sourceOptions.isPopUpToInclusive()
                    saveState = sourceOptions.shouldPopUpToSaveState()
                }
            }

            // Apply the source to copy.
            restoreState = sourceOptions.shouldRestoreState()
        }

        // Set the nav options to this action.
        action.navOptions = navOptions

        // Finalize the navigation.
        controller.navigate(navDirections, navOptions)
    }
}

inline fun <reified T, reified U> T.asInstance(klass: KClass<U>): U?
where U : T & Any
{
    return if (this is U)
        this
    else
        null
}