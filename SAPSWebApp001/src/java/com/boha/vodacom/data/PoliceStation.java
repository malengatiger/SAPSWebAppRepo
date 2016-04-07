/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aubreymalabie
 */
@Entity
@Table(name = "policeStation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoliceStation.findAll", query = "SELECT p FROM PoliceStation p"),
    @NamedQuery(name = "PoliceStation.findByPoliceStationID", query = "SELECT p FROM PoliceStation p WHERE p.policeStationID = :policeStationID"),
    @NamedQuery(name = "PoliceStation.findByName", query = "SELECT p FROM PoliceStation p WHERE p.name = :name"),
    @NamedQuery(name = "PoliceStation.findByLatitude", query = "SELECT p FROM PoliceStation p WHERE p.latitude = :latitude"),
    @NamedQuery(name = "PoliceStation.findByLongitude", query = "SELECT p FROM PoliceStation p WHERE p.longitude = :longitude"),
    @NamedQuery(name = "PoliceStation.findByEmail", query = "SELECT p FROM PoliceStation p WHERE p.email = :email"),
    @NamedQuery(name = "PoliceStation.findByCellphone", query = "SELECT p FROM PoliceStation p WHERE p.cellphone = :cellphone")})
public class PoliceStation implements Serializable {

    @OneToMany(mappedBy = "policeStation")
    private List<PanicIncident> panicIncidentList;

    @JoinColumn(name = "cityID", referencedColumnName = "cityID")
    @ManyToOne
    private City city;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "policeStationID")
    private Integer policeStationID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name")
    private String name;
    @Lob
    @Size(max = 65535)
    @Column(name = "address")
    private String address;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Size(max = 256)
    @Column(name = "email")
    private String email;
    @Size(max = 50)
    @Column(name = "cellphone")
    private String cellphone;
    @OneToMany(mappedBy = "policeStation")
    private List<Officer> officerList;

    public PoliceStation() {
    }

    public PoliceStation(Integer policeStationID) {
        this.policeStationID = policeStationID;
    }

    public PoliceStation(Integer policeStationID, String name) {
        this.policeStationID = policeStationID;
        this.name = name;
    }

    public Integer getPoliceStationID() {
        return policeStationID;
    }

    public void setPoliceStationID(Integer policeStationID) {
        this.policeStationID = policeStationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @XmlTransient
    public List<Officer> getOfficerList() {
        return officerList;
    }

    public void setOfficerList(List<Officer> officerList) {
        this.officerList = officerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (policeStationID != null ? policeStationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoliceStation)) {
            return false;
        }
        PoliceStation other = (PoliceStation) object;
        if ((this.policeStationID == null && other.policeStationID != null) || (this.policeStationID != null && !this.policeStationID.equals(other.policeStationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.vodacom.data.PoliceStation[ policeStationID=" + policeStationID + " ]";
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @XmlTransient
    public List<PanicIncident> getPanicIncidentList() {
        return panicIncidentList;
    }

    public void setPanicIncidentList(List<PanicIncident> panicIncidentList) {
        this.panicIncidentList = panicIncidentList;
    }

    
}
