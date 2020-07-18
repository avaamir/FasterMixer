package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentMixerListBinding
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MixerListFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel

class MixerListFragment : Fragment(), MixerAdapter.Interaction {
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
      viewModel.mixers.observe(viewLifecycleOwner, Observer {
          if (it != null) {
              if (it.isSucceed) {
                  mixerAdapter.submitList(it.entity)
              } else {
                  //TODo
              }
          } else {
              //TODo
          }
      })
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

    override fun onCallClicked(mixer: Mixer) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mixer.phone)))
    }

    override fun onEndLoadingClicked(mixer: Mixer) { /*just for batch*/
    }

}
