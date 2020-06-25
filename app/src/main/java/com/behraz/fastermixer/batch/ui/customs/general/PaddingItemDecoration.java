package com.behraz.fastermixer.batch.ui.customs.general;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ASN on 3/31/2018.
 */

public class PaddingItemDecoration extends RecyclerView.ItemDecoration {

    private final int size;
    private final int flag;

    public PaddingItemDecoration(int size, int flag) {
        this.size = size;
        this.flag = flag;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (flag == 0) {
            outRect.bottom += size;
        } if (flag == 1){
            outRect.left += size;
            outRect.right += size;
        } else if (flag == 2){
            outRect.right += size;
            outRect.left += size;
            outRect.bottom += size;
        } else if (flag == 3){
            outRect.left += size;
        }
    }
}
