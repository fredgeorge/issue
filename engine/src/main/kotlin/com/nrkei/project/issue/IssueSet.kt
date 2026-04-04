/*
 *  Copyright (c) 2000-2026 by Fred George
 *  May be used freely except for training; license required for training.
 *  @author Fred George  fredgeorge@acm.org
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Issue.Companion.filter
import com.nrkei.project.issue.Issue.Companion.filterByState
import kotlinx.serialization.Serializable

// Understands aberrations in a process
class IssueSet private constructor(private val buckets: MutableMap<IssueType<*>, Bucket<*>>) {

    constructor() : this(mutableMapOf())

    // Required for serialization
    companion object

    fun raise(issue: Issue<*>) = issue.also {
        bucket(it.issueType).add(it)
    }

    fun issues(state: Issue.State): List<Issue<*>> =
        buckets.values.flatMap { it.allIssues() }.filterByState(state)

    fun <I : Issue<I>> issues(issueType: IssueType<I>): List<I> =
        bucket(issueType).all()

    fun <I : Issue<I>> issues(issueType: IssueType<I>, state: Issue.State): List<I> =
        issues(issueType).filter(state)

    fun <I : Issue<I>> issues(state: Issue.State, issueType: IssueType<I>): List<I> =
        issues(issueType, state)

    fun accept(visitor: IssueVisitor) {
        visitor.preVisit(this, buckets.keys.toList())
        buckets.values.forEach { it.accept(visitor) }
        visitor.postVisit(this, buckets.keys.toList())
    }

    fun toDto() = IssueSetDto(this)

    override fun toString() = PrettyPrint(this).toString()

    @Suppress("UNCHECKED_CAST")
    private fun <I : Issue<I>> bucket(issueType: IssueType<I>): Bucket<I> =
        buckets.getOrPut(issueType) { Bucket(issueType) } as Bucket<I>

    class Bucket<I : Issue<I>> internal constructor(private val issueType: IssueType<I>) {
        private val issues = mutableSetOf<I>()

        fun add(issue: Issue<*>) {
            require(issue.issueType == issueType)
            @Suppress("UNCHECKED_CAST")
            issues.add(issue as I)
        }

        fun all(): List<I> = issues.toList()

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