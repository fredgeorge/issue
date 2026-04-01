/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.util

import com.nrkei.project.issue.Issue
import com.nrkei.project.issue.Issue.State.OPEN
import com.nrkei.project.issue.IssueDto
import com.nrkei.project.issue.IssueParty
import com.nrkei.project.issue.IssueType
import java.util.Objects

internal class TestIssue1(
    val description: String,
    raisedBy: IssueParty,
    state: State,
    closedBy: IssueParty?
) : Issue<TestIssue1>(raisedBy, state, closedBy) {
    companion object TestIssue1Type : IssueType<TestIssue1>

    constructor(description: String) : this(description, TestParty, OPEN, null)

    override val issueType: IssueType<TestIssue1> = TestIssue1Type

    override fun equals(other: Any?) =
        super.equals(other) && other is TestIssue1 && this.description == other.description

    override fun hashCode() = Objects.hash(super.hashCode(), description)

    override fun <I : Issue<I>> dto() =
        @Suppress("UNCHECKED_CAST")
        TestIssue1Dto(
            raisedBy.name,
            state,
            closedBy?.name,
            description
        ) as IssueDto<I>

    internal data class TestIssue1Dto(
        override val raisedBy: String,
        override val state: State,
        override val closedBy: String?,
        val description: String
    ) : IssueDto<TestIssue1> {
        override fun <I : Issue<I>> toIssue(): Issue<*> =
            TestIssue1(
                description,
                IssueParty(raisedBy),
                state,
                closedBy?.let { IssueParty(it) }
            )
    }
}

internal class TestIssue2(
    val label: String,
    raisedBy: IssueParty,
    state: State,
    closedBy: IssueParty?
) : Issue<TestIssue2>(raisedBy, state, closedBy) {
    companion object TestIssue2Type : IssueType<TestIssue2>

    constructor(label: String) : this(label, TestParty, OPEN, null)

    override val issueType: IssueType<TestIssue2> = TestIssue2Type

    override fun equals(other: Any?) =
        super.equals(other) && other is TestIssue2 && this.label == other.label

    override fun hashCode() = Objects.hash(super.hashCode(), label)

    @Suppress("UNCHECKED_CAST")
    override fun <I : Issue<I>> dto() = TestIssue2Dto(
        raisedBy.name,
        state,
        closedBy?.name,
        label
    ) as IssueDto<I>

    internal data class TestIssue2Dto(
        override val raisedBy: String,
        override val state: State,
        override val closedBy: String?,
        val label: String
    ) : IssueDto<TestIssue2> {
        override fun <I : Issue<I>> toIssue(): Issue<*> =
            TestIssue2(
                label,
                IssueParty(raisedBy),
                state,
                closedBy?.let { IssueParty(it) }
            )
    }
}

private val TestParty: IssueParty = IssueParty("IssueTest")
