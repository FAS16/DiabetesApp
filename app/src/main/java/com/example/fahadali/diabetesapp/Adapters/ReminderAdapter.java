package com.example.fahadali.diabetesapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fahadali.diabetesapp.Model.Reminder;
import com.example.fahadali.diabetesapp.R;

import java.util.List;

/**
 * Created by fahadali on 16/01/2018.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> { //Binds the date to the view


    private Context cntxt; //Used to inflate the layout
    private List<Reminder> reminderList;


    public ReminderAdapter(Context cntxt, List<Reminder> reminderList) {
        this.cntxt = cntxt;
        this.reminderList = reminderList;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //Creates ViewHolder (UI elements) instance

        if (viewType == 0) {

            LayoutInflater inflater = LayoutInflater.from(cntxt);
            View view = inflater.inflate(R.layout.reminder_list_element, null);
            ReminderViewHolder holder = new ReminderViewHolder(view);
            return holder;

        } else {

            LayoutInflater inflater = LayoutInflater.from(cntxt);
            View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ReminderViewHolder(itemView) {
            };

        }

    }

    @Override
    public void onBindViewHolder(final ReminderViewHolder holder, final int position) { //Binding the data to the UI elements

        Reminder reminder = reminderList.get(position);
        holder.note_TV.setText(reminder.getNote());
        holder.date_TV.setText(reminder.getDate());
        holder.time_TV.setText(reminder.getTime());
        holder.repeat_TV.setText(reminder.getRepeat());
        holder.priority_TV.setText(reminder.getPriority());

    }



    @Override
    public int getItemCount() { //Returns size of list
        return reminderList.size();
    }



    public class ReminderViewHolder extends RecyclerView.ViewHolder { //Holds the view

        ImageView reminderIcon_IV;
        TextView note_TV, date_TV, time_TV, repeat_TV, priority_TV;
        ImageButton options_IB;


        public ReminderViewHolder(View itemView) {
            super(itemView);

            reminderIcon_IV = itemView.findViewById(R.id.reminder_IV);
            note_TV = itemView.findViewById(R.id.note);
            date_TV = itemView.findViewById(R.id.date_rem);
            time_TV = itemView.findViewById(R.id.time_rem);
            repeat_TV = itemView.findViewById(R.id.repeat_rem);
            priority_TV = itemView.findViewById(R.id.priority_rem);
            options_IB = itemView.findViewById(R.id.menu_card_view_rem);

        }
    }

}