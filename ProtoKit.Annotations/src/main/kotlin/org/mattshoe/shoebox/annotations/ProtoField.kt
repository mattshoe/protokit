package org.mattshoe.shoebox.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ProtoField(
    val number: Int,
    val type: Scalar
)