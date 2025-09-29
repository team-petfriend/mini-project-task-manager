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

    public boolean isCommentAuthor(Long commentId, Authentication authentication) {
        if (commentId == null || authentication == null) return false;

        Long me = extractUserId(authentication);

        return commentRepository.findById(commentId)
                .map(comment -> comment.getCommenter().getId().equals(me))
                .orElse(false);
    }

    public boolean isNotificationOwner(Long notificationId, Authentication authentication) {
        if (notificationId == null || authentication == null) return false;

        Long me = extractUserId(authentication);

        return notificationRepository.findById(notificationId)
                .map(notification -> notification.getUser().getId().equals(me))
                .orElse(false);
    }

    private Long extractUserId (Authentication authentication) {
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal up) {
            return up.getId();
        }
        return null;
    }
}
