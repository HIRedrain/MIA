package mia.controller.admin

import mia.dto.PageResponse
import mia.dto.admin.user.AdminUserResponse
import mia.dto.admin.user.UserRoleUpdateRequest
import mia.service.AdminUserService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/users")
class AdminUserRestController(
    private val adminUserService: AdminUserService
) {
    @GetMapping
    fun getUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageResponse<AdminUserResponse>> {
        return ResponseEntity.ok(adminUserService.getUsers(page, size))
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Int
    ): ResponseEntity<AdminUserResponse> {
        return ResponseEntity.ok(adminUserService.getUser(userId))
    }

    @PatchMapping("/{userId}/role")
    fun updateRole(
        authentication: Authentication,
        @PathVariable userId: Int,
        @RequestBody request: UserRoleUpdateRequest
    ): ResponseEntity<AdminUserResponse> {
        return ResponseEntity.ok(
            adminUserService.updateRole(
                currentLoginId = authentication.name,
                userId = userId,
                role = request.role
            )
        )
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        authentication: Authentication,
        @PathVariable userId: Int
    ): ResponseEntity<Void> {
        adminUserService.deleteUser(
            currentLoginId = authentication.name,
            userId = userId
        )
        return ResponseEntity.noContent().build()
    }
}
