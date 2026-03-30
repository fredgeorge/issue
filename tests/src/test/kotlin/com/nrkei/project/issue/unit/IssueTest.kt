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
import com.nrkei.project.issue.unit.IssueTest.TestIssue1.TestIssue1Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

// Ensures Issue works correctly
internal class IssueTest {

    @Test
    fun `can create and find Issues`() {
        IssueSet().also { issueSet ->
            TestIssue1("A", IssueParty(javaClass.simpleName)).also { issue ->
                assertEquals(0, issueSet.issues(OPEN).size)
                assertEquals(0, issueSet.issues(TestIssue1Type).size)
                assertEquals(0, issueSet.issues(TestIssue1Type, OPEN).size)
                assertEquals(0, issueSet.issues(OPEN, TestIssue1Type).size)
                issueSet.raise(issue)
                assertEquals(1, issueSet.issues(OPEN).size)
                assertEquals(1, issueSet.issues(TestIssue1Type).size)
                assertEquals(1, issueSet.issues(TestIssue1Type, OPEN).size)
                issueSet.issues(OPEN, TestIssue1Type).also { issues ->
                    assertEquals(1, issues.size)
                    assertEquals("A", issues.first().description)
                }
            }
        }
    }

    @Test
    fun `can create and close Issues - cannot close an Issue more than once`() {
        IssueSet().also { issueSet ->
            IssueParty(javaClass.simpleName).also { raisedBy ->
                val issueA = TestIssue1("A", raisedBy)
                val issueB = TestIssue1("B", raisedBy)
                val issueC = TestIssue1("C", raisedBy)
                issueSet.raise(issueA)
                issueSet.raise(issueB)
                issueSet.raise(issueC)
                assertState(issueSet, OPEN to 3, RESOLVED to 0, DISMISSED to 0)
                issueA.be(RESOLVED, raisedBy)
                issueB.be(DISMISSED, raisedBy)
                assertState(issueSet, OPEN to 1, RESOLVED to 1, DISMISSED to 1)
                assertThrows<IllegalStateException> { issueA.be(DISMISSED, raisedBy) }
                assertThrows<IllegalStateException> { issueB.be(RESOLVED, raisedBy) }
                assertThrows<IllegalStateException> { issueC.be(OPEN, raisedBy) }
            }
        }
    }

    private fun assertState(issueSet: IssueSet, vararg stateCounts: Pair<Issue.State, Int>) {
        stateCounts.forEach { (state, count) ->
            assertEquals(count, issueSet.issues(TestIssue1Type, state).size)
        }
    }

    private class TestIssue1(val description: String, raisedBy: IssueParty) : Issue<TestIssue1>(raisedBy) {
        companion object TestIssue1Type : IssueType<TestIssue1>

        override val issueType: IssueType<TestIssue1> = TestIssue1Type
    }
}