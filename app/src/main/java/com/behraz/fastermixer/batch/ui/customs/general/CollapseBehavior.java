package com.behraz.fastermixer.batch.ui.customs.general;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class CollapseBehavior<V extends ViewGroup> extends CoordinatorLayout.Behavior<V>{


    public CollapseBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
        if (isBottomSheet(dependency)) {
            BottomSheetBehavior behavior = ((BottomSheetBehavior) ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior());

            int peekHeight = behavior.getPeekHeight();
            // The default peek height is -1, which
            // gets resolved to a 16:9 ratio with the parent
            final int actualPeek = peekHeight >= 0 ? peekHeight : (int) (((parent.getHeight() * 1.0) / (16.0)) * 9.0);
            if (dependency.getTop() >= actualPeek) {
                // Only perform translations when the
                // view is between "hidden" and "collapsed" states
                final int dy = dependency.getTop() - parent.getHeight();
                ViewCompat.setTranslationY(child, dy/2);
                return true;
            }
        }

        return false;
    }

    private static boolean isBottomSheet(@NonNull View view) {
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof CoordinatorLayout.LayoutParams) {
            return ((CoordinatorLayout.LayoutParams) lp)
                    .getBehavior() instanceof BottomSheetBehavior;
        }
        return false;
    }


}