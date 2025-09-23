package com.example.petfriend.security.util;

import com.example.petfriend.repository.CommentRepository;
import com.example.petfriend.repository.NotificationRepository;
import com.example.petfriend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("authz")
@RequiredArgsConstructor
public class AuthorizationChecker {
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    /** Comment 작성자 본인인지 확인 */
    public boolean isCommentAuthor(Long commentId, Authentication authentication) {
        if (commentId == null || authentication == null) return false;

        Long me = extractUserId(authentication);

        return commentRepository.findById(commentId)
                .map(comment -> comment.getCommenter().getId().equals(me))
                .orElse(false);
    }

    /** 알림 소유자 확인 */
    public boolean isNotificationOwner(Long notificationId, Authentication authentication) {
        if (notificationId == null || authentication == null) return false;

        Long me = extractUserId(authentication);

        return notificationRepository.findById(notificationId)
                .map(notification -> notification.getUser().getId().equals(me))
                .orElse(false);
    }

    // == 프로젝트의 Principal 구조에 맞게 사용자 ID 추출 == //
    private Long extractUserId (Authentication authentication) {
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal up) {
            return up.getId();
        }
        return null;
    }
}
