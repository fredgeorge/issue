/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.TestIssue1.TestIssue1Dto
import com.nrkei.project.issue.TestIssue2.TestIssue2Dto

object TestIssuePersistence {

    internal fun TestIssue1Dto.toIssue() =
        TestIssue1(
            description = description,
            raisedBy = IssueParty(raisedBy),
            state = state,
            closedBy = closedBy?.let { IssueParty(it) }
        )

    internal fun TestIssue2Dto.toIssue() =
        TestIssue2(
            label = label,
            raisedBy = IssueParty(raisedBy),
            state = state,
            closedBy = closedBy?.let { IssueParty(it) }
        )
}