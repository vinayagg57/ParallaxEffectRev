package com.example.nitikaaggarwal.parallaxrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nitikaaggarwal.parallaxrecyclerview.utils.AppConstants;

import java.util.List;

/**
 * Created by nitikaaggarwal on 26/06/16.
 */
public class MultipleRowAdapter extends RecyclerView.Adapter<MultipleRowViewHolder> {

    private LayoutInflater inflater = null;
    private List<MultipleRowModel> multipleRowModelList;

    public MultipleRowAdapter(Context context, List<MultipleRowModel> multipleRowModelList){
        this.multipleRowModelList = multipleRowModelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MultipleRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == AppConstants.FIRST_ROW)
            view = inflater.inflate(R.layout.view_row_first, parent, false);
        else if (viewType == AppConstants.OTHER_ROW)
            view = inflater.inflate(R.layout.view_row_second, parent, false);

        return new MultipleRowViewHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(MultipleRowViewHolder holder, int position) {
           holder.multipleContent.setText(multipleRowModelList.get(position).modelContent);
    }

    @Override
    public int getItemCount() {
        return (multipleRowModelList != null && multipleRowModelList.size() > 0 ) ? multipleRowModelList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        MultipleRowModel multipleRowModel = multipleRowModelList.get(position);

        if (multipleRowModel != null)
            return multipleRowModel.type;

        return super.getItemViewType(position);
    }
}


