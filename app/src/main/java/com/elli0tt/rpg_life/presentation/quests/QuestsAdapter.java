package com.elli0tt.rpg_life.presentation.quests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestsAdapter extends ListAdapter<Quest, QuestsAdapter.QuestsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(int position);
    }

    public interface OnIsCompleteCheckBoxClickListener {
        void onClick(boolean isCompleted, int position);
    }

    public interface OnIsImportantCheckBoxClickListener {
        void onClick(boolean isImportant, int position);
    }

    public interface OnSelectionFinishedListener {
        void onSelectionFinished();
    }

    private OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener;
    private OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnSelectionFinishedListener onSelectionFinishedListener;

    QuestsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Quest> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Quest>() {
                @Override
                public boolean areItemsTheSame(@NonNull Quest oldItem, @NonNull Quest newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Quest oldItem, @NonNull Quest newItem) {
                    return oldItem.getName().equals(newItem.getName()) &&
                            oldItem.getDifficulty() == newItem.getDifficulty() &&
                            oldItem.getDescription().equals(newItem.getDescription()) &&
                            oldItem.isCompleted() == newItem.isCompleted() &&
                            oldItem.isImportant() == newItem.isImportant();
                }
            };

    void setOnIsCompleteCheckBoxClickListener(OnIsCompleteCheckBoxClickListener listener) {
        onIsCompleteCheckBoxClickListener = listener;
    }

    void setOnIsImportantCheckBoxClickListener(OnIsImportantCheckBoxClickListener listener) {
        onIsImportantCheckBoxClickListener = listener;
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    void setOnSelectionFinishedListener(OnSelectionFinishedListener onSelectionFinishedListener) {
        this.onSelectionFinishedListener = onSelectionFinishedListener;
    }

    @Override
    public void submitList(@Nullable List<Quest> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    @NonNull
    @Override
    public QuestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quest_recycler_item, parent, false);
        return new QuestsViewHolder(
                view,
                onIsCompleteCheckBoxClickListener,
                onIsImportantCheckBoxClickListener,
                onItemClickListener,
                onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestsViewHolder holder, int position) {
        holder.setOnItemClickListener(onItemClickListener);
        holder.bind(getItem(position), isSelected(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private List<Boolean> selectedPositions;

    private OnItemClickListener saveLastOnItemClickListener;

    private OnItemClickListener selectionOnItemClickListener = position -> {
        selectedPositions.set(position, !selectedPositions.get(position));

        if (isNothingSelected()) {
            finishSelection();
        } else {
            notifyItemChanged(position);
        }
    };


    void startSelection(int selectedPosition) {
        saveLastOnItemClickListener = onItemClickListener;
        onItemClickListener = selectionOnItemClickListener;

        selectedPositions = new ArrayList<>(getItemCount());
        for (int i = 0; i < getItemCount(); ++i) {
            selectedPositions.add(false);
        }
        selectedPositions.set(selectedPosition, true);

        notifyDataSetChanged();
    }

    void finishSelection() {
        if (onSelectionFinishedListener != null) {
            onSelectionFinishedListener.onSelectionFinished();
        }
        onItemClickListener = saveLastOnItemClickListener;
        selectedPositions = null;

        notifyDataSetChanged();
    }

    private boolean isSelected(int position) {
        return selectedPositions != null ? selectedPositions.get(position) : false;
    }

    private boolean isNothingSelected() {
        for (boolean element : selectedPositions) {
            if (element) {
                return false;
            }
        }
        return true;
    }

    void selectAll() {
        for (int i = 0; i < selectedPositions.size(); ++i) {
            selectedPositions.set(i, true);
        }
        notifyDataSetChanged();
    }

    List<Quest> getSelectedQuests() {
        List<Quest> selectedQuests = new ArrayList<>();
        for (int i = 0; i < selectedPositions.size(); ++i) {
            if (selectedPositions.get(i)) {
                selectedQuests.add(getItem(i));
            }
        }
        return selectedQuests;
    }


    static class QuestsViewHolder extends RecyclerView.ViewHolder {
        private CheckBox isCompletedCheckBox;
        private TextView nameTextView;
        private TextView difficultyTextView;
        private CheckBox isImportantCheckBox;

        private String difficultyVeryEasyText;
        private String difficultyEasyText;
        private String difficultyNormalText;
        private String difficultyHardText;
        private String difficultyVeryHardText;
        private String difficultyImpossibleText;
        private String difficultyErrorText;

        QuestsViewHolder(
                @NonNull View itemView,
                final OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener,
                final OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener,
                final OnItemClickListener onItemClickListener,
                final OnItemLongClickListener onItemLongClickListener) {
            super(itemView);

            isCompletedCheckBox = itemView.findViewById(R.id.quest_is_completed);
            nameTextView = itemView.findViewById(R.id.quest_name);
            difficultyTextView = itemView.findViewById(R.id.quest_difficulty_value);
            isImportantCheckBox = itemView.findViewById(R.id.quest_is_important_check_box);

            difficultyVeryEasyText =
                    itemView.getContext().getText(R.string.difficulty_very_easy_text).toString();
            difficultyEasyText =
                    itemView.getContext().getText(R.string.difficulty_easy_text).toString();
            difficultyNormalText =
                    itemView.getContext().getText(R.string.difficulty_normal_text).toString();
            difficultyHardText =
                    itemView.getContext().getText(R.string.difficulty_hard_text).toString();
            difficultyVeryHardText =
                    itemView.getContext().getText(R.string.difficulty_very_hard_text).toString();
            difficultyImpossibleText =
                    itemView.getContext().getText(R.string.difficulty_impossible_text).toString();
            difficultyErrorText =
                    itemView.getContext().getText(R.string.difficulty_error_text).toString();

            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
            itemView.setOnLongClickListener(
                    createOnItemLongClickListener(onItemLongClickListener));
            isCompletedCheckBox.setOnClickListener(createOnIsCompletedClickListener(onIsCompleteCheckBoxClickListener));
            isImportantCheckBox.setOnClickListener(createOnIsImportantClickListener(onIsImportantCheckBoxClickListener));
        }

        void bind(Quest quest, boolean isSelected) {
            isCompletedCheckBox.setChecked(quest.isCompleted());
            nameTextView.setText(quest.getName());
            difficultyTextView.setText(getDifficultyStringValue(quest.getDifficulty()));
            isImportantCheckBox.setChecked(quest.isImportant());
            itemView.setActivated(isSelected);
        }

        void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
        }

        private String getDifficultyStringValue(@Quest.Difficulty int difficultyLevel) {
            switch (difficultyLevel) {
                case Quest.VERY_EASY:
                    return difficultyVeryEasyText;
                case Quest.EASY:
                    return difficultyEasyText;
                case Quest.NORMAL:
                    return difficultyNormalText;
                case Quest.HARD:
                    return difficultyHardText;
                case Quest.VERY_HARD:
                    return difficultyVeryHardText;
                case Quest.IMPOSSIBLE:
                    return difficultyImpossibleText;
                default:
                    return difficultyErrorText;
            }
        }

        private View.OnClickListener createOnIsCompletedClickListener(
                final OnIsCompleteCheckBoxClickListener listener) {
            return v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClick(((CheckBox) v).isChecked(), position);
                }
            };
        }

        private View.OnClickListener createOnIsImportantClickListener(
                final OnIsImportantCheckBoxClickListener listener) {
            return v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClick(((CheckBox) v).isChecked(), position);
                }
            };
        }

        private View.OnClickListener createOnItemClickListener(
                final QuestsAdapter.OnItemClickListener listener) {
            return v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);

                }
            };
        }

        private View.OnLongClickListener createOnItemLongClickListener(
                final QuestsAdapter.OnItemLongClickListener listener) {
            return v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onLongClick(position);
                }
                return true;
            };
        }
    }
}
