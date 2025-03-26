package id.asistenrakyat.adapter

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.asistenrakyat.databinding.MessageItemBinding
import id.asistenrakyat.utils.ChatMessage
import androidx.core.net.toUri

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

                    // event long click untuk menampilkan popup menu
                    binding.tvBotMessage.setOnLongClickListener {
                        showPopupMenu(it, chatMessage.message)
                        true
                    }
                }
            }
        }
        private fun showPopupMenu(view: View, message: String) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menu.apply {
                add("Salin Teks")
                add("Laporkan")
            }

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Salin Teks" -> {
                        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Pesan berhasil di salin", message)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(view.context, "Pesan disalin", Toast.LENGTH_SHORT).show()
                        true
                    }
                    "Laporkan" -> {
                        reportMessage(view.context, message)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun reportMessage(context: Context, message: String) {
            val emailIntent = Intent(Intent.ACTION_VIEW).apply {
                data = ("mailto:ardevcreations@gmail.com" +
                        "?subject=" + Uri.encode("Laporan Pesan Bot") +
                        "&body=" + Uri.encode("Pesan yang dilaporkan:\n$message")).toUri()
            }

            try {
                context.startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Gmail tidak ditemukan", Toast.LENGTH_SHORT).show()
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