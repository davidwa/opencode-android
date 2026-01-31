package ai.opencode.mobile.manager

import ai.opencode.mobile.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SessionManager private constructor() {
    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions: StateFlow<List<Session>> = _sessions
    
    private val _currentSession = MutableStateFlow<Session?>(null)
    val currentSession: StateFlow<Session?> = _currentSession
    
    private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    
    private val eventSourceFactory = EventSources.createFactory(
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS) // No timeout for SSE
            .build()
    )
    
    private var currentEventSource: EventSource? = null
    
    companion object {
        @Volatile
        private var instance: SessionManager? = null
        
        fun getInstance(): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager().also { instance = it }
            }
        }
    }
    
    fun setSessions(sessionList: List<Session>) {
        _sessions.value = sessionList
    }
    
    fun addSession(session: Session) {
        _sessions.value = _sessions.value + session
    }
    
    fun removeSession(sessionID: String) {
        _sessions.value = _sessions.value.filter { it.id != sessionID }
        if (_currentSession.value?.id == sessionID) {
            _currentSession.value = null
        }
    }
    
    fun setCurrentSession(session: Session?) {
        _currentSession.value = session
    }
    
    fun updateSessionStatus(sessionID: String, status: SessionStatus) {
        _sessions.value = _sessions.value.map { session ->
            if (session.id == sessionID) {
                session.copy(status = status)
            } else {
                session
            }
        }
        
        if (_currentSession.value?.id == sessionID) {
            _currentSession.value = _currentSession.value?.copy(status = status)
        }
    }
    
    fun getMessages(sessionID: String): List<Message> {
        return _messages.value[sessionID] ?: emptyList()
    }
    
    fun addMessage(sessionID: String, message: Message) {
        val currentMessages = _messages.value[sessionID] ?: emptyList()
        _messages.value = _messages.value + (sessionID to (currentMessages + message))
    }
    
    fun clearMessages(sessionID: String) {
        _messages.value = _messages.value - sessionID
    }
    
    fun connectToEventStream(
        serverUrl: String,
        username: String,
        password: String,
        onEvent: (ServerEvent) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        currentEventSource?.cancel()
        
        val request = Request.Builder()
            .url("$serverUrl/event")
            .addHeader("Authorization", Credentials.basic(username, password))
            .addHeader("Accept", "text/event-stream")
            .build()
        
        currentEventSource = eventSourceFactory.newEventSource(request, object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                // Connected
            }
            
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                try {
                    val json = JSONObject(data)
                    onEvent(ServerEvent(type ?: "message", json))
                } catch (e: Exception) {
                    onEvent(ServerEvent("raw", JSONObject().put("data", data)))
                }
            }
            
            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                onError(t ?: Exception("Unknown error"))
            }
            
            override fun onClosed(eventSource: EventSource) {
                // Connection closed
            }
        })
    }
    
    fun disconnectEventStream() {
        currentEventSource?.cancel()
        currentEventSource = null
    }
    
    fun clear() {
        _sessions.value = emptyList()
        _currentSession.value = null
        _messages.value = emptyMap()
        disconnectEventStream()
    }
}
