/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Issue.State.OPEN
import kotlinx.serialization.Serializable
import java.util.*

class TestIssue1 private constructor(
    val description: String,
    raisedBy: IssueParty,
    state: State,
    closedBy: IssueParty?
) : Issue<TestIssue1>(raisedBy, state, closedBy) {

    companion object TestIssue1Type : IssueType<TestIssue1>

    constructor(description: String) : this(description, TestParty, OPEN, null)

    override val issueType = TestIssue1Type

    // Necessary for sets to work
    override fun equals(other: Any?) =
        super.equals(other) && other is TestIssue1 && this.description == other.description

    override fun hashCode() = Objects.hash(super.hashCode(), description)

    @Suppress("UNCHECKED_CAST")
    override fun <I : Issue<I>> toDto() =
        TestIssue1Dto(
            raisedBy.name,
            state,
            closedBy?.name,
            description
        ) as IssueDto<I>

    @Serializable
    data class TestIssue1Dto(
        override val raisedBy: String,
        override val state: State,
        override val closedBy: String?,
        val description: String
    ) : IssueDto<TestIssue1>
}

class TestIssue2 private constructor(
    val label: String,
    raisedBy: IssueParty,
    state: State,
    closedBy: IssueParty?
) : Issue<TestIssue2>(raisedBy, state, closedBy) {
    companion object TestIssue2Type : IssueType<TestIssue2>

    constructor(label: String) : this(label, TestParty, OPEN, null)

    override val issueType = TestIssue2Type

    // Necessary for sets to work
    override fun equals(other: Any?) =
        super.equals(other) && other is TestIssue2 && this.label == other.label

    override fun hashCode() = Objects.hash(super.hashCode(), label)

    @Suppress("UNCHECKED_CAST")
    override fun <I : Issue<I>> toDto() = TestIssue2Dto(
        raisedBy.name,
        state,
        closedBy?.name,
        label
    ) as IssueDto<I>

    @Serializable
    data class TestIssue2Dto(
        override val raisedBy: String,
        override val state: State,
        override val closedBy: String?,
        val label: String
    ) : IssueDto<TestIssue2>
}

val TestParty: IssueParty = IssueParty("IssueTest")
