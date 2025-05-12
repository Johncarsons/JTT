package com.example.jtt.model

import com.example.jtt.network.Message
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName

data class Confession(
    @DocumentId val id: String = "",
    val text: String = "",
    val roast: String = "",
    val timestamp: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
)
data class OpenAIRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    @SerializedName("max_tokens") val maxTokens: Int = 60
)
data class Message(
    val role: String = "user",
    val content: String)