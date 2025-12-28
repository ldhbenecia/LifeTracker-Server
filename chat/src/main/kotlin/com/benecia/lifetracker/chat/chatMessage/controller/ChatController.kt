package com.benecia.lifetracker.chat.chatMessage.controller

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageService
import com.benecia.lifetracker.common.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chat/rooms")
class ChatController(
    private val chatMessageService: ChatMessageService,
) {

    @GetMapping("/{roomId}/messages")
    fun getMessages(
        @PathVariable roomId: Long,
        @RequestParam(required = false) lastMessageTimestamp: Long?,
        @RequestParam(defaultValue = "30") size: Int,
    ): ApiResponse<List<ChatMessage>> {
        val messages = chatMessageService.getMessages(roomId, lastMessageTimestamp, size)
        return ApiResponse.success(messages)
    }
}
