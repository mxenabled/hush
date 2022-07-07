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
data class Iteration(
    val id: Int,
    val iid: Int,
    val sequence: Int,
    @SerialName("group_id")
    @SerializedName("group_id")
    val groupId: Int,
    val state: Int,
    @SerialName("created_at")
    @SerializedName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerialName("start_date")
    @SerializedName("start_date")
    val startDate: String?,
    @SerialName("due_date")
    @SerializedName("due_date")
    val dueDate: String?,
    @SerialName("web_url")
    @SerializedName("web_url")
    val webUrl: String,
)
