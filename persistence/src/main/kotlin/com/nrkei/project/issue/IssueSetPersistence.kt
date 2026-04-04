/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.Encoding.fromBase64
import com.nrkei.project.issue.Encoding.toBase64
import com.nrkei.project.issue.IssueSet.IssueSetDto
import kotlinx.serialization.json.Json

// Understands persistence of a Rectangle by converting
// it to a DTO, serializing that DTO as JSON, and
// optionally wrapping the JSON in Base64 for
// text-safe storage or transmission.

internal fun IssueSet.toMemento(json: Json) = toBase64(toDto(), json)

internal fun IssueSet.Companion.fromMemento(memento: String, json: Json) =
    fromBase64<IssueSetDto>(memento, json).let { issueSetDto ->
        IssueSet().also { issueSet ->
            issueSetDto.issueDtos.map { it.toIssue() }.forEach { issue ->
                issueSet.raise(issue)
            }
        }
    }