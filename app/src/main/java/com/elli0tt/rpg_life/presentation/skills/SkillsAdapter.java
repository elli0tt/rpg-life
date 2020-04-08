package com.elli0tt.rpg_life.presentation.skills;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Skill;

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
        private AppCompatImageView startTimerImageView;
        private TextView levelTextView;
        private ProgressBar xpProgressBar;

        ViewHolder(@NonNull View itemView,
                   final OnStartTimerFabClickListener onStartTimerFabClickListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            startTimerImageView = itemView.findViewById(R.id.start_timer_image_view);
            levelTextView = itemView.findViewById(R.id.level_value_text_view);
            xpProgressBar = itemView.findViewById(R.id.xp_progress_bar);

            startTimerImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onStartTimerFabClickListener != null && position != RecyclerView.NO_POSITION) {
                    onStartTimerFabClickListener.onClick();
                }
            });
        }

        void bind(@NonNull Skill skill) {
            nameTextView.setText(skill.getName());
            levelTextView.setText(Long.toString(skill.getLevel()));
            xpProgressBar.setMax(Skill.maxXpPercentage);
            xpProgressBar.setProgress(skill.getXpPercentage());
        }
    }
}
