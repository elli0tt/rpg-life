package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.elli0tt.rpg_life.R

@Entity(tableName = "characteristics_table")
class Characteristic {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var name: String = ""
    var value = 0
    var description: String = ""
    //Builder
    var imageResource = 0

    constructor() { //do nothing
    }

    @Ignore
    constructor(name: String, value: Int, description: String, imageResource: Int) {
        this.name = name
        this.value = value
        this.description = description
        this.imageResource = imageResource
    }

    @Ignore
    constructor(name: String) : this(name, DEFAULT_VALUE, DEFAULT_DESCRIPTION, DEFAULT_IMAGE_RESOURCE) {
    }

    companion object {
        private const val DEFAULT_DESCRIPTION = ""
        private const val DEFAULT_VALUE = 0
        private const val DEFAULT_IMAGE_RESOURCE = R.drawable.ic_endurance
    }
}