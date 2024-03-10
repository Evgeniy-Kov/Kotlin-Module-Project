fun main() {

    //    showMenuWithoutLambda(ItemType.ArchivesList, archives)

    showMenuWithLambda(
        ItemType.ArchivesList,
        archives,
        selectItem = { selectedArchive ->
            val archive = selectedArchive as Archive
            showMenuWithLambda(
                itemType = ItemType.NotesList,
                listOfItems = archive.notes,
                selectItem = { selectedNote ->
                    val note = selectedNote as Note
                    showNote(note)
                },
                createItem = { createNote(archive.notes) },
            )
        },
        createItem = { createArchive() },
    )
}


val archives: MutableList<Archive> = mutableListOf()


fun createArchive() {
    val newArchiveName = getStringFromInput("Введите название архива:")
    archives.add(Archive(newArchiveName))
}


fun createNote(notes: MutableList<Note>) {
    val newNoteName = getStringFromInput("Введите название заметки:")
    val newNoteText = getStringFromInput("Введите текст заметки:")

    notes.add(Note(newNoteName, newNoteText))
}


fun getStringFromInput(requestMessage: String): String {
    var input: String
    while (true) {
        println(requestMessage)
        input = readln()
        if (input.isNotBlank()) break
        println("Строка не может быть пустой. Повторите ввод.")
    }
    return input
}


fun showMenuWithoutLambda(itemType: ItemType, listOfItems: List<MenuItem>) {
    while (true) {
        println(
            """
            Список ${itemType.listOf}:
            -1. Выход
             0. Создать ${itemType.newItem}
        """.trimIndent()
        )
        listOfItems.forEachIndexed { index, menuItem ->
            println(" ${index + 1} ${menuItem.name}")
        }
        println("Введите номер пункта меню:")
        when (val input = readln().toIntOrNull()) {
            null -> println(
                """
                Введенное значение не является числом.
                Необходимо ввести значение от -1 до ${listOfItems.size}
            """.trimIndent()
            )

            -1 -> break

            0 -> when (itemType) {
                ItemType.ArchivesList -> createArchive()
                ItemType.NotesList -> {
                    createNote(listOfItems as MutableList<Note>)
                }
            }

            in 1..listOfItems.size -> {
                when (itemType) {
                    ItemType.ArchivesList -> {
                        val archive = listOfItems[input - 1] as Archive
                        showMenuWithoutLambda(ItemType.NotesList, archive.notes)
                    }

                    ItemType.NotesList -> {
                        val note = listOfItems[input - 1] as Note
                        showNote(note)
                    }
                }
            }

            else -> {
                println(
                    """
                    Пункта с номером $input не существует.
                    Необходимо ввести значение от -1 до ${listOfItems.size}
                """.trimIndent()
                )
            }
        }

    }
}


fun showMenuWithLambda(
    itemType: ItemType,
    listOfItems: List<MenuItem>,
    selectItem: (MenuItem) -> Unit,
    createItem: () -> Unit
) {
    while (true) {
        println(
            """
            Список ${itemType.listOf}:
            -1. Выход
             0. Создать ${itemType.newItem}
        """.trimIndent()
        )
        listOfItems.forEachIndexed { index, menuItem ->
            println(" ${index + 1}. ${menuItem.name}")
        }

        println("Введите номер пункта меню:")

        when (val input = readln().toIntOrNull()) {
            null -> println(
                """
                Введенное значение не является числом.
                Необходимо ввести значение от -1 до ${listOfItems.size}
            """.trimIndent()
            )

            -1 -> break
            0 -> createItem.invoke()
            in 1..listOfItems.size -> {
                val index = input - 1
                selectItem.invoke(listOfItems[index])
            }

            else -> {
                println(
                    """
                    Пункта с номером $input не существует.
                    Необходимо ввести значение от -1 до ${listOfItems.size}
                """.trimIndent()
                )
            }
        }

    }
}


fun showNote(note: Note) {
    println(
        """
        Заметка: ${note.name}
        
        ${note.text}
        
        Для возврата в предыдущее меню, введите любой символ.
    """.trimIndent()
    )
    readln()
}