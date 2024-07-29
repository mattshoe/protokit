package org.mattshoe.shoebox

import org.mattshoe.shoebox.annotations.ProtoField
import org.mattshoe.shoebox.annotations.ProtoMessage
import org.mattshoe.shoebox.annotations.Scalar

@ProtoMessage(name = "ExampleMessage")
data class Example(
    val name: String,
    val id: Int
)

fun main() {
    println("Hello World!")
}