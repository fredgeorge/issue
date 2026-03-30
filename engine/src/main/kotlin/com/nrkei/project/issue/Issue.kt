/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Issue.State.OPEN

// Understands something aberrant in a process
abstract class Issue<I : Issue<I>>(private val raisedBy: IssueParty) {
    abstract val issueType: IssueType<I>

    private var closedBy: IssueParty? = null
    private var state: State = OPEN

    companion object {
        internal fun <I : Issue<I>> Iterable<I>.filter(state: State) =
            this.filter { it.state == state }

        internal fun Iterable<Issue<*>>.filterByState(state: State) =
            this.filter { it.state == state }
    }

    fun be(newState: State, closedBy: IssueParty) {
        state = state.nextState(newState)
        this.closedBy = closedBy
    }

    fun accept(visitor: IssueVisitor) = visitor.visit(this, issueType)

    override fun toString() =
        "${this.javaClass.simpleName} in state $state raised by ${raisedBy.name}${closedBy?.let { " closed by ${it.name}" } ?: ""}"

    enum class State {
        OPEN,
        RESOLVED,
        DISMISSED,
        ;

        internal fun nextState(newState: State) = newState.also { newState ->
                check(newState != OPEN && this == OPEN) { "Cannot transition from $this to $newState" }
            }
    }
}

// Understands someont or something that can raise or resolve an issue
data class IssueParty(val name: String)

// Understands classifications of Issues
interface IssueType<I : Issue<I>>
