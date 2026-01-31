package ai.opencode.mobile.model

sealed class Message {
    abstract val content: String
    abstract val timestamp: Long
    
    data class User(override val content: String, override val timestamp: Long = System.currentTimeMillis()) : Message()
    data class Assistant(override val content: String, override val timestamp: Long = System.currentTimeMillis()) : Message()
    data class System(override val content: String, override val timestamp: Long = System.currentTimeMillis()) : Message()
    data class Error(override val content: String, override val timestamp: Long = System.currentTimeMillis()) : Message()
    
    val isUser: Boolean get() = this is User
    val isAssistant: Boolean get() = this is Assistant
}
