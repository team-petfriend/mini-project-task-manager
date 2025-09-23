package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.admin.request.RoleRequest;
import com.example.petfriend.dto.admin.response.RoleResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;


    @PostMapping("/roles/add")
    public ResponseEntity<ResponseDto<RoleResponse.AddRoleResponse>> addRole(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody RoleRequest.AddRoleRequest req
    ) {
        ResponseDto<RoleResponse.AddRoleResponse> response = adminService.addRole(userPrincipal, req);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/roles/replace")
    public ResponseEntity<ResponseDto<RoleResponse.ReplaceResponse>> replaceRole(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody RoleRequest.ReplaceRequest req
    ) {
        ResponseDto<RoleResponse.ReplaceResponse> response = adminService.replaceRole(userPrincipal, req);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/roles/remove")
    public ResponseEntity<ResponseDto<RoleResponse.DeleteResponse>> deleteRole(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody RoleRequest.DeleteRequest req
    ) {
        ResponseDto<RoleResponse.DeleteResponse> response = adminService.DeleteRole(userPrincipal, req);
        return ResponseEntity.ok().body(response);
    }
}
