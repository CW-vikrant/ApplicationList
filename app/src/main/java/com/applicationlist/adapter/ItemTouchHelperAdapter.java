package com.applicationlist.adapter;

/**
 * Created by Vikrant Chauhan on 10/14/2016.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
