package cn.mirrorming.blog.repository;

import cn.mirrorming.blog.domain.es.EsMovie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 @author mireal
 **/

public interface MovieRepository extends ElasticsearchRepository<EsMovie, Integer> {
    List<EsMovie> findMoviesByActorLike(String name);

    Page<EsMovie> findByActorOrTitleOrDescription(String actor, String title, String desc, Pageable pageable);

    Page<EsMovie> findByActor(String actor, Pageable pageable);

    Page<EsMovie> findByNameOrTitleOrActorOrDescription(String condition, String condition2, String condition3, String condition4, Pageable pageable);
}