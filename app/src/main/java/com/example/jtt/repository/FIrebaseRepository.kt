//package com.example.jtt.repository
//import android.util.Log
//import com.example.jtt.model.Confession
//import com.example.jtt.network.Message
//import com.example.jtt.network.OpenAIApi
//import com.example.jtt.network.OpenAIRequest
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.tasks.await
//import javax.inject.Inject
//
//class FirebaseRepository @Inject constructor() {
//    private val db = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance()
//
//    suspend fun postConfession(text: String): Result<String> {
//        return try {
//            // 1. Authenticate
//            auth.signInAnonymously().await()
//
//            // 2. Save confession
//            val confession = Confession(text = text)
//            val docRef = db.collection("confessions").add(confession).await()
//            val docId = docRef.id ?: throw Exception("Failed to get document ID")
//
//            // 3. Generate roast
//            val roast = try {
//                OpenAIApi.service.generateRoast(
//                    OpenAIRequest(
//                        messages = listOf(
//                            Message(content = "Roast this confession playfully: \"$text\"")
//                        )
//                    )
//                ).body()?.choices?.firstOrNull()?.message?.content
//                    ?: "No roast generated"
//            } catch (e: Exception) {
//                Log.e("OpenAI", "Roast failed", e)
//                "AI service unavailable"
//            }
//
//            // 4. Update document
//            db.collection("confessions").document(docId)
//                .update("roast", roast)
//                .await()
//
//            Result.success(roast)
//        } catch (e: Exception) {
//            Log.e("FirebaseRepo", "Error: ${e.message}")
//            Result.failure(e)
//        }
//    }
//
//    suspend fun getConfessions(
//        pageSize: Int = 10,
//        lastDocId: String? = null
//    ): Result<List<Confession>> {
//        return try {
//            var query = db.collection("confessions")
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .limit(pageSize.toLong())
//
//            lastDocId?.let {
//                val lastDoc = db.collection("confessions").document(it).get().await()
//                query = query.startAfter(lastDoc)
//            }
//
//            val result = query.get().await()
//            Result.success(result.toObjects(Confession::class.java))
//        } catch (e: Exception) {
//            Log.e("FirebaseRepo", "Fetch failed: ${e.message}")
//            Result.failure(e)
//        }
//    }
//}
package com.example.jtt.repository

import android.util.Log
import com.example.jtt.model.Confession
import com.example.jtt.network.Message
import com.example.jtt.network.OpenAIApi
import com.example.jtt.network.OpenAIRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun postConfession(text: String): Result<String> {
        return try {
            // 1. Anonymous authentication
            auth.signInAnonymously().await()
            Log.d("FirebaseRepo", "User authenticated anonymously")

            // 2. Save confession to Firestore
            val confession = Confession(text = text)
            val docRef = db.collection("confessions").add(confession).await()
            val docId = docRef.id ?: throw Exception("Firestore document ID is null")
            Log.d("FirebaseRepo", "Confession saved with ID: $docId")

            // 3. Generate AI roast
            val roast = try {
                OpenAIApi.service.generateRoast(
                    OpenAIRequest(
                        messages = listOf(
                            Message(content = "Roast this confession playfully in one sentence: \"$text\"")
                        )
                    )
                ).body()?.choices?.firstOrNull()?.message?.content
                    ?: "No roast generated"
            } catch (e: Exception) {
                Log.e("OpenAI", "AI roast generation failed", e)
                "AI service unavailable"
            }

            // 4. Update confession with roast
            db.collection("confessions").document(docId)
                .update("roast", roast)
                .await()
            Log.d("FirebaseRepo", "Roast successfully updated")

            Result.success(roast)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "postConfession failed: ${e.message}")
            Result.failure(Exception("Failed to process confession. Please try again."))
        }
    }

    suspend fun getConfessions(
        pageSize: Int = 10,
        lastDocId: String? = null
    ): Result<List<Confession>> {
        return try {
            var query = db.collection("confessions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())

            lastDocId?.let {
                val lastDoc = db.collection("confessions").document(it).get().await()
                query = query.startAfter(lastDoc)
            }

            val result = query.get().await()
            val confessions = result.toObjects(Confession::class.java)
            Log.d("FirebaseRepo", "Fetched ${confessions.size} confessions")

            Result.success(confessions)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "getConfessions failed: ${e.message}")
            Result.failure(Exception("Failed to load confessions. Please try again."))
        }
    }
}