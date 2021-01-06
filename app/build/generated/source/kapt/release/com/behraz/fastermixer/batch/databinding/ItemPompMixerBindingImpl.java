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
        sViewsWithIds.put(R.id.textView11, 8);
        sViewsWithIds.put(R.id.textView12, 9);
        sViewsWithIds.put(R.id.textView13, 10);
        sViewsWithIds.put(R.id.textView15, 11);
        sViewsWithIds.put(R.id.textView17, 12);
        sViewsWithIds.put(R.id.btnCall, 13);
        sViewsWithIds.put(R.id.btn_show_mixer_on_map, 14);
        sViewsWithIds.put(R.id.carId, 15);
        sViewsWithIds.put(R.id.textView19, 16);
        sViewsWithIds.put(R.id.gpNotNeeded, 17);
        sViewsWithIds.put(R.id.tvSpeedState, 18);
        sViewsWithIds.put(R.id.tvLastDataTime, 19);
        sViewsWithIds.put(R.id.imageView8, 20);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemPompMixerBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 21, sIncludes, sViewsWithIds));
    }
    private ItemPompMixerBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[13]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[14]
            , (com.behraz.fastermixer.batch.ui.customs.fastermixer.CarIdView) bindings[15]
            , (androidx.cardview.widget.CardView) bindings[0]
            , (androidx.constraintlayout.widget.Group) bindings[17]
            , (android.widget.ImageView) bindings[20]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[19]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[18]
            );
        this.frame.setTag(null);
        this.textView14.setTag(null);
        this.textView16.setTag(null);
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
        java.lang.String javaLangStringMixerDriverName = null;
        java.lang.String javaLangStringMixerDriverNameJavaLangString = null;
        java.lang.String mixerLoadInfoAmount = null;
        float mixerCapacity = 0f;
        boolean mixerDriverNameEmpty = false;
        com.behraz.fastermixer.batch.models.LoadInfo mixerLoadInfo = null;
        java.lang.String mixerCapacityJavaLangString = null;
        java.lang.String mixerDriverNameEmptyJavaLangStringJavaLangStringMixerDriverNameJavaLangString = null;
        com.behraz.fastermixer.batch.models.Mixer mixer = mMixer;
        java.lang.String mixerLoadInfoDensity = null;
        java.lang.String mixerCarName = null;
        java.lang.String mixerDriverName = null;
        java.lang.String mixerLoadInfoSlump = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (mixer != null) {
                    // read mixer.state
                    mixerState = mixer.getState();
                    // read mixer.capacity
                    mixerCapacity = mixer.getCapacity();
                    // read mixer.loadInfo
                    mixerLoadInfo = mixer.getLoadInfo();
                    // read mixer.carName
                    mixerCarName = mixer.getCarName();
                    // read mixer.driverName
                    mixerDriverName = mixer.getDriverName();
                }


                // read (mixer.capacity) + ("")
                mixerCapacityJavaLangString = (mixerCapacity) + ("");
                if (mixerLoadInfo != null) {
                    // read mixer.loadInfo.amount
                    mixerLoadInfoAmount = mixerLoadInfo.getAmount();
                    // read mixer.loadInfo.density
                    mixerLoadInfoDensity = mixerLoadInfo.getDensity();
                    // read mixer.loadInfo.slump
                    mixerLoadInfoSlump = mixerLoadInfo.getSlump();
                }
                if (mixerDriverName != null) {
                    // read mixer.driverName.empty
                    mixerDriverNameEmpty = mixerDriverName.isEmpty();
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(mixerDriverNameEmpty) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x4L) != 0) {

                // read ("(") + (mixer.driverName)
                javaLangStringMixerDriverName = ("(") + (mixerDriverName);


                // read (("(") + (mixer.driverName)) + (")")
                javaLangStringMixerDriverNameJavaLangString = (javaLangStringMixerDriverName) + (")");
        }

        if ((dirtyFlags & 0x3L) != 0) {

                // read mixer.driverName.empty ? "" : (("(") + (mixer.driverName)) + (")")
                mixerDriverNameEmptyJavaLangStringJavaLangStringMixerDriverNameJavaLangString = ((mixerDriverNameEmpty) ? ("") : (javaLangStringMixerDriverNameJavaLangString));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView14, mixerLoadInfoSlump);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView16, mixerLoadInfoDensity);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView20, mixerCapacityJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView21, mixerDriverNameEmptyJavaLangStringJavaLangStringMixerDriverNameJavaLangString);
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
        flag 2 (0x3L): mixer.driverName.empty ? "" : (("(") + (mixer.driverName)) + (")")
        flag 3 (0x4L): mixer.driverName.empty ? "" : (("(") + (mixer.driverName)) + (")")
    flag mapping end*/
    //end
}