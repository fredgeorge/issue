/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue.unit

import com.nrkei.project.issue.RectangleJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures Rectangle JSON persistence operates correctly
internal class RectangleJsonTest {

    @Test
    fun roundTripJson() {
        val original = RectangleJson(4.0, 6.0)

        val json = original.toJson()
        val restored = RectangleJson.fromJson(json)

        assertEquals("""{"length":4.0,"width":6.0}""", json)
        assertEquals(original, restored)
        assertEquals(24.0, restored.area())
    }
}