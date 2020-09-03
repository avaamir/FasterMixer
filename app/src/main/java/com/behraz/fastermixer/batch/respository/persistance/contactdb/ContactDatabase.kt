package com.behraz.fastermixer.batch.respository.persistance.contactdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.behraz.fastermixer.batch.models.Contact


@Database(entities = [Contact::class], version = 6, exportSchema = false)
abstract class ContactDatabase  : RoomDatabase() {

    abstract fun getDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null


        fun getInstance(context: Context): ContactDatabase =
            INSTANCE
                ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    )
                        .also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ContactDatabase::class.java, "contact.db"
            ).fallbackToDestructiveMigration().build()
    }
}