/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

// Understands something aberrant in a process
abstract class Issue(private val raisedBy: IssueParty) {
    abstract val issueType: IssueType

    private lateinit var closedBy: IssueParty
    private var state: State = IssueState.OPEN

    companion object {
        internal fun Iterable<Issue>.filter(state: IssueState) =
            this.filter { it.state == state }
    }

    fun be(state: State, closedBy: IssueParty) = state.be(state, closedBy, this)

    sealed interface State {
        fun be(state: State, closedBy: IssueParty, issue: Issue) {
            throw IllegalStateException("Cannot change state of a completed Issue")
        }
    }

    enum class IssueState : State {
        OPEN {
            override fun be(state: State, closedBy: IssueParty, issue: Issue) {
                issue.state = state.also {
                    require(state != this) { "Issue is already open" }
                    issue.closedBy = closedBy
                }

            }
        },
        RESOLVED,
        DISMISSED
    }
}