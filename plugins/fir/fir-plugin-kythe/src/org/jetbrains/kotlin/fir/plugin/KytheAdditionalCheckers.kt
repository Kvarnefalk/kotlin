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

class KytheAdditionalCheckers(session: FirSession) : FirAdditionalCheckersExtension(session) {
    override val key: FirPluginKey
        get() = KythePluginKey

    override val declarationCheckers: DeclarationCheckers = object : DeclarationCheckers() {
        override val memberDeclarationCheckers: Set<FirMemberDeclarationChecker> = setOf(MemberDeclarationChecker)
        override val basicDeclarationCheckers: Set<FirBasicDeclarationChecker> = setOf(BasicDeclarationChecker)
        override val functionCheckers: Set<FirFunctionChecker> = setOf(FunctionChecker)
        override val propertyCheckers: Set<FirPropertyChecker> = setOf(PropertyChecker)
        override val classCheckers: Set<FirClassChecker> = setOf(ClassChecker)
        override val regularClassCheckers: Set<FirRegularClassChecker> = setOf(RegularClassChecker)
        override val constructorCheckers: Set<FirConstructorChecker> = setOf(ConstructorChecker)
        override val fileCheckers: Set<FirFileChecker> = setOf(FileChecker)
        override val controlFlowAnalyserCheckers: Set<FirControlFlowChecker> = setOf(ControlFlowChecker)
        override val variableAssignmentCfaBasedCheckers: Set<AbstractFirPropertyInitializationChecker> = setOf(VariableAssignmentCfaBasedChecker)
    }

    override val expressionCheckers: ExpressionCheckers = object: ExpressionCheckers() {
        override val basicExpressionCheckers: Set<FirBasicExpressionChecker> = setOf(BasicExpressionChecker)
        override val qualifiedAccessCheckers: Set<FirQualifiedAccessChecker> = setOf(QualifiedAccessChecker)
        override val functionCallCheckers: Set<FirFunctionCallChecker> = setOf(FunctionCallChecker)
        override val variableAssignmentCheckers: Set<FirVariableAssignmentChecker> = setOf(VariableAssignmentChecker)
        override val tryExpressionCheckers: Set<FirTryExpressionChecker> = setOf(TryExpressionChecker)
        override val whenExpressionCheckers: Set<FirWhenExpressionChecker> = setOf(WhenExpressionChecker)
    }

}
