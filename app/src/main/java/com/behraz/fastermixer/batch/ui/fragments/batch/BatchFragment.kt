package com.behraz.fastermixer.batch.ui.fragments.batch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutBatchFragmentBinding
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.BatchActivityViewModel

class BatchFragment : Fragment(), MixerAdapter.BatchAdapterInteraction {
    private lateinit var mBinding: LayoutBatchFragmentBinding
    private lateinit var viewModel: BatchActivityViewModel

    private val mAdapter = MixerAdapter(false, this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(BatchActivityViewModel::class.java)
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_batch_fragment, container, false)
        initViews()
        subscribeObservers()
        return mBinding.root
    }

    private fun subscribeObservers() {
        viewModel.mixers.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isSucceed) {
                    it.entity?.let { mixers ->
                        if (mixers.isEmpty()) {
                            mBinding.animationView.visibility = View.VISIBLE
                            mBinding.tvNoMessage.visibility = View.VISIBLE
                        } else {
                            mBinding.animationView.visibility = View.GONE
                            mBinding.tvNoMessage.visibility = View.GONE
                        }
                        mAdapter.submitList(mixers)
                    }
                } else {
                    toast(it.message)
                }
            } else {
                snack(Constants.SERVER_ERROR) {
                    viewModel.refreshMixers()
                }
            }
        }
    }


    fun initViews() {
        mBinding.mixerRecycler.adapter = mAdapter
        mBinding.mixerRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mBinding.mixerRecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
    }

    override fun onCallClicked(mixer: Mixer) {
        toast("not yet implemented")
        //val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mixer.phone))
        //startActivity(intent)
    }

    override fun onEndLoadingClicked(mixer: Mixer) {
        //toast(mixer.carName + " onEnd")
        toast("${mixer.location}")
    }
}