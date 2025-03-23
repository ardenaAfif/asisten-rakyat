package id.asistenrakyat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.asistenrakyat.databinding.MessageItemBinding
import id.asistenrakyat.utils.ChatMessage

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val messages: MutableList<ChatMessage> = mutableListOf()

    inner class ChatViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            if (chatMessage.type == ChatMessage.Type.USER) {
                binding.tvBotMessage.visibility = View.GONE
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvMessage.text = chatMessage.message
            } else {
                binding.tvBotMessage.visibility = View.VISIBLE
                binding.tvMessage.visibility = View.GONE
                binding.tvBotMessage.text = chatMessage.message
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        return ChatViewHolder(
            MessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = messages[position]
        holder.bind(chatMessage)
    }

    override fun getItemCount(): Int = messages.size

    fun sendMessage(chatMessage: ChatMessage) {
        messages.add(chatMessage)
        notifyItemInserted(messages.size - 1)
    }

//    fun sendMessage(chatMessage: ChatMessage) {
//        val lastMessageType = if (messages.isNotEmpty()) messages.last().type else null
//
//        if (lastMessageType != ChatMessage.Type.BOT && chatMessage.type == ChatMessage.Type.BOT) {
//            messages.add(chatMessage)
//            notifyItemInserted(messages.size - 1)
//        } else if (lastMessageType == ChatMessage.Type.BOT && chatMessage.type == ChatMessage.Type.BOT) {
//            messages[messages.lastIndex] = chatMessage
//            notifyItemChanged(messages.lastIndex)
//        } else {
//            messages.add(chatMessage)
//            notifyItemInserted(messages.size - 1)
//        }
//    }
}