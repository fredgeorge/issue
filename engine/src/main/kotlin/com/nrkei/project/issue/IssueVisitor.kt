package com.nrkei.project.issue

import com.nrkei.project.issue.IssueSet.Bucket

// Understands traversing the Issue object graph
interface IssueVisitor {
    fun preVisit(issueSet: IssueSet) { }
    fun postVisit(issueSet: IssueSet) { }
    fun preVisit(bundle: Bucket<*>, type: IssueType<*>) { }
    fun postVisit(bundle: Bucket<*>, type: IssueType<*>) { }
    fun visit(issue: Issue<*>, type: IssueType<*>)
}