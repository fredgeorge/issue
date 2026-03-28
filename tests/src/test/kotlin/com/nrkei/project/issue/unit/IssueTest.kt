/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.Issue
import com.nrkei.project.issue.Issue.IssueState.DISMISSED
import com.nrkei.project.issue.Issue.IssueState.OPEN
import com.nrkei.project.issue.Issue.IssueState.RESOLVED
import com.nrkei.project.issue.IssueParty
import com.nrkei.project.issue.IssueSet
import com.nrkei.project.issue.IssueType
import com.nrkei.project.issue.unit.IssueTest.TestIssue.TestIssueType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

// Ensures Issue works correctly
internal class IssueTest : IssueParty {
    override val name: String = javaClass.simpleName

    @Test
    fun `can create and find Issues`() {
        IssueSet().also { issueSet ->
            TestIssue("A", this).also { issue ->
                assertEquals(0, issueSet.issues(OPEN).size)
                assertEquals(0, issueSet.issues(TestIssueType).size)
                assertEquals(0, issueSet.issues(TestIssueType, OPEN).size)
                assertEquals(0, issueSet.issues(OPEN, TestIssueType).size)
                issueSet.raise(issue)
                assertEquals(1, issueSet.issues(OPEN).size)
                assertEquals(1, issueSet.issues(TestIssueType).size)
                assertEquals(1, issueSet.issues(TestIssueType, OPEN).size)
                issueSet.issues(OPEN, TestIssueType).also { issues ->
                    assertEquals(1, issues.size)
                    assertEquals("A", issues.first().description)
                }
            }
        }
    }

    @Test
    fun `can create and close Issues - cannot close an Issue more than once`() {
        IssueSet().also { issueSet ->
            val issueA = TestIssue("A", this)
            val issueB = TestIssue("B", this)
            val issueC = TestIssue("C", this)
            issueSet.raise(issueA)
            issueSet.raise(issueB)
            issueSet.raise(issueC)
            assertState(issueSet, OPEN to 3, RESOLVED to 0, DISMISSED to 0)
            issueA.be(RESOLVED, this)
            issueB.be(DISMISSED, this)
            assertState(issueSet, OPEN to 1, RESOLVED to 1, DISMISSED to 1)
            assertThrows<IllegalStateException> { issueA.be(DISMISSED, this) }
            assertThrows<IllegalStateException> { issueB.be(RESOLVED, this) }
            assertThrows<IllegalStateException> { issueC.be(OPEN, this) }
        }
    }

    private fun assertState(issueSet: IssueSet, vararg stateCounts: Pair<Issue.State, Int>) {
        stateCounts.forEach { (state, count) ->
            assertEquals(count, issueSet.issues(TestIssueType, state).size)
        }
    }

    private class TestIssue(val description: String, raisedBy: IssueParty) : Issue<TestIssue>(raisedBy) {
        companion object TestIssueType : IssueType<TestIssue>

        override val issueType: IssueType<TestIssue> = TestIssueType
    }
}