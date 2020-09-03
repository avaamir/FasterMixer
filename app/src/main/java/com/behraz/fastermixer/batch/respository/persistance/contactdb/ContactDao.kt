package com.behraz.fastermixer.batch.respository.persistance.contactdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.behraz.fastermixer.batch.models.Contact

@Dao
interface ContactDao {

    @get:Query("SELECT * FROM contacts")
    val allContacts: LiveData<List<Contact>>

    @Query("SELECT * FROM contacts WHERE company = :company")
    fun filterByCompany(company: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts WHERE company LIKE :company AND (displayName LIKE :keyword OR mobileNumber Like :keyword)")
    fun searchAndFilterByCompany(keyword: String, company: String): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Contact)

    @Update
    suspend fun update(item: Contact)

    @Delete
    suspend fun delete(item: Contact)

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()

    @Query("SELECT * FROM contacts WHERE mobileNumber = :phone")
    suspend fun exists(phone: String): Contact?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Contact>)
}