package com.elli0tt.rpg_life.presentation.character;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Characteristic;

import java.util.List;
import java.util.Locale;

public class CharacteristicsAdapter extends RecyclerView.Adapter<CharacteristicsAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onClick(int position);
    }

    List<Characteristic> characteristicList;

//    public CharacteristicsAdapter(List<Characteristic> characteristicList) {
//        this.characteristicList = characteristicList;
//    }

    public void setCharacteristicList(List<Characteristic> characteristicList) {
        this.characteristicList = characteristicList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.characteristics_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (characteristicList != null) {
            Characteristic characteristic = characteristicList.get(position);
            holder.characteristicImage.setImageResource(characteristic.getImageResource());
            holder.characteristicName.setText(characteristic.getName());
            holder.characteristicValue.setText(String.format(Locale.getDefault(), "%d", characteristic.getValue()));
        } else{
            // TODO
            // I don't know if I have to do something here
            // I'll try to find out later
        }
    }

    @Override
    public int getItemCount() {
        if (characteristicList != null){
            return characteristicList.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView characteristicImage;
        TextView characteristicName;
        TextView characteristicValue;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            characteristicImage = itemView.findViewById(R.id.characteristic_image);
            characteristicName = itemView.findViewById(R.id.characteristic_name);
            characteristicValue = itemView.findViewById(R.id.characteristic_value);
        }
    }
}
