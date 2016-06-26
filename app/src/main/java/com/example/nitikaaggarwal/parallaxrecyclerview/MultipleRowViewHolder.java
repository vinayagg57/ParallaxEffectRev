package com.example.nitikaaggarwal.parallaxrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nitikaaggarwal.parallaxrecyclerview.utils.AppConstants;

/**
 * Created by nitikaaggarwal on 26/06/16.
 */
public class MultipleRowViewHolder extends RecyclerView.ViewHolder {

    public TextView multipleContent;

    private int type;

    public MultipleRowViewHolder(View itemView, int type) {
        super(itemView);

        if (type == AppConstants.FIRST_ROW){
             multipleContent = (TextView)itemView.findViewById(R.id.row_first_name_tv);
        }else if(type == AppConstants.OTHER_ROW) {
            multipleContent = (TextView)itemView.findViewById(R.id.row_other_name_tv);
        }
    }
}

