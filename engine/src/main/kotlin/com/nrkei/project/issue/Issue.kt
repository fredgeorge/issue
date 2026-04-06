/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import kotlinx.serialization.Polymorphic
import java.util.*

/**
 * Represents an aberration in a process.
 *
 * An [Issue] tracks who raised it, its current [State], and optionally who closed it.
 * Concrete issue types should subclass this and provide a specific [issueType] and DTO mapping.
 */
abstract class Issue<I : Issue<I>>(
    protected val raisedBy: IssueParty,
    protected var state: State,
    protected var closedBy: IssueParty? = null
) {
    /**
     * The classification of this issue.
     */
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
                this.issueType == other.issueType &&
                this.state == other.state &&
                this.closedBy == other.closedBy
    }

    // Base hashCode on only immutable properties
    override fun hashCode() = Objects.hash(raisedBy, issueType)

    /**
     * Changes the issue to a new state and records who closed it.
     *
     * @param newState the new state to transition to
     * @param closedBy the party responsible for closing the issue
     */
    fun be(newState: State, closedBy: IssueParty) {
        state = state.nextState(newState)
        this.closedBy = closedBy
    }

    /**
     * Converts this issue into a serializable DTO.
     */
    abstract fun <I: Issue<I>> toDto(): IssueDto<I>

    /**
     * Accepts a visitor for traversing the issue graph.
     *
     * @param visitor the visitor to notify
     */
    fun accept(visitor: IssueVisitor) = visitor.visit(this, issueType)

    override fun toString() =
        "${this.javaClass.simpleName} in state $state raised by ${raisedBy.name}${closedBy?.let { " closed by ${it.name}" } ?: ""}"

    enum class State {
        OPEN,
        RESOLVED,
        DISMISSED,
        ;

        internal fun nextState(newState: State) = newState.also {
                check(this == OPEN) { "Cannot transition from $this" }
                check(newState != OPEN) { "Cannot re-open an Issue" }
            }
    }
}

/**
 * Identifies a person or system that can raise or resolve an [Issue].
 *
 * @property name the display name of the party
 */
data class IssueParty(val name: String)

/**
 * Marker for a specific issue classification.
 *
 * @param I the concrete issue type
 */
interface IssueType<I : Issue<I>>

/**
 * Serializable DTO representation of an [Issue].
 *
 * @param I the corresponding issue type
 */
@Polymorphic
interface IssueDto<I>{
    /**
     * The name of the party that raised the issue.
     */
    val raisedBy: String

    /**
     * The current state of the issue.
     */
    val state: Issue.State

    /**
     * The name of the party that closed the issue, or `null` if it is still open.
     */
    val closedBy: String?
}
