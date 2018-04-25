package edu.gatech.cs2340.shelterme.util;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.controllers.AccountDetails;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.Model;

import static java.security.AccessController.getContext;

/**
 * The type Message adapter.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final Message[] dataSet;

    /**
     * The type View holder.
     */
// Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder { // implements View.OnClickListener,
//            View.OnLongClickListener {
        /**
         * The Message text view.
         */
// each data item is a Message in this case:
//         String message;
//         String timeSent;
//         String senderEmail;
//         boolean isAddressed;
        final TextView messageTextView;

        /**
         * Instantiates a new View holder.
         *
         * @param v the v
         */
        ViewHolder(View v) {
            super(v);
            messageTextView = v.findViewById(R.id._row);
        }


    }

    /**
     * Instantiates a new Message adapter.
     *
     * @param dataSet the data set
     */
// Provide a suitable constructor (depends on the kind of data set)
    public MessageAdapter(Message[] dataSet) {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element
        if (dataSet[position] != null) {
            String msgAddressed = dataSet[position].isAddressed() ? "Message Status: Addressed"
                    : "Message Status: Unaddressed";
//            holder.messageTextView.setText(String.format("%s\n%s\n\t%s\n\t\t%s",
            holder.messageTextView.setText(String.format("%s\n%s\n%s",
                    msgAddressed,
                    "Sent: "+dataSet[position].getTimeSent(),
                    "RE: "+dataSet[position].getMessage() //,
                     //,
//                    dataSet[position].getSenderEmail()
            ));
            TextView row = holder.itemView.findViewById(R.id._row);
//            Drawable[] addressedIndicator = row.getCompoundDrawables();
            if (!dataSet[position].isAddressed()) {
//                holder.itemView.findViewById(R.id.messageAddressed).setVisibility(View.GONE);
//                addressedIndicator[0].setAlpha(0);
//                row.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                row.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unread_message, 0);
            } else {
//                holder.itemView.findViewById(R.id.messageAddressed).setVisibility(View.VISIBLE);
//                addressedIndicator[0].setAlpha(255);
//                row.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_message1, 0);
                row.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.read_message, 0);
            }
        }

        // Make message items in the RecycleView clickable for details & interactions
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Message msg = dataSet[holder.getAdapterPosition()];
                if (msg != null) {
                    Intent intent = new Intent(v.getContext(), AccountDetails.class);
                    intent.putExtra("SenderEmail", msg.getSenderEmail());
                    intent.putExtra("MessageBody", msg.getMessage());
                    intent.putExtra("TimeSent", msg.getTimeSent());
                    v.getContext().startActivity(intent);
                }
                return false;
            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.length;
    }

}


