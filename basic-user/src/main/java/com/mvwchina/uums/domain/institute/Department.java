package com.mvwchina.uums.domain.institute;

import com.mvwchina.domain.BaseEntity;
import com.mvwchina.uums.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 下午2:42
 */
@Getter
@Setter
@Entity
public class Department extends BaseEntity {

    @ManyToOne
    private Subject subject;

    @OneToMany(mappedBy = "department")
    private List<User> users;

}
