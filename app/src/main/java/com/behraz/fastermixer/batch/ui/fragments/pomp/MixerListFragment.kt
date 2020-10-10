package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentMixerListBinding
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel

class MixerListFragment : Fragment(), MixerAdapter.PompAdapterInteraction {
    private val mixerAdapter = MixerAdapter(true, this)
    private lateinit var mBinding: FragmentMixerListBinding
    private lateinit var viewModel: PompActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mixer_list, container, false)
        initViews()
        observeViewModel()
        return mBinding.root
    }

    private fun observeViewModel() {
        viewModel.shouldShowAllMixers.observe(viewLifecycleOwner, Observer { shouldShowAll ->
            if (shouldShowAll)
                handleServerResponse(viewModel.allMixers.value)
            else
                handleServerResponse(viewModel.requestMixers.value)
        })

        viewModel.allMixers.observe(viewLifecycleOwner, Observer {
            if (viewModel.shouldShowAllMixers.value!!) {
                handleServerResponse(it)
            }
        })

        viewModel.requestMixers.observe(viewLifecycleOwner, Observer {
            if (!viewModel.shouldShowAllMixers.value!!) {
                handleServerResponse(it)
            }
        })


    }

    private fun handleServerResponse(response: Entity<List<Mixer>>?) {
        if (response != null) {
            if (response.isSucceed) {
                mixerAdapter.submitList(response.entity)
            } else {
                toast(response.message)
                println("debug:error:MixerListFragment:handleServerResponse->response.isSucceed=${response.isSucceed}")
                //TODo
            }
        } else {
            toast(Constants.SERVER_ERROR)
            println("debug:error:MixerListFragment:handleServerResponse->Response is null")
        }
    }

    private fun initViews() {
        mBinding.btnMap.setOnClickListener { activity!!.onBackPressed() }

        mBinding.mixerRecycler.adapter = mixerAdapter
        mBinding.mixerRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )
        mBinding.mixerRecycler.layoutManager =
            LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
    }


    fun scrollToTop() {
        Handler().postDelayed({
            mBinding.mixerRecycler.smoothScrollToPosition(0)
        }, 1000)
    }

    override fun onShowOnMapClicked(mixer: Mixer) {
        LocalBroadcastManager.getInstance(context!!)
            .sendBroadcast(Intent(Constants.ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP).apply {
                putExtra(Constants.ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP_MIXER_ID, mixer.id)
            })
    }

    override fun onCallClicked(mixer: Mixer) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mixer.phone)))
    }
}
