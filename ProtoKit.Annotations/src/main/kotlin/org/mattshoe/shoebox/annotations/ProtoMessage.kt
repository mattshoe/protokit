package org.mattshoe.shoebox.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ProtoMessage(
    val name: String = "",
    val packageName: String = ""
)

