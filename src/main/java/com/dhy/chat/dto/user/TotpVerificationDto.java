package com.dhy.chat.dto.user;

import javax.validation.constraints.NotBlank;

public class TotpVerificationDto {

    @NotBlank
    private String mfaId;

    @NotBlank
    private String code;

    public String getMfaId() {
        return mfaId;
    }

    public void setMfaId(String mfaId) {
        this.mfaId = mfaId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
