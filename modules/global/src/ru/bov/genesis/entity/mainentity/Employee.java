package ru.bov.genesis.entity.mainentity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXMessageDialog;
import ru.bov.genesis.entity.services.*;

import javax.inject.Inject;
import javax.persistence.*;
import java.awt.*;
import java.util.Date;
import java.util.Set;
import java.util.Collection;
import com.haulmont.chile.core.annotations.Composition;
import java.util.List;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;

import static javax.swing.JOptionPane.showMessageDialog;

@Listeners("genesis_EmployeeItemListener")
@NamePattern("%s %s %s %s %s %s %s|lastName,firstName,middleName,position,profession,direction_work,fieldStatus")
@Table(name = "GENESIS_EMPLOYEE")
@Entity(name = "genesis$Employee")
public class Employee extends StandardEntity {
    private static final long serialVersionUID = 4684379642843823654L;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_PHOTO_ID")
    protected FileDescriptor image_photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_SIGN_ID")
    protected FileDescriptor image_sign;

    @Column(name = "FIRST_NAME")
    protected String firstName;

    @Column(name = "MIDDLE_NAME")
    protected String middleName;

    @Column(name = "LAST_NAME")
    protected String lastName;

    @Lob
    @Column(name = "FULL_NAME")
    @MetaProperty(related = "lastName")
    private String fullName;


    @Column(name = "POSITION_")
    protected String position;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRECTION_WORK_ID")
    protected Directing direction_work;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFESSION_ID")
    protected Professions profession;




    @Temporal(TemporalType.DATE)
    @Column(name = "PASS_ISSUED_DATE")
    protected Date pass_issued_date;

    @Lob
    @Column(name = "PASS_COMMON")
    protected String pass_common;

    @Column(name = "GENDER")
    protected String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTHDAY")
    protected Date birthday;

    @Column(name = "PLACE_OF_BIRTH")
    protected String placeOfBirth;

    @Column(name = "ADDRESS_POSTAL")
    protected String addressPostal;

    @Column(name = "ADDRESS_RESIDENTIAL")
    protected String addressResidential;

    @Column(name = "ADDRESS_REGISTRATION")
    protected String addressRegistration;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_ADDRES_REGISTRATION")
    protected Date dateAddresRegistration;

    @Column(name = "MARTIAL_STATUS")
    protected Integer martialStatus;

    @Column(name = "NUMBER_IFNS")
    protected String numberIFNS;

    @Column(name = "NUMBER_INN")
    protected String numberINN;

    @Column(name = "NUMBER_PFR")
    protected String numberPFR;

    @Column(name = "PHONE_MOBILE")
    protected String phoneMobile;

    @Column(name = "PHONE_HOME")
    protected String phoneHome;

    @Column(name = "PHONE_OFFICE")
    protected String phoneOffice;

    @Column(name = "EMAIL_PRIVATE")
    protected String emailPrivate;

    @Email
    @Column(name = "EMAIL_OFFICE")
    protected String emailOffice;


    @Column(name = "NAKS_NUMBER")
    protected String naks_number;

    @Temporal(TemporalType.DATE)
    @Column(name = "NAKS_DATE_EXPIRE")
    protected Date naks_date_expire;

    @Column(name = "NAKS_NGDO")
    protected String naks_ngdo;

    @Column(name = "NAKS_OHNVP")
    protected String naks_ohnvp;

    @Column(name = "NAKS_GO")
    protected String naks_go;

    @Column(name = "NAKS_PTO")
    protected String naks_pto;

    @Column(name = "NAKS_SK")
    protected String naks_sk;

    @Column(name = "NAKS_KO")
    protected String naks_ko;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NAKS_IMAGE_ID")
    protected FileDescriptor naks_image;

    @Column(name = "VIK_NUMBER")
    protected String vik_number;

    @Temporal(TemporalType.DATE)
    @Column(name = "VIK_DATE_EXPIRE")
    protected Date vik_date_expire;

    @Column(name = "VIK_VIK")
    protected String vik_vik;

    @Column(name = "VIK_RK")
    protected String vik_rk;

    @Column(name = "VIK_VK")
    protected String vik_vk;

    @Column(name = "VIK_MK")
    protected String vik_mk;

    @Column(name = "VIK_EK")
    protected String vik_ek;

    @Column(name = "VIK_UK")
    protected String vik_uk;

