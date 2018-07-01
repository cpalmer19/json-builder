package code.palm.json

import org.junit.Assert.*
import org.junit.Test

class JsonBuilderTest {
    @Test
    fun `jsonObj simpleValues`() {
        val json = jsonObj {
            "foo" to "bar"
            "num" to 42
            "pi" to 3.14
            "working" to false
            "something" to node(null)
        }

        assertEquals(
            """{ "foo": "bar", "num": 42, "pi": 3.14, "working": false, "something": null }""",
            json.toString()
        )
    }

    @Test
    fun `jsonObj nested jsonObj`() {
        val json = jsonObj {
            "obj" to jsonObj {
                "foo" to "bar"
            }
        }

        assertEquals(
            """{ "obj": { "foo": "bar" } }""",
            json.toString()
        )
    }

    @Test
    fun `jsonObj nested jsonArr`() {
        val json = jsonObj {
            "arr" to jsonArray(
                "foo"
            )
        }

        assertEquals(
            """{ "arr": [ "foo" ] }""",
            json.toString()
        )
    }

    @Test
    fun `jsonArray simpleValues`() {
        val json = jsonArray(
            "foo",
            42,
            3.14159,
            true,
            null
        )

        assertEquals(
            """[ "foo", 42, 3.14159, true, null ]""",
            json.toString()
        )
    }

    @Test
    fun `jsonArray nested jsonObj`() {
        val json = jsonArray(
            jsonObj {
                "foo" to "bar"
            },
            jsonObj {
                "foo" to "bar2"
            }
        )

        assertEquals(
            """[ { "foo": "bar" }, { "foo": "bar2" } ]""",
            json.toString()
        )
    }

    @Test
    fun `jsonArray nested jsonArray`() {
        val json = jsonArray(
            jsonArray(
                42,
                50
            ),
            jsonArray(
                "foo",
                "bar"
            )
        )

        assertEquals(
            """[ [ 42, 50 ], [ "foo", "bar" ] ]""",
            json.toString()
        )
    }

    @Test
    fun `jsonObj nested arrayOf`() {
        val json = jsonObj {
            "foo" to arrayOf(1, 2, 3)
        }

        assertEquals(
            """{ "foo": [ 1, 2, 3 ] }""",
            json.toString()
        )
    }

    @Test
    fun `jsonArray nested arrayOf`() {
        val json = jsonArray(
            arrayOf(1, 2, 3)
        )

        assertEquals(
            """[ [ 1, 2, 3 ] ]""",
            json.toString()
        )
    }

    @Test
    fun `jsonArray emptyArray`() {
        val json = jsonArray()
        assertEquals("[ ]", json.toString())
    }

    @Test
    fun `jsonObj emptyObject`() {
        val json = jsonObj { }
        assertEquals("{ }", json.toString())
    }
}