package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.admin.request.RoleRequest;
import com.example.petfriend.dto.admin.response.RoleResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

public interface AdminService {
    ResponseDto<RoleResponse.AddRoleResponse> addRole(UserPrincipal userPrincipal, RoleRequest.@Valid AddRoleRequest req);
    ResponseDto<RoleResponse.ReplaceResponse> replaceRole(UserPrincipal userPrincipal, RoleRequest.@Valid ReplaceRequest req);
    ResponseDto<RoleResponse.DeleteResponse> DeleteRole(UserPrincipal userPrincipal, RoleRequest.@Valid DeleteRequest req);
}
