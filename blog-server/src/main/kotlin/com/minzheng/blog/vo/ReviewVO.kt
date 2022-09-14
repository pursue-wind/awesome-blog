package com.minzheng.blog.vo

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import lombok.Builder
import lombok.Data
import javax.validation.constraints.NotNull

/**
 * 审核
 *
 * @author c
 * @date 2021/08/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "审核")
class ReviewVO(
    /**
     * id列表
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    val idList: MutableList<Int>? = null,

    /**
     * 状态值
     */
    @NotNull(message = "状态值不能为空")
    @ApiModelProperty(name = "isDelete", value = "删除状态", required = true, dataType = "Integer")
    val isReview: Int? = null
)