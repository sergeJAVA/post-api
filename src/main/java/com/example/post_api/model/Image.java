package com.example.post_api.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Image {
    private Long id;
    private String userId;
    private String downloadPath;
    private String name;
}
