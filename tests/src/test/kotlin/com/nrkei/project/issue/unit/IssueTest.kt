/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.Issue
import com.nrkei.project.issue.Issue.State.DISMISSED
import com.nrkei.project.issue.Issue.State.OPEN
import com.nrkei.project.issue.Issue.State.RESOLVED
import com.nrkei.project.issue.IssueParty
import com.nrkei.project.issue.IssueSet
import com.nrkei.project.issue.IssueType
import com.nrkei.project.issue.unit.IssueTest.TestIssue1.TestIssue1Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.jvm.javaClass

// Ensures Issue works correctly
internal class IssueTest {
    companion object {
        private val TestParty: IssueParty = IssueParty("IssueTest")
    }

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
                val issue1A1 = raise(TestIssue1("A"))
                val issue1A2 = raise(TestIssue1("A"))
                val issue1B = raise(TestIssue1("B"))
                val issue1C = raise(TestIssue1("C"))
                val issue2A = raise(TestIssue2("A"))
                val issue2B1 = raise(TestIssue2("B"))
                val issue2B2 = raise(TestIssue2("B"))
                val issue2C1 = raise(TestIssue2("C"))
                val issue2C2 = raise(TestIssue2("C"))
                assertState(OPEN to 6, RESOLVED to 0, DISMISSED to 0)
                issue1B.be(DISMISSED, raisedBy)
                issue1C.be(RESOLVED, raisedBy)
                assertState(OPEN to 4, RESOLVED to 1, DISMISSED to 1)
                assertThrows<IllegalStateException> { issue1B.be(RESOLVED, raisedBy) }
                assertThrows<IllegalStateException> { issue1C.be(DISMISSED, raisedBy) }
                assertThrows<IllegalStateException> { issue1C.be(OPEN, raisedBy) }
                println(issueSet)
            }
        }
    }

    private fun assertState(vararg stateCounts: Pair<Issue.State, Int>) {
        stateCounts.forEach { (state, count) ->
            assertEquals(count, issueSet.issues(state).size)
        }
    }

    private data class TestIssue1(val description: String) : Issue<TestIssue1>(TestParty) {
        companion object TestIssue1Type : IssueType<TestIssue1>

        override val issueType: IssueType<TestIssue1> = TestIssue1Type
        override fun toString() = super.toString()  // avoid default data class toString()
    }

    private data class TestIssue2(val label: String) : Issue<TestIssue2>(TestParty) {
        companion object TestIssue2Type : IssueType<TestIssue2>

        override val issueType: IssueType<TestIssue2> = TestIssue2Type
        override fun toString() = super.toString()  // avoid default data class toString()
    }
}