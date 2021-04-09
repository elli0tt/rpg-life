package com.elli0tt.rpg_life.presentation.adapter.quests;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Difficulty;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.screen.quests.QuestsViewModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    public QuestsAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnIsCompleteCheckBoxClickListener(OnIsCompleteCheckBoxClickListener listener) {
        onIsCompleteCheckBoxClickListener = listener;
    }

    public void setOnIsImportantCheckBoxClickListener(OnIsImportantCheckBoxClickListener listener) {
        onIsImportantCheckBoxClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnSelectionFinishedListener(OnSelectionFinishedListener onSelectionFinishedListener) {
        this.onSelectionFinishedListener = onSelectionFinishedListener;
    }

    public void setViewModel(QuestsViewModel viewModel) {
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

    public void startSelection(int selectedPosition) {
        saveLastOnItemClickListener = onItemClickListener;
        onItemClickListener = selectionOnItemClickListener;

        selectedPositions = new ArrayList<>(getItemCount());
        for (int i = 0; i < getItemCount(); ++i) {
            selectedPositions.add(false);
        }
        selectedPositions.set(selectedPosition, true);

        notifyDataSetChanged();
    }

    public void finishSelection() {
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

    public void selectAll() {
        for (int i = 0; i < selectedPositions.size(); ++i) {
            selectedPositions.set(i, true);
        }
        notifyDataSetChanged();
    }

    public List<Quest> getSelectedQuests() {
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
        private LikeButton isImportantLikeButton;
        private TextView dateDueTextView;
        private AppCompatImageView repeatImageView;
        private AppCompatImageView hasSubquestsImageView;
        private TextView dayNumberTextView;
        private ConstraintLayout constraintLayout;

        private QuestsViewModel viewModel;

        private ConstraintSet constraintSet;

        QuestsViewHolder(
                @NonNull View itemView,
                final OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener,
                final OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener,
                final OnItemClickListener onItemClickListener,
                final OnItemLongClickListener onItemLongClickListener,
                final QuestsViewModel viewModel) {
            super(itemView);
            this.viewModel = viewModel;

            findViews();

            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
            itemView.setOnLongClickListener(
                    createOnItemLongClickListener(onItemLongClickListener));
            isCompletedCheckBox.setOnClickListener(createOnIsCompletedClickListener(onIsCompleteCheckBoxClickListener));
            isImportantLikeButton.setOnLikeListener(createOnIsImportantClickListener(onIsImportantCheckBoxClickListener));
        }

        private void findViews() {
            isCompletedCheckBox = itemView.findViewById(R.id.is_completed_check_box);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            difficultyTextView = itemView.findViewById(R.id.difficulty_text_view);
            isImportantLikeButton = itemView.findViewById(R.id.is_important_like_button);
            dateDueTextView = itemView.findViewById(R.id.date_due_text_view);
            repeatImageView = itemView.findViewById(R.id.repeat_image_view);
            hasSubquestsImageView = itemView.findViewById(R.id.has_subquests_image_view);
            dayNumberTextView = itemView.findViewById(R.id.day_number_text_view);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }

        @SuppressLint("SetTextI18n")
        void bind(Quest quest, boolean isSelected) {
            constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            setConstraints(quest);

            isCompletedCheckBox.setChecked(quest.isCompleted());
            nameTextView.setText(quest.getName());
            isImportantLikeButton.setLiked(quest.isImportant());

            bindDifficulty(quest.getDifficulty());
            bindDateDue(quest.getDateDueState(), quest.getDateDue());
            bindDayNumber(quest.getDayNumber(), quest.isChallenge());

            repeatImageView.setVisibility(quest.getRepeatState().equals(Quest.RepeatState.NOT_SET)
                    ? View.GONE : View.VISIBLE);
            hasSubquestsImageView.setVisibility(quest.getHasSubquests() ? View.VISIBLE : View.GONE);

            itemView.setActivated(isSelected);
        }

        private void setConstraints(Quest quest) {
            if (quest.getDifficulty().equals(Difficulty.NOT_SET)) {
                constraintSet.setMargin(R.id.date_due_text_view, ConstraintSet.START, 0);
                constraintSet.connect(R.id.name_text_view, ConstraintSet.BOTTOM,
                        R.id.date_due_text_view, ConstraintSet.TOP);
                if (quest.getDateDueState().equals(Quest.DateState.NOT_SET)) {
                    constraintSet.connect(R.id.has_subquests_image_view, ConstraintSet.START,
                            R.id.is_completed_check_box, ConstraintSet.END, 0);
                    if (!quest.getHasSubquests()) {
                        constraintSet.setMargin(R.id.name_text_view, ConstraintSet.TOP, 0);
                        constraintSet.connect(R.id.name_text_view, ConstraintSet.BOTTOM,
                                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                        constraintSet.connect(R.id.date_due_text_view, ConstraintSet.TOP,
                                ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                    } else {
                        constraintSet.setMargin(R.id.name_text_view, ConstraintSet.TOP,
                                (int) getDimension(R.dimen.list_item_quest_start_start_margin_small));
                        constraintSet.connect(R.id.name_text_view, ConstraintSet.BOTTOM,
                                R.id.date_due_text_view, ConstraintSet.TOP);
                        constraintSet.connect(R.id.date_due_text_view, ConstraintSet.TOP,
                                R.id.name_text_view, ConstraintSet.BOTTOM);
                    }
                } else {
                    constraintSet.connect(R.id.has_subquests_image_view, ConstraintSet.START,
                            R.id.repeat_image_view, ConstraintSet.END,
                            (int) getDimension(R.dimen.list_item_quest_start_start_margin_small));
                    constraintSet.setMargin(R.id.name_text_view, ConstraintSet.TOP,
                            (int) getDimension(R.dimen.list_item_quest_start_start_margin_small));
                    constraintSet.connect(R.id.name_text_view, ConstraintSet.BOTTOM,
                            R.id.date_due_text_view, ConstraintSet.TOP);
                    constraintSet.connect(R.id.date_due_text_view, ConstraintSet.TOP,
                            R.id.name_text_view, ConstraintSet.BOTTOM);
                }
            } else {
                constraintSet.setMargin(R.id.date_due_text_view, ConstraintSet.START,
                        (int) getDimension(R.dimen.list_item_quest_start_start_margin_small));
                constraintSet.connect(R.id.name_text_view, ConstraintSet.BOTTOM,
                        R.id.difficulty_text_view, ConstraintSet.TOP);
                constraintSet.connect(R.id.has_subquests_image_view, ConstraintSet.START,
                        R.id.repeat_image_view, ConstraintSet.END,
                        (int) getDimension(R.dimen.list_item_quest_start_start_margin_small));
                constraintSet.setMargin(R.id.name_text_view, ConstraintSet.TOP,
                        (int) getDimension(R.dimen.list_item_quest_start_start_margin_small));
                constraintSet.connect(R.id.name_text_view, ConstraintSet.BOTTOM,
                        R.id.date_due_text_view, ConstraintSet.TOP);
                constraintSet.connect(R.id.date_due_text_view, ConstraintSet.TOP,
                        R.id.name_text_view, ConstraintSet.BOTTOM);
            }

            constraintSet.applyTo(constraintLayout);
        }

        private void bindDifficulty(@NotNull Difficulty difficulty) {
            if (difficulty.equals(Difficulty.NOT_SET)) {
                difficultyTextView.setVisibility(View.GONE);
            } else {
                difficultyTextView.setVisibility(View.VISIBLE);
                difficultyTextView.setText(getDifficultyStringValue(difficulty));
            }
        }

        private void bindDateDue(@NotNull Quest.DateState dateDueState, Calendar dateDue) {
            if (dateDueState.equals(Quest.DateState.NOT_SET)) {
                dateDueTextView.setVisibility(View.INVISIBLE);
            } else {
                dateDueTextView.setVisibility(View.VISIBLE);

                dateDueTextView.setText(viewModel.getDateDueFormatted(dateDueState, dateDue));
                dateDueTextView.setTextColor(itemView.getContext().getResources()
                        .getColor(viewModel.getDateDueColor(dateDue)));
            }
        }

        private void bindDayNumber(int dayNumber, boolean isChallenge) {
            if (isChallenge) {
                dayNumberTextView.setVisibility(View.VISIBLE);
                dayNumberTextView.setText(String.format(Locale.getDefault(), "%d", dayNumber + 1));
            } else {
                dayNumberTextView.setVisibility(View.GONE);
            }
        }

        void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
        }

        @NotNull
        private String getDifficultyStringValue(@NotNull Difficulty difficultyLevel) {
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

        private OnLikeListener createOnIsImportantClickListener(
                final OnIsImportantCheckBoxClickListener listener) {
            return new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onClick(true, position);
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onClick(false, position);
                    }
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

        private float getDimension(@DimenRes int resId) {
            return itemView.getResources().getDimension(resId);
        }
    }
}