    @Column(name = "VIK_PVT")
    protected String vik_pvt;

    @Column(name = "VIK_VD")
    protected String vik_vd;

    @Column(name = "VIK_PVK")
    protected String vik_pvk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VIK_IMAGE_ID")
    protected FileDescriptor vik_image;

    @Column(name = "VUZ_NAME")
    protected String vuz_name;

    @Column(name = "VUZ_SITY")
    protected String vuz_sity;

    @Column(name = "VUZ_NUMBER")
    protected String vuz_number;

    @Column(name = "VUZ_DATE_BEGIN", length = 4)
    protected String vuz_date_begin;

    @Column(name = "VUZ_DATE_END", length = 4)
    protected String vuz_date_end;

    @Column(name = "VUZ_SPEC")
    protected String vuz_spec;

    @Column(name = "VUZ_KVAL")
    protected String vuz_kval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VUZ_IMAGE_ID")
    protected FileDescriptor vuz_image;

    @Temporal(TemporalType.DATE)
    @Column(name = "MEDICAL_CHECK_BASE_BEGIN")
    protected Date medicalCheckBaseBegin;

    @Temporal(TemporalType.DATE)
    @Column(name = "MEDICAL_CHECK_BASE_END")
    protected Date medicalCheckBaseEnd;

    @Temporal(TemporalType.DATE)
    @Column(name = "MEDICAL_CHECK_PERIODIC_BEGIN")
    protected Date medicalCheckPeriodicBegin;

