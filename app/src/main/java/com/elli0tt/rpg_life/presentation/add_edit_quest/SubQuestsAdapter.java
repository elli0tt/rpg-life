package com.elli0tt.rpg_life.presentation.add_edit_quest;


import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.ArrayList;
import java.util.List;

public class SubQuestsAdapter extends ListAdapter<Quest, SubQuestsAdapter.SubQuestsViewHolder> {

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

    public interface OnRemoveButtonClickListener {
        void onClick(int position);
    }

    private OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener;
    private OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnRemoveButtonClickListener onRemoveButtonClickListener;

    SubQuestsAdapter() {
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
                            oldItem.isImportant() == newItem.isImportant() &&
                            oldItem.getDateDue().equals(newItem.getDateDue()) &&
                            oldItem.getDateDueState().equals(newItem.getDateDueState());
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

    void setOnRemoveButtonClickListener(OnRemoveButtonClickListener onRemoveButtonClickListener) {
        this.onRemoveButtonClickListener = onRemoveButtonClickListener;
    }

    @Override
    public void submitList(@Nullable List<Quest> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    @NonNull
    @Override
    public SubQuestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subquest_recycler_item, parent, false);
        return new SubQuestsViewHolder(
                view,
                onIsCompleteCheckBoxClickListener,
                onIsImportantCheckBoxClickListener,
                onItemClickListener,
                onItemLongClickListener,
                onRemoveButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubQuestsViewHolder holder, int position) {
        holder.setOnItemClickListener(onItemClickListener);
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class SubQuestsViewHolder extends RecyclerView.ViewHolder {
        private CheckBox isCompletedCheckBox;
        private TextView nameTextView;
        private CheckBox isImportantCheckBox;
        private TextView dateDueTextView;
        private AppCompatImageView repeatImageView;
        private Button removeButton;

        private ColorStateList defaultTextViewColor;

        private int greenColorId;
        private int redColorId;

        SubQuestsViewHolder(
                @NonNull View itemView,
                final OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener,
                final OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener,
                final OnItemClickListener onItemClickListener,
                final OnItemLongClickListener onItemLongClickListener,
                final OnRemoveButtonClickListener onRemoveButtonClickListener) {
            super(itemView);

            isCompletedCheckBox = itemView.findViewById(R.id.subquest_is_completed);
            nameTextView = itemView.findViewById(R.id.subquest_name);
            isImportantCheckBox = itemView.findViewById(R.id.subquest_is_important_check_box);
            dateDueTextView = itemView.findViewById(R.id.subquest_date_due_text_view);
            repeatImageView = itemView.findViewById(R.id.subquest_repeat_image_view);
            removeButton = itemView.findViewById(R.id.subquest_remove_button);

            defaultTextViewColor = dateDueTextView.getTextColors();
            greenColorId =
                    itemView.getContext().getResources().getColor(R.color.colorQuestDateStateBeforeDateDue);
            redColorId =
                    itemView.getContext().getResources().getColor(R.color.colorQuestDateStateAfterDateDue);

            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
            itemView.setOnLongClickListener(
                    createOnItemLongClickListener(onItemLongClickListener));
            isCompletedCheckBox.setOnClickListener(createOnIsCompletedClickListener(onIsCompleteCheckBoxClickListener));
            isImportantCheckBox.setOnClickListener(createOnIsImportantClickListener(onIsImportantCheckBoxClickListener));
            removeButton.setOnClickListener(createOnRemoveButtonClickListener(onRemoveButtonClickListener));
        }

        void bind(Quest quest) {
            isCompletedCheckBox.setChecked(quest.isCompleted());
            nameTextView.setText(quest.getName());
            isImportantCheckBox.setChecked(quest.isImportant());
            switch (quest.getDateDueState()) {
                case NOT_SET:
                    dateDueTextView.setText(itemView.getContext()
                            .getString(R.string.quest_recycler_due_date_infinity));
                    dateDueTextView.setTextColor(defaultTextViewColor);
                    break;
                case BEFORE_DATE_DUE:
                    dateDueTextView.setText(itemView.getContext().getString(R.string.quest_recycler_due_date)
                            + " " + Quest.getDateDueFormatted(quest.getDateDue()));
                    dateDueTextView.setTextColor(greenColorId);
                    break;
                case AFTER_DATE_DUE:
                    dateDueTextView.setText(itemView.getContext().getString(R.string.quest_recycler_due_date)
                            + " " + Quest.getDateDueFormatted(quest.getDateDue()));
                    dateDueTextView.setTextColor(redColorId);
                    break;
                case TODAY:
                    dateDueTextView.setText(R.string.quest_date_due_today);
                    dateDueTextView.setTextColor(greenColorId);
                    break;
                case TOMORROW:
                    dateDueTextView.setText(R.string.quest_date_due_tomorrow);
                    dateDueTextView.setTextColor(greenColorId);
                    break;
            }
            repeatImageView.setImageTintList(defaultTextViewColor);
            if (quest.getRepeatState().equals(Quest.RepeatState.NOT_SET)) {
                repeatImageView.setVisibility(View.INVISIBLE);
            } else {
                repeatImageView.setVisibility(View.VISIBLE);
            }
        }

        void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
        }

        private String getDifficultyStringValue(@Quest.Difficulty int difficultyLevel) {
            switch (difficultyLevel) {
                case Quest.VERY_EASY:
                    return itemView.getContext().getText(R.string.difficulty_very_easy_text).toString();
                case Quest.EASY:
                    return itemView.getContext().getText(R.string.difficulty_easy_text).toString();
                case Quest.NORMAL:
                    return itemView.getContext().getText(R.string.difficulty_normal_text).toString();
                case Quest.HARD:
                    return itemView.getContext().getText(R.string.difficulty_hard_text).toString();
                case Quest.VERY_HARD:
                    return itemView.getContext().getText(R.string.difficulty_very_hard_text).toString();
                case Quest.IMPOSSIBLE:
                    return itemView.getContext().getText(R.string.difficulty_impossible_text).toString();
                default:
                    return itemView.getContext().getText(R.string.difficulty_error_text).toString();
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
                final OnItemClickListener listener) {
            return v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);

                }
            };
        }

        private View.OnLongClickListener createOnItemLongClickListener(
                final OnItemLongClickListener listener) {
            return v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onLongClick(position);
                }
                return true;
            };
        }

        private View.OnClickListener createOnRemoveButtonClickListener(
                final OnRemoveButtonClickListener listener) {
            return v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClick(position);
                }
            };
        }
    }
}

