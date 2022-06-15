package com.mx.hush.core.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.mx.hush.core.models package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SuppressionsSuppressVulnerabilityName_QNAME = new QName("https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", "vulnerabilityName");
    private final static QName _SuppressionsSuppressCve_QNAME = new QName("https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", "cve");
    private final static QName _SuppressionsSuppressCvssBelow_QNAME = new QName("https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", "cvssBelow");
    private final static QName _SuppressionsSuppressCwe_QNAME = new QName("https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", "cwe");
    private final static QName _SuppressionsSuppressCpe_QNAME = new QName("https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", "cpe");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mx.hush.core.models
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Suppressions }
     * 
     */
    public Suppressions createSuppressions() {
        return new Suppressions();
    }

    /**
     * Create an instance of {@link Suppressions.Suppress }
     * 
     */
    public Suppressions.Suppress createSuppressionsSuppress() {
        return new Suppressions.Suppress();
    }

    /**
     * Create an instance of {@link RegexStringType }
     * 
     */
    public RegexStringType createRegexStringType() {
        return new RegexStringType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegexStringType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", name = "vulnerabilityName", scope = Suppressions.Suppress.class)
    public JAXBElement<RegexStringType> createSuppressionsSuppressVulnerabilityName(RegexStringType value) {
        return new JAXBElement<RegexStringType>(_SuppressionsSuppressVulnerabilityName_QNAME, RegexStringType.class, Suppressions.Suppress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", name = "cve", scope = Suppressions.Suppress.class)
    public JAXBElement<String> createSuppressionsSuppressCve(String value) {
        return new JAXBElement<String>(_SuppressionsSuppressCve_QNAME, String.class, Suppressions.Suppress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", name = "cvssBelow", scope = Suppressions.Suppress.class)
    public JAXBElement<BigDecimal> createSuppressionsSuppressCvssBelow(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_SuppressionsSuppressCvssBelow_QNAME, BigDecimal.class, Suppressions.Suppress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", name = "cwe", scope = Suppressions.Suppress.class)
    public JAXBElement<BigInteger> createSuppressionsSuppressCwe(BigInteger value) {
        return new JAXBElement<BigInteger>(_SuppressionsSuppressCwe_QNAME, BigInteger.class, Suppressions.Suppress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegexStringType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd", name = "cpe", scope = Suppressions.Suppress.class)
    public JAXBElement<RegexStringType> createSuppressionsSuppressCpe(RegexStringType value) {
        return new JAXBElement<RegexStringType>(_SuppressionsSuppressCpe_QNAME, RegexStringType.class, Suppressions.Suppress.class, value);
    }

}
