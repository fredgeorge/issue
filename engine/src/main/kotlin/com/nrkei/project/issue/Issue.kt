/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.nrkei.project.issue.Issue.State.OPEN
import java.util.*

// Understands something aberrant in a process
abstract class Issue<I : Issue<I>>(
    protected val raisedBy: IssueParty,
    protected var state: State,
    protected var closedBy: IssueParty? = null
) {
    constructor(raisedBy: IssueParty) : this(raisedBy, OPEN)

    abstract val issueType: IssueType<I>


    override fun equals(other: Any?) =
        this === other || (other is Issue<*> && this.equals(other))

    private fun equals(other: Issue<*>): Boolean {
        return this.raisedBy == other.raisedBy &&
                this.state == other.state
                && this.closedBy == other.closedBy
    }

    override fun hashCode() = Objects.hash(raisedBy, state, closedBy)

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

    abstract fun <I: Issue<I>> dto(): IssueDto<I>

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

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
// Understands a raw representation of an Issue
interface IssueDto<I>{
    val raisedBy: String
    val state: Issue.State
    val closedBy: String?
    fun <I : Issue<I>> toIssue(): Issue<*>
}
