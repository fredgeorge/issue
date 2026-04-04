/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import kotlinx.serialization.Polymorphic
import java.util.*

// Understands something aberrant in a process
abstract class Issue<I : Issue<I>>(
    protected val raisedBy: IssueParty,
    protected var state: State,
    protected var closedBy: IssueParty? = null
) {
    abstract val issueType: IssueType<I>

    companion object {
        internal fun <I : Issue<I>> Iterable<I>.filter(state: State) =
            this.filter { it.state == state }

        internal fun Iterable<Issue<*>>.filterByState(state: State) =
            this.filter { it.state == state }
    }

    override fun equals(other: Any?) =
        this === other || (other is Issue<*> && this.equals(other))

    private fun equals(other: Issue<*>): Boolean {
        return this.raisedBy == other.raisedBy &&
                this.issueType == other.issueType
    }

    override fun hashCode() = Objects.hash(raisedBy, state, closedBy)

    fun be(newState: State, closedBy: IssueParty) {
        state = state.nextState(newState)
        this.closedBy = closedBy
    }

    abstract fun <I: Issue<I>> toDto(): IssueDto<I>

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

// Understands a raw representation of an Issue
@Polymorphic
interface IssueDto<I>{
    val raisedBy: String
    val state: Issue.State
    val closedBy: String?
}
