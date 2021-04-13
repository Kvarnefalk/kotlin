/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.plugin

import kotlin.text.Charsets.UTF_8

data class VName(val signature: String, val corpus: String, val root: String, val path: String, val language: String)

interface FactEmitter {
    fun emit(source: VName, edgeKind: String?, target: VName?, factName: String?, factValue: ByteArray?)

    fun emitFact(source: VName, factName: String, factValue: ByteArray) {
        emit(source, null, null, factName, factValue)
    }

    fun emitFact(source: VName, factName: String, factValue: String) {
        emit(source, null, null, factName, factValue.toByteArray(UTF_8))
    }

    fun emitEdge(source: VName, edgeKind: String, target: VName) {
        emit(source, edgeKind, target, null, null)
    }
}

