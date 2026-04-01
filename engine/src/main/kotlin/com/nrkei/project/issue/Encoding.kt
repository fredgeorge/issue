/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.nrkei.project.issue.IssueSet.IssueSetDto
import java.util.Base64
import kotlin.jvm.java

val mapper = jacksonObjectMapper()

internal inline fun <reified T : Any> toBase64(dto: T) =
    Base64.getEncoder().encodeToString(
            toJson(dto).toByteArray(Charsets.UTF_8)
        )

internal inline fun <reified T : Any> toJson(dto: T) =
    mapper.writeTypedValueAsString(dto)


inline fun <reified T> ObjectMapper.writeTypedValueAsString(value: T): String =
    writerFor(jacksonTypeRef<T>()).writeValueAsString(value)

internal fun issueSetDtoFromBase64(
    base64: String
) = fromJson(fromBase64ToJson(base64), IssueSetDto::class.java)

internal fun fromBase64ToJson(base64: String)=
    String(Base64.getDecoder().decode(base64), Charsets.UTF_8)

internal fun <T> fromJson(
    json: String,
    clazz: Class<T>
): T =
    mapper.readValue(json, clazz)
