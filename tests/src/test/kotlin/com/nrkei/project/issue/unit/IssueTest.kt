/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.Issue
import com.nrkei.project.issue.Issue.IssueState.OPEN
import com.nrkei.project.issue.IssueParty
import com.nrkei.project.issue.IssueSet
import com.nrkei.project.issue.IssueType
import com.nrkei.project.issue.unit.IssueTest.TestIssue.TestIssueType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures Issue works correctly
internal class IssueTest: IssueParty {
    override val name: String = javaClass.simpleName

    @Test fun `can create and resolve Issues`() {
        IssueSet().also { issueSet ->
            TestIssue(this, "A").also { issue ->
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

    private class TestIssue(raisedBy: IssueParty, val description: String): Issue<TestIssue>(raisedBy) {
        companion object TestIssueType: IssueType<TestIssue>

        override val issueType: IssueType<TestIssue> = TestIssueType
    }
}