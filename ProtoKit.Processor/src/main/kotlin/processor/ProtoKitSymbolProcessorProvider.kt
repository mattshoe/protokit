package org.mattshoe.shoebox.processor

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import io.github.mattshoe.shoebox.stratify.stratifyProvider

class ProtoKitSymbolProcessorProvider: SymbolProcessorProvider by stratifyProvider<ProtoKitSymbolProcessor>()