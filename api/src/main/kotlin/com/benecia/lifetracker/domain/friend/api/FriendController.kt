package com.benecia.lifetracker.domain.friend.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.friend.dto.FriendResponse
import com.benecia.lifetracker.domain.friend.dto.NewFriendRequest
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.user.service.FriendService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/friends")
class FriendController(
    private val friendService: FriendService,
) {
    @PostMapping
    fun addFriend(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: NewFriendRequest,
    ): ApiResponse<Long> {
        val id = friendService.add(loginUser.id, request.toNewFriend())
        return ApiResponse.created(id)
    }

    @GetMapping
    fun findAllByUserId(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ApiResponse<List<FriendResponse>> {
        val friends = friendService.findAllByUserId(loginUser.id)
        val responseList = friends.map { FriendResponse.of(it) }
        return ApiResponse.success(responseList)
    }

    @GetMapping("/requests")
    fun findPendingRequests(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ApiResponse<List<FriendResponse>> {
        val requests = friendService.findPendingRequests(loginUser.id)
        val responseList = requests.map { FriendResponse.of(it) }
        return ApiResponse.success(responseList)
    }

    @PostMapping("/{friendRequestId}/accept")
    fun acceptRequest(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable friendRequestId: Long,
    ): ApiResponse<Long> {
        val id = friendService.acceptRequest(loginUser.id, friendRequestId)
        return ApiResponse.success(id)
    }

    @PostMapping("/{friendRequestId}/reject")
    fun rejectRequest(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable friendRequestId: Long,
    ): ApiResponse<Long> {
        val id = friendService.rejectRequest(loginUser.id, friendRequestId)
        return ApiResponse.success(id)
    }
}
