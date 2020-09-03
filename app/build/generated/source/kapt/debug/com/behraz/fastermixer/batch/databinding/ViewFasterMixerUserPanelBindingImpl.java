package com.behraz.fastermixer.batch.databinding;
import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ViewFasterMixerUserPanelBindingImpl extends ViewFasterMixerUserPanelBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.linearLayout2, 2);
        sViewsWithIds.put(R.id.scrollUserPanel, 3);
        sViewsWithIds.put(R.id.iv_profile, 4);
        sViewsWithIds.put(R.id.linearLayout, 5);
        sViewsWithIds.put(R.id.tv_username, 6);
        sViewsWithIds.put(R.id.textView, 7);
        sViewsWithIds.put(R.id.tv_personal_code, 8);
        sViewsWithIds.put(R.id.textView3, 9);
        sViewsWithIds.put(R.id.btnCall, 10);
        sViewsWithIds.put(R.id.btnLogout, 11);
        sViewsWithIds.put(R.id.btnRecord, 12);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ViewFasterMixerUserPanelBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private ViewFasterMixerUserPanelBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[10]
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[11]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[12]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[4]
            , (android.widget.LinearLayout) bindings[5]
            , (android.view.View) bindings[2]
            , (android.widget.HorizontalScrollView) bindings[3]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[6]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[1];
        this.mboundView1.setTag(null);
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
        if (BR.user == variableId) {
            setUser((com.behraz.fastermixer.batch.models.User) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setUser(@Nullable com.behraz.fastermixer.batch.models.User User) {
        this.mUser = User;
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
        flag 0 (0x1L): user
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}