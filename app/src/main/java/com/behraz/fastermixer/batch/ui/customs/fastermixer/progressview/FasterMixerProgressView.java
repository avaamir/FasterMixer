package com.behraz.fastermixer.batch.ui.customs.fastermixer.progressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.models.Progress;
import com.behraz.fastermixer.batch.models.ProgressState;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FasterMixerProgressView extends LinearLayout implements FasterMixerProgressAdapter.Interaction {
    private OnStateChangedListener onStateChangedListener;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FasterMixerProgressAdapter adapter = new FasterMixerProgressAdapter(this);

    public FasterMixerProgressView(Context context) {
        super(context);
    }

    public FasterMixerProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FasterMixerProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FasterMixerProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        View root = inflate(getContext(), R.layout.view_faster_mixer_progress, this);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.FasterMixerProgressView);

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = root.findViewById(R.id.recycler_view);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));


        attributes.recycle();
    }


    public void setProgressItems(List<Progress> progresses) {
        adapter.submitList(progresses);
    }

    public void showProgressBar(boolean shouldShow) {
        if (shouldShow) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    public void addOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    public void nextState() {
        Progress nextState;
        List<Progress> currentList = adapter.getCurrentList();
        if (adapter.getCurrentStatePosition() == currentList.size() - 1) {
            Progress currentState = adapter.getCurrentState();
            currentState.setState(ProgressState.Done);
            nextState = currentState;
        } else {
            nextState = currentList.get(adapter.getCurrentStatePosition() + 1);
            Progress currentState = adapter.getCurrentState();
            currentState.setState(ProgressState.Done);
            nextState.setState(ProgressState.InProgress);
        }
        if (onStateChangedListener != null) {
            onStateChangedListener.onStateChanged(nextState);
        }
        adapter.notifyDataSetChanged();
    }

    public void prevState() {
        if (adapter.getCurrentStatePosition() == 0)
            return;

        List<Progress> currentList = adapter.getCurrentList();
        Progress prevState = currentList.get(adapter.getCurrentStatePosition() - 1);
        Progress currentState = adapter.getCurrentState();
        currentState.setState(ProgressState.NotStarted);
        prevState.setState(ProgressState.InProgress);
        adapter.notifyDataSetChanged();
        if (onStateChangedListener != null) {
            onStateChangedListener.onStateChanged(prevState);
        }
    }


    public Progress getPrevState() {
        if (adapter.getCurrentStatePosition() == 0)
            return null;
        return adapter.getCurrentList().get(adapter.getCurrentStatePosition() - 1);
    }

    public Progress getNextState() {
        if (adapter.getCurrentStatePosition() == adapter.getCurrentList().size() - 1)
            return null;
        return adapter.getCurrentList().get(adapter.getCurrentStatePosition() + 1);
    }

    public Progress getCurrentState() {
        return adapter.getCurrentState();
    }

    @Override
    public void onDoManualClicked(@NotNull Progress item) {
        nextState();
    }

    public void resetProgress() {
        List<Progress> currentList = adapter.getCurrentList();
        currentList.get(0).setState(ProgressState.InProgress);
        for(int i = 1 ; i < currentList.size() ; i++) {
            currentList.get(i).setState(ProgressState.NotStarted);
        }
        adapter.notifyDataSetChanged();
        if (onStateChangedListener != null) {
            onStateChangedListener.onStateChanged(currentList.get(0));
        }
    }

    public interface OnStateChangedListener {
        void onStateChanged(Progress progress);
    }

}
