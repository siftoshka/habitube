package az.amorphist.poster.utils.animation;

import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;

public class VegaXLayoutManager extends RecyclerView.LayoutManager {

    private int scroll = 0;
    private SparseArray<Rect> locationRects = new SparseArray<>();
    private SparseBooleanArray attachedItems = new SparseBooleanArray();
    private ArrayMap<Integer, Integer> viewTypeHeightMap = new ArrayMap<>();

    private boolean needSnap = false;
    private int lastDy = 0;
    private int maxScroll = -1;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Recycler recycler;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        this.adapter = newAdapter;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.recycler = recycler;
        if (state.isPreLayout()) {
            return;
        }

        buildLocationRects();

        detachAndScrapAttachedViews(recycler);
        layoutItemsOnCreate(recycler);
    }

    private void buildLocationRects() {
        locationRects.clear();
        attachedItems.clear();

        int tempPosition = getPaddingTop();
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            int viewType = adapter.getItemViewType(i);
            int itemHeight;
            if (viewTypeHeightMap.containsKey(viewType)) {
                itemHeight = viewTypeHeightMap.get(viewType);
            } else {
                View itemView = recycler.getViewForPosition(i);
                addView(itemView);
                measureChildWithMargins(itemView, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                itemHeight = getDecoratedMeasuredHeight(itemView);
                viewTypeHeightMap.put(viewType, itemHeight);
            }

            Rect rect = new Rect();
            rect.left = getPaddingLeft();
            rect.top = tempPosition;
            rect.right = getWidth() - getPaddingRight();
            rect.bottom = rect.top + itemHeight;
            locationRects.put(i, rect);
            attachedItems.put(i, false);
            tempPosition = tempPosition + itemHeight;
        }

        if (itemCount == 0) {
            maxScroll = 0;
        } else {
            computeMaxScroll();
        }
    }

    private void computeMaxScroll() {
        maxScroll = locationRects.get(locationRects.size() - 1).bottom - getHeight();
        if (maxScroll < 0) {
            maxScroll = 0;
            return;
        }

        int itemCount = getItemCount();
        int screenFilledHeight = 0;
        for (int i = itemCount - 1; i >= 0; i--) {
            Rect rect = locationRects.get(i);
            screenFilledHeight = screenFilledHeight + (rect.bottom - rect.top);
            if (screenFilledHeight > getHeight()) {
                int extraSnapHeight = getHeight() - (screenFilledHeight - (rect.bottom - rect.top));
                maxScroll = maxScroll + extraSnapHeight;
                break;
            }
        }
    }

    private void layoutItemsOnCreate(RecyclerView.Recycler recycler) {
        int itemCount = getItemCount();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        for (int i = 0; i < itemCount; i++) {
            Rect thisRect = locationRects.get(i);
            if (Rect.intersects(displayRect, thisRect)) {
                View childView = recycler.getViewForPosition(i);
                addView(childView);
                measureChildWithMargins(childView, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                layoutItem(childView, locationRects.get(i));
                attachedItems.put(i, true);
                childView.setPivotY(0);
                childView.setPivotX(childView.getMeasuredWidth() / 2);
                if (thisRect.top - scroll > getHeight()) {
                    break;
                }
            }
        }
    }

    private void layoutItemsOnScroll() {
        int childCount = getChildCount();
        int itemCount = getItemCount();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        int firstVisiblePosition = -1;
        int lastVisiblePosition = -1;
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            int position = getPosition(child);
            if (!Rect.intersects(displayRect, locationRects.get(position))) {
                removeAndRecycleView(child, recycler);
                attachedItems.put(position, false);
            } else {
                if (lastVisiblePosition < 0) {
                    lastVisiblePosition = position;
                }

                if (firstVisiblePosition < 0) {
                    firstVisiblePosition = position;
                } else {
                    firstVisiblePosition = Math.min(firstVisiblePosition, position);
                }

                layoutItem(child, locationRects.get(position));
            }
        }

        if (firstVisiblePosition > 0) {
            for (int i = firstVisiblePosition - 1; i >= 0; i--) {
                if (Rect.intersects(displayRect, locationRects.get(i)) &&
                        !attachedItems.get(i)) {
                    reuseItemOnSroll(i, true);
                } else {
                    break;
                }
            }
        }
        for (int i = lastVisiblePosition + 1; i < itemCount; i++) {
            if (Rect.intersects(displayRect, locationRects.get(i)) &&
                    !attachedItems.get(i)) {
                reuseItemOnSroll(i, false);
            } else {
                break;
            }
        }
    }

    private void reuseItemOnSroll(int position, boolean addViewFromTop) {
        View scrap = recycler.getViewForPosition(position);
        measureChildWithMargins(scrap, 0, 0);
        scrap.setPivotY(0);
        scrap.setPivotX(scrap.getMeasuredWidth() / 2);

        if (addViewFromTop) {
            addView(scrap, 0);
        } else {
            addView(scrap);
        }
        layoutItem(scrap, locationRects.get(position));
        attachedItems.put(position, true);
    }

    private void layoutItem(View child, Rect rect) {
        int topDistance = scroll - rect.top;
        int layoutTop, layoutBottom;
        int itemHeight = rect.bottom - rect.top;
        if (topDistance < itemHeight && topDistance > 0) {
            float rate1 = (float) topDistance / itemHeight;
            float rate2 = 1 - rate1 * rate1 / 3;
            float rate3 = 1 - rate1 * rate1;
            child.setScaleX(rate2);
            child.setScaleY(rate2);
            child.setAlpha(rate3);
            layoutTop = 0;
            layoutBottom = itemHeight;
        } else {
            child.setScaleX(1);
            child.setScaleY(1);
            child.setAlpha(1);

            layoutTop = rect.top - scroll;
            layoutBottom = rect.bottom - scroll;
        }
        layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || dy == 0) {
            return 0;
        }
        int travel = dy;
        if (dy + scroll < 0) {
            travel = -scroll;
        } else if (dy + scroll > maxScroll) {
            travel = maxScroll - scroll;
        }
        scroll += travel;
        lastDy = dy;
        if (!state.isPreLayout() && getChildCount() > 0) {
            layoutItemsOnScroll();
        }

        return travel;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        new StartSnapHelper().attachToRecyclerView(view);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            needSnap = true;
        }
        super.onScrollStateChanged(state);
    }

    public int getSnapHeight() {
        if (!needSnap) {
            return 0;
        }
        needSnap = false;

        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            Rect itemRect = locationRects.get(i);
            if (displayRect.intersect(itemRect)) {

                if (lastDy > 0) {
                    if (i < itemCount - 1) {
                        Rect nextRect = locationRects.get(i + 1);
                        return nextRect.top - displayRect.top;
                    }
                }
                return itemRect.top - displayRect.top;
            }
        }
        return 0;
    }

    public View findSnapView() {
        if (getChildCount() > 0) {
            return getChildAt(0);
        }
        return null;
    }
}
