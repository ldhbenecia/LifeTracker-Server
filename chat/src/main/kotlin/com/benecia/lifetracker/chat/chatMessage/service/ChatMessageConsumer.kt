package com.benecia.lifetracker.chat.chatMessage.service

import com.benecia.lifetracker.common.config.QueueNames
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.simp.SimpMessagingTemplate

class ChatMessageConsumer(
    private val messagingTemplate: SimpMessagingTemplate,
) {

    /**
     * 큐에서 메세지를 받아 WebSocket으로 BroadCast
     */
    @RabbitListener(queues = [QueueNames.CHAT_QUEUE])
    fun receiveChatMessage(message: ChatMessage) {
        messagingTemplate.convertAndSend("/topic/chat/${message.roomId}", message)
    }
}
