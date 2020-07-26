package cn.mirrorming.blog.domain.dto.bilibili;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/26 14:25
 */
@NoArgsConstructor
@Data
public class BilibiliVideo {

    private boolean status;
    private VideoData videoData;

    @NoArgsConstructor
    @Data
    public static class VideoData {
        private Tlist tlist;
        private int count;
        private int pages;
        private List<Vlist> vlist;

        @NoArgsConstructor
        @Data
        public static class Tlist {
            private Type type;

            @NoArgsConstructor
            @Data
            public static class Type {
                private int tid;
                private int count;
                private String name;
            }
        }

        @NoArgsConstructor
        @Data
        public static class Vlist {
            private int comment;
            private int typeid;
            private int play;
            private String pic;
            private String subtitle;
            private String description;
            private String copyright;
            private String title;
            private int review;
            private String author;
            private int mid;
            private int is_union_video;
            private int created;
            private String length;
            private int video_review;
            private int is_pay;
            private int favorites;
            private int aid;
            private int is_steins_gate;
            private boolean hide_click;
        }
    }
}