    @Temporal(TemporalType.DATE)
    @Column(name = "MEDICAL_CHECK_PERIODIC_END")
    protected Date medicalCheckPeriodicEnd;

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "employee")
    protected List<Courses> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUILDING_ID")
    protected Building building;

    @OneToMany(mappedBy = "employee")
    protected List<StorageFiles> storageFile;



    @Column(name = "FIELD_STATUS")
    protected String fieldStatus;

    @Lob
    @Column(name = "COMMENT_MAN")
    protected String comment_man;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    protected Department department;

    @Column(name = "DEPARTMENT_TERRITORY")
    protected String department_territory;

    @Column(name = "CONTRACT_TYPE")
    protected String contractType;

    @Temporal(TemporalType.DATE)
    @Column(name = "CONTRACT_DATE")
    protected Date contractDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EMPLOYMENT_DATE")
    protected Date employmentDate;

    @Column(name = "UD_CK_NUMBER")
    protected String udCkNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_CK_EXPIRE")
    protected Date udCkExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_CK_IMAGE_ID")
    protected FileDescriptor udCkImage;

    @Column(name = "UD_FIRST_HELP_NUMBER")
    protected String udFirstHelpNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_FIRST_HELP_EXPIRE")
    protected Date udFirstHelpExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_FIRST_HELP_IMAGE_ID")
    protected FileDescriptor udFirstHelpImage;

    @Column(name = "UD_ECO_SAF_NUMBER")
    protected String udEcoSafNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_ECO_SAF_EXPIRE")
    protected Date udEcoSafExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_ECO_SAF_IMAGE_ID")
    protected FileDescriptor udEcoSafImage;

    @Column(name = "UD_WORK_HI_NUMBER")
    protected String udWorkHiNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_WORK_HI_EXPIRE")
    protected Date udWorkHiExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_WORK_HI_IMAGE_ID")
    protected FileDescriptor udWorkHiImage;

    @Column(name = "UD_DRIVE_SAF_NUMBER")
    protected String udDriveSafNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_DRIVE_SAF_EXPIRE")
    protected Date udDriveSafExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_DRIVE_SAF_IMAGE_ID")
    protected FileDescriptor udDriveSafImage;

    @Column(name = "UD_EB_NUMBER")
    protected String udEbNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_EB_EXPIRE")
    protected Date udEbExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_EB_IMAGE_ID")
    protected FileDescriptor udEbImage;

    @Column(name = "UD_OT_NUMBER")
    protected String udOtNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_OT_EXPIRE")
    protected Date udOtExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_OT_IMAGE_ID")
    protected FileDescriptor udOtImage;

    @Column(name = "UD_PTM_NUMBER")
    protected String udPtmNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_PTM_EXPIRE")
    protected Date udPtmExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_PTM_IMAGE_ID")
    protected FileDescriptor udPtmImage;

    @Column(name = "UD_PB_NUMBER")
    protected String udPbNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "UD_PB_EXPIRE")
    protected Date udPbExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UD_PB_IMAGE_ID")
    protected FileDescriptor udPbImage;

    @Column(name = "EXPIRE_DATE_ALL")
    protected String expireDateAll;


    @OneToMany(mappedBy = "employee")
    private List<Event> event;





    @Column(name = "ARHIV")
    private Boolean arhiv;

    @OneToMany(mappedBy = "employee")
    private List<StorageFiles> vuzFiles;

    @OneToMany(mappedBy = "employee")
    private List<StorageFiles> vikFiles;

    @OneToMany(mappedBy = "employee")
    private List<StorageFiles> naksFiles;

    public void setVuzFiles(List<StorageFiles> vuzFiles) {
        this.vuzFiles = vuzFiles;
    }

    public List<StorageFiles> getVuzFiles() {
        return vuzFiles;
    }

    public void setVikFiles(List<StorageFiles> vikFiles) {
        this.vikFiles = vikFiles;
    }

    public List<StorageFiles> getVikFiles() {
        return vikFiles;
    }

    public void setNaksFiles(List<StorageFiles> naksFiles) {
        this.naksFiles = naksFiles;
    }

    public List<StorageFiles> getNaksFiles() {
        return naksFiles;
    }


    public void setArhiv(Boolean arhiv) {
        this.arhiv = arhiv;
    }

    public Boolean getArhiv() {
        return arhiv;
    }


    public void setEvent(List<Event> event) {
        this.event = event;
    }

    public List<Event> getEvent() {
        return event;
    }


    public void setExpireDateAll(String expireDateAll) {
        this.expireDateAll = expireDateAll;
    }

    public String getExpireDateAll() {
        return expireDateAll;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }



    public Date getUdFirstHelpExpire() {
        return udFirstHelpExpire;
    }

    public void setUdFirstHelpExpire(Date udFirstHelpExpire) {
        this.udFirstHelpExpire = udFirstHelpExpire;
    }


    public void setUdCkNumber(String udCkNumber) {
        this.udCkNumber = udCkNumber;
    }

    public String getUdCkNumber() {
        return udCkNumber;
    }

    public void setUdCkExpire(Date udCkExpire) {
        this.udCkExpire = udCkExpire;
    }

    public Date getUdCkExpire() {
        return udCkExpire;
    }

    public void setUdCkImage(FileDescriptor udCkImage) {
        this.udCkImage = udCkImage;
    }

    public FileDescriptor getUdCkImage() {
        return udCkImage;
    }

    public void setUdFirstHelpNumber(String udFirstHelpNumber) {
        this.udFirstHelpNumber = udFirstHelpNumber;
    }

    public String getUdFirstHelpNumber() {
        return udFirstHelpNumber;
    }

    public void setUdFirstHelpImage(FileDescriptor udFirstHelpImage) {
        this.udFirstHelpImage = udFirstHelpImage;
    }

    public FileDescriptor getUdFirstHelpImage() {
        return udFirstHelpImage;
    }

    public void setUdEcoSafNumber(String udEcoSafNumber) {
        this.udEcoSafNumber = udEcoSafNumber;
    }

    public String getUdEcoSafNumber() {
        return udEcoSafNumber;
    }

    public void setUdEcoSafExpire(Date udEcoSafExpire) {
        this.udEcoSafExpire = udEcoSafExpire;
    }

    public Date getUdEcoSafExpire() {
        return udEcoSafExpire;
    }

    public void setUdEcoSafImage(FileDescriptor udEcoSafImage) {
        this.udEcoSafImage = udEcoSafImage;
    }

    public FileDescriptor getUdEcoSafImage() {
        return udEcoSafImage;
    }

    public void setUdWorkHiNumber(String udWorkHiNumber) {
        this.udWorkHiNumber = udWorkHiNumber;
    }

    public String getUdWorkHiNumber() {
        return udWorkHiNumber;
    }

    public void setUdWorkHiExpire(Date udWorkHiExpire) {
        this.udWorkHiExpire = udWorkHiExpire;
    }

    public Date getUdWorkHiExpire() {
        return udWorkHiExpire;
    }

    public void setUdWorkHiImage(FileDescriptor udWorkHiImage) {
        this.udWorkHiImage = udWorkHiImage;
    }

    public FileDescriptor getUdWorkHiImage() {
        return udWorkHiImage;
    }

    public void setUdDriveSafNumber(String udDriveSafNumber) {
        this.udDriveSafNumber = udDriveSafNumber;
    }

    public String getUdDriveSafNumber() {
        return udDriveSafNumber;
    }

    public void setUdDriveSafExpire(Date udDriveSafExpire) {
        this.udDriveSafExpire = udDriveSafExpire;
    }

    public Date getUdDriveSafExpire() {
        return udDriveSafExpire;
    }

    public void setUdDriveSafImage(FileDescriptor udDriveSafImage) {
        this.udDriveSafImage = udDriveSafImage;
    }

    public FileDescriptor getUdDriveSafImage() {
        return udDriveSafImage;
    }

    public void setUdEbNumber(String udEbNumber) {
        this.udEbNumber = udEbNumber;
    }

    public String getUdEbNumber() {
        return udEbNumber;
    }

    public void setUdEbExpire(Date udEbExpire) {
        this.udEbExpire = udEbExpire;
    }

    public Date getUdEbExpire() {
        return udEbExpire;
    }

    public void setUdEbImage(FileDescriptor udEbImage) {
        this.udEbImage = udEbImage;
    }

    public FileDescriptor getUdEbImage() {
        return udEbImage;
    }

    public void setUdOtNumber(String udOtNumber) {
        this.udOtNumber = udOtNumber;
    }

    public String getUdOtNumber() {
        return udOtNumber;
    }

    public void setUdOtExpire(Date udOtExpire) {
        this.udOtExpire = udOtExpire;
    }

    public Date getUdOtExpire() {
        return udOtExpire;
    }

    public void setUdOtImage(FileDescriptor udOtImage) {
        this.udOtImage = udOtImage;
    }

    public FileDescriptor getUdOtImage() {
        return udOtImage;
    }

    public void setUdPtmNumber(String udPtmNumber) {
        this.udPtmNumber = udPtmNumber;
    }

    public String getUdPtmNumber() {
        return udPtmNumber;
    }

    public void setUdPtmExpire(Date udPtmExpire) {
        this.udPtmExpire = udPtmExpire;
    }

    public Date getUdPtmExpire() {
        return udPtmExpire;
    }

    public void setUdPtmImage(FileDescriptor udPtmImage) {
        this.udPtmImage = udPtmImage;
    }

    public FileDescriptor getUdPtmImage() {
        return udPtmImage;
    }

    public void setUdPbNumber(String udPbNumber) {
        this.udPbNumber = udPbNumber;
    }

    public String getUdPbNumber() {
        return udPbNumber;
    }

    public void setUdPbExpire(Date udPbExpire) {
        this.udPbExpire = udPbExpire;
    }

    public Date getUdPbExpire() {
        return udPbExpire;
    }

    public void setUdPbImage(FileDescriptor udPbImage) {
        this.udPbImage = udPbImage;
    }

    public FileDescriptor getUdPbImage() {
        return udPbImage;
    }


    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }


    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractType() {
        return contractType;
    }



    public void setNumberIFNS(String numberIFNS) {
        this.numberIFNS = numberIFNS;
    }

    public String getNumberIFNS() {
        return numberIFNS;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment_territory(String department_territory) {
        this.department_territory = department_territory;
    }

    public String getDepartment_territory() {
        return department_territory;
    }


    public void setComment_man(String comment_man) {
        this.comment_man = comment_man;
    }

    public String getComment_man() {
        return comment_man;
    }


    public void setImage_photo(FileDescriptor image_photo) {
        this.image_photo = image_photo;
    }

    public FileDescriptor getImage_photo() {
        return image_photo;
    }

    public void setImage_sign(FileDescriptor image_sign) {
        this.image_sign = image_sign;
    }

    public FileDescriptor getImage_sign() {
        return image_sign;
    }

    public void setPass_common(String pass_common) {
        this.pass_common = pass_common;
    }

    public String getPass_common() {
        return pass_common;
    }


    public String getFullName() {
        return getLastName() + " " + getFirstName() + " " + getMiddleName();
    }

    public void setFullName(String fullName) {
        this.fullName = getLastName() + " " + getFirstName() + " " + getMiddleName();
    }


    public void setFieldStatus(String fieldStatus) {
        this.fieldStatus = fieldStatus;
    }

    public String getFieldStatus() {
        return fieldStatus;
    }







    public void setStorageFile(List<StorageFiles> storageFile) {
        this.storageFile = storageFile;
    }

    public List<StorageFiles> getStorageFile() {
        return storageFile;
    }


    public Building getBuilding() { return building; }

    public void setBuilding(Building building) { this.building = building; }


    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }


    public void setMedicalCheckBaseBegin(Date medicalCheckBaseBegin) {
        this.medicalCheckBaseBegin = medicalCheckBaseBegin;
    }

    public Date getMedicalCheckBaseBegin() {
        return medicalCheckBaseBegin;
    }

    public void setMedicalCheckBaseEnd(Date medicalCheckBaseEnd) {
        this.medicalCheckBaseEnd = medicalCheckBaseEnd;
    }

    public Date getMedicalCheckBaseEnd() {
        return medicalCheckBaseEnd;
    }

    public void setMedicalCheckPeriodicBegin(Date medicalCheckPeriodicBegin) {
        this.medicalCheckPeriodicBegin = medicalCheckPeriodicBegin;
    }

    public Date getMedicalCheckPeriodicBegin() {
        return medicalCheckPeriodicBegin;
    }

    public void setMedicalCheckPeriodicEnd(Date medicalCheckPeriodicEnd) {
        this.medicalCheckPeriodicEnd = medicalCheckPeriodicEnd;
    }

    public Date getMedicalCheckPeriodicEnd() {
        return medicalCheckPeriodicEnd;
    }






    public String getVuz_date_begin() {
        return vuz_date_begin;
    }

    public void setVuz_date_begin(String vuz_date_begin) {
        this.vuz_date_begin = vuz_date_begin;
    }


    public String getVuz_date_end() {
        return vuz_date_end;
    }

    public void setVuz_date_end(String vuz_date_end) {
        this.vuz_date_end = vuz_date_end;
    }


    public void setVuz_name(String vuz_name) {
        this.vuz_name = vuz_name;
    }

    public String getVuz_name() {
        return vuz_name;
    }

    public void setVuz_sity(String vuz_sity) {
        this.vuz_sity = vuz_sity;
    }

    public String getVuz_sity() {
        return vuz_sity;
    }

    public void setVuz_number(String vuz_number) {
        this.vuz_number = vuz_number;
    }

    public String getVuz_number() {
        return vuz_number;
    }

    public void setVuz_spec(String vuz_spec) {
        this.vuz_spec = vuz_spec;
    }

    public String getVuz_spec() {
        return vuz_spec;
    }

    public void setVuz_kval(String vuz_kval) {
        this.vuz_kval = vuz_kval;
    }

    public String getVuz_kval() {
        return vuz_kval;
    }

    public void setVuz_image(FileDescriptor vuz_image) {
        this.vuz_image = vuz_image;
    }

    public FileDescriptor getVuz_image() {
        return vuz_image;
    }


    public void setNaks_image(FileDescriptor naks_image) {
        this.naks_image = naks_image;
    }

    public FileDescriptor getNaks_image() {
        return naks_image;
    }

    public void setVik_image(FileDescriptor vik_image) {
        this.vik_image = vik_image;
    }

    public FileDescriptor getVik_image() {
        return vik_image;
    }


    public void setNaks_number(String naks_number) {
        this.naks_number = naks_number;
    }

    public String getNaks_number() {
        return naks_number;
    }

    public void setNaks_date_expire(Date naks_date_expire) {
        this.naks_date_expire = naks_date_expire;
    }

    public Date getNaks_date_expire() {
        return naks_date_expire;
    }

    public void setNaks_ngdo(String naks_ngdo) {
        this.naks_ngdo = naks_ngdo;
    }

    public String getNaks_ngdo() {
        return naks_ngdo;
    }

    public void setNaks_ohnvp(String naks_ohnvp) {
        this.naks_ohnvp = naks_ohnvp;
    }

    public String getNaks_ohnvp() {
        return naks_ohnvp;
    }

    public void setNaks_go(String naks_go) {
        this.naks_go = naks_go;
    }

    public String getNaks_go() {
        return naks_go;
    }

    public void setNaks_pto(String naks_pto) {
        this.naks_pto = naks_pto;
    }

    public String getNaks_pto() {
        return naks_pto;
    }

    public void setNaks_sk(String naks_sk) {
        this.naks_sk = naks_sk;
    }

    public String getNaks_sk() {
        return naks_sk;
    }

    public void setNaks_ko(String naks_ko) {
        this.naks_ko = naks_ko;
    }

    public String getNaks_ko() {
        return naks_ko;
    }

    public void setVik_number(String vik_number) {
        this.vik_number = vik_number;
    }

    public String getVik_number() {
        return vik_number;
    }

    public void setVik_date_expire(Date vik_date_expire) {
        this.vik_date_expire = vik_date_expire;
    }

    public Date getVik_date_expire() {
        return vik_date_expire;
    }

    public void setVik_vik(String vik_vik) {
        this.vik_vik = vik_vik;
    }

    public String getVik_vik() {
        return vik_vik;
    }

    public void setVik_rk(String vik_rk) {
        this.vik_rk = vik_rk;
    }

    public String getVik_rk() {
        return vik_rk;
    }

    public void setVik_vk(String vik_vk) {
        this.vik_vk = vik_vk;
    }

    public String getVik_vk() {
        return vik_vk;
    }

    public void setVik_mk(String vik_mk) {
        this.vik_mk = vik_mk;
    }

    public String getVik_mk() {
        return vik_mk;
    }

    public void setVik_ek(String vik_ek) {
        this.vik_ek = vik_ek;
    }

    public String getVik_ek() {
        return vik_ek;
    }

    public void setVik_uk(String vik_uk) {
        this.vik_uk = vik_uk;
    }

    public String getVik_uk() {
        return vik_uk;
    }

    public void setVik_pvt(String vik_pvt) {
        this.vik_pvt = vik_pvt;
    }

    public String getVik_pvt() {
        return vik_pvt;
    }

    public void setVik_vd(String vik_vd) {
        this.vik_vd = vik_vd;
    }

    public String getVik_vd() {
        return vik_vd;
    }

    public void setVik_pvk(String vik_pvk) {
        this.vik_pvk = vik_pvk;
    }

    public String getVik_pvk() {
        return vik_pvk;
    }


    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    public String getAddressPostal() {
        return addressPostal;
    }

    public void setMartialStatus(MaritalStatus martialStatus) {
        this.martialStatus = martialStatus == null ? null : martialStatus.getId();
    }

    public MaritalStatus getMartialStatus() {
        return martialStatus == null ? null : MaritalStatus.fromId(martialStatus);
    }

    public void setNumberINN(String numberINN) {
        this.numberINN = numberINN;
    }

    public String getNumberINN() {
        return numberINN;
    }

    public void setNumberPFR(String numberPFR) {
        this.numberPFR = numberPFR;
    }

    public String getNumberPFR() {
        return numberPFR;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    public String getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public void setEmailPrivate(String emailPrivate) {
        this.emailPrivate = emailPrivate;
    }

    public String getEmailPrivate() {
        return emailPrivate;
    }

    public void setEmailOffice(String emailOffice) {
        this.emailOffice = emailOffice;
    }

    public String getEmailOffice() {
        return emailOffice;
    }


    public void setGender(GenderMan gender) {
        this.gender = gender == null ? null : gender.getId();
    }

    public GenderMan getGender() {
        return gender == null ? null : GenderMan.fromId(gender);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setAddressResidential(String addressResidential) {
        this.addressResidential = addressResidential;
    }

    public String getAddressResidential() {
        return addressResidential;
    }

    public void setAddressRegistration(String addressRegistration) {
        this.addressRegistration = addressRegistration;
    }

    public String getAddressRegistration() {
        return addressRegistration;
    }

    public void setDateAddresRegistration(Date dateAddresRegistration) {
        this.dateAddresRegistration = dateAddresRegistration;
    }

    public Date getDateAddresRegistration() {
        return dateAddresRegistration;
    }








    public void setPass_issued_date(Date pass_issued_date) {
        this.pass_issued_date = pass_issued_date;
    }

    public Date getPass_issued_date() {
        return pass_issued_date;
    }





    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }



    public void setDirection_work(Directing direction_work) {
        this.direction_work = direction_work;
    }

    public Directing getDirection_work() {
        return direction_work;
    }

    public void setProfession(Professions profession) {
        this.profession = profession;
    }

    public Professions getProfession() {
        return profession;
    }


}