package com.fragua.model;

import jakarta.xml.bind.annotation.XmlElement;

public class Cliente {

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String addressLine6;
    private String alternateEmailID;
    private String beeper;
    private String city;
    private String company;
    private String country;
    private String dayFaxNo;
    private String dayPhone;
    private String department;
    private String eMailID;
    private String eveningFaxNo;
    private String eveningPhone;
    private String firstName;
    private String jobTitle;
    private String lastName;
    private String middleName;
    private String mobilePhone;
    private String otherPhone;
    private Integer personid;
    private String state;
    private String suffix;
    private String title;
    private String zipCode;

    public String getAddressLine1() {
        return addressLine1;
    }

    @XmlElement
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    @XmlElement
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    @XmlElement
    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressLine4() {
        return addressLine4;
    }

    @XmlElement
    public void setAddressLine4(String addressLine4) {
        this.addressLine4 = addressLine4;
    }

    public String getAddressLine5() {
        return addressLine5;
    }

    @XmlElement
    public void setAddressLine5(String addressLine5) {
        this.addressLine5 = addressLine5;
    }

    public String getAddressLine6() {
        return addressLine6;
    }

    @XmlElement
    public void setAddressLine6(String addressLine6) {
        this.addressLine6 = addressLine6;
    }

    public String getAlternateEmailID() {
        return alternateEmailID;
    }

    @XmlElement
    public void setAlternateEmailID(String alternateEmailID) {
        this.alternateEmailID = alternateEmailID;
    }

    public String getBeeper() {
        return beeper;
    }

    @XmlElement
    public void setBeeper(String beeper) {
        this.beeper = beeper;
    }

    public String getCity() {
        return city;
    }

    @XmlElement
    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    @XmlElement
    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    @XmlElement
    public void setCountry(String country) {
        this.country = country;
    }

    public String getDayFaxNo() {
        return dayFaxNo;
    }

    @XmlElement
    public void setDayFaxNo(String dayFaxNo) {
        this.dayFaxNo = dayFaxNo;
    }

    public String getDayPhone() {
        return dayPhone;
    }

    @XmlElement
    public void setDayPhone(String dayPhone) {
        this.dayPhone = dayPhone;
    }

    public String getDepartment() {
        return department;
    }

    @XmlElement
    public void setDepartment(String department) {
        this.department = department;
    }

    public String geteMailID() {
        return eMailID;
    }

    @XmlElement
    public void seteMailID(String eMailID) {
        this.eMailID = eMailID;
    }

    public String getEveningFaxNo() {
        return eveningFaxNo;
    }

    @XmlElement
    public void setEveningFaxNo(String eveningFaxNo) {
        this.eveningFaxNo = eveningFaxNo;
    }

    public String getEveningPhone() {
        return eveningPhone;
    }

    @XmlElement
    public void setEveningPhone(String eveningPhone) {
        this.eveningPhone = eveningPhone;
    }

    public String getFirstName() {
        return firstName;
    }

    @XmlElement
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    @XmlElement
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLastName() {
        return lastName;
    }

    @XmlElement
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @XmlElement
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    @XmlElement
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    @XmlElement
    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public Integer getPersonID() {
        return personid;
    }

    @XmlElement
    public void setPersonID(Integer personid) {
        this.personid = personid;
    }

    public String getState() {
        return state;
    }

    @XmlElement
    public void setState(String state) {
        this.state = state;
    }

    public String getSuffix() {
        return suffix;
    }

    @XmlElement
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTitle() {
        return title;
    }

    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }

    public String getZipCode() {
        return zipCode;
    }

    @XmlElement
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    
}