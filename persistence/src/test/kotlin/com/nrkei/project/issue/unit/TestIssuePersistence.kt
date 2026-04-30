/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.*
import com.nrkei.project.issue.TestIssue1.TestIssue1Dto
import com.nrkei.project.issue.TestIssue2.TestIssue2Dto
import kotlin.reflect.jvm.isAccessible

// Understands creation of test issues from DTOs
object TestIssuePersistence {

    // Build using reflection in case constructor is private
    internal fun TestIssue1Dto.toIssue(): TestIssue1 {
        val ctor = TestIssue1::class.constructors.first { it.parameters.size == 4 }
        ctor.isAccessible = true
        return ctor.call(
            this.description,
            IssueParty(this.raisedBy),
            this.state,
            this.closedBy?.let { IssueParty(it) }
        )
    }

    // Build using reflection in case constructor is private
    internal fun TestIssue2Dto.toIssue(): TestIssue2 {
        val ctor = TestIssue2::class.constructors.first { it.parameters.size == 4 }
        ctor.isAccessible = true
        return ctor.call(
            this.label,
            IssueParty(this.raisedBy),
            this.state,
            this.closedBy?.let { IssueParty(it) }
        )
    }
}