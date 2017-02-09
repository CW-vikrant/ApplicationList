package com.applicationlist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicationlist.pojo.Contact;
import com.applicationlist.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vikrant Chauhan on 10/14/2016.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<Contact> contactList;

    public ContactsAdapter(List<Contact> contactList){
        this.contactList = contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.number.setText(Integer.toString(contact.getNumber()));
        holder.note.setText(contact.getNote());
    }

    @Override
    public int getItemCount() {
        return contactList!=null ? contactList.size() : 0;
    }

    @Override
    public void onItemDismiss(int position) {
        contactList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(contactList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(contactList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name,number,note;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            note = (TextView) itemView.findViewById(R.id.note);
        }
    }
}
