package com.nrkei.project.issue

import com.nrkei.project.issue.IssueSet.Bucket
/**
 * Visitor for traversing an [IssueSet] and its contained issues.
 *
 * Implementations can hook into the traversal before and after visiting the whole set,
 * before and after visiting each bucket, and for each individual issue.
 */
interface IssueVisitor {
    /**
     * Called before visiting the issues in an [IssueSet].
     *
     * @param issueSet the set being visited
     * @param types the issue types present in the set
     */
    fun preVisit(issueSet: IssueSet, types: List<IssueType<*>>) { }

    /**
     * Called after visiting the issues in an [IssueSet].
     *
     * @param issueSet the set being visited
     * @param types the issue types present in the set
     */
    fun postVisit(issueSet: IssueSet, types: List<IssueType<*>>) { }

    /**
     * Called before visiting the issues in a bucket.
     *
     * @param bundle the bucket being visited
     * @param type the issue type stored in the bucket
     */
    fun preVisit(bundle: Bucket<*>, type: IssueType<*>) { }

    /**
     * Called after visiting the issues in a bucket.
     *
     * @param bundle the bucket being visited
     * @param type the issue type stored in the bucket
     */
    fun postVisit(bundle: Bucket<*>, type: IssueType<*>) { }

    /**
     * Called for each issue encountered during traversal.
     *
     * @param issue the current issue
     * @param type the issue's type
     */
    fun visit(issue: Issue<*>, type: IssueType<*>) { }
}