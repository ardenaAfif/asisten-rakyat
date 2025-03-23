package id.asistenrakyat.utils

data class ChatMessage (val message: String, val type: Type) {
    enum class Type {
        USER,
        BOT
    }
}