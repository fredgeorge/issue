package com.nrkei.project.issue.unit

// Sample Jackson use with annotations

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
interface Animal {
    val name: String
}

data class Dog(override val name: String, val barkVolume: Double) : Animal
data class Cat(override val name: String, val likesCatnip: Boolean) : Animal

inline fun <reified T> ObjectMapper.writeTypedValueAsString(value: T): String =
    writerFor(jacksonTypeRef<T>()).writeValueAsString(value)

class PolymorphicTest {
    val mapper = jacksonObjectMapper().apply {
        registerSubtypes(NamedType(Dog::class.java, "dog"))
        registerSubtypes(NamedType(Cat::class.java, "cat"))
    }

    @Test
    fun `should serialize and deserialize polymorphic animals`() {
        val animals: List<Animal> = listOf(
            Dog("Buddy", 10.5),
            Cat("Mittens", true),
        )

        // Serialize animals. Since we serialize the parent interface, serialization is polymorphic
        val json = mapper.writeTypedValueAsString(animals)
        println(json)

        // Verify JSON contains the "type" discriminator
        assert(json.contains("\"type\":\"dog\""))
        assert(json.contains("\"type\":\"cat\""))

        val decoded: List<Animal> = mapper.readValue(json)

        assertEquals(2, decoded.size)
        assert(decoded[0] is Dog)
        assertEquals("Buddy", (decoded[0] as Dog).name)
        assert(decoded[1] is Cat)
    }
}