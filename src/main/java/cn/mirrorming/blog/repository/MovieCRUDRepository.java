package cn.mirrorming.blog.repository;

import cn.mirrorming.blog.domain.es.EsMovie;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 @author mireal
 */
public interface MovieCRUDRepository extends ElasticsearchCrudRepository<EsMovie,Integer> {
}
