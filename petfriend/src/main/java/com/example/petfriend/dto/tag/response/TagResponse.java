package com.example.petfriend.dto.tag.response;

import com.example.petfriend.common.enums.TagType;
import com.example.petfriend.entity.Tag;

public class TagResponse {

    public record DetailTag(
            Long projectId,
            TagType name,
            String color
    ) {
        public static DetailTag from(Tag tag) {
            if (tag == null) return null;
                return new DetailTag(
                        tag.getProject().getId(),
                        tag.getName(),
                        tag.getColor()
                );
        }
    }

}
