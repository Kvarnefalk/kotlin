/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.plugin

object StreamFactEmitter: FactEmitter {
    override fun emit(source: VName, edgeKind: String?, target: VName?, factName: String?, factValue: ByteArray?) {
        println()
    }

}
