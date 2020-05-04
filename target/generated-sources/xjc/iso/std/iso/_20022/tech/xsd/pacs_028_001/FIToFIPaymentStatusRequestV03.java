//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.05.05 at 12:48:45 AM MSK 
//


package iso.std.iso._20022.tech.xsd.pacs_028_001;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FIToFIPaymentStatusRequestV03 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FIToFIPaymentStatusRequestV03"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GrpHdr" type="{urn:iso:std:iso:20022:tech:xsd:pacs.028.001.03}GroupHeader91__1"/&gt;
 *         &lt;element name="OrgnlGrpInf" type="{urn:iso:std:iso:20022:tech:xsd:pacs.028.001.03}OriginalGroupInformation27__1"/&gt;
 *         &lt;element name="TxInf" type="{urn:iso:std:iso:20022:tech:xsd:pacs.028.001.03}PaymentTransaction113__1"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FIToFIPaymentStatusRequestV03", propOrder = {
    "grpHdr",
    "orgnlGrpInf",
    "txInf"
})
public class FIToFIPaymentStatusRequestV03 {

    @XmlElement(name = "GrpHdr", required = true)
    protected GroupHeader911 grpHdr;
    @XmlElement(name = "OrgnlGrpInf", required = true)
    protected OriginalGroupInformation271 orgnlGrpInf;
    @XmlElement(name = "TxInf", required = true)
    protected PaymentTransaction1131 txInf;

    /**
     * Gets the value of the grpHdr property.
     * 
     * @return
     *     possible object is
     *     {@link GroupHeader911 }
     *     
     */
    public GroupHeader911 getGrpHdr() {
        return grpHdr;
    }

    /**
     * Sets the value of the grpHdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupHeader911 }
     *     
     */
    public void setGrpHdr(GroupHeader911 value) {
        this.grpHdr = value;
    }

    /**
     * Gets the value of the orgnlGrpInf property.
     * 
     * @return
     *     possible object is
     *     {@link OriginalGroupInformation271 }
     *     
     */
    public OriginalGroupInformation271 getOrgnlGrpInf() {
        return orgnlGrpInf;
    }

    /**
     * Sets the value of the orgnlGrpInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link OriginalGroupInformation271 }
     *     
     */
    public void setOrgnlGrpInf(OriginalGroupInformation271 value) {
        this.orgnlGrpInf = value;
    }

    /**
     * Gets the value of the txInf property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentTransaction1131 }
     *     
     */
    public PaymentTransaction1131 getTxInf() {
        return txInf;
    }

    /**
     * Sets the value of the txInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentTransaction1131 }
     *     
     */
    public void setTxInf(PaymentTransaction1131 value) {
        this.txInf = value;
    }

}
