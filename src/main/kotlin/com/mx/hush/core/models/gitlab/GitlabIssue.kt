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

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitlabIssue(
    val state: String,
    val description: String,
    val Author: Author,
    val milestone: Milestone?,
    @SerialName("project_id")
    @SerializedName("project_id")
    val projectId: Int,
    val assignees: List<Assignee>?,
    val assignee: Assignee?,
    val type: String,
    @SerialName("updated_at")
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerialName("closed_at")
    @SerializedName("closed_at")
    val closedAt: String?,
    @SerialName("closed_by")
    @SerializedName("closed_by")
    val closedBy: String?,
    val id: Int,
    val title: String,
    @SerialName("created_at")
    @SerializedName("created_at")
    val createdAt: String,
    @SerialName("moved_to_id")
    @SerializedName("moved_to_id")
    val movedToId: Int?,
    val iid: Int,
    val labels: List<String>?,
    val upvotes: Int,
    val downvotes: Int,
    @SerialName("merge_requests_count")
    @SerializedName("merge_requests_count")
    val mergeRequestsCount: Int,
    @SerialName("user_notes_count")
    @SerializedName("user_notes_count")
    val userNotesCount: Int,
    @SerialName("due_date")
    @SerializedName("due_date")
    val dueDate: String?,
    @SerialName("web_url")
    @SerializedName("web_url")
    val webUrl: String,
    val references: References,
    @SerialName("time_stats")
    @SerializedName("time_stats")
    val timeStats: TimeStats,
    @SerialName("has_tasks")
    @SerializedName("has_tasks")
    val hasTasks: Boolean,
    @SerialName("task_status")
    @SerializedName("task_status")
    val taskStatus: String?,
    val confidential: Boolean,
    @SerialName("discussion_locked")
    @SerializedName("discussion_locked")
    val discussionLocked: String?,
    @SerialName("issue_type")
    @SerializedName("issue_type")
    val issueType: String,
    val severity: String,
    @SerialName("_links")
    @SerializedName("_links")
    val links: Links,
    @SerialName("task_completion_status")
    @SerializedName("task_completion_status")
    val taskCompletionStatus: TaskCompletionStatus,
    val weight: Int?,
    @SerialName("service_desk_reply_to")
    @SerializedName("service_desk_reply_to")
    val serviceDeskReplyTo: String?,
    @SerialName("epic_iid")
    @SerializedName("epic_iid")
    val epicIid: Int?,
    val epic: String?,
    val iteration: Iteration?,
) {
    class Deserializer : ResponseDeserializable<Array<GitlabIssue>> {
        override fun deserialize(content: String): Array<GitlabIssue>
                = Gson().fromJson(content, Array<GitlabIssue>::class.java)
    }
}
