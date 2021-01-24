package com.ivanasharov.smartplanner.data.repositories.contacts

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.ivanasharov.smartplanner.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val mContentResolver: ContentResolver
) : ContactRepository {

    private val mListContacts = ArrayList<Contact>()
    private val mUri: Uri = ContactsContract.Contacts.CONTENT_URI
    private val mOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

    override fun getAllContacts(): Flow<List<Contact>> = flow {
        loadContacts()
        emit(mListContacts.toList())
    }

    private fun loadContacts() {

        val cursor = mContentResolver.query(mUri,
            null, null, null, mOrder);

        if(cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                    )
                )
                val name = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                val hasPhone = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER
                    )
                )

                var phone = ""
                if (Integer.parseInt(hasPhone) > 0) {
                    // extract phone number
                    phone = loadPhone(id)
                    mListContacts.add(Contact(id, name, phone))
                }

            }
        }
    }

    private fun loadPhone(id: String): String{
        var phone = ""
        val args: Array<String> = arrayOf(id)
        val pCur = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", args, null)

        if (pCur != null) {
            while (pCur.moveToNext()) {
                phone = pCur.getString (pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
            pCur.close()
        }
        return phone
    }

}