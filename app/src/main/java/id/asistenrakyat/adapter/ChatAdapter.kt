package id.asistenrakyat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.asistenrakyat.databinding.MessageItemBinding
import id.asistenrakyat.utils.ChatMessage

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var recyclerView: RecyclerView? = null

    private val messages: MutableList<ChatMessage> = mutableListOf()

    inner class ChatViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            if (chatMessage.type == ChatMessage.Type.USER) {
                binding.tvBotMessage.visibility = View.GONE
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvMessage.text = chatMessage.message
            } else {
                binding.tvMessage.visibility = View.GONE
                if (chatMessage.isLoading){
                    binding.tvBotMessage.visibility = View.GONE
                    binding.dotsLoader.visibility = View.VISIBLE
                    binding.dotsLoader.show()
                } else{
                    binding.tvBotMessage.visibility = View.VISIBLE
                    binding.dotsLoader.visibility = View.GONE
                    binding.tvBotMessage.text = chatMessage.message
                    binding.dotsLoader.hide()
                }
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
        try {
            val chatMessage = messages[position]
            holder.bind(chatMessage)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = messages.size

    fun sendMessage(chatMessage: ChatMessage) {
        messages.add(chatMessage)
        if (messages.isNotEmpty()) {
            notifyItemInserted(messages.size - 1)
            // scroll to bottom
            recyclerView?.scrollToPosition(messages.size - 1)
        }
    }

    fun startLoading(){
        sendMessage(ChatMessage("", ChatMessage.Type.BOT, true))
    }

    fun stopLoading(message : String){
        if (messages.isNotEmpty() && messages.last().isLoading) {
            messages.removeAt(messages.size - 1)
            notifyItemRemoved(messages.size) // Gunakan notifyItemRemoved
            sendMessage(ChatMessage(message, ChatMessage.Type.BOT, false))
        } else {
            // Handle kasus di mana tidak ada item loading untuk dihapus
            sendMessage(ChatMessage(message, ChatMessage.Type.BOT, false))
        }
    }
}