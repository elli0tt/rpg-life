package com.elli0tt.rpg_life.presentation.quests

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl


internal class InsertEmptyQuestWorker(private val context: Context,
                                      workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val questsRepository = QuestsRepositoryImpl(context)
        val outputData = Data.Builder()
                .putInt(Constants.KEY_QUEST_ID, questsRepository.insertEmptyQuestSync().toInt())
                .build();
        return Result.success(outputData);
    }
}