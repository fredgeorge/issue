/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.util

import com.nrkei.project.issue.Issue
import com.nrkei.project.issue.IssueSet
import org.junit.jupiter.api.Assertions.assertEquals


internal fun assertStates(issueSet: IssueSet, vararg stateCounts: Pair<Issue.State, Int>) {
    stateCounts.forEach { (state, count) ->
        assertEquals(count, issueSet.issues(state).size)
    }
}