/**
 * This file is part of JEMMA - http://jemma.energy-home.org
 * (C) Copyright 2013 Telecom Italia (http://www.telecomitalia.it)
 *
 * JEMMA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) version 3
 * or later as published by the Free Software Foundation, which accompanies
 * this distribution and is available at http://www.gnu.org/licenses/lgpl.html
 *
 * JEMMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License (LGPL) for more details.
 *
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.9-03/31/2009 04:14 PM(snajper)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.03 at 05:23:14 PM CEST 
//


package org.energy_home.jemma.zgd.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InterPANMessageResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InterPANMessageResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ASDUHandle" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" minOccurs="0"/>
 *         &lt;element name="ConfirmStatus" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterPANMessageResult", propOrder = {
    "asduHandle",
    "confirmStatus"
})
public class InterPANMessageResult {

    @XmlElement(name = "ASDUHandle")
    @XmlSchemaType(name = "unsignedByte")
    protected Short asduHandle;
    @XmlElement(name = "ConfirmStatus")
    @XmlSchemaType(name = "unsignedByte")
    protected Short confirmStatus;

    /**
     * Gets the value of the asduHandle property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getASDUHandle() {
        return asduHandle;
    }

    /**
     * Sets the value of the asduHandle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setASDUHandle(Short value) {
        this.asduHandle = value;
    }

    /**
     * Gets the value of the confirmStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getConfirmStatus() {
        return confirmStatus;
    }

    /**
     * Sets the value of the confirmStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setConfirmStatus(Short value) {
        this.confirmStatus = value;
    }

}
