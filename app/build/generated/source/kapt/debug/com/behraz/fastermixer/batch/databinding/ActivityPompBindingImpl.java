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
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.layoutDemo, 1);
        sViewsWithIds.put(R.id.btnMessage, 2);
        sViewsWithIds.put(R.id.imageView2, 3);
        sViewsWithIds.put(R.id.tvMessageCount, 4);
        sViewsWithIds.put(R.id.mapContainer, 5);
        sViewsWithIds.put(R.id.frame_user_buttons, 6);
        sViewsWithIds.put(R.id.btn_map, 7);
        sViewsWithIds.put(R.id.btn_projects, 8);
        sViewsWithIds.put(R.id.btn_mixers, 9);
        sViewsWithIds.put(R.id.btn_messages, 10);
        sViewsWithIds.put(R.id.btn_voice_message, 11);
        sViewsWithIds.put(R.id.btnLogout, 12);
        sViewsWithIds.put(R.id.linearLayout4, 13);
        sViewsWithIds.put(R.id.btnWeather, 14);
        sViewsWithIds.put(R.id.btnMyLocation, 15);
        sViewsWithIds.put(R.id.frameGPSState, 16);
        sViewsWithIds.put(R.id.frame_internet, 17);
        sViewsWithIds.put(R.id.ivInternet, 18);
        sViewsWithIds.put(R.id.frame_gps, 19);
        sViewsWithIds.put(R.id.ivGPS, 20);
        sViewsWithIds.put(R.id.frame_voip, 21);
        sViewsWithIds.put(R.id.ivVoip, 22);
    }
    // views
    @NonNull
    private final androidx.coordinatorlayout.widget.CoordinatorLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityPompBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 23, sIncludes, sViewsWithIds));
    }
    private ActivityPompBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[12]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[7]
            , (androidx.cardview.widget.CardView) bindings[2]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[10]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[9]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[15]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[8]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[11]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[14]
            , (android.widget.LinearLayout) bindings[16]
            , (android.widget.LinearLayout) bindings[19]
            , (android.widget.LinearLayout) bindings[17]
            , (androidx.cardview.widget.CardView) bindings[6]
            , (android.widget.LinearLayout) bindings[21]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[20]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.ImageView) bindings[22]
            , (android.view.View) bindings[1]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.FrameLayout) bindings[5]
            , (android.widget.TextView) bindings[4]
            );
        this.mboundView0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[0];
        this.mboundView0.setTag(null);
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}