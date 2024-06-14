package com.khanhnq.libraryapp.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.khanhnq.libraryapp.R;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private final int resourceSelectShift;
    private final int resourceCategoryShift;

    public CategoryAdapter(@NonNull Context context, int resourceSelectShift, int resourceCategoryShift, @NonNull List<Category> objects) {
        super(context, resourceSelectShift, objects);
        this.resourceSelectShift = resourceSelectShift;
        this.resourceCategoryShift = resourceCategoryShift;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resourceSelectShift, parent, false);
        }
        TextView tvSelected = convertView.findViewById(R.id.tv_selected);
        Category category = this.getItem(position);
        if (category != null) {
            tvSelected.setText(category.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resourceCategoryShift, parent, false);
        }
        TextView tvCategory = convertView.findViewById(R.id.tv_category);
        Category category = this.getItem(position);
        if (category != null) {
            tvCategory.setText(category.getName());
        }
        return convertView;
    }
}
