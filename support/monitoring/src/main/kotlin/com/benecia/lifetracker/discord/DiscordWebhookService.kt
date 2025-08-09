package com.benecia.lifetracker.discord

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class DiscordWebhookService(
    @Value("\${discord.dev.webhook-url}") private val webhookUrl: String,
    private val restClient: RestClient,

) {

    fun sendErrorNotification(
        title: String,
        description: String,
        color: Int = 0xFF0000,
        fields: List<Pair<String, String>> = emptyList(),
    ) {
        val embed = mutableMapOf<String, Any>(
            "title" to title,
            "description" to description,
            "color" to color,
        )
        if (fields.isNotEmpty()) {
            embed["fields"] = fields.map { (n, v) ->
                mapOf("name" to n, "value" to v, "inline" to false)
            }
        }

        val payload = mapOf(
            "username" to "Error Alert Bot",
            "embeds" to listOf(embed),
        )

        restClient.post()
            .uri(webhookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)
            .retrieve()
            .toBodilessEntity()
    }
}
