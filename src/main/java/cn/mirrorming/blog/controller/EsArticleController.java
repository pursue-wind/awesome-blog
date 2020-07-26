package cn.mirrorming.blog.controller;//package cn.mirrorming.blog.controller;
//
//import cn.mirrorming.blog.domain.dto.ArticleSearchResultDTO;
//import cn.mirrorming.blog.domain.dto.BaseResult;
//import cn.mirrorming.blog.domain.dto.base.ResultData;
//import cn.mirrorming.blog.domain.dto.music.Result;
//import cn.mirrorming.blog.domain.elasticsearch.EsArticle;
//import cn.mirrorming.blog.mapper.mirror.MyArticleMapper;
//import cn.mirrorming.blog.repository.ArticleRepository;
//import com.google.common.collect.Lists;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.SearchResultMapper;
//import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
//import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// @author Mireal Chan
// * @date 2019-04-12
// */
//@RestController
//@RequestMapping("/es/article")
//public class EsArticleController {
//
//    @Autowired
//    private MyArticleMapper myArticleMapper;
//    @Autowired
//    private ArticleRepository articleRepository;
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @GetMapping("/addArticleToEs")
//    public ResultData getAllArticle() {
//        List<EsArticle> esArticles = myArticleMapper.selectAllAricleInfo();
//        esArticles.forEach(esArticle -> {
//            articleRepository.save(esArticle);
//        });
//        return BaseResult.ok("success");
//    }
//
//    @GetMapping("/search")
//    public ResultData searchArticle(@RequestParam(value = "condition", required = true) String condition,
//                                    @RequestParam(value = "pageNum", defaultValue = "0", required = true) Integer pageNum,
//                                    @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
//
//        SearchQuery sq = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.multiMatchQuery(condition, "title", "userName", "summary", "categoryName", "content", "tagName"))
//                .withHighlightFields(
//                        new HighlightBuilder.Field("title").preTags("<strong>").postTags("</strong>"),
//                        new HighlightBuilder.Field("userName").preTags("<strong>").postTags("</strong>"),
//                        new HighlightBuilder.Field("summary").preTags("<strong>").postTags("</strong>"),
//                        new HighlightBuilder.Field("categoryName").preTags("<strong>").postTags("</strong>"),
//                        new HighlightBuilder.Field("content").preTags("<strong>").postTags("</strong>"),
//                        new HighlightBuilder.Field("tagName").preTags("<strong>").postTags("</strong>"))
//                .withPageable(PageRequest.of(pageNum, pageSize)).build();
//
//        Page<EsArticle> result = elasticsearchTemplate.queryForPage(sq, EsArticle.class, new SearchResultMapper() {
//
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                ArrayList<EsArticle> articles = Lists.newArrayList();
//                SearchHits hits = response.getHits();
//                for (SearchHit searchHit : hits) {
//                    if (hits.getHits().length <= 0) {
//                        return null;
//                    }
//                    EsArticle article = new EsArticle();
//
//                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
//                    if (highlightFields.get("title") != null) {
//                        article.setTitle(highlightFields.get("title").getFragments()[0].toString());
//                    }
//
//                    if (highlightFields.get("userName") != null) {
//                        article.setContent(highlightFields.get("userName").getFragments()[0].toString());
//                    }
//
//                    if (highlightFields.get("summary") != null) {
//                        article.setSummary(highlightFields.get("summary").getFragments()[0].toString());
//                    }
//
//                    if (highlightFields.get("categoryName") != null) {
//                        article.setSummary(highlightFields.get("categoryName").getFragments()[0].toString());
//                    }
//
//                    if (highlightFields.get("content") != null) {
//                        article.setSummary(highlightFields.get("content").getFragments()[0].toString());
//                    }
//
//                    if (highlightFields.get("tagName") != null) {
//                        article.setSummary(highlightFields.get("tagName").getFragments()[0].toString());
//                    }
//
//                    article.setId(Integer.parseInt(searchHit.getId()));
//                    articles.add(article);
//                }
//                if (articles.size() > 0) {
//                    return new AggregatedPageImpl<>((List<T>) articles);
//                }
//                return null;
//            }
//        });
//        List<ArticleSearchResultDTO> resultList = Lists.newArrayList();
//        StringBuffer htmlResult = new StringBuffer();
//        StringBuffer noHtmlRes = new StringBuffer();
//        if (null == result)
//            return BaseResult.notOk("没有搜索到内容");
//        if (null == result.getContent())
//            return BaseResult.notOk("没有搜索到内容");
//
//        List<EsArticle> content = result.getContent();
//
//        content.parallelStream().forEach(esArticle -> {
//            if (StringUtils.isNotEmpty(esArticle.getTitle())) {
//                htmlResult.append("标题：").append(esArticle.getTitle()).append("<br/>");
//                noHtmlRes.append(esArticle.getTitle().replace("<strong>", "").replace("</strong>", "")).append(" ");
//            }
//            if (StringUtils.isNotEmpty(esArticle.getSummary())) {
//                htmlResult.append("摘要：").append(esArticle.getSummary()).append("<br/>");
//                noHtmlRes.append(esArticle.getSummary().replace("<strong>", "").replace("</strong>", "")).append(" ");
//            }
//            if (StringUtils.isNotEmpty(esArticle.getContent())) {
//                htmlResult.append("内容：").append(esArticle.getContent()).append("<br/>");
//                noHtmlRes.append(esArticle.getContent().replace("<strong>", "").replace("</strong>", "")).append(" ");
//            }
//            resultList.add(new ArticleSearchResultDTO(esArticle.getId(), htmlResult.toString(), noHtmlRes.toString()));
//            htmlResult.setLength(0);
//            noHtmlRes.setLength(0);
//        });
//        return ResultData.succeed(resultList);
//    }
//
//    @GetMapping("/searchByTitle")
//    public ResultData queryArticlesByTitle(String query) {
//        Pageable pageable = PageRequest.of(0, 100);
//        Page<EsArticle> result = articleRepository.findByTitle(query, pageable);
//        return ResultData.succeed(result);
//    }
//}
