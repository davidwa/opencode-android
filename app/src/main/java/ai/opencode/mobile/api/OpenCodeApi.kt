package ai.opencode.mobile.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class OpenCodeApi {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val apiKey = "sk-AmcUJKfcndoXVrUcTqvo3swHFFODOosMQcuQRDqCyeZ5HyU9Wt3PqQUVLYYEm7N3"
    private val baseUrl = "https://api.opencode.ai/v1"
    
    suspend fun sendMessage(content: String): String {
        return try {
            // Try OpenCode API first
            val response = callOpenCodeApi(content)
            if (response != null) return response
            
            // Fallback to OpenAI-compatible API
            val fallbackResponse = callFallbackApi(content)
            if (fallbackResponse != null) return fallbackResponse
            
            // Final fallback: local response
            generateLocalResponse(content)
            
        } catch (e: Exception) {
            "Network error: ${e.message}\n\nNote: Full OpenCode functionality requires backend server. This is a demo client."
        }
    }
    
    private fun callOpenCodeApi(content: String): String? {
        return try {
            val json = JSONObject().apply {
                put("model", "opencode-v1")
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", content)
                    })
                })
                put("stream", false)
            }
            
            val request = Request.Builder()
                .url("$baseUrl/chat/completions")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val jsonResponse = JSONObject(body)
                    jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun callFallbackApi(content: String): String? {
        // Try alternative APIs (OpenRouter, etc.)
        return try {
            val json = JSONObject().apply {
                put("model", "gpt-3.5-turbo")
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", "You are OpenCode, an autonomous AI coding agent. Help users write code, debug issues, and build projects.")
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", content)
                    })
                })
            }
            
            val request = Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val jsonResponse = JSONObject(body)
                    jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun generateLocalResponse(content: String): String {
        // Smart local responses when API unavailable
        val lower = content.toLowerCase()
        
        return when {
            lower.contains("hello") || lower.contains("hi") -> 
                "Hello! 👋 I'm OpenCode Mobile. I'm currently running in offline mode because the backend server is not reachable.\n\nTo get full AI capabilities, please:\n1. Check your internet connection\n2. Verify API key configuration\n3. Ensure OpenCode backend is accessible"
            
            lower.contains("code") || lower.contains("write") ->
                "I'd love to write code for you! Here's a simple example:\n\n```kotlin\n// Hello World in Kotlin\nfun main() {\n    println(\"Hello, OpenCode!\")\n}\n```\n\n**Note:** I'm currently operating in demo mode. For full code generation with context awareness, project analysis, and execution, the OpenCode backend connection is required."
            
            lower.contains("terminal") || lower.contains("command") ->
                "The Terminal feature requires full OpenCode backend.\n\nWhat you can do now:\n- Browse the file manager\n- Edit code in the editor\n- Use chat for code snippets\n\nFor command execution, please connect to OpenCode server."
            
            else -> "I understand you want to: \"$content\"\n\n**Status:** OpenCode Mobile is running in demo/offline mode.\n\n**Capabilities limited:**\n❌ Real AI responses\n❌ Terminal command execution\n❌ File system operations\n❌ Code execution\n\n**Working features:**\n✅ Chat interface\n✅ Code display with syntax highlighting\n✅ File browser (read-only)\n✅ Code editor\n\nTo unlock full OpenCode power, connect to backend server with valid API key."
        }
    }
    
    suspend fun executeCommand(command: String): String {
        return try {
            val json = JSONObject().apply {
                put("command", command)
                put("cwd", "/data/data/ai.opencode.mobile/files")
            }
            
            val request = Request.Builder()
                .url("$baseUrl/execute")
                .addHeader("Authorization", "Bearer $apiKey")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string() ?: "No output"
                } else {
                    "Error: ${response.code}"
                }
            }
        } catch (e: Exception) {
            "Command execution requires OpenCode backend. Error: ${e.message}"
        }
    }
}
