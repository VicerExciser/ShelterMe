package edu.gatech.cs2340.shelterme.util;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.controllers.AccountDetails;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.Model;

import static java.security.AccessController.getContext;

/**
 * The type Message adapter.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>
        implements Serializable{
    private final Message[] dataSet;
    private Set<ViewHolder> viewList;
    int order;

    /**
     * The type View holder.
     */
// Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder { // implements View.OnClickListener,
//            View.OnLongClickListener {
        /**
         * The Message text view.
         */
// each data item is a Message in this case:
//         String message;
//         String timeSent;
//         boolean isAddressed;
        final TextView messageTextView;
        String time, body;
        boolean beenAddressed;

        /**
         * Instantiates a new View holder.
         *
         * @param v the v
         */
        ViewHolder(View v) {
            super(v);
            messageTextView = v.findViewById(R.id._row);

//            order = viewList.size();
            Message msg = dataSet[order % getItemCount()];
            if (msg != null) {
                this.beenAddressed = msg.isAddressed();
                this.time = msg.getTimeSent();
                this.body = msg.getMessage();

                String msgAddressed = beenAddressed ? "Message Status: Addressed"
                        : "Message Status: Unaddressed";
                messageTextView.setText(String.format("%s\n%s\n%s",
                        msgAddressed,
                        "Sent: " + time,
                        "RE: " + body
                ));


                if (!beenAddressed) {
                    messageTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.unread_message, 0);
                } else {
                    messageTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.read_message, 0);
                }
            }
//            order++;
//            if (viewList == null) {
//                viewList = new ArrayList<>();
//            }
//            viewList.add(this);
        }

//        @Override
//        public void onClick

        public TextView getMessageTextView() { return this.messageTextView; }

        public boolean hasBeenAddressed() { return this.beenAddressed; }

        public void setBeenAddressed(boolean toggle) { this.beenAddressed = toggle; }

        public String getTime() {
            return this.time;
        }

        public void setTime(String t) { this.time = t;}

        public String getBody() {
            return this.body;
        }

        public void setBody(String b) { this.body = b; }

        @Override
        public String toString() { return this.time; }
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
        this.order = 0;
        this.viewList = new HashSet<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.recycler_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(row);
        if (viewHolder != null) {
            if (viewList == null) {
                viewList = new HashSet<>();
            }
            if (viewHolder.getTime() != null) {
                boolean contains = false;
                for (ViewHolder vh : viewList) {
                    if (vh != null && vh.getTime() != null) {
                        if (viewHolder.getTime().equals(vh.getTime())) {
                            contains = true;
                            break;
                        }
                    } else {
                        viewList.remove(vh);
                    }
                }
                if (!contains) {
                    viewList.add(viewHolder);
                    order++;
                }
            } else {
                Log.e("MessageAdapter", "VH HAS NULL TIMESTAMP");
            }
        }
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element
        if (holder != null && holder.getTime() != null) {
            if (viewList == null) {
                viewList = new HashSet<>();
            }
            if (dataSet[position] != null) {
//            if (!viewList.contains(holder)) {
                boolean contains = false;
                for (ViewHolder vh : viewList) {
                    if (vh != null && vh.getTime() != null) {
                        if (holder.getTime().equals(vh.getTime())) {
                            contains = true;
                            break;
                        }
                    } else {
                        viewList.remove(vh);
                    }
                }
                if (!contains) {
                    viewList.add(holder);
                }

                holder.setBeenAddressed(dataSet[position].isAddressed());
                holder.setBody(dataSet[position].getMessage());
                holder.setTime(dataSet[position].getTimeSent());

                TextView row = holder.itemView.findViewById(R.id._row);
                String msgAddressed;
                if (dataSet[position].isAddressed()) {
                    msgAddressed = "Message Status: Addressed";
                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.read_message, 0);
                } else {
                    msgAddressed = "Message Status: Unaddressed";
                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.unread_message, 0);
                }
                holder.messageTextView.setText(String.format("%s\n%s\n%s",
                        msgAddressed,
                        "Sent: " + dataSet[position].getTimeSent(),
                        "RE: " + dataSet[position].getMessage()
                ));

                // Display little icons to indicate whether a message has been addressed by an admin yet
