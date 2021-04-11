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
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.doFirResolveTestBench
import org.jetbrains.kotlin.fir.extensions.BunchOfRegisteredExtensions
import org.jetbrains.kotlin.fir.resolve.transformers.createAllCompilerResolveProcessors
import java.io.File

abstract class AbstractFirKytheDiagnosticTest : AbstractFirBaseDiagnosticsTest() {
    val pluginPhasesEnabled: Boolean
        get() = true

    override fun runAnalysis(testDataFile: File, testFiles: List<BaseDiagnosticsTest.TestFile>, firFilesPerSession: Map<FirSession, List<FirFile>>) {
        for ((session, firFiles) in firFilesPerSession) {
            doFirResolveTestBench(
                firFiles,
                createAllCompilerResolveProcessors(session, pluginPhasesEnabled = pluginPhasesEnabled),
                gc = false
            )
        }
        val allFirFiles = firFilesPerSession.values.flatten()
        checkFir(testDataFile, allFirFiles)
    }

    fun checkFir(testDataFile: File, firFiles: List<FirFile>) {
        print("Hello ${testDataFile.absolutePath}, ${firFiles.toString()}")
    }

    override fun getFirExtensions(): BunchOfRegisteredExtensions {
        return KytheComponentRegistrar().configure()
    }

    override fun updateConfiguration(configuration: CompilerConfiguration) {
        super.updateConfiguration(configuration)
        val jar = File("plugins/fir/fir-plugin-kythe/plugin-annotations/build/libs/plugin-annotations-1.5.255-SNAPSHOT.jar")
        if (!jar.exists()) {
            throw AssertionError("Jar with annotations does not exist. Please run :plugins:fir:fir-plugin-prototype:plugin-annotations:jar")
        }
        configuration.addJvmClasspathRoot(jar)
    }

}
