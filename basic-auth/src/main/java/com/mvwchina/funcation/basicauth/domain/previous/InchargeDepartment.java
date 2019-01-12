package com.mvwchina.funcation.basicauth.domain.previous;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ca_t_inchargedepartment")
public class InchargeDepartment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String caId;//ca id

    private String departmentId;//部门 id
    private String roleCode;//角色编码
    private Boolean deleted = false;//是否已删除

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "humanId")
    private Human human;
}
