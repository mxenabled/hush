/**
 * Copyright 2020 MX Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.hush.core.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="suppress">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;sequence minOccurs="0">
 *                     &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;/sequence>
 *                   &lt;choice minOccurs="0">
 *                     &lt;element name="filePath" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
 *                     &lt;element name="sha1" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}sha1Type"/>
 *                     &lt;element name="gav" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
 *                     &lt;element name="packageUrl" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
 *                   &lt;/choice>
 *                   &lt;choice maxOccurs="unbounded">
 *                     &lt;element name="cpe" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
 *                     &lt;element name="cve" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}cveType"/>
 *                     &lt;element name="vulnerabilityName" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
 *                     &lt;element name="cwe" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                     &lt;element name="cvssBelow" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}cvssScoreType"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *                 &lt;attribute name="base" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;attribute name="until" type="{http://www.w3.org/2001/XMLSchema}date" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "suppress"
})
@XmlRootElement(name = "suppressions")
public class Suppressions {

    protected List<Suppressions.Suppress> suppress;

    /**
     * Gets the value of the suppress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the suppress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuppress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Suppressions.Suppress }
     * 
     * 
     */
    public List<Suppressions.Suppress> getSuppress() {
        if (suppress == null) {
            suppress = new ArrayList<Suppressions.Suppress>();
        }
        return this.suppress;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;sequence minOccurs="0">
     *           &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;/sequence>
     *         &lt;choice minOccurs="0">
     *           &lt;element name="filePath" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
     *           &lt;element name="sha1" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}sha1Type"/>
     *           &lt;element name="gav" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
     *           &lt;element name="packageUrl" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
     *         &lt;/choice>
     *         &lt;choice maxOccurs="unbounded">
     *           &lt;element name="cpe" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
     *           &lt;element name="cve" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}cveType"/>
     *           &lt;element name="vulnerabilityName" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}regexStringType"/>
     *           &lt;element name="cwe" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *           &lt;element name="cvssBelow" type="{https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd}cvssScoreType"/>
     *         &lt;/choice>
     *       &lt;/sequence>
     *       &lt;attribute name="base" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="until" type="{http://www.w3.org/2001/XMLSchema}date" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "notes",
        "filePath",
        "sha1",
        "gav",
        "packageUrl",
        "cpeOrCveOrVulnerabilityName"
    })
    public static class Suppress {

        protected String notes;
        protected RegexStringType filePath;
        protected String sha1;
        protected RegexStringType gav;
        protected RegexStringType packageUrl;
        @XmlElementRefs({
            @XmlElementRef(name = "cwe", namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "vulnerabilityName", namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "cvssBelow", namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "cpe", namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "cve", namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", type = JAXBElement.class, required = false)
        })
        protected List<JAXBElement<?>> cpeOrCveOrVulnerabilityName;
        @XmlAttribute(name = "base")
        protected Boolean base;
        @XmlAttribute(name = "until")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar until;

        /**
         * Gets the value of the notes property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNotes() {
            return notes;
        }

        /**
         * Sets the value of the notes property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNotes(String value) {
            this.notes = value;
        }

        /**
         * Gets the value of the filePath property.
         * 
         * @return
         *     possible object is
         *     {@link RegexStringType }
         *     
         */
        public RegexStringType getFilePath() {
            return filePath;
        }

        /**
         * Sets the value of the filePath property.
         * 
         * @param value
         *     allowed object is
         *     {@link RegexStringType }
         *     
         */
        public void setFilePath(RegexStringType value) {
            this.filePath = value;
        }

        /**
         * Gets the value of the sha1 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSha1() {
            return sha1;
        }

        /**
         * Sets the value of the sha1 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSha1(String value) {
            this.sha1 = value;
        }

        /**
         * Gets the value of the gav property.
         * 
         * @return
         *     possible object is
         *     {@link RegexStringType }
         *     
         */
        public RegexStringType getGav() {
            return gav;
        }

        /**
         * Sets the value of the gav property.
         * 
         * @param value
         *     allowed object is
         *     {@link RegexStringType }
         *     
         */
        public void setGav(RegexStringType value) {
            this.gav = value;
        }

        /**
         * Gets the value of the packageUrl property.
         * 
         * @return
         *     possible object is
         *     {@link RegexStringType }
         *     
         */
        public RegexStringType getPackageUrl() {
            return packageUrl;
        }

        /**
         * Sets the value of the packageUrl property.
         * 
         * @param value
         *     allowed object is
         *     {@link RegexStringType }
         *     
         */
        public void setPackageUrl(RegexStringType value) {
            this.packageUrl = value;
        }

        /**
         * Gets the value of the cpeOrCveOrVulnerabilityName property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cpeOrCveOrVulnerabilityName property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCpeOrCveOrVulnerabilityName().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
         * {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
         * {@link JAXBElement }{@code <}{@link RegexStringType }{@code >}
         * {@link JAXBElement }{@code <}{@link RegexStringType }{@code >}
         * {@link JAXBElement }{@code <}{@link String }{@code >}
         * 
         * 
         */
        public List<JAXBElement<?>> getCpeOrCveOrVulnerabilityName() {
            if (cpeOrCveOrVulnerabilityName == null) {
                cpeOrCveOrVulnerabilityName = new ArrayList<JAXBElement<?>>();
            }
            return this.cpeOrCveOrVulnerabilityName;
        }

        /**
         * Gets the value of the base property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isBase() {
            if (base == null) {
                return false;
            } else {
                return base;
            }
        }

        /**
         * Sets the value of the base property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setBase(Boolean value) {
            this.base = value;
        }

        /**
         * Gets the value of the until property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getUntil() {
            return until;
        }

        /**
         * Sets the value of the until property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setUntil(XMLGregorianCalendar value) {
            this.until = value;
        }

    }

}
