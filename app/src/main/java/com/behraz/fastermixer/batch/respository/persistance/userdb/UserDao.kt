package com.behraz.fastermixer.batch.respository.persistance.userdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.behraz.fastermixer.batch.models.User

@Dao
interface UserDao {

    @get:Query("SELECT * FROM user_tb")
    val users: LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: User)

    @Update
    suspend fun update(item: User)

    @Delete
    suspend fun delete(item: User)

    @Query("DELETE FROM user_tb")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_tb WHERE id = :id")
    suspend fun exists(id: Int): User?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert1(item: User)

    @Query("DELETE FROM user_tb")
     fun deleteAll1()
}