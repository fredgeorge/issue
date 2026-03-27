/*
 *  Copyright (c) 2000-2026 by Fred George
 *  May be used freely except for training; license required for training.
 *  @author Fred George  fredgeorge@acm.org
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Issue.IssueState
import com.nrkei.project.issue.Issue.Companion.filter
import com.nrkei.project.issue.Issue.Companion.filterByState

// Understands aberrations in a process
class IssueSet {
    private val issues = mutableMapOf<IssueType<*>, MutableSet<Issue<*>>>()

    fun <I: Issue<I>>raise(issue: I) =
        issues.getOrPut(issue.issueType) { mutableSetOf() }.add(issue)

    fun issues(state: IssueState) = issues.flatMap { it.value }.filterByState(state)

    fun <I : Issue<I>> issues(issueType: IssueType<I>): List<I> =
        issues[issueType].orEmpty().map {
            @Suppress("UNCHECKED_CAST")
            it as I
        }

    fun <I : Issue<I>> issues(issueType: IssueType<I>, state: Issue.State): List<I> =
        issues(issueType).filter(state)

    fun <I : Issue<I>> issues(state: Issue.State, issueType: IssueType<I>) =
        issues(issueType, state)
}