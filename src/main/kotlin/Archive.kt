data class Archive(override val name: String) : MenuItem {
    val notes = mutableListOf<Note>()
}