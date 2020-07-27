package com.dhy.chat.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class GetUserChatMsgListDto extends PageSortAndFilterDto {

    @NotBlank
    @ApiModelProperty(required = true)
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}