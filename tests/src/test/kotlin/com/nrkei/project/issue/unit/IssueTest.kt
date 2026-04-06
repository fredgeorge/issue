/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.*
import com.nrkei.project.issue.Issue.State.*
import com.nrkei.project.issue.TestIssue1.TestIssue1Type
import com.nrkei.project.issue.util.assertStates
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

// Ensures Issue works correctly
internal class IssueTest {
    private val issueSet = IssueSet()

    @Test
    fun `can create and find Issues`() {
        issueSet.apply {
            TestIssue1("A").also { issue ->
                assertEquals(0, issues(OPEN).size)
                assertEquals(0, issues(TestIssue1Type).size)
                assertEquals(0, issues(TestIssue1Type, OPEN).size)
                assertEquals(0, issues(OPEN, TestIssue1Type).size)
                raise(issue)
                assertEquals(1, issues(OPEN).size)
                assertEquals(1, issues(TestIssue1Type).size)
                assertEquals(1, issues(TestIssue1Type, OPEN).size)
                issues(OPEN, TestIssue1Type).also { issues ->
                    assertEquals(1, issues.size)
                    assertEquals("A", issues.first().description)
                }
            }
        }
    }

    @Test
    fun `can create and close Issues - cannot close an Issue more than once`() {
        issueSet.apply {
            IssueParty(javaClass.simpleName).also { raisedBy ->
                raise(TestIssue1("A"))
                raise(TestIssue1("A"))
                val issue1B = raise(TestIssue1("B"))
                val issue1C = raise(TestIssue1("C"))
                raise(TestIssue2("A"))
                raise(TestIssue2("B"))
                raise(TestIssue2("B"))
                raise(TestIssue2("C"))
                raise(TestIssue2("C"))
                assertStates(OPEN to 6, RESOLVED to 0, DISMISSED to 0)
                issue1B.be(DISMISSED, raisedBy)
                issue1C.be(RESOLVED, raisedBy)
                assertStates(OPEN to 4, RESOLVED to 1, DISMISSED to 1)
                assertThrows<IllegalStateException> { issue1B.be(RESOLVED, raisedBy) }
                assertThrows<IllegalStateException> { issue1C.be(DISMISSED, raisedBy) }
                assertThrows<IllegalStateException> { issue1C.be(OPEN, raisedBy) }
                println(issueSet)
            }
        }
    }

    @Test
    fun `resolved Issue not replaced by new Issue`() {
        issueSet.apply {
            val originalIssue = raise(TestIssue1("A"))
            originalIssue.be(RESOLVED, TestParty)
            raise(TestIssue1("A"))
            assertStates(OPEN to 1, RESOLVED to 1, DISMISSED to 0)
        }
    }

    private fun assertStates(vararg stateCounts: Pair<Issue.State, Int>) {
        assertStates(issueSet, *(stateCounts))
    }
}