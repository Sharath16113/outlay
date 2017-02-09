package com.outlay.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.outlay.R;
import com.outlay.domain.model.Category;
import com.outlay.utils.IconUtils;
import com.outlay.utils.ResourceUtils;
import com.outlay.view.adapter.listener.OnCategoryClickListener;
import com.outlay.view.helper.itemtouch.ItemTouchHelperAdapter;
import com.outlay.view.helper.itemtouch.ItemTouchHelperViewHolder;
import com.outlay.view.helper.itemtouch.OnDragListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class CategoriesDraggableGridAdapter extends RecyclerView.Adapter<CategoriesDraggableGridAdapter.CategoryDraggableViewHolder> implements ItemTouchHelperAdapter {
    private List<Category> items = new ArrayList<>();
    private OnDragListener dragListener;
    private OnCategoryClickListener clickListener;

    public void setItems(List<Category> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
    }

    @Override
    public void onInteractionComplete(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (dragListener != null) {
            dragListener.onEndDrag(viewHolder);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Category elementFrom = items.get(fromPosition);
        items.remove(fromPosition);
        items.add(toPosition, elementFrom);
        notifyItemMoved(fromPosition, toPosition);

        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOrder(i);
        }
        return true;
    }

    @Override
    public CategoryDraggableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_category, parent, false);
        final CategoryDraggableViewHolder viewHolder = new CategoryDraggableViewHolder(v);
        return viewHolder;
    }

    public List<Category> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(CategoryDraggableViewHolder holder, int position) {
        Context context = holder.categoryContainer.getContext();
        Category currentOne = items.get(position);
        int iconCodeRes = ResourceUtils.getIntegerResource(context, currentOne.getIcon());
        Drawable categoryIcon = IconUtils.getCategoryIcon(context, iconCodeRes, currentOne.getColor(), R.dimen.category_icon);
        holder.categoryIcon.setImageDrawable(categoryIcon);
        holder.categoryTitle.setText(currentOne.getTitle());
        holder.categoryIcon.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCategoryClicked(currentOne);
            }
        });
    }

    public void setDragListener(OnDragListener dragListener) {
        this.dragListener = dragListener;
    }

    public class CategoryDraggableViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @Bind(R.id.categoryContainer)
        View categoryContainer;

        @Bind(R.id.categoryIcon)
        ImageView categoryIcon;

        @Bind(R.id.categoryTitle)
        TextView categoryTitle;

        public CategoryDraggableViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(categoryIcon.getContext(), R.color.dark));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
