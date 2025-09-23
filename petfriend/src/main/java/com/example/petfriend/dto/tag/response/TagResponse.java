package com.example.petfriend.dto.tag.response;

import com.example.petfriend.entity.Tag;

public class TagResponse {

    public record DetailTag(
            String name,
            String color
    ) {
        public static DetailTag from(Tag tag) {
            if (tag == null) return null;
                return new DetailTag(
                        tag.getName(),
                        tag.getColor()
                );
        }
    }

}
