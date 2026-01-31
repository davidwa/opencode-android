package ai.opencode.mobile

import ai.opencode.mobile.model.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class MessageAdapter(private var messages: List<Message>) : 
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    fun getMessages(): List<Message> = messages
    
    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }
    
    override fun getItemViewType(position: Int): Int {
        return when (messages[position].info.role) {
            "user" -> 0
            "assistant" -> 1
            "system" -> 2
            else -> 1
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layout = when (viewType) {
            0 -> R.layout.item_message_user
            1 -> R.layout.item_message_assistant
            2 -> R.layout.item_message_system
            else -> R.layout.item_message_assistant
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MessageViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position], dateFormat)
    }
    
    override fun getItemCount() = messages.size
    
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentText: TextView = itemView.findViewById(R.id.contentText)
        private val timeText: TextView? = itemView.findViewById(R.id.timeText)
        
        fun bind(message: Message, dateFormat: SimpleDateFormat) {
            contentText.text = message.getTextContent()
            timeText?.text = dateFormat.format(message.info.createdAt)
        }
    }
}
