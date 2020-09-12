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
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(30);
        sIncludes.setIncludes(1, 
            new String[] {"item_message_vertical"},
            new int[] {3},
            new int[] {com.behraz.fastermixer.batch.R.layout.item_message_vertical});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.layoutDemo, 2);
        sViewsWithIds.put(R.id.frame_top, 4);
        sViewsWithIds.put(R.id.jobProgressView, 5);
        sViewsWithIds.put(R.id.btnMessage, 6);
        sViewsWithIds.put(R.id.imageView2, 7);
        sViewsWithIds.put(R.id.tvMessageCount, 8);
        sViewsWithIds.put(R.id.btnShowAllMixersToggle, 9);
        sViewsWithIds.put(R.id.mapContainer, 10);
        sViewsWithIds.put(R.id.frame_user_buttons, 11);
        sViewsWithIds.put(R.id.btn_map, 12);
        sViewsWithIds.put(R.id.btn_projects, 13);
        sViewsWithIds.put(R.id.btn_mixers, 14);
        sViewsWithIds.put(R.id.btn_messages, 15);
        sViewsWithIds.put(R.id.btn_voice_message, 16);
        sViewsWithIds.put(R.id.btnLogout, 17);
        sViewsWithIds.put(R.id.frameBottomButtons, 18);
        sViewsWithIds.put(R.id.btnWeather, 19);
        sViewsWithIds.put(R.id.btnMyLocation, 20);
        sViewsWithIds.put(R.id.btnRoute, 21);
        sViewsWithIds.put(R.id.frameGPSState, 22);
        sViewsWithIds.put(R.id.frame_internet, 23);
        sViewsWithIds.put(R.id.ivInternet, 24);
        sViewsWithIds.put(R.id.frame_gps, 25);
        sViewsWithIds.put(R.id.ivGPS, 26);
        sViewsWithIds.put(R.id.frame_voip, 27);
        sViewsWithIds.put(R.id.ivVoip, 28);
        sViewsWithIds.put(R.id.gpBtns, 29);
    }
    // views
    @NonNull
    private final androidx.coordinatorlayout.widget.CoordinatorLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityPompBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 30, sIncludes, sViewsWithIds));
    }
    private ActivityPompBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[17]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[12]
            , (androidx.cardview.widget.CardView) bindings[6]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[15]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[14]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[20]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[13]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[21]
            , (android.widget.Button) bindings[9]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[16]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[19]
            , (android.widget.LinearLayout) bindings[18]
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.LinearLayout) bindings[25]
            , (android.widget.LinearLayout) bindings[23]
            , (android.widget.FrameLayout) bindings[4]
            , (androidx.cardview.widget.CardView) bindings[11]
            , (android.widget.LinearLayout) bindings[27]
            , (androidx.constraintlayout.widget.Group) bindings[29]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[26]
            , (android.widget.ImageView) bindings[24]
            , (android.widget.ImageView) bindings[28]
            , (com.behraz.fastermixer.batch.ui.customs.fastermixer.progressview.FasterMixerProgressView) bindings[5]
            , (android.view.View) bindings[2]
            , (com.behraz.fastermixer.batch.databinding.ItemMessageVerticalBinding) bindings[3]
            , (android.widget.FrameLayout) bindings[10]
            , (android.widget.TextView) bindings[8]
            );
        this.mboundView0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        layoutNewMessage.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (layoutNewMessage.hasPendingBindings()) {
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
        layoutNewMessage.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeLayoutNewMessage((com.behraz.fastermixer.batch.databinding.ItemMessageVerticalBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeLayoutNewMessage(com.behraz.fastermixer.batch.databinding.ItemMessageVerticalBinding LayoutNewMessage, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
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
        executeBindingsOn(layoutNewMessage);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): layoutNewMessage
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}