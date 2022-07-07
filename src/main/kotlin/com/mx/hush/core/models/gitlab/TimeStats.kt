/**
 * Copyright 2020 MX Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.hush.core.models.gitlab

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeStats(
    @SerialName("time_estimate")
    @SerializedName("time_estimate")
    val timeEstimate: Int?,
    @SerialName("total_time_spent")
    @SerializedName("total_time_spent")
    val totalTimeSpent: Int?,
    @SerialName("human_time_estimate")
    @SerializedName("human_time_estimate")
    val humanTimeEstimate: String?,
    @SerialName("human_total_time_spent")
    @SerializedName("human_total_time_spent")
    val humanTotalTimeSpent: String?,
)