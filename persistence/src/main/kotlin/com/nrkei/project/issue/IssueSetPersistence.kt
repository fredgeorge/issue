/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Encoding.fromBase64
import com.nrkei.project.issue.Encoding.toBase64
import com.nrkei.project.issue.IssueSet.IssueSetDto
import com.nrkei.project.issue.TestIssue1.TestIssue1Dto
import com.nrkei.project.issue.TestIssue2.TestIssue2Dto
import com.nrkei.project.issue.TestIssuePersistence.toIssue
import kotlinx.serialization.json.Json

// Understands persistence of a IssueSet by converting
// it to a DTO, serializing that DTO as JSON, and
// optionally wrapping the JSON in Base64 for
// text-safe storage or transmission.

internal fun IssueSet.toMemento(json: Json) = toBase64(toDto(), json)

internal fun IssueSet.Companion.fromMemento(memento: String, json: Json) =
    fromBase64<IssueSetDto>(memento, json).let { issueSetDto ->
        IssueSet().also { issueSet ->
            issueSetDto.issueDtos.map { issueFrom(it) }.forEach { issue ->
                issueSet.raise(issue)
            }
        }
    }

private fun issueFrom(dto: IssueDto<*>): Issue<*> {
    return when(dto) {
        is TestIssue1Dto -> dto.toIssue()
        is TestIssue2Dto -> dto.toIssue()
        else -> throw IllegalArgumentException("Unknown issue type: ${dto.javaClass.simpleName}")
    }
}