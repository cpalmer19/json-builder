package code.palm.json

/**
 * Primary method for building a JsonObject.
 * Uses a higher-order function for the curly-brace syntax.
 * Within the body use String.to() (infix) for any object to add the
 * mapping to this JsonObject.
 */
fun jsonObj(body: JsonObject.() -> Unit): JsonObject {
    val obj = JsonObject()
    obj.body()
    return obj
}

/**
 * Secondary method for building a JsonObject.
 * Uses a vararg of String mappings to any object.
 * Useful if an array of mappings already exists.
 */
fun jsonObj(vararg pairs: Pair<String, Any?>): JsonObject {
    return JsonObject().apply {
        pairs.forEach { (k, v) -> elements[k] = node(v) }
    }
}

/**
 * Constructs a JsonArray.
 * Arguments can be any number of any type of object.
 */
fun jsonArray(vararg values: Any?): JsonArray {
    return JsonArray().apply {
        values.forEach { elements += node(it) }
    }
}

/**
 * Converts any object to a corresponding JsonNode value.
 *
 * null, string, number, and boolean values are wrapped accordingly.
 * JsonArrays and JsonObjects are left unchanged.
 * Arrays are turned into JsonArrays.
 * Any other object is converted to a JsonString using the value returned by toString().
 */
fun node(value: Any?): JsonNode {
    return when (value) {
        null -> JsonNull()
        is JsonNode -> value
        is String -> JsonString(value)
        is Number -> JsonNumber(value)
        is Boolean -> JsonBoolean(value)
        is Array<*> -> jsonArray(*value)
        else -> JsonString(value.toString())
    }
}

abstract class JsonNode

class JsonObject : JsonNode() {
    internal val elements = linkedMapOf<String, JsonNode>()

    infix fun String.to(value: Any?) {
        elements[this] = node(value)
    }

    fun members(vararg pairs: Pair<String, Any?>) {
        pairs.forEach { (k, v) -> elements[k] = node(v) }
    }

    override fun toString(): String {
        return elements.asSequence().joinToString(
            separator = ",",
            prefix = "{",
            postfix = " }"
        ) { entry -> """ "${entry.key}": ${entry.value}""" }
    }
}

class JsonArray : JsonNode() {
    internal val elements = mutableListOf<JsonNode>()

    override fun toString(): String {
        return elements.asSequence().joinToString(
            separator = ",",
            prefix = "[",
            postfix = " ]"
        ) { " $it" }
    }
}

class JsonString(private val value: String) : JsonNode() {
    override fun toString(): String = '"' + value.safeEscape() + '"'

    private fun String.safeEscape(): String {
        // TODO
        return this
    }
}

class JsonNumber(private val value: Number) : JsonNode() {
    override fun toString(): String = value.toString()
}

class JsonBoolean(private val value: Boolean) : JsonNode() {
    override fun toString(): String = value.toString()
}

class JsonNull : JsonNode() {
    override fun toString(): String = "null"
}