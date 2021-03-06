package com.elli0tt.rpg_life.presentation.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.AddEditQuestFragment
import com.elli0tt.rpg_life.presentation.screen.main.MainActivity

class QuestReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val builder = NotificationCompat.Builder(context!!, MainActivity.CHANNEL_ID)
                .setContentTitle(intent?.getStringExtra(AddEditQuestFragment.EXTRA_REMINDER_TITLE))
                .setContentText("")
                .setSmallIcon(R.drawable.ic_round_today_white_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(intent?.getIntExtra(AddEditQuestFragment.EXTRA_NOTIFICATION_ID, 0)!!, builder.build())
    }
}