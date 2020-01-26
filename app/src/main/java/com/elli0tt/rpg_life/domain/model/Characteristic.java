package com.elli0tt.rpg_life.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.elli0tt.rpg_life.R;

@Entity (tableName = "characteristics_table")
public class Characteristic {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull private String name;
    private int value;
    @NonNull private String description;
    private int imageResource;

    private static final String DEFAULT_DESCRIPTION = "";
    private static final int DEFAULT_VALUE = 0;
    private static final int DEFAULT_IMAGE_RESOURCE = R.drawable.ic_endurance;

    public Characteristic(){
        //do nothing
    }

    @Ignore
    public Characteristic(@NonNull String name, int value, @NonNull String description, int imageResource) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.imageResource = imageResource;
    }

    @Ignore
    public Characteristic(@NonNull String name){
        this(name, DEFAULT_VALUE, DEFAULT_DESCRIPTION, DEFAULT_IMAGE_RESOURCE);
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    //Builder
//    public static class Builder{
//        private String name;
//        private int value = DEFAULT_VALUE;
//        private String description = DEFAULT_DESCRIPTION;
//        private int imageResource = DEFAULT_IMAGE_RESOURCE;
//
//        private static final String DEFAULT_DESCRIPTION = "";
//        private static final int DEFAULT_VALUE = 0;
//        private static final int DEFAULT_IMAGE_RESOURCE = R.drawable.ic_endurance;
//
//        public Builder(@NonNull String name){
//            this.name = name;
//        }
//
//        public Builder setValue(int value){
//            this.value = value;
//            return this;
//        }
//
//        public Builder setDescription(@NonNull String description){
//            this.description = description;
//            return this;
//        }
//
//        public Builder setImageResource(int imageResource){
//            this.imageResource = imageResource;
//            return this;
//        }
//
//        public Characteristic build(){
//            return new Characteristic(name, value, description, imageResource);
//        }
//    }
}
