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
        sViewsWithIds.put(R.id.btnMessage, 1);
        sViewsWithIds.put(R.id.imageView2, 2);
        sViewsWithIds.put(R.id.tvMessageCount, 3);
        sViewsWithIds.put(R.id.mapContainer, 4);
        sViewsWithIds.put(R.id.frame_user_buttons, 5);
        sViewsWithIds.put(R.id.btn_map, 6);
        sViewsWithIds.put(R.id.btn_projects, 7);
        sViewsWithIds.put(R.id.btn_mixers, 8);
        sViewsWithIds.put(R.id.btn_messages, 9);
        sViewsWithIds.put(R.id.btn_voice_message, 10);
        sViewsWithIds.put(R.id.btnLogout, 11);
        sViewsWithIds.put(R.id.linearLayout4, 12);
        sViewsWithIds.put(R.id.btnWeather, 13);
        sViewsWithIds.put(R.id.btnMyLocation, 14);
        sViewsWithIds.put(R.id.linearLayout2, 15);
        sViewsWithIds.put(R.id.frame_internet, 16);
        sViewsWithIds.put(R.id.ivInternet, 17);
        sViewsWithIds.put(R.id.frame_gps, 18);
        sViewsWithIds.put(R.id.ivGPS, 19);
        sViewsWithIds.put(R.id.frame_voip, 20);
        sViewsWithIds.put(R.id.ivVoip, 21);
    }
    // views
    @NonNull
    private final androidx.coordinatorlayout.widget.CoordinatorLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityPompBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 22, sIncludes, sViewsWithIds));
    }
    private ActivityPompBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[11]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[6]
            , (androidx.cardview.widget.CardView) bindings[1]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[9]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[8]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[14]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[7]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[10]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[13]
            , (android.widget.LinearLayout) bindings[18]
            , (android.widget.LinearLayout) bindings[16]
            , (androidx.cardview.widget.CardView) bindings[5]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.ImageView) bindings[19]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.ImageView) bindings[21]
            , (android.widget.LinearLayout) bindings[15]
            , (android.widget.LinearLayout) bindings[12]
            , (android.widget.FrameLayout) bindings[4]
            , (android.widget.TextView) bindings[3]
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