/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.plugin

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.cfa.AbstractFirPropertyInitializationChecker
import org.jetbrains.kotlin.fir.analysis.checkers.cfa.FirControlFlowChecker
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.*
import org.jetbrains.kotlin.fir.analysis.checkers.expression.*
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirPluginKey
import org.jetbrains.kotlin.fir.plugin.checkers.declaration.*
import org.jetbrains.kotlin.fir.plugin.checkers.expression.*

class TestKytheAdditionalCheckers(session: FirSession) : KytheAdditionalCheckers(session) {

    private var emitter: TestFactEmitter? = null

    override fun getEmitter(): FactEmitter {
        if (emitter == null) {
            val file = java.io.File.createTempFile("test-kythe-", ".facts")
            emitter = TestFactEmitter(file.absolutePath)
        }
        return emitter!!
    }

    fun getTestEmitterFilePath(): String {
        if (emitter != null) {
            return emitter!!.getTestFilePath()
        }
        return ""
    }
}