//                if (!dataSet[position].isAddressed()) {
//                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                            R.drawable.unread_message, 0);
//                    holder.setBeenAddressed(false);
//                } else {
//                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                            R.drawable.read_message, 0);
//                    holder.setBeenAddressed(true);
//                }
            }

            // Display little icons to indicate whether a message has been addressed by an admin yet
//            TextView row = holder.itemView.findViewById(R.id._row);
//            for (int i = 0; i < dataSet.length; i++) {
//                if (dataSet[i] != null) {
//                    if (!dataSet[i].isAddressed()) {
//                        row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                                R.drawable.unread_message, 0);
//                    } else {
//                        row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                                R.drawable.read_message, 0);
//                    }
//                } else {
//                    int temp_i = i;
//                    int j = i + 1;
//                    while (j < dataSet.length && dataSet[j] != null) {
//                        dataSet[temp_i] = dataSet[j];
//                        j++;
//                        temp_i++;
//                    }
//                }
//            }
//        }

            // Make message items in the RecycleView clickable for details & interactions
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int index = holder.getAdapterPosition();
                    Message msg = dataSet[index];
                    if (msg != null) {
                        Intent intent = new Intent(v.getContext(), AccountDetails.class);
                        intent.putExtra("SenderEmail", msg.getSenderEmail());
                        intent.putExtra("MessageBody", msg.getMessage());
                        intent.putExtra("TimeSent", msg.getTimeSent());
//                    intent.putExtra("MessageAdapter", MessageAdapter.this);
                        v.getContext().startActivity(intent);
                    }
                    notifyItemChanged(index);
//                updateMessageTextViewString();
                    return false;
                }
            });
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    public void updateMessageTextViewString() {
        if (viewList != null) {
            for (ViewHolder tv : viewList) {
//            int index = viewList.indexOf(tv);
//            if (dataSet[index] != null) {
                TextView row = tv.getMessageTextView();
                String msgAddressed;
                if (tv.hasBeenAddressed()) {
                    msgAddressed = "Message Status: Addressed";
                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.read_message, 0);
                } else {
                    msgAddressed = "Message Status: Unaddressed";
                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.unread_message, 0);
                }

                row.setText(String.format("%s\n%s\n%s",
                        msgAddressed,
                        "Sent: " + tv.getTime(),
                        "RE: " + tv.getBody()
                ));

//            }
            }
        }
    }

//    public void updateTextView(final ViewHolder holder, int position) {
//        if (position >= 0 && dataSet[position] != null) {
//            String msgAddressed = dataSet[position].isAddressed() ? "Message Status: Addressed"
//                    : "Message Status: Unaddressed";
//            holder.messageTextView.setText(String.format("%s\n%s\n%s",
//                    msgAddressed,
//                    "Sent: " + dataSet[position].getTimeSent(),
//                    "RE: " + dataSet[position].getMessage()
//            ));
//        }
//        // Display little icons to indicate whether a message has been addressed by an admin yet
//
//        row = holder.itemView.findViewById(R.id._row);
//        for (int i = 0; i < dataSet.length; i++) {
//            if (dataSet[i] != null) {
//                if (position < 0) {
//                    String msgAddressed = dataSet[i].isAddressed() ? "Message Status: Addressed"
//                            : "Message Status: Unaddressed";
//                    holder.messageTextView.setText(String.format("%s\n%s\n%s",
//                            msgAddressed,
//                            "Sent: " + dataSet[i].getTimeSent(),
//                            "RE: " + dataSet[i].getMessage()
//                    ));
//                }
//
//                if (!dataSet[i].isAddressed()) { // && row != null) {
//                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                            R.drawable.unread_message, 0);
//                } else {
//                    row.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                            R.drawable.read_message, 0);
//                }
//            } else {
//                int temp_i = i;
//                int j = i + 1;
//                while (j < dataSet.length && dataSet[j] != null) {
//                    dataSet[temp_i] = dataSet[j];
//                    j++;
//                    temp_i++;
//                }
//            }
//        }
//    }

}


