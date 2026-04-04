/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.Encoding.defaultIssueSerializers
import com.nrkei.project.issue.IssueDto
import com.nrkei.project.issue.IssueParty
import com.nrkei.project.issue.util.TestIssue1
import com.nrkei.project.issue.util.TestIssue1.TestIssue1Dto
import com.nrkei.project.issue.util.TestIssue2
import com.nrkei.project.issue.util.TestIssue2.TestIssue2Dto
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

object TestIssuePersistence {

    private val issueSerializers = SerializersModule {
        polymorphic(IssueDto::class) {
            subclass(TestIssue1Dto::class)
            subclass(TestIssue2Dto::class)
        }
    }
    private val json = Json {
        serializersModule = defaultIssueSerializers + issueSerializers
        prettyPrint = false
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }

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