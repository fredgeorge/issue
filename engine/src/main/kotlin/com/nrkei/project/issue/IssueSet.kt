/*
 *  Copyright (c) 2000-2026 by Fred George
 *  May be used freely except for training; license required for training.
 *  @author Fred George  fredgeorge@acm.org
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Issue.IssueState
import com.nrkei.project.issue.Issue.Companion.filter

// Understands abberations in a process
class IssueSet {
    private val issues = mutableMapOf<IssueType, MutableSet<Issue>>()

    fun raise(issue: Issue) =
        issues.getOrPut(issue.issueType) { mutableSetOf() }.add(issue)

    fun issues(state: IssueState) = issues.flatMap { it.value }.filter(state)
}