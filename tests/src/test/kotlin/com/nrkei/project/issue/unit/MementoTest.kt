/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.fasterxml.jackson.databind.jsontype.NamedType
import com.nrkei.project.issue.IssueSet
import com.nrkei.project.issue.mapper
import com.nrkei.project.issue.util.TestIssue1
import com.nrkei.project.issue.util.TestIssue1.TestIssue1Type
import com.nrkei.project.issue.util.TestIssue2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

// Ensures IssueSet can be serialized and deserialized
internal class MementoTest {

    @BeforeEach fun setup() {
        mapper.apply {
            registerSubtypes(NamedType(TestIssue1::class.java, "TestIssue1\$TestIssue1Dto"))
            registerSubtypes(NamedType(TestIssue2::class.java, "TestIssue2\$TestIssue2Dto"))
        }
    }

    @Disabled
    @Test
    fun `save and restore one Issue`(){
        IssueSet().also { originalSet ->
            originalSet.raise(TestIssue1("A"))
            originalSet.memento().also { memento ->
                IssueSet.restore(memento).also { restoredSet ->
                    Assertions.assertEquals(1, restoredSet.issues(TestIssue1Type).size)
                    Assertions.assertEquals(originalSet.issues(TestIssue1Type), restoredSet.issues(TestIssue1Type))
                }
            }
        }
    }
}