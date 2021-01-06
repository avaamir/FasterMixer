package com.behraz.fastermixer.batch.databinding;
import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemCustomerBindingImpl extends ItemCustomerBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textView23, 8);
        sViewsWithIds.put(R.id.textView25, 9);
        sViewsWithIds.put(R.id.textView28, 10);
        sViewsWithIds.put(R.id.textView30, 11);
        sViewsWithIds.put(R.id.textView32, 12);
        sViewsWithIds.put(R.id.textView34, 13);
        sViewsWithIds.put(R.id.textView36, 14);
        sViewsWithIds.put(R.id.textView38, 15);
        sViewsWithIds.put(R.id.btnCustomerList, 16);
    }
    // views
    @NonNull
    private final androidx.cardview.widget.CardView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemCustomerBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private ItemCustomerBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.cardview.widget.CardView) bindings[16]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[7]
            );
        this.mboundView0 = (androidx.cardview.widget.CardView) bindings[0];
        this.mboundView0.setTag(null);
        this.textView24.setTag(null);
        this.textView26.setTag(null);
        this.textView29.setTag(null);
        this.textView31.setTag(null);
        this.textView33.setTag(null);
        this.textView37.setTag(null);
        this.textView39.setTag(null);
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
        if (BR.customer == variableId) {
            setCustomer((com.behraz.fastermixer.batch.models.Customer) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setCustomer(@Nullable com.behraz.fastermixer.batch.models.Customer Customer) {
        this.mCustomer = Customer;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.customer);
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
        java.lang.String customerSlumpJavaLangString = null;
        java.lang.String customerAddress = null;
        java.lang.String customerAmount = null;
        java.lang.String customerStartTime = null;
        int customerSlump = 0;
        java.lang.String customerDensity = null;
        java.lang.String customerName = null;
        com.behraz.fastermixer.batch.models.Customer customer = mCustomer;
        java.lang.String customerMixerCount = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (customer != null) {
                    // read customer.address
                    customerAddress = customer.getAddress();
                    // read customer.amount
                    customerAmount = customer.getAmount();
                    // read customer.startTime
                    customerStartTime = customer.getStartTime();
                    // read customer.slump
                    customerSlump = customer.getSlump();
                    // read customer.density
                    customerDensity = customer.getDensity();
                    // read customer.name
                    customerName = customer.getName();
                    // read customer.mixerCount
                    customerMixerCount = customer.getMixerCount();
                }


                // read (customer.slump) + ("")
                customerSlumpJavaLangString = (customerSlump) + ("");
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView24, customerName);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView26, customerStartTime);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView29, customerAddress);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView31, customerSlumpJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView33, customerDensity);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView37, customerAmount);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView39, customerMixerCount);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): customer
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}