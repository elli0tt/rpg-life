package com.elli0tt.rpg_life.presentation.screen.quests;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Difficulty;
import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestsAdapter extends ListAdapter<Quest, QuestsAdapter.QuestsViewHolder> {

    private static final DiffUtil.ItemCallback<Quest> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Quest>() {
                @Override
                public boolean areItemsTheSame(@NonNull Quest oldItem, @NonNull Quest newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Quest oldItem, @NonNull Quest newItem) {
                    return oldItem.getName().equals(newItem.getName()) &&
                            oldItem.getDifficulty().equals(newItem.getDifficulty()) &&
                            oldItem.getDescription().equals(newItem.getDescription()) &&
                            oldItem.isCompleted() == newItem.isCompleted() &&
                            oldItem.isImportant() == newItem.isImportant() &&
                            oldItem.getDateDue().equals(newItem.getDateDue()) &&
                            oldItem.getDateDueState().equals(newItem.getDateDueState()) &&
                            oldItem.getRepeatState().equals(newItem.getRepeatState()) &&
                            oldItem.getHasSubquests() == newItem.getHasSubquests();
                }
            };
    private OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener;
    private OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnSelectionFinishedListener onSelectionFinishedListener;
    private QuestsViewModel viewModel;
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

    QuestsAdapter() {
        super(DIFF_CALLBACK);
    }

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

    void setViewModel(QuestsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void submitList(@Nullable List<Quest> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    @NonNull
    @Override
    public QuestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_quest, parent, false);
        return new QuestsViewHolder(
                view,
                onIsCompleteCheckBoxClickListener,
                onIsImportantCheckBoxClickListener,
                onItemClickListener,
                onItemLongClickListener,
                viewModel);
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

    static class QuestsViewHolder extends RecyclerView.ViewHolder {
        private CheckBox isCompletedCheckBox;
        private TextView nameTextView;
        private TextView difficultyTextView;
        private CheckBox isImportantCheckBox;
        private TextView dateDueTextView;
        private AppCompatImageView repeatImageView;
        private AppCompatImageView hasSubquestsImageView;
        private TextView dayNumberTextView;

        private ColorStateList defaultTextViewColor;

        private QuestsViewModel viewModel;

        QuestsViewHolder(
                @NonNull View itemView,
                final OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener,
                final OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener,
                final OnItemClickListener onItemClickListener,
                final OnItemLongClickListener onItemLongClickListener,
                final QuestsViewModel viewModel) {
            super(itemView);
            this.viewModel = viewModel;

            isCompletedCheckBox = itemView.findViewById(R.id.is_completed_check_box);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            difficultyTextView = itemView.findViewById(R.id.difficulty_text_view);
            isImportantCheckBox = itemView.findViewById(R.id.is_important_check_box);
            dateDueTextView = itemView.findViewById(R.id.date_due_text_view);
            repeatImageView = itemView.findViewById(R.id.repeat_image_view);
            hasSubquestsImageView = itemView.findViewById(R.id.has_subquests_image_view);
            dayNumberTextView = itemView.findViewById(R.id.day_number_text_view);

            defaultTextViewColor = dateDueTextView.getTextColors();

            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
            itemView.setOnLongClickListener(
                    createOnItemLongClickListener(onItemLongClickListener));
            isCompletedCheckBox.setOnClickListener(createOnIsCompletedClickListener(onIsCompleteCheckBoxClickListener));
            isImportantCheckBox.setOnClickListener(createOnIsImportantClickListener(onIsImportantCheckBoxClickListener));
        }

        @SuppressLint("SetTextI18n")
        void bind(Quest quest, boolean isSelected) {
            isCompletedCheckBox.setChecked(quest.isCompleted());
            nameTextView.setText(quest.getName());
            if (quest.getDifficulty().equals(Difficulty.NOT_SET)) {
                difficultyTextView.setVisibility(View.GONE);
            } else {
                difficultyTextView.setVisibility(View.VISIBLE);
                difficultyTextView.setText(itemView.getContext().getString(R.string.quest_difficulty,
                        getDifficultyStringValue(quest.getDifficulty())));
            }
            isImportantCheckBox.setChecked(quest.isImportant());

            if (quest.getDateDueState().equals(Quest.DateState.NOT_SET)) {
                dateDueTextView.setVisibility(View.GONE);
            } else {
                dateDueTextView.setVisibility(View.VISIBLE);

                dateDueTextView.setText(itemView.getContext().getString(R.string.quest_recycler_date_due,
                        viewModel.getDateDueFormatted(quest.getDateDueState(),
                                quest.getDateDue())));
                dateDueTextView.setTextColor(itemView.getContext().getResources()
                        .getColor(viewModel.getDateDueColor(quest.getDateDue())));
            }

            repeatImageView.setImageTintList(defaultTextViewColor);
            hasSubquestsImageView.setImageTintList(defaultTextViewColor);
            if (quest.getRepeatState().equals(Quest.RepeatState.NOT_SET)) {
                repeatImageView.setVisibility(View.GONE);
            } else {
                repeatImageView.setVisibility(View.VISIBLE);
            }

            hasSubquestsImageView.setVisibility(quest.getHasSubquests() ? View.VISIBLE : View.GONE);

            if (quest.isChallenge()) {
                dayNumberTextView.setVisibility(View.VISIBLE);
                dayNumberTextView.setText(Integer.toString(quest.getDayNumber() + 1));
            } else {
                dayNumberTextView.setVisibility(View.GONE);
            }
            itemView.setActivated(isSelected);
        }

        void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
        }

        private String getDifficultyStringValue(Difficulty difficultyLevel) {
            switch (difficultyLevel) {
                case VERY_EASY:
                    return itemView.getContext().getString(R.string.quest_difficulty_very_easy);
                case EASY:
                    return itemView.getContext().getString(R.string.quest_difficulty_easy);
                case NORMAL:
                    return itemView.getContext().getString(R.string.quest_difficulty_normal);
                case HARD:
                    return itemView.getContext().getString(R.string.quest_difficulty_hard);
                case VERY_HARD:
                    return itemView.getContext().getString(R.string.quest_difficulty_very_hard);
                case IMPOSSIBLE:
                    return itemView.getContext().getString(R.string.quest_difficulty_impossible);
                case NOT_SET:
                    return itemView.getContext().getString(R.string.quest_difficulty_not_set);
                default:
                    return itemView.getContext().getString(R.string.quest_difficulty_error);
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