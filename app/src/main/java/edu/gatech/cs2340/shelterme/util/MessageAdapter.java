package edu.gatech.cs2340.shelterme.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Message;

/**
 * Created by austincondict on 4/10/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final Message[] dataSet;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is a Message in this case:
//         String message;
//         String timeSent;
//         String senderEmail;
//         boolean isAddressed;
        final TextView messageTextView;
        ViewHolder(View v) {
            super(v);
            messageTextView = v.findViewById(R.id._row);
        }

        // Make message items in the RecycleView clickable for details & interactions
        @Override
        public void onClick(View v) {

        }

//        private final View.OnClickListener mOnClickListener = new View.OnClickListener();
//
//        @Override
//        public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.myview, parent, false);
//            view.setOnClickListener(mOnClickListener);
//            return new MyViewHolder(view);
//        }
    }

    // Provide a suitable constructor (depends on the kind of data set)
    public MessageAdapter(Message[] dataSet) {
        this.dataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.recycler_item_layout, parent, false);
        return new ViewHolder(row);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element
        if (dataSet[position] != null) {
            holder.messageTextView.setText(String.format("%s\n\t%s\n\t\t%s",
                    dataSet[position].getMessage(),
                    dataSet[position].getTimeSent(),
                    dataSet[position].getSenderEmail()));
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.length;
    }

}


