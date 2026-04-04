/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.*
import com.nrkei.project.issue.Encoding.defaultIssueSerializers
import com.nrkei.project.issue.Issue.State.*
import com.nrkei.project.issue.TestIssue1.TestIssue1Type
import com.nrkei.project.issue.TestIssue2.TestIssue2Type
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
    fun `save and restore one Issue`() {
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

    @Test
    fun `save and restore multiple Issues in multiple states`() {
        IssueSet().also { originalSet ->
            originalSet.raise(TestIssue1("A"))
            originalSet.raise(TestIssue1("B"))
            originalSet.raise(TestIssue2("A"))
            originalSet.raise(TestIssue2("B"))
            originalSet.issues(TestIssue1Type).first().be(RESOLVED, TestParty)
            originalSet.issues(TestIssue2Type).first().be(DISMISSED, TestParty)
            originalSet.toMemento(json).also { memento ->
                IssueSet.fromMemento(memento, json).also { restoredSet ->
                    assertEquals(2, restoredSet.issues(TestIssue1Type).size)
                    assertEquals(2, restoredSet.issues(TestIssue2Type).size)
                    assertEquals(2, restoredSet.issues(OPEN).size)
                    assertEquals(1, restoredSet.issues(RESOLVED).size)
                    assertEquals(1, restoredSet.issues(DISMISSED).size)
                    assertEquals(originalSet.issues(TestIssue1Type), restoredSet.issues(TestIssue1Type))
                }
            }
        }
    }
}