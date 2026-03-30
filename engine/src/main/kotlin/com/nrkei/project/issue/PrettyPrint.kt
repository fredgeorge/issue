package com.nrkei.project.issue

// Understands rendering IssueSet in a human-readable format
internal class PrettyPrint(issueSet: IssueSet) : IssueVisitor {
    private var result = ""
    init {
        issueSet.accept(this)
    }

    override fun toString() = result

    override fun preVisit(issueSet: IssueSet, types: List<IssueType<*>>) {
        result += "IssueSet with Buckets for ${types.joinToString(", ") { it.javaClass.simpleName }}\n"
    }

    override fun preVisit(bundle: IssueSet.Bucket<*>, type: IssueType<*>) {
        result += "\tBucket for ${type.javaClass.simpleName}:\n"
    }

    override fun visit(issue: Issue<*>, type: IssueType<*>) {
        result += "\t\t${issue}\n"
    }
}