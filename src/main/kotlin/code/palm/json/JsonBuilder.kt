package code.palm.json

/**
 * Constructs a JsonObject using the curly-brace syntax of a lambda.
 * Within the body use 'String to Any?' for any object to add the mapping to
 * this JsonObject.
 */
fun jsonObj(body: JsonObject.() -> Unit): JsonObject {
    val obj = JsonObject()
    obj.body()
    return obj
}

/**
 * Constructs a JsonObject from a vararg of String -> Any? mappings.
 * Useful if an array of mappings already exists.
 */
fun jsonObj(vararg pairs: Pair<String, Any?>): JsonObject {
    return JsonObject().apply { set(*pairs) }
}

/**
 * Constructs a JsonArray.
 * Arguments can be any number of any type of object.
 */
fun jsonArray(vararg values: Any?): JsonArray {
    return JsonArray().apply { add(*values) }
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

//------------------------------------------------------------
// Node types

/**
 * Base class for a JSON node/value
 */
abstract class JsonNode

/**
 * An Object that contains String -> JSON value mappings
 */
class JsonObject : JsonNode() {
    private val elements = linkedMapOf<String, JsonNode>()

    /**
     * Shortcut for mapping a String to any value within this JsonObject
     */
    infix fun String.to(value: Any?) {
        elements[this] = node(value)
    }

    fun set(vararg pairs: Pair<String, Any?>) {
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

/**
 * An Array that contains a series of JSON values
 */
class JsonArray : JsonNode() {
    private val elements = mutableListOf<JsonNode>()

    fun add(vararg values: Any?) {
        values.forEach { elements += node(it) }
    }

    override fun toString(): String {
        return elements.asSequence().joinToString(
            separator = ",",
            prefix = "[",
            postfix = " ]"
        ) { " $it" }
    }
}

/**
 * Represents a JSON string value.
 * Will automatically ensure proper escape values for control characters.
 */
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