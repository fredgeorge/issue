/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.*
import com.nrkei.project.issue.Encoding.defaultIssueSerializers
import com.nrkei.project.issue.TestIssue1.TestIssue1Type
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures IssueSet can be serialized and deserialized
internal class MementoTest {

    private val issueSerializers = SerializersModule {
        polymorphic(IssueDto::class) {
            subclass(TestIssue1.TestIssue1Dto::class)
            subclass(TestIssue2.TestIssue2Dto::class)
        }
    }
    private val json = Json(Encoding.defaultJson) {
        serializersModule = defaultIssueSerializers + issueSerializers
    }

    @Test
    fun `save and restore one Issue`(){
        IssueSet().also { originalSet ->
            originalSet.raise(TestIssue1("A"))
            originalSet.toMemento(json).also { memento ->
                IssueSet.fromMemento(memento, json).also { restoredSet ->
                    assertEquals(1, restoredSet.issues(TestIssue1Type).size)
                    assertEquals(originalSet.issues(TestIssue1Type), restoredSet.issues(TestIssue1Type))
                }
            }
        }
    }
}