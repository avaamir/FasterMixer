package com.behraz.fastermixer.batch.databinding;
import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemPompMixerBindingImpl extends ItemPompMixerBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textView11, 9);
        sViewsWithIds.put(R.id.textView12, 10);
        sViewsWithIds.put(R.id.textView13, 11);
        sViewsWithIds.put(R.id.textView15, 12);
        sViewsWithIds.put(R.id.textView17, 13);
        sViewsWithIds.put(R.id.btnCall, 14);
        sViewsWithIds.put(R.id.textView8, 15);
        sViewsWithIds.put(R.id.textView10, 16);
        sViewsWithIds.put(R.id.textView19, 17);
    }
    // views
    @NonNull
    private final androidx.cardview.widget.CardView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemPompMixerBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 18, sIncludes, sViewsWithIds));
    }
    private ItemPompMixerBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[14]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[8]
            , (com.behraz.fastermixer.batch.ui.customs.fastermixer.CarIdView) bindings[15]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[1]
            );
        this.mboundView0 = (androidx.cardview.widget.CardView) bindings[0];
        this.mboundView0.setTag(null);
        this.textView14.setTag(null);
        this.textView16.setTag(null);
        this.textView18.setTag(null);
        this.textView20.setTag(null);
        this.textView21.setTag(null);
        this.textView22.setTag(null);
        this.textView9.setTag(null);
        this.tvMixerName.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.mixer == variableId) {
            setMixer((com.behraz.fastermixer.batch.models.Mixer) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setMixer(@Nullable com.behraz.fastermixer.batch.models.Mixer Mixer) {
        this.mMixer = Mixer;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.mixer);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String mixerState = null;
        java.lang.String mixerLoadInfoAmountFromTotal = null;
        java.lang.String mixerLoadInfoAmount = null;
        com.behraz.fastermixer.batch.models.LoadInfo mixerLoadInfo = null;
        java.lang.String mixerLoadInfoStartTime = null;
        com.behraz.fastermixer.batch.models.Mixer mixer = mMixer;
        java.lang.String mixerLoadInfoDensity = null;
        java.lang.String mixerCarName = null;
        java.lang.String mixerDriverName = null;
        java.lang.String mixerLoadInfoSlump = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (mixer != null) {
                    // read mixer.state
                    mixerState = mixer.getState();
                    // read mixer.loadInfo
                    mixerLoadInfo = mixer.getLoadInfo();
                    // read mixer.carName
                    mixerCarName = mixer.getCarName();
                    // read mixer.driverName
                    mixerDriverName = mixer.getDriverName();
                }


                if (mixerLoadInfo != null) {
                    // read mixer.loadInfo.amountFromTotal
                    mixerLoadInfoAmountFromTotal = mixerLoadInfo.getAmountFromTotal();
                    // read mixer.loadInfo.amount
                    mixerLoadInfoAmount = mixerLoadInfo.getAmount();
                    // read mixer.loadInfo.startTime
                    mixerLoadInfoStartTime = mixerLoadInfo.getStartTime();
                    // read mixer.loadInfo.density
                    mixerLoadInfoDensity = mixerLoadInfo.getDensity();
                    // read mixer.loadInfo.slump
                    mixerLoadInfoSlump = mixerLoadInfo.getSlump();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView14, mixerLoadInfoSlump);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView16, mixerLoadInfoDensity);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView18, mixerLoadInfoAmountFromTotal);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView20, mixerLoadInfoStartTime);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView21, mixerDriverName);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView22, mixerState);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView9, mixerLoadInfoAmount);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvMixerName, mixerCarName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): mixer
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}