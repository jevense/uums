package com.mvwchina.funcation.basicauth.domain;

import com.mvwchina.domain.BaseEntity;
import com.mvwchina.enumeration.DeviceEnum;
import com.mvwchina.enumeration.OSEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Calendar;

@Getter
@Setter
@Entity
public class LoginHistory extends BaseEntity {

    private String userID;

    private String token;

    private String ip;

    private OSEnum osEnum;

    private DeviceEnum deviceEnum;

    private String message;

    private Calendar loginTime;

}
