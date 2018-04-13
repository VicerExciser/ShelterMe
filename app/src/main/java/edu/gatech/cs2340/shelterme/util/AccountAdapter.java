package edu.gatech.cs2340.shelterme.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;

// --Commented out by Inspection START (4/13/2018 6:17 PM):
///**
// * Created by austincondict on 4/8/18.
// */
//
//public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
//    private final Account[] dataSet;
//
//// --Commented out by Inspection START (4/13/2018 6:17 PM):
////    public AccountAdapter(Account[] dataSet) {
////        this.dataSet = dataSet;
////    }
//// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View row = inflater.inflate(R.layout.recycler_item_layout, parent, false);
//        return new ViewHolder(row);
//    }
//
//    /**
//     * Inner view holder class that assigns view elements for each card in the recycler view
//     */
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        final TextView accountTextView;
//// --Commented out by Inspection START (4/13/2018 6:17 PM):
////        ViewHolder(View v) {
////            super(v);
////            accountTextView = v.findViewById(R.id._row);
////        }
//// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
//
//        // TODO: Make account items in the RecycleView clickable for details & interactions
//        @Override
//        public void onClick(View v) {
//
//        }
//
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        if (dataSet[position] != null) {
//            String locked = dataSet[position].isAccountLocked() ? "Locked" : "Unlocked";
//            holder.accountTextView.setText(String.format("%s (%s)\n%s\n\tAccount Status: %s",
//                    dataSet[position].getName(),
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
                    dataSet[position].getUsername(),
                    dataSet[position].getEmail(),
                    locked));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
