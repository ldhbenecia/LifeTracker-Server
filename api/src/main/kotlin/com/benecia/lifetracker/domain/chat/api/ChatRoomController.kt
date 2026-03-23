package com.benecia.lifetracker.domain.chat.api

import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomSummary
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomService
import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.chat.dto.DefaultRoomResponse
import com.benecia.lifetracker.security.userdetails.LoginUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/chat/rooms")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {

    @GetMapping()
    fun readRooms(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ApiResponse<List<ChatRoomSummary>> {
        val rooms = chatRoomService.readRooms(loginUser.id)
        return ApiResponse.success(rooms)
    }

    @PostMapping()
    fun createRoom(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestParam("opponentId") opponentId: UUID,
    ): ApiResponse<DefaultRoomResponse> {
        val roomId = chatRoomService.findOrCreateRoom(loginUser.id, opponentId)
        return ApiResponse.success(DefaultRoomResponse(roomId))
    }

    @PutMapping("/{roomId}/remove")
    fun removeRoom(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable("roomId") roomId: Long,
    ): ApiResponse<DefaultRoomResponse> {
        chatRoomService.removeRoom(loginUser.id, roomId)
        return ApiResponse.success(DefaultRoomResponse(roomId))
    }

    @PutMapping("/{roomId}/notification")
    fun setNotification(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable roomId: Long,
        @RequestParam enabled: Boolean,
    ): ApiResponse<DefaultRoomResponse> {
        chatRoomService.setNotification(loginUser.id, roomId, enabled)
        return ApiResponse.success(DefaultRoomResponse(roomId))
    }
}
