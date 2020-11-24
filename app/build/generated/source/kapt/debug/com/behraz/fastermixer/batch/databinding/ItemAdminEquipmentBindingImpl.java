package com.behraz.fastermixer.batch.databinding;
import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemAdminEquipmentBindingImpl extends ItemAdminEquipmentBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.pelakView, 2);
        sViewsWithIds.put(R.id.ivEquipment, 3);
        sViewsWithIds.put(R.id.btn_show_mixer_on_map, 4);
        sViewsWithIds.put(R.id.linearLayout4, 5);
        sViewsWithIds.put(R.id.tvLastDataTime, 6);
        sViewsWithIds.put(R.id.imageView8, 7);
        sViewsWithIds.put(R.id.textView53, 8);
        sViewsWithIds.put(R.id.tvState, 9);
    }
    // views
    @NonNull
    private final androidx.cardview.widget.CardView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemAdminEquipmentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private ItemAdminEquipmentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton) bindings[4]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.LinearLayout) bindings[5]
            , (com.behraz.fastermixer.batch.ui.customs.fastermixer.CarIdView) bindings[2]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[9]
            );
        this.mboundView0 = (androidx.cardview.widget.CardView) bindings[0];
        this.mboundView0.setTag(null);
        this.textView54.setTag(null);
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
        if (BR.equipment == variableId) {
            setEquipment((com.behraz.fastermixer.batch.models.AdminEquipment) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setEquipment(@Nullable com.behraz.fastermixer.batch.models.AdminEquipment Equipment) {
        this.mEquipment = Equipment;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.equipment);
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
        com.behraz.fastermixer.batch.models.AdminEquipment equipment = mEquipment;
        java.lang.String equipmentName = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (equipment != null) {
                    // read equipment.name
                    equipmentName = equipment.getName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView54, equipmentName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): equipment
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}