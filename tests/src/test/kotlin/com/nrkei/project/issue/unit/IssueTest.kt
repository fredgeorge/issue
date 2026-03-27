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
import com.nrkei.project.issue.unit.IssueTest.TestIssue.Companion.TEST_ISSUE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures Issue works correctly
internal class IssueTest: IssueParty {

    @Test fun `can create and resolve Issues`() {
        IssueSet().also { issueSet ->
            TestIssue(this, "A").also { issue ->
                assertEquals(0, issueSet.issues(OPEN).size)
                assertEquals(0, issueSet.issues(TEST_ISSUE).size)
                assertEquals(0, issueSet.issues(TEST_ISSUE, OPEN).size)
                assertEquals(0, issueSet.issues(OPEN, TEST_ISSUE).size)
                issueSet.raise(issue)
                issueSet.issues(OPEN).also { issues ->
                    assertEquals(1, issues.size)
                    assertEquals(1, issueSet.issues(TEST_ISSUE).size)
                    assertEquals(1, issueSet.issues(TEST_ISSUE, OPEN).size)
                    assertEquals(1, issueSet.issues(OPEN, TEST_ISSUE).size)
                    assertEquals("A", (issues as List<TestIssue>).first().description)
                }
            }
        }
    }

    private class TestIssue(raisedBy: IssueParty, val description: String): Issue(raisedBy) {
        companion object {
            val TEST_ISSUE = object : IssueType {}
        }

        override val issueType = TEST_ISSUE
    }
}