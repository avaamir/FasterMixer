package com.behraz.fastermixer.batch.databinding;
import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemPlanBindingImpl extends ItemPlanBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textView41, 9);
        sViewsWithIds.put(R.id.textView43, 10);
        sViewsWithIds.put(R.id.textView45, 11);
        sViewsWithIds.put(R.id.textView47, 12);
        sViewsWithIds.put(R.id.textView49, 13);
    }
    // views
    @NonNull
    private final androidx.cardview.widget.CardView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemPlanBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private ItemPlanBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ProgressBar) bindings[6]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[7]
            );
        this.mboundView0 = (androidx.cardview.widget.CardView) bindings[0];
        this.mboundView0.setTag(null);
        this.progressBar2.setTag(null);
        this.textView35.setTag(null);
        this.textView42.setTag(null);
        this.textView44.setTag(null);
        this.textView46.setTag(null);
        this.textView48.setTag(null);
        this.textView50.setTag(null);
        this.textView51.setTag(null);
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
        if (BR.plan == variableId) {
            setPlan((com.behraz.fastermixer.batch.models.Plan) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPlan(@Nullable com.behraz.fastermixer.batch.models.Plan Plan) {
        this.mPlan = Plan;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.plan);
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
        java.lang.String planPlannedAmountJavaLangString = null;
        com.behraz.fastermixer.batch.models.Plan plan = mPlan;
        java.lang.String planAddress = null;
        java.lang.String planWaitingAmountJavaLangString = null;
        float planPlannedAmount = 0f;
        boolean planProgressInt0 = false;
        java.lang.String planProgressJavaLangString = null;
        float planWaitingAmount = 0f;
        java.lang.String planRequestStateTitle = null;
        float planSentAmount = 0f;
        java.lang.String planSentAmountJavaLangString = null;
        int planProgress = 0;
        int planProgressInt0PlanProgressInt1 = 0;
        java.lang.String planCustomerName = null;
        com.behraz.fastermixer.batch.models.enums.RequestState planRequestState = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (plan != null) {
                    // read plan.address
                    planAddress = plan.getAddress();
                    // read plan.plannedAmount
                    planPlannedAmount = plan.getPlannedAmount();
                    // read plan.waitingAmount
                    planWaitingAmount = plan.getWaitingAmount();
                    // read plan.sentAmount
                    planSentAmount = plan.getSentAmount();
                    // read plan.progress
                    planProgress = plan.getProgress();
                    // read plan.customerName
                    planCustomerName = plan.getCustomerName();
                    // read plan.requestState
                    planRequestState = plan.getRequestState();
                }


                // read (plan.plannedAmount) + (" متر")
                planPlannedAmountJavaLangString = (planPlannedAmount) + (" متر");
                // read (plan.waitingAmount) + (" متر")
                planWaitingAmountJavaLangString = (planWaitingAmount) + (" متر");
                // read (plan.sentAmount) + (" متر")
                planSentAmountJavaLangString = (planSentAmount) + (" متر");
                // read plan.progress != 0
                planProgressInt0 = (planProgress) != (0);
                // read (plan.progress) + ("%")
                planProgressJavaLangString = (planProgress) + ("%");
            if((dirtyFlags & 0x3L) != 0) {
                if(planProgressInt0) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }
                if (planRequestState != null) {
                    // read plan.requestState.title
                    planRequestStateTitle = planRequestState.getTitle();
                }
        }
        // batch finished

        if ((dirtyFlags & 0x3L) != 0) {

                // read plan.progress != 0 ? plan.progress : 1
                planProgressInt0PlanProgressInt1 = ((planProgressInt0) ? (planProgress) : (1));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.progressBar2.setProgress(planProgressInt0PlanProgressInt1);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView35, planRequestStateTitle);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView42, planAddress);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView44, planCustomerName);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView46, planPlannedAmountJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView48, planSentAmountJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView50, planWaitingAmountJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView51, planProgressJavaLangString);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): plan
        flag 1 (0x2L): null
        flag 2 (0x3L): plan.progress != 0 ? plan.progress : 1
        flag 3 (0x4L): plan.progress != 0 ? plan.progress : 1
    flag mapping end*/
    //end
}