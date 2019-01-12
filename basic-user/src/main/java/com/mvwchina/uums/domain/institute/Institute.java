package com.mvwchina.uums.domain.institute;

import com.mvwchina.domain.BaseEntity;
import com.mvwchina.enumeration.GradeEnum;
import com.mvwchina.enumeration.LevelEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Name: 机构
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 下午2:38
 */
@Getter
@Setter
@Entity
public class Institute extends BaseEntity {

    /* 机构编码 */
    private String code;

    @OneToMany(mappedBy = "institute")
    private List<Subject> subjects;

    /* 委培医院级别 */
    @Enumerated(EnumType.STRING)
    private LevelEnum levelEnum;

    /* 委培医院等次 */
    @Enumerated(EnumType.STRING)
    private GradeEnum gradeEnum;
}
