package cn.mirrorming.blog.repository;

import cn.mirrorming.blog.domain.es.EsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 @author mireal
 */
public interface ArticleRepository extends ElasticsearchRepository<EsArticle, Integer> {
    Page<EsArticle> findByTitle(String title, Pageable pageable);
}
