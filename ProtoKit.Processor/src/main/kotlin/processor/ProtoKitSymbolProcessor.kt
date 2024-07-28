package org.mattshoe.shoebox.processor

import com.github.mustachejava.DefaultMustacheFactory
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import io.github.mattshoe.shoebox.stratify.StratifySymbolProcessor
import io.github.mattshoe.shoebox.stratify.ksp.StratifyResolver
import io.github.mattshoe.shoebox.stratify.strategy.AnnotationStrategy
import io.github.mattshoe.shoebox.stratify.strategy.Strategy
import org.mattshoe.shoebox.annotations.ProtoMessage
import java.io.StringWriter

class ProtoKitSymbolProcessor: StratifySymbolProcessor() {
    override suspend fun buildStrategies(resolver: StratifyResolver): List<Strategy<KSNode, out KSNode>> {
        return listOf(
            AnnotationStrategy(
                ProtoMessage::class,
                ProtoMessageProcessor(resolver)
            )
        )
    }
}