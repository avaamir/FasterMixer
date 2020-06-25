package com.behraz.fastermixer.batch.utils.general

import android.widget.CompoundButton

fun getRadioButtonsCheckChangedListener(radiosAndLogicMap: Map<CompoundButton, () -> Unit>) =
    object : CompoundButton.OnCheckedChangeListener {
        private val radios get() = radiosAndLogicMap.keys

        override fun onCheckedChanged(radio: CompoundButton?, isChecked: Boolean) {
            radios.forEach {
                it.setOnCheckedChangeListener(null)
                if (it != radio)
                    it.isChecked = false
            }
            //logic
            radiosAndLogicMap[radio]?.invoke()
            //
            radios.forEach {
                it.setOnCheckedChangeListener(this)
            }
        }
    }
