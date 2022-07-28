package com.mx.hush.core.helpers

import com.mx.hush.core.models.*
import org.apache.commons.validator.routines.UrlValidator

class InputPrompt(private var prompt: String) {

    private var validOptions: Array<String>? = null
    private var color: Color = Color.YELLOW
    private var default: String? = null
    private var type: PromptType = PromptType.TEXT

    private var inputText: String = ""

    fun prompt(): InputPrompt {
        println(color.colorizeText("$prompt ${getHelperText()}"))
        var inputText = readLine().toString()

        if (default != null && inputText.isBlank()) {
            println("Defaulting to $default")
            inputText = default as String
        }

        if (!isValidInput(inputText)) {
            println(red("Invalid input."))
            prompt()
            return this
        }

        this.inputText = inputText

        return this
    }

    fun getBoolean(): Boolean {
        @OptIn(ExperimentalStdlibApi::class)
        return inputText.lowercase() == "y"
    }

    fun getString(): String {
        return inputText
    }

    fun setDefault(default: Boolean): InputPrompt {
        this.default = if (default) "y" else "n"

        return this
    }

    fun setDefault(default: String): InputPrompt {
        this.default = default

        return this
    }

    fun setValidOptions(options: Array<String>): InputPrompt {
        validOptions = options

        return this
    }

    fun setColor(color: Color): InputPrompt {
        this.color = color

        return this
    }

    fun setType(type: PromptType): InputPrompt {
        this.type = type

        return this
    }

    private fun getHelperText(): String {
        if (type == PromptType.BOOLEAN) {
            val y = if (default == null || default != "y") "y" else cyan("Y")
            val n = if (default == null || default != "n") "n" else cyan("N")

            return none("($y/$n)")
        }

        if (validOptions != null) {
            return none("(" + validOptions!!.joinToString("/") + ")").replace(default!!, cyan(default!!))
        }

        return ""
    }

    private fun isValidInput(input: String): Boolean {
        if (input.isBlank()) {
            return false
        }

        @OptIn(ExperimentalStdlibApi::class)
        if (input.lowercase() != "y" && input.lowercase() != "n" && type == PromptType.BOOLEAN) {
            return false
        }

        if (type == PromptType.URL && !UrlValidator().isValid(input)) {
            return false
        }

        @OptIn(ExperimentalStdlibApi::class)
        if (validOptions != null && !validOptions!!.contains(input.lowercase())) {
            return false
        }

        return true
    }

    enum class PromptType {
        TEXT, BOOLEAN, URL
    }

    enum class Color {
        CYAN {
            override fun colorizeText(text: String): String {
                return cyan(text)
            }
        },
        RED {
            override fun colorizeText(text: String): String {
                return red(text)
            }
        },
        YELLOW {
            override fun colorizeText(text: String): String {
                return yellow(text)
            }
        },
        GREEN {
            override fun colorizeText(text: String): String {
                return green(text)
            }
        },
        WHITE {
            override fun colorizeText(text: String): String {
                return none(text)
            }
        };

        abstract fun colorizeText(text: String): String
    }
}