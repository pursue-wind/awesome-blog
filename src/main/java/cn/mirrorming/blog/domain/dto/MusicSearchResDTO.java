package cn.mirrorming.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mireal Chan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicSearchResDTO implements Serializable {

    private List<MusicSearchResult> musicSearchResults;
    /**
     * 搜索结果总数
     */
    private int songCount;

    private static final long serialVersionUID = 1L;
}