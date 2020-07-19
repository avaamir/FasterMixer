package com.behraz.fastermixer.batch.databinding;
import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityPompBindingImpl extends ActivityPompBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(17);
        sIncludes.setIncludes(1, 
            new String[] {"item_customer"},
            new int[] {3},
            new int[] {com.behraz.fastermixer.batch.R.layout.item_customer});
        sIncludes.setIncludes(2, 
            new String[] {"layout_mixer"},
            new int[] {4},
            new int[] {com.behraz.fastermixer.batch.R.layout.layout_mixer});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.mapContainer, 5);
        sViewsWithIds.put(R.id.frame_top, 6);
        sViewsWithIds.put(R.id.jobProgressView, 7);
        sViewsWithIds.put(R.id.btnArrow, 8);
        sViewsWithIds.put(R.id.btnMessage, 9);
        sViewsWithIds.put(R.id.imageView2, 10);
        sViewsWithIds.put(R.id.tvMessageCount, 11);
        sViewsWithIds.put(R.id.bottomSheet, 12);
        sViewsWithIds.put(R.id.fasterMixerUserPanel, 13);
        sViewsWithIds.put(R.id.btnHideUserPanel, 14);
        sViewsWithIds.put(R.id.btnWeather, 15);
        sViewsWithIds.put(R.id.btnMyLocation, 16);
    }
    // views
    @NonNull
    private final androidx.coordinatorlayout.widget.CoordinatorLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityPompBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private ActivityPompBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (androidx.core.widget.NestedScrollView) bindings[12]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[8]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[14]
            , (androidx.cardview.widget.CardView) bindings[9]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[16]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[15]
            , (com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel) bindings[13]
            , (android.widget.FrameLayout) bindings[1]
            , (android.widget.FrameLayout) bindings[2]
            , (androidx.cardview.widget.CardView) bindings[6]
            , (android.widget.ImageView) bindings[10]
            , (com.behraz.fastermixer.batch.ui.customs.fastermixer.progressview.FasterMixerProgressView) bindings[7]
            , (com.behraz.fastermixer.batch.databinding.ItemCustomerBinding) bindings[3]
            , (com.behraz.fastermixer.batch.databinding.LayoutMixerBinding) bindings[4]
            , (android.widget.FrameLayout) bindings[5]
            , (android.widget.TextView) bindings[11]
            );
        this.frameCustomer.setTag(null);
        this.frameMixer.setTag(null);
        this.mboundView0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
        }
        layoutCustomer.invalidateAll();
        layoutMixer.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (layoutCustomer.hasPendingBindings()) {
            return true;
        }
        if (layoutMixer.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewModel == variableId) {
            setViewModel((com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        layoutCustomer.setLifecycleOwner(lifecycleOwner);
        layoutMixer.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeLayoutCustomer((com.behraz.fastermixer.batch.databinding.ItemCustomerBinding) object, fieldId);
            case 1 :
                return onChangeLayoutMixer((com.behraz.fastermixer.batch.databinding.LayoutMixerBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeLayoutCustomer(com.behraz.fastermixer.batch.databinding.ItemCustomerBinding LayoutCustomer, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeLayoutMixer(com.behraz.fastermixer.batch.databinding.LayoutMixerBinding LayoutMixer, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
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
        // batch finished
        executeBindingsOn(layoutCustomer);
        executeBindingsOn(layoutMixer);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): layoutCustomer
        flag 1 (0x2L): layoutMixer
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}