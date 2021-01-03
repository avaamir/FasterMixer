package com.behraz.fastermixer.batch.utils.general

import android.content.ContentProviderOperation
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.behraz.fastermixer.batch.models.Contact

// Creates a contact entry from the current UI values, using the currently-selected account.
fun Context.createNewContact(contacts: List<Contact>) {
    contacts.forEach { contact ->
        val ops = ArrayList<ContentProviderOperation>()
        contact.run {
            ops.add(
                ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI
                )
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build()
            )


            //------------------------------------------------------ Names

            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        displayName
                    ).build()
            )


            //------------------------------------------------------ Mobile Number

            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                    .build()
            )


            //------------------------------------------------------ Home Numbers
            if (homeNumber != null) {
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
                        .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                        )
                        .build()
                )
            }

            //------------------------------------------------------ Work Numbers
            if (workNumber != null) {
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber)
                        .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                        )
                        .build()
                )
            }


            //------------------------------------------------------ Email
            if (emailID != null) {
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                        .withValue(
                            ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK
                        )
                        .build()
                )
            }

            //------------------------------------------------------ Organization
            if (company != "" && jobTitle != "") {
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                        )
                        .withValue(
                            ContactsContract.CommonDataKinds.Organization.COMPANY,
                            company
                        )
                        .withValue(
                            ContactsContract.CommonDataKinds.Organization.TYPE,
                            ContactsContract.CommonDataKinds.Organization.TYPE_WORK
                        )
                        .withValue(
                            ContactsContract.CommonDataKinds.Organization.TITLE,
                            jobTitle
                        )
                        .withValue(
                            ContactsContract.CommonDataKinds.Organization.TYPE,
                            ContactsContract.CommonDataKinds.Organization.TYPE_WORK
                        )
                        .build()
                )
            }
        }

        // Asking the Contact provider to create a new contact
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
            println("debug:ex: ${e.message}")
        }
    }
}


fun Context.readContactList(): ArrayList<Contact> {
    val contacts = HashMap<String, Contact>()

    val cur: Cursor? = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        null, null, null, null
    )
    if ((cur?.count ?: 0) > 0) {
        while (cur != null && cur.moveToNext()) {
            val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
            val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

            //Getting contact Phone
            if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                val pCur: Cursor? = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                while (pCur != null && pCur.moveToNext()) {
                    val phoneNo: String =
                        pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    if (contacts[name] == null) {
                        contacts[name] =
                            Contact(
                                name,
                                phoneNo,
                                "",
                                ""
                            )
                        //println("debug:notNull $name, $phoneNo")
                    }
                }
                pCur?.close()
            }
            //Getting Contact Organization
            //1- Getting ContactRawID
            var rawContactId = "-1"
            contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                arrayOf(ContactsContract.RawContacts._ID),
                ContactsContract.RawContacts.CONTACT_ID + "= ?",
                arrayOf(id),
                null
            )?.apply {
                if (moveToFirst()) {
                    rawContactId =
                        getInt(getColumnIndex(ContactsContract.RawContacts._ID)).toString()
                }
                close()
            }
            //2- Getting contact organization using rawContactId
            if (rawContactId != "-1") {
                contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                    arrayOf(
                        rawContactId,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                    ),
                    null
                )?.apply {
                    if (moveToFirst()) {
                        val organization =
                            getString(getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY))
                        contacts[name]?.company = organization ?: ""
                    }
                    close()
                }

            }
        }
    }
    cur?.close()
    return ArrayList(contacts.values)
}


