package com.behraz.fastermixer.batch.ui.customs.general

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseSelectableAdapter<T>(
    private val DIFF_CALLBACK: DiffUtil.ItemCallback<T>
) : ListAdapter<T, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var lastItem: T? = null
    private var lastSelectedItemPosition = -1

    abstract var T.isItemSelected: Boolean
    abstract fun onItemSelected(t: T?, position: Int)

    override fun submitList(list: List<T>?) {
        if (list != null) {
            if (lastItem != null) {
                if (list.isNotEmpty()) {
                    for (i in list.indices) {
                        if (DIFF_CALLBACK.areItemsTheSame(
                                list[i],
                                lastItem!!
                            )
                        ) { //lastAddress avaz nashode vali list avaz shode
                            lastItem = list[i]
                            lastSelectedItemPosition = i
                            lastItem!!.isItemSelected = true
                            break
                        } else if (i == list.lastIndex) {
                            lastItem = list[0]
                            lastSelectedItemPosition = 0
                            lastItem!!.isItemSelected =true
                        }
                    }
                } else {
                    lastSelectedItemPosition = -1
                    lastItem = null
                }
            } else {  //avalin bar ke dare initial mishe lastAddress null hast
                if (list.isNotEmpty()) {
                    lastItem = list[0]
                    lastSelectedItemPosition = 0
                    lastItem!!.isItemSelected = true
                }
            }
        }

        super.submitList(list)
        /*if (currentList.size > 0) {
            lastItem = currentList[0]
            lastItem!!.isSelected = true
            lastSelectedItemPosition = 0
        }*/
        onItemSelected(lastItem, lastSelectedItemPosition)
    }

    /**use this function in viewHolder, When implementing onClickListeners use this for select item and this notify ui which item is selected*/
    protected fun onItemClicked(absoluteAdapterPosition: Int) { //use this fun in views you want to have select ability
        val item = currentList[absoluteAdapterPosition]
        if (lastItem == null) {
            item.isItemSelected = true
            lastItem = item
            lastSelectedItemPosition = absoluteAdapterPosition
            notifyItemChanged(absoluteAdapterPosition)
            onItemSelected(item, absoluteAdapterPosition)
        } else if (lastItem !== item) {
            lastItem!!.isItemSelected = false
            item.isItemSelected = true

            val lastPositionTemp = lastSelectedItemPosition
            lastItem = item
            lastSelectedItemPosition = absoluteAdapterPosition

            notifyItemChanged(absoluteAdapterPosition)
            notifyItemChanged(lastPositionTemp)

            onItemSelected(item, lastSelectedItemPosition)
        }
    }
}