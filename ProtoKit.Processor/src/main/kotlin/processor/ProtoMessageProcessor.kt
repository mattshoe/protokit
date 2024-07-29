package org.mattshoe.shoebox.processor

import com.github.mustachejava.DefaultMustacheFactory
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import io.github.mattshoe.shoebox.stratify.ksp.StratifyResolver
import io.github.mattshoe.shoebox.stratify.model.GeneratedFile
import io.github.mattshoe.shoebox.stratify.processor.Processor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.mattshoe.shoebox.annotations.Scalar
import java.io.StringWriter

class ProtoMessageProcessor(
    private val resolver: StratifyResolver,
    private val logger: KSPLogger
): Processor<KSClassDeclaration> {
    override val targetClass = KSClassDeclaration::class
    val scalarMap: Map<String, Scalar> = mapOf(
        "kotlin.String" to Scalar.STRING,
        "kotlin.Int" to Scalar.INT32,
        "kotlin.Long" to Scalar.INT64,
        "kotlin.Boolean" to Scalar.BOOL,
        "kotlin.Float" to Scalar.FLOAT,
        "kotlin.Double" to Scalar.DOUBLE,
        "kotlin.ByteArray" to Scalar.BYTES,
        "kotlin.Short" to Scalar.INT32,  // Kotlin's Short mapped to int32
        "kotlin.Byte" to Scalar.INT32     // Maps Kotlin's Byte to int32 in protobuf
    )
    private val numberMutex = Mutex()

    override suspend fun process(node: KSClassDeclaration): Set<GeneratedFile> {
        val packageName = "protokit.${node.packageName.asString()}"
        val className = node.simpleName.asString()

        return setOf(
            GeneratedFile(
                packageName =  packageName,
                fileName = className,
                output = generateProtoFile(
                    node,
                    packageName,
                    className
                ),
                extension = "proto"
            )
        )
    }

    private suspend fun generateProtoFile(
        ksClass: KSClassDeclaration,
        packageName: String,
        className: String
    ): String {
        val fields = ksClass.getAllProperties().toList().map { property ->
            val name = property.simpleName.asString()
            val type = determineScalar(property)
            val number = extractFieldNumber(property)
            mapOf("name" to name, "type" to type, "number" to number)
        }.toList()

        val templateData = mapOf(
            "packageName" to packageName,
            "messageName" to className,
            "fields" to fields
        )

        logger.warn("Template Data: ${templateData.keys.joinToString { "$it: ${templateData[it]}" }}")

        val mustache = DefaultMustacheFactory().compile("protoTemplate.mustache")
        val writer = StringWriter()
        withContext(Dispatchers.IO) {
            mustache.execute(writer, templateData).flush()
        }

        return writer.toString()
    }

    private fun determineScalar(property: KSPropertyDeclaration): String {
        val kotlinType = property.type.resolve().declaration.qualifiedName?.asString().toString()
        return scalarMap[kotlinType]?.value ?: "string" // Default to string if not found
    }

    private var lineNumber = 1
    private suspend fun extractFieldNumber(property: KSPropertyDeclaration): Int {
        // Implement your logic to extract the field number, possibly from annotations
        return numberMutex.withLock {
            lineNumber++
        }
    }
}