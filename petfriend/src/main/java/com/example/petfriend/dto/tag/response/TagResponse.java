package com.example.petfriend.dto.tag.response;

import com.example.petfriend.entity.Tag;

public class TagResponse {

    public record DetailTag(
            String name,
            String color
    ) {
        /** 형변환은 response단에서 사용 */

        public static DetailTag from(Tag tag) {
            if (tag == null) return null;
                return new DetailTag(
                        tag.getName(),
                        tag.getColor()
                );
        }
    }

}
