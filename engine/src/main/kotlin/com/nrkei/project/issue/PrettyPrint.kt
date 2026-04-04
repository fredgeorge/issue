package com.nrkei.project.issue

// Understands rendering IssueSet in a human-readable format
internal class PrettyPrint(issueSet: IssueSet) : IssueVisitor {
    private val result = StringBuilder()
    init {
        issueSet.accept(this)
    }

    override fun toString() = result.toString()

    override fun preVisit(issueSet: IssueSet, types: List<IssueType<*>>) {
        result.appendLine("IssueSet with Buckets for ${types.joinToString(", ") { it.javaClass.simpleName }}")
    }

    override fun preVisit(bundle: IssueSet.Bucket<*>, type: IssueType<*>) {
        result.appendLine("\tBucket for ${type.javaClass.simpleName}:")
    }

    override fun visit(issue: Issue<*>, type: IssueType<*>) {
        result.appendLine("\t\t${issue}")
    }
}