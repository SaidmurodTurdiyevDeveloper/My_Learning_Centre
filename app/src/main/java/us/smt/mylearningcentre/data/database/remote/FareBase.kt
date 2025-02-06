package us.smt.mylearningcentre.data.database.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FireBaseHelper(private val db: FirebaseFirestore, private val auth: FirebaseAuth) {

    companion object {
        const val collectionStudent = "Student"
        const val collectionApplication = "Application"
        const val collectionMessage = "Message"
        const val collectionTask = "Task"
        const val collectionClub = "Club"
        const val collectionClubCategory = "ClubCategory"
        private var instance: FireBaseHelper? = null
        fun inject(db: FirebaseFirestore, auth: FirebaseAuth) {
            instance = FireBaseHelper(db, auth)
        }

        fun getInstance() = instance!!
    }

    suspend fun getFcmToken(): String? {
        return FirebaseMessaging.getInstance().token
            .await()
    }

    suspend fun getAllData(collectionName: String): List<DocumentSnapshot> {
        return db.collection(collectionName)
            .get()
            .await()
            .documents
    }

    suspend fun getDatWithId(id: String, collectionName: String): DocumentSnapshot? {
        return db.collection(collectionName)
            .document(id)
            .get()
            .await()
    }


    suspend fun updateItem(id: String, newMap: Map<String, Any>, collectionName: String) {
        db.collection(collectionName)
            .document(id)
            .update(newMap)
            .await()
    }

    suspend fun deleteItem(id: String, collectionName: String) {
        db.collection(collectionName)
            .document(id)
            .delete()
            .await()
    }

    suspend fun addNewItem(mapData: Map<String, Any>, collectionName: String): String {
        return db.collection(collectionName).add(mapData).await().id
    }

    suspend fun register(email: String, password: String): Boolean {
        return auth.createUserWithEmailAndPassword(email, password).await().user != null
    }

    suspend fun login(email: String, password: String): Boolean {
        return auth.signInWithEmailAndPassword(email, password).await().user != null
    }

}