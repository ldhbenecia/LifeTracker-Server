package com.benecia.lifetracker.domain.todo.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.todo.dto.ModifyTodoRequest
import com.benecia.lifetracker.domain.todo.dto.ModifyTodoResponse
import com.benecia.lifetracker.domain.todo.dto.NewTodoRequest
import com.benecia.lifetracker.domain.todo.dto.NewTodoResponse
import com.benecia.lifetracker.domain.todo.dto.TodoResponse
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.todocore.service.TodoService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/todos")
class TodoController(
    private val todoService: TodoService,
) {
    @GetMapping("/{id}")
    fun findTodo(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
    ): ApiResponse<TodoResponse> {
        val todoInfo = todoService.findTodoById(loginUser.id, id)
        return ApiResponse.success(TodoResponse.of(todoInfo))
    }

    @GetMapping("/month")
    fun findTodosByMonth(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestParam year: Int,
        @RequestParam month: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ApiResponse<List<TodoResponse>> {
        val todos = todoService.findTodosByMonth(loginUser.id, year, month, page, size)
        val responseList = todos.map { TodoResponse.of(it) }
        return ApiResponse.success(responseList)
    }

    @PostMapping
    fun addTodo(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: NewTodoRequest,
    ): ApiResponse<NewTodoResponse> {
        val todoId = todoService.addTodo(loginUser.id, request.toNewTodo())
        return ApiResponse.created(NewTodoResponse(todoId))
    }

    @PutMapping("/{id}")
    fun modifyTodo(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
        @RequestBody request: ModifyTodoRequest,
    ): ApiResponse<ModifyTodoResponse> {
        val todoId = todoService.modifyTodo(loginUser.id, id, request.toModifyTodo())
        return ApiResponse.success(ModifyTodoResponse(todoId))
    }
}
