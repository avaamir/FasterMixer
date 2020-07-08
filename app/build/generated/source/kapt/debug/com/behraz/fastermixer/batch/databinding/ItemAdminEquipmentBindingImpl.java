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
        sViewsWithIds.put(R.id.pelakView, 3);
        sViewsWithIds.put(R.id.ivEquipment, 4);
        sViewsWithIds.put(R.id.frame_state, 5);
        sViewsWithIds.put(R.id.ivState, 6);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemAdminEquipmentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds));
    }
    private ItemAdminEquipmentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[6]
            , (com.behraz.fastermixer.batch.ui.customs.fastermixer.CarIdView) bindings[3]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[2]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.textView53.setTag(null);
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
        java.lang.String equipmentStateDesc = null;
        com.behraz.fastermixer.batch.models.AdminEquipment equipment = mEquipment;
        java.lang.String equipmentName = null;
        com.behraz.fastermixer.batch.models.enums.EquipmentState equipmentState = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (equipment != null) {
                    // read equipment.name
                    equipmentName = equipment.getName();
                    // read equipment.state
                    equipmentState = equipment.getState();
                }


                if (equipmentState != null) {
                    // read equipment.state.desc
                    equipmentStateDesc = equipmentState.getDesc();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView53, equipmentStateDesc);
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