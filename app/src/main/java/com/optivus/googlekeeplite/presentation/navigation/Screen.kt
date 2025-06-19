package com.optivus.googlekeeplite.presentation.navigation


sealed class Screen(val route: String) {
    object NoteList : Screen("note_list_screen")
    object AddEditNote : Screen("add_edit_note_screen")
    object Settings : Screen("settings_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withOptionalArgs(vararg args: Pair<String, String>): String {
        return buildString {
            append(route)
            if (args.isNotEmpty()) {
                append("?")
                args.forEachIndexed { index, (key, value) ->
                    if (index > 0) append("&")
                    append("$key=$value")
                }
            }
        }
    }
}