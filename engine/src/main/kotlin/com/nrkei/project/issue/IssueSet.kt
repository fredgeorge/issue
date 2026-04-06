/*
 *  Copyright (c) 2000-2026 by Fred George
 *  May be used freely except for training; license required for training.
 *  @author Fred George  fredgeorge@acm.org
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Issue.Companion.filter
import com.nrkei.project.issue.Issue.Companion.filterByState
import kotlinx.serialization.Serializable

/**
 * A collection of issues organized by [IssueType].
 *
 * Supports raising issues, querying them by type or state, and traversing the set with
 * an [IssueVisitor].
 */
class IssueSet private constructor(private val buckets: MutableMap<IssueType<*>, Bucket<*>>) {

    constructor() : this(mutableMapOf())

    // Required for serialization
    companion object

    /**
     * Adds the given issue to this set.
     *
     * @param issue the issue to raise
     * @return the same issue instance
     */
    fun raise(issue: Issue<*>) = issue.also {
        bucket(it.issueType).add(it)
    }

    /**
     * Returns all issues in the given [state], across all issue types.
     *
     * @param state the state to filter by
     * @return all matching issues
     */
    fun issues(state: Issue.State): List<Issue<*>> =
        buckets.values.flatMap { it.allIssues() }.filterByState(state)

    /**
     * Returns all issues for the given [issueType].
     *
     * @param issueType the type of issue to retrieve
     * @return all matching issues
     */
    fun <I : Issue<I>> issues(issueType: IssueType<I>): List<I> =
        bucket(issueType).all()

    /**
     * Returns all issues for the given [issueType] that are in the given [state].
     *
     * @param issueType the type of issue to retrieve
     * @param state the state to filter by
     * @return all matching issues
     */
    fun <I : Issue<I>> issues(issueType: IssueType<I>, state: Issue.State): List<I> =
        issues(issueType).filter(state)

    /**
     * Returns all issues in the given [state] for the given [issueType].
     *
     * @param state the state to filter by
     * @param issueType the type of issue to retrieve
     * @return all matching issues
     */
    fun <I : Issue<I>> issues(state: Issue.State, issueType: IssueType<I>): List<I> =
        issues(issueType, state)

    /**
     * Traverses this issue set with the given visitor.
     *
     * @param visitor the visitor to notify during traversal
     */
     fun accept(visitor: IssueVisitor) {
        buckets.keys.toList().also { issueTypes ->
            visitor.preVisit(this, issueTypes)
            buckets.values.forEach { it.accept(visitor) }
            visitor.postVisit(this, issueTypes)
        }
    }

    /**
     * Converts this issue set to a serializable DTO.
     */
    fun toDto() = IssueSetDto(this)

    override fun toString() = PrettyPrint(this).toString()

    @Suppress("UNCHECKED_CAST")
    private fun <I : Issue<I>> bucket(issueType: IssueType<I>): Bucket<I> =
        buckets.getOrPut(issueType) { Bucket(issueType) } as Bucket<I>

    /**
     * Groups issues for a single [IssueType].
     *
     * @param I the concrete issue type
     */
    class Bucket<I : Issue<I>> internal constructor(private val issueType: IssueType<I>) {
        private val issues = mutableSetOf<I>()

        /**
         * Adds an issue to this bucket.
         *
         * @param issue the issue to add
         */
        fun add(issue: Issue<*>) {
            require(issue.issueType == issueType)
            @Suppress("UNCHECKED_CAST")
            issues.add(issue as I)
        }

        /**
         * Returns all issues stored in this bucket.
         *
         * @return the issues as a list
         */
        fun all(): List<I> = issues.toList()

        /**
         * Returns all issues stored in this bucket as the base [Issue] type.
         *
         * @return the issues as a list of [Issue]
         */
        fun allIssues(): List<Issue<*>> = all()

        internal fun accept(visitor: IssueVisitor) {
            visitor.preVisit(this, issueType)
            issues.forEach { it.accept(visitor) }
            visitor.postVisit(this, issueType)
        }
    }

    @Serializable
    data class IssueSetDto(val issueDtos: List<IssueDto<*>>) {
        internal constructor(issueSet: IssueSet) : this(
            issueSet.buckets.values
                .flatMap { it.allIssues() }
                .map { it.toDto() })
    }
}