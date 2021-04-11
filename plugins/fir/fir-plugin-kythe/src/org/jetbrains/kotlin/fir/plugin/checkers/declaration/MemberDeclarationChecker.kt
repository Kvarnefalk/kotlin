/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.plugin.checkers.declaration

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirMemberDeclarationChecker
import org.jetbrains.kotlin.fir.analysis.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.declarations.FirMemberDeclaration
import org.jetbrains.kotlin.fir.declarations.containerSource
import org.jetbrains.kotlin.fir.plugin.FactEmitter
import org.jetbrains.kotlin.fir.plugin.VName
import org.jetbrains.kotlin.fir.plugin.VNameSignature
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.kotlin.fir.realPsi

class MemberDeclarationChecker(val emitter: FactEmitter): FirMemberDeclarationChecker() {
    override fun check(declaration: FirMemberDeclaration, context: CheckerContext, reporter: DiagnosticReporter) {
        val filePath = declaration.source.psi?.containingFile?.containingDirectory?.name + declaration.source.psi?.containingFile?.name
        val test = declaration.source.psi?.containingFile?.virtualFile
        val t2 = declaration.source.psi?.containingFile?.parent
        val t3 = declaration.source.psi?.containingFile?.originalFile?.containingDirectory
        println("${test}, ${t2}, ${t3}")
        val nodeVName = VName(VNameSignature.generate(declaration), "", "", filePath, "kotlin")
        emitter.emitFact(nodeVName, "testFactName", "testFactValue")
    }
}

