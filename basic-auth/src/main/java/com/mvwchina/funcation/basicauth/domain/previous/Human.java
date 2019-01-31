package com.mvwchina.funcation.basicauth.domain.previous;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ca_t_human")
public class Human {
    //系统字段
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String caId;//ca id

    private String comments;//系统备注
    private Boolean deleted;//是否已删除
    private Boolean frozen;//是否已冻结
    private Boolean deletedByInstitute;//是否已被机构删除
    private Date createTime;//创建时间
    private Date modifiedDate;//修改时间
    private Date lockedUntil;//锁定截至时间
    //公共字段
    private String name;//姓名
    //    private String allRoles;//所有角色编码集合
    private String account;//用户名

    @Where(clause = "deleted is null or deleted !=1")
    @OneToMany(mappedBy = "human", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<HumanRole> humanRoleList;//角色列表
    private String allRoles;//所有角色编码大串
    private String cellphone;//手机号码
    private String identificationTypeCode;//证件类型编码
    private String identificationNumber;//证件号码
    private String email;//电子邮箱
    private String pwd;//密码
    private String remark;//备注
    private String portrait;//头像
    //业务必须字段
    private String instituteNumber;//机构编码
    private String departmentId;//科室id
    private String idTypeCode;//身份类型编码
    private String traineeMajorCode;//参培专业编码
    private Integer traineeYear;//参培年份
    private Boolean hasLicense;//是否获得医师资格证书
    private String licenseNum;//医师资格证书号码
    private String internshipMajorCode;//实习专业编码
    private String apprenticeMajorCode;//见习专业编码
    private String postgraduateMajorCode;//研究生专业编码
    private String advancedMajorCode;//进修生专业编码
    private String nursingDepartmentId;//所在护理部id
    //业务选填字段
    private String genderCode;//性别编码
    private Date birthday;//生日
    private String ethnicityCode;//民族编码
    private String qq;//qq号码
    private String weChat;//微信号码
    private String address;//家庭住址
    private String diplomaCode;//学历编码
    private String degreeCode;//学位编码
    private String degreeTypeCode;//学位类型编码
    private String graduationCollege;//毕业院校
    private String graduationMajor;//毕业专业
    private Integer graduationYear;//毕业年份
    private String specialityTitleCode;//专业技术职称编码
    private String occupation;//现任职务

    private Date occupationStartFrom;//现职聘用时间
    private Boolean isIntermediateOver3Years;//是否中级满3年

    private Date teachingStartFrom;//起始带教时间
    private String lengthOfTeaching;//带教年限
    private String teachingSubjectCode;//可带教专业
    private String studentFreshTypeCode;//应届/往届生
    //业务选填字段（委培）
    private String formerWorkPlace;//来参加培训人员的原工作单位
    private String entrustingHospitalLevelCode;//委培医院级别编码
    private String entrustingHospitalGradeCode;//委培医院等次编码
    private String entrustingMedicalInstituteTypeCode;//委培医疗卫生机构类别编码
    private String entrustingMedicalInstituteCharacterCode;//委培医疗卫生机构性质编码
    //业务选填字段（轮转）
    private String schedulingDepartmentId;//轮转科室id
    private String duration;//培训年限
    private Boolean isTrainedCooperatively;//是否在协同单位培训
    private String cooperativeInstitute;//协同单位
    private String studentSourceCode;//学生来源编码
    private String grade;//年级
    private String englishLevelCode;//英语水平编码
    private String computerLevelCode;//计算机水平编码

    private Date admissionDate;//入院时间

    private Date departureDate;//离院时间
    //带教
    private String trainerCapableSubjectCode;//住培生导师可带教专业编码
    private String tutorCapableSubjectCode;//研究生导师可带教专业编码

    private Date beAttendingTime;//聘任主治职称时间
    private Boolean hasCertificate;//是否有师资证
    private String certificateNum;//师资证号
    private String certificateTypeCode;//师资证件类型编码
    //管理员
    @Where(clause = "deleted is null or deleted !=1")
    @OneToMany(mappedBy = "human", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<InchargeDepartment> inchargeDepartmentList;//管理的部门列表

    @Where(clause = "deleted is null or deleted !=1")
    @OneToMany(mappedBy = "human", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<InchargeSubject> inchargeSubjectList;//管理的专业列表

}