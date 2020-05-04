//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.05.05 at 12:48:45 AM MSK 
//


package iso.std.iso._20022.tech.xsd.pacs_028_001;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the iso.std.iso._20022.tech.xsd.pacs_028_001 package. 
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

    private final static QName _Document_QNAME = new QName("urn:iso:std:iso:20022:tech:xsd:pacs.028.001.03", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: iso.std.iso._20022.tech.xsd.pacs_028_001
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Document }
     * 
     */
    public Document createDocument() {
        return new Document();
    }

    /**
     * Create an instance of {@link BranchAndFinancialInstitutionIdentification61 }
     * 
     */
    public BranchAndFinancialInstitutionIdentification61 createBranchAndFinancialInstitutionIdentification61() {
        return new BranchAndFinancialInstitutionIdentification61();
    }

    /**
     * Create an instance of {@link ClearingSystemMemberIdentification21 }
     * 
     */
    public ClearingSystemMemberIdentification21 createClearingSystemMemberIdentification21() {
        return new ClearingSystemMemberIdentification21();
    }

    /**
     * Create an instance of {@link FIToFIPaymentStatusRequestV03 }
     * 
     */
    public FIToFIPaymentStatusRequestV03 createFIToFIPaymentStatusRequestV03() {
        return new FIToFIPaymentStatusRequestV03();
    }

    /**
     * Create an instance of {@link FinancialInstitutionIdentification181 }
     * 
     */
    public FinancialInstitutionIdentification181 createFinancialInstitutionIdentification181() {
        return new FinancialInstitutionIdentification181();
    }

    /**
     * Create an instance of {@link GroupHeader911 }
     * 
     */
    public GroupHeader911 createGroupHeader911() {
        return new GroupHeader911();
    }

    /**
     * Create an instance of {@link OriginalGroupInformation271 }
     * 
     */
    public OriginalGroupInformation271 createOriginalGroupInformation271() {
        return new OriginalGroupInformation271();
    }

    /**
     * Create an instance of {@link OriginalTransactionReference281 }
     * 
     */
    public OriginalTransactionReference281 createOriginalTransactionReference281() {
        return new OriginalTransactionReference281();
    }

    /**
     * Create an instance of {@link PaymentTransaction1131 }
     * 
     */
    public PaymentTransaction1131 createPaymentTransaction1131() {
        return new PaymentTransaction1131();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Document }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Document }{@code >}
     */
    @XmlElementDecl(namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.028.001.03", name = "Document")
    public JAXBElement<Document> createDocument(Document value) {
        return new JAXBElement<Document>(_Document_QNAME, Document.class, null, value);
    }

}
