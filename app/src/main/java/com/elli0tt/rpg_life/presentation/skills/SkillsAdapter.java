package com.elli0tt.rpg_life.presentation.skills;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SkillsAdapter extends ListAdapter<Skill, SkillsAdapter.ViewHolder> {

    public interface OnStartTimerFabClickListener {
        void onClick();
    }

    private OnStartTimerFabClickListener onStartTimerFabClickListener;

    public void setOnStartTimerFabClickListener(OnStartTimerFabClickListener listener) {
        onStartTimerFabClickListener = listener;
    }

    SkillsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Skill> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Skill>() {
        @Override
        public boolean areItemsTheSame(@NonNull Skill oldItem, @NonNull Skill newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Skill oldItem, @NonNull Skill newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_recycler_item,
                parent, false);
        return new ViewHolder(view, onStartTimerFabClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private FloatingActionButton startTimerFab;

        ViewHolder(@NonNull View itemView,
                   final OnStartTimerFabClickListener onStartTimerFabClickListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.skills_recycler_item_name_text_view);
            startTimerFab = itemView.findViewById(R.id.skills_recycler_item_start_timer_fab);

            startTimerFab.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onStartTimerFabClickListener != null && position != RecyclerView.NO_POSITION){
                    onStartTimerFabClickListener.onClick();
                }
            });
        }

        void bind(@NonNull Skill skill) {
            nameTextView.setText(skill.getName());
        }
    }
}
