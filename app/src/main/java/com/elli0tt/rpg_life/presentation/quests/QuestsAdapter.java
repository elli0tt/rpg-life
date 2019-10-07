package com.elli0tt.rpg_life.presentation.quests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Quest;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener;
    private OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private SelectionTracker<Long> selectionTracker;

    public QuestsAdapter() {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
    }

    private static final DiffUtil.ItemCallback<Quest> DIFF_CALLBACK = new DiffUtil.ItemCallback<Quest>() {
        @Override
        public boolean areItemsTheSame(@NonNull Quest oldItem, @NonNull Quest newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Quest oldItem, @NonNull Quest newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDifficulty() == newItem.getDifficulty();
        }
    };

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

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void removeOnItemClickListener(){
        onItemClickListener = null;
    }

    @NonNull
    @Override
    public QuestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quest_list_item, parent, false);
        return new QuestsViewHolder(
                view,
                onIsCompleteCheckBoxClickListener,
                onIsImportantCheckBoxClickListener,
                onItemClickListener,
                onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestsViewHolder holder, int position) {
        holder.bind(getItem(position), selectionTracker.isSelected((long) (position)));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class QuestsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.quest_is_completed) CheckBox isCompletedCheckBox;
        @BindView(R.id.quest_name) TextView nameTextView;
        @BindView(R.id.quest_difficulty_value) TextView difficultyTextView;
        @BindView(R.id.quest_is_important_check_box) CheckBox isImportantCheckBox;

        @BindString(R.string.difficulty_very_easy_text) String difficultyVeryEasyText;
        @BindString(R.string.difficulty_easy_text) String difficultyEasyText;
        @BindString(R.string.difficulty_normal_text) String difficultyNormalText;
        @BindString(R.string.difficulty_hard_text) String difficultyHardText;
        @BindString(R.string.difficulty_very_hard_text) String difficultyVeryHardText;
        @BindString(R.string.difficulty_impossible_text) String difficultyImpossibleText;
        @BindString(R.string.difficulty_error_text) String difficultyErrorText;

        private QuestsAdapter.OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener;
        private QuestsAdapter.OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener;
        private QuestsAdapter.OnItemClickListener onItemClickListener;
        private QuestsAdapter.OnItemLongClickListener onItemLongClickListener;

        QuestsViewHolder(
                @NonNull View itemView,
                final QuestsAdapter.OnIsCompleteCheckBoxClickListener onIsCompleteCheckBoxClickListener,
                final QuestsAdapter.OnIsImportantCheckBoxClickListener onIsImportantCheckBoxClickListener,
                final QuestsAdapter.OnItemClickListener onItemClickListener,
                final QuestsAdapter.OnItemLongClickListener onItemLongClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onIsCompleteCheckBoxClickListener = onIsCompleteCheckBoxClickListener;
            this.onIsImportantCheckBoxClickListener = onIsImportantCheckBoxClickListener;
            this.onItemClickListener = onItemClickListener;
            this.onItemLongClickListener = onItemLongClickListener;

            itemView.setOnClickListener(createOnItemClickListener(onItemClickListener));
//            itemView.setOnLongClickListener(
//                    createOnItemLongClickListener(onItemLongClickListener));
        }

        void bind(Quest quest, boolean isActivated) {
            isCompletedCheckBox.setChecked(quest.isCompleted());
            nameTextView.setText(quest.getName());
            difficultyTextView.setText(getDifficultyStringValue(quest.getDifficulty()));
            isImportantCheckBox.setChecked(quest.isImportant());
            itemView.setActivated(isActivated);
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

        @OnClick(R.id.quest_is_completed)
        void onIsCompletedClick(CheckBox checkBox) {
            int position = getAdapterPosition();
            if (onIsCompleteCheckBoxClickListener != null && position != RecyclerView.NO_POSITION) {
                onIsCompleteCheckBoxClickListener.onClick(checkBox.isChecked(), position);
            }
        }

        @OnClick(R.id.quest_is_important_check_box)
        void onIsImportantClick(CheckBox checkBox) {
            int position = getAdapterPosition();
            if (onIsImportantCheckBoxClickListener != null && position != RecyclerView.NO_POSITION) {
                onIsImportantCheckBoxClickListener.onClick(checkBox.isChecked(), position);
            }
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

        ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Nullable
                @Override
                public Long getSelectionKey() {
                    return getItemId();
                }
            };
        }
    }
}
