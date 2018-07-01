# json-builder

Simple, intuitive, and general json builder for kotlin.
No external dependencies required to use it.

## Example use:

```
val json = jsonObj {
    "foo" to "bar"                // map to any string
    "pi" to 3.14                  // map to any number (int, float, long, etc.)
    "status" to true              // map to boolean
    "nothing" to null             // map to null
    "items" to arrayOf(1, 2, 3)   // map to a regular Kotlin/Java array
    "obj_array" to jsonArray(     // create an array of objects
        jsonObj {
            "foo" to "bar"
        },
        jsonObj {
            "foo" to "bar2"
        }
    )
    "mixed_items" to jsonArray(   // create an array of mixed item types
        12,
        "element number 2"
    )
}

val jsonStr = json.toString()
```

Note curly braces for jsonObj and parentheses for jsonArray.
Also note there are no commas in the object declaration, but there are commas for the array declaration.
