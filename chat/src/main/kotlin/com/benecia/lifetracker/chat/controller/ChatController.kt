package com.benecia.lifetracker.chat.controller

import com.benecia.lifetracker.chat.dto.ChatMessage
import com.benecia.lifetracker.chat.service.ChatMessageProducer
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class ChatController(
    private val chatMessageProducer: ChatMessageProducer,
) {
    @MessageMapping("/chat.send")
    fun sendChatMessage(message: ChatMessage) {
        chatMessageProducer.sendChatMessage(message)
    }
}