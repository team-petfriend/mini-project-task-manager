package com.example.petfriend.security.util;

import com.example.petfriend.entity.Tag;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TagRepository;
import com.example.petfriend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("tagAuthz")
@RequiredArgsConstructor
public class  TagAuthorizationChecker{
    private final TagRepository tagRepository;

/** principal(loginId) 해당 tagId인지 검증하는 로직 */
public boolean isAuthor (Authentication authentication, Long tagId) {

    if ( authentication == null || tagId == null ) return false;

    String loginId = authentication.getName();

    Tag tag = tagRepository.findById(tagId)
            .orElse(null);

    if ( tag == null) return false;

    return tag.getId().equals(loginId) ;
}

///** USER가 해당 Tag를 삭제할 수 있는지 확인하는 로직 */
//public boolean deleteTag(Long tagId, Authentication authentication) {
//    Long me = extractUserId(authentication);
//
//    return tagRepository.findById(tagId)
//            .map(t -> t.getId().equals(me))
//            .orElse(false);
//}
//
//
///** user를 검증하고 반환하는 로직 */
//public Long extractUserId (Authentication authentication) {
//    if (authentication == null) return null;
//
//    Object principal = authentication.getAuthorities();
//
//    if (principal instanceof UserPrincipal user) {
//        return user.getId();
//    }
//    return null;
//}
}
