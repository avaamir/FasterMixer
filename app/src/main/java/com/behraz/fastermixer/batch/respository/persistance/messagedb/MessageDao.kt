package com.behraz.fastermixer.batch.respository.persistance.messagedb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.behraz.fastermixer.batch.models.Message

@Dao
interface MessageDao {

    @get:Query("SELECT * FROM messages")
    val allMessage: LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Message)

    @Update
    suspend fun update(item: Message)

    @Delete
    suspend fun delete(item: Message)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessage()

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun exists(id: Int): Message?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Message>)
}