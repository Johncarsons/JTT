package com.example.jtt.network
//
//import com.google.firebase.BuildConfig
////import okhttp3.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.Body
//import retrofit2.http.Headers
//import retrofit2.http.POST
//import retrofit2.Response  // Correct import
//
//
//interface OpenAIApiService {
//    @Headers("Content-Type: application/json", "Authorization: Bearer ${BuildConfig.OPENAI_API_KEY}")
//    @POST("v1/chat/completions")
//    suspend fun generateRoast(
//        @Body request: OpenAIRequest
//    ): Response<OpenAIResponse>
//}
//
//data class OpenAIRequest(
//    val model: String = "gpt-3.5-turbo",
//    val messages: List<Message>,
//    val maxtokens: Int = 60
//)
//
//data class Message(val role: String = "user", val content: String)
//data class OpenAIResponse(val choices: List<Choice>)
//data class Choice(val message: Message)
//
//// Retrofit Client
//object OpenAIApi {
//    private const val BASE_URL = "https://api.openai.com/"
//
//    val service: OpenAIApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(OpenAIApiService::class.java)
//    }
//}
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

interface OpenAIApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun generateRoast(
        @Body request: OpenAIRequest
    ): Response<OpenAIResponse>  // Fixed Response type
}

data class OpenAIRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    @SerializedName("max_tokens") val maxTokens: Int = 60  // Serialized name
)

data class Message(val role: String = "user", val content: String)
data class OpenAIResponse(val choices: List<Choice>)
data class Choice(val message: Message)

annotation class OpenAIApi
