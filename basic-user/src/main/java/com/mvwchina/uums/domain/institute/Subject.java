package com.mvwchina.uums.domain.institute;

import com.mvwchina.domain.BaseEntity;
import com.mvwchina.enumeration.SubjectEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 下午2:59
 */
@Getter
@Setter
@Entity
public class Subject extends BaseEntity {

    @ManyToOne
    private Institute institute;

    /* 编码 */
    private String code;

    /* 名称 */
    private String name;

    /* 描述 */
    private String description;

    /* 对应国家标准专业 */
    @Enumerated(EnumType.STRING)
    private SubjectEnum subjectEnum;

    @OneToMany(mappedBy = "subject")
    private List<Department> departments;
}
