package id.asistenrakyat.utils

data class ChatMessage (val message: String, val type: Type, val isLoading: Boolean = false) {
    enum class Type {
        USER,
        BOT
    }
}