package com.elli0tt.rpg_life.presentation.adapter.skills;

import android.annotation.SuppressLint;
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

    private OnStartTimerFabClickListener onStartTimerFabClickListener;
    private OnItemClickListener onItemClickListener;

    public SkillsAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnStartTimerFabClickListener(OnStartTimerFabClickListener listener) {
        onStartTimerFabClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_skill,
                parent, false);
        return new ViewHolder(view, onStartTimerFabClickListener, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public interface OnStartTimerFabClickListener {
        void onClick();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final AppCompatImageView startTimerImageView;
        private final TextView levelTextView;
        private final ProgressBar xpProgressBar;
        private final TextView xpLeftToNextLevelTextView;

        ViewHolder(@NonNull View itemView,
                   final OnStartTimerFabClickListener onStartTimerFabClickListener,
                   final OnItemClickListener onItemClickListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            startTimerImageView = itemView.findViewById(R.id.start_timer_image_view);
            levelTextView = itemView.findViewById(R.id.level_value_text_view);
            xpProgressBar = itemView.findViewById(R.id.xp_progress_bar);
            xpLeftToNextLevelTextView =
                    itemView.findViewById(R.id.xp_left_to_next_level_value_text_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onClick(position);
                }
            });

            startTimerImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onStartTimerFabClickListener != null && position != RecyclerView.NO_POSITION) {
                    onStartTimerFabClickListener.onClick();
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void bind(@NonNull Skill skill) {
            nameTextView.setText(skill.getName());
            levelTextView.setText(Long.toString(skill.getLevel()));
            xpProgressBar.setMax((int) skill.getXpToNextLevel());
            xpProgressBar.setProgress((int) (skill.getXpToNextLevel() - skill.getXpLeftToNextLevel()));
            xpLeftToNextLevelTextView.setText(itemView.getContext().getString(R.string.xp_left_to_next_level,
                    (int) (skill.getXpToNextLevel() - skill.getXpLeftToNextLevel()),
                    (int) skill.getXpToNextLevel()));
        }
    }
}
