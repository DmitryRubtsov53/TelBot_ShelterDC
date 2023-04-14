package com.devsteam.getname.telbot_shelterdc.model;

import java.util.List;

//@Entity
//@Table(name = "cat_owner")
public class CatOwner {             // Модель базы данных владельцев кошек
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_do",nullable = false)
    private Long idDO;
//    @Column(name = "chat_id")
    private Long chatId;
//    @Column(name = "full_name")
    private String fullName;
//    @Column(name = "phone")
    private String phone;
//    @Column(name = "address")
    private String address;
//    @Column(name = "status")
    private StatusOwner status;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cat_id")
    private Cat cat;    // На испытательный срок - одно животное в одни руки.

//    @OneToMany(mappedBy = "cat_owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportList;  // Архив ежедневных отчетов "усыновителя" питомца.

// --------------------- Constructors ---------------------------------------------------
    public CatOwner() {}

    public CatOwner(String fullName, String phone, String address, StatusOwner status) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public CatOwner(Long idDO, Long chatId, String fullName, String phone, String address, StatusOwner status) {
        this.idDO = idDO;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }
//------------ Getters & setters -------------------------------------------------------

    public Long getIdDO() {
        return idDO;
    }

    public void setIdDO(Long idDO) {
        this.idDO = idDO;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StatusOwner getStatus() {
        return status;
    }

    public void setStatus(StatusOwner status) {
        this.status = status;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }
}
