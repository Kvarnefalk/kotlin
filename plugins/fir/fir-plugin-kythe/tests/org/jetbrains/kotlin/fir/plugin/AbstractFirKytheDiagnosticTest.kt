/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.plugin

import org.jetbrains.kotlin.checkers.BaseDiagnosticsTest
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.AbstractFirBaseDiagnosticsTest
import org.jetbrains.kotlin.fir.AbstractFirDiagnosticsTest
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.collectors.AbstractDiagnosticCollector
import org.jetbrains.kotlin.fir.analysis.collectors.FirDiagnosticsCollector
import org.jetbrains.kotlin.fir.analysis.diagnostics.FirDiagnostic
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.doFirResolveTestBench
import org.jetbrains.kotlin.fir.extensions.BunchOfRegisteredExtensions
import org.jetbrains.kotlin.fir.extensions.PluginServicesInitialization
import org.jetbrains.kotlin.fir.extensions.extensionService
import org.jetbrains.kotlin.fir.resolve.ScopeSession
import org.jetbrains.kotlin.fir.resolve.transformers.createAllCompilerResolveProcessors
import java.io.File

abstract class AbstractFirKytheDiagnosticTest : AbstractFirBaseDiagnosticsTest() {
    val pluginPhasesEnabled: Boolean
        get() = true

    override fun getFirExtensions(): BunchOfRegisteredExtensions {
        return TestKytheComponentRegistrar().configure()
    }

    override fun runAnalysis(testDataFile: File, testFiles: List<TestFile>, firFilesPerSession: Map<FirSession, List<FirFile>>) {
        for ((session, firFiles) in firFilesPerSession) {
            doFirResolveTestBench(
                firFiles,
                createAllCompilerResolveProcessors(session, pluginPhasesEnabled = pluginPhasesEnabled),
                gc = false
            )
        }

        val allFirFiles = firFilesPerSession.values.flatten()
        collectDiagnostics(allFirFiles)
    }

    @OptIn(PluginServicesInitialization::class)
    fun collectDiagnostics(firFiles: List<FirFile>): Map<FirFile, List<FirDiagnostic<*>>> {
        val collectors = mutableMapOf<FirSession, AbstractDiagnosticCollector>()
        val result = mutableMapOf<FirFile, List<FirDiagnostic<*>>>()
        var testPath: String? = null
        for (firFile in firFiles) {

            val session = firFile.session

            val extensions = session.extensionService.getAllExtensions()
            // TODO:
            // Generate temp file that we add to the emitter to print to
            extensions.first().let {
                if (it is TestKytheAdditionalCheckers) {
                    val checker: TestKytheAdditionalCheckers = it
                    testPath = checker.getTestEmitterFilePath()
                }
            }
            val collector = collectors.computeIfAbsent(session) { createCollector(session) }
            print(collector.toString())
            print(testPath)
            result[firFile] = collector.collectDiagnostics(firFile).toList()
            val txt = File(testPath).readText()
            print (txt)
        }
        return result
    }

    private fun createCollector(session: FirSession): AbstractDiagnosticCollector {
        return FirDiagnosticsCollector.create(session, ScopeSession()) // seems this class is obsolete, so do not care about correctness of the scope session here
    }

}
