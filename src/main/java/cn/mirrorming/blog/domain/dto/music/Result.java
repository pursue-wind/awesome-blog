package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;

import java.util.List;

/**
 * @author mireal
 */
@Data
public class Result {
    private List<Songs> songs;
    private int songCount;
}