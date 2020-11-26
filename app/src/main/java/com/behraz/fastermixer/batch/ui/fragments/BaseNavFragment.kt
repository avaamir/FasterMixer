package com.behraz.fastermixer.batch.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.findNavController

class BaseNavFragment : Fragment() {

    private var isFirstTimeInit = true

    private lateinit var onNavChangeListener: OnNavigationChangedListener

    var currentDestination: NavDestination? = null
        private set

    var toolbarTitle = ""
        private set

    private var layoutRes: Int = -1
    private var navHostId: Int = -1    // root destinations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // extract arguments from bundle
        arguments?.let {
            layoutRes = it.getInt(KEY_LAYOUT)
            navHostId = it.getInt(KEY_NAV_HOST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layoutRes, container, false)
    }


    override fun onStart() {
        super.onStart()
        requireActivity().findNavController(navHostId)
            .addOnDestinationChangedListener { controller, destination, arguments ->
                currentDestination = destination
                if (!isFirstTimeInit) {
                    toolbarTitle = onNavChangeListener.notifyNavigationChanged(destination)
                } else { //age avalin bar bud ke dasht init mishod chun toolbar beyn hame fragment ha moshtarak hast titr dorost nemikhord va titresh mishod un fragmenti ke akhar az hame init shode be in elat in var ro tarif kardim va bar aval titr ro az label mikhonim
                    toolbarTitle = destination.label.toString()
                    isFirstTimeInit = false
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onNavChangeListener = requireActivity() as OnNavigationChangedListener
    }

    fun onBackPressed(): Boolean = requireActivity()
        .findNavController(navHostId)
        .navigateUp()

    fun popToRoot() {
        val navController = requireActivity().findNavController(navHostId)
        navController.popBackStack(navController.graph.startDestination, false)
    }

    interface OnNavigationChangedListener {
        fun notifyNavigationChanged(destination: NavDestination): String //return toolbar title
    }

    companion object {
        private const val KEY_LAYOUT = "layout_key"
        private const val KEY_NAV_HOST = "nav_host_key"

        fun newInstance(layoutRes: Int, navHostId: Int) = BaseNavFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_LAYOUT, layoutRes)
                putInt(KEY_NAV_HOST, navHostId)
            }
        }
    }
}