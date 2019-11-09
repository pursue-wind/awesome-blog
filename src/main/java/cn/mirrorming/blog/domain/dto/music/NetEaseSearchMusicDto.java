package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/9 13:08
 */
@NoArgsConstructor
@Data
public class NetEaseSearchMusicDto {
    private Result result;
    private int code;

    @NoArgsConstructor
    @Data
    public static class Result {
        private int songCount;
        private List<Songs> songs;

        @NoArgsConstructor
        @Data
        public static class Songs {
            private String name;
            private int id;
            private int position;
            private int status;
            private int fee;
            private int copyrightId;
            private String disc;
            private int no;
            private Album album;
            private boolean starred;
            private int popularity;
            private int score;
            private int starredNum;
            private int duration;
            private int playedNum;
            private int dayPlays;
            private int hearTime;
            private String ringtone;
            private Object crbt;
            private Object audition;
            private String copyFrom;
            private String commentThreadId;
            private Object rtUrl;
            private int ftype;
            private int copyright;
            private String mp3Url;
            private Object rurl;
            private BMusic bMusic;
            private int rtype;
            private int mvid;
            private HMusic hMusic;
            private MMusic mMusic;
            private LMusic lMusic;
            private List<?> alias;
            private List<ArtistsX> artists;
            private List<?> rtUrls;

            @NoArgsConstructor
            @Data
            public static class Album {
                private String name;
                private int id;
                private String type;
                private int size;
                private long picId;
                private String blurPicUrl;
                private int companyId;
                private long pic;
                private String picUrl;
                private long publishTime;
                private String description;
                private String tags;
                private String company;
                private String briefDesc;
                private Artist artist;
                private int status;
                private int copyrightId;
                private String commentThreadId;
                private List<?> songs;
                private List<?> alias;
                private List<Artists> artists;

                @NoArgsConstructor
                @Data
                public static class Artist {
                    private String name;
                    private int id;
                    private int picId;
                    private int img1v1Id;
                    private String briefDesc;
                    private String picUrl;
                    private String img1v1Url;
                    private int albumSize;
                    private String trans;
                    private int musicSize;
                    private List<?> alias;
                }

                @NoArgsConstructor
                @Data
                public static class Artists {
                    private String name;
                    private int id;
                    private int picId;
                    private int img1v1Id;
                    private String briefDesc;
                    private String picUrl;
                    private String img1v1Url;
                    private int albumSize;
                    private String trans;
                    private int musicSize;
                    private List<?> alias;
                }
            }

            @NoArgsConstructor
            @Data
            public static class BMusic {
                private String name;
                private int id;
                private int size;
                private String extension;
                private int sr;
                private long dfsId;
                private int bitrate;
                private int playTime;
                private double volumeDelta;
            }

            @NoArgsConstructor
            @Data
            public static class HMusic {
                private String name;
                private int id;
                private int size;
                private String extension;
                private int sr;
                private long dfsId;
                private int bitrate;
                private int playTime;
                private double volumeDelta;
            }

            @NoArgsConstructor
            @Data
            public static class MMusic {
                private String name;
                private int id;
                private int size;
                private String extension;
                private int sr;
                private long dfsId;
                private int bitrate;
                private int playTime;
                private double volumeDelta;
            }

            @NoArgsConstructor
            @Data
            public static class LMusic {
                private String name;
                private int id;
                private int size;
                private String extension;
                private int sr;
                private long dfsId;
                private int bitrate;
                private int playTime;
                private double volumeDelta;
            }

            @NoArgsConstructor
            @Data
            public static class ArtistsX {
                private String name;
                private int id;
                private int picId;
                private int img1v1Id;
                private String briefDesc;
                private String picUrl;
                private String img1v1Url;
                private int albumSize;
                private String trans;
                private int musicSize;
                private List<?> alias;
            }
        }
    }
}
