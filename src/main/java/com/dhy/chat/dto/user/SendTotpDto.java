package com.dhy.chat.dto.user;

import com.dhy.chat.enums.MfaType;

import javax.validation.constraints.NotBlank;

/**
 * @author vghosthunter
 */
public class SendTotpDto {

    @NotBlank
    private String mfaId;

    private MfaType mfaType = MfaType.EMAIL;

    public String getMfaId() {
        return mfaId;
    }

    public void setMfaId(String mfaId) {
        this.mfaId = mfaId;
    }

    public MfaType getMfaType() {
        return mfaType;
    }

    public void setMfaType(MfaType mfaType) {
        this.mfaType = mfaType;
    }
}
