<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output indent="yes"/>
<xsl:template match="/">
    <orden>
        <sucursal>
                <xsl:value-of select="ShipmentAdvices/@ShipNode"/>
        </sucursal>
        <sucursalTraspaso>
                <xsl:value-of select="ShipmentAdvices/@ReceivingNode"/>
        </sucursalTraspaso>
        
        <ordenOriginal><xsl:value-of select="ShipmentAdvices/@ParentSalesOrderNo"/></ordenOriginal> // Request Tranferencia
        
        
        <idPaqueteria><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@CarrierServiceCode"/></idPaqueteria> // Dato de tipo cadena
        <fcreacion><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@OrderDate"/></fcreacion>
        <idOrden><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@SalesOrderNo"/></idOrden>
        <fprogramada><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@ReqDeliveryDate"/></fprogramada>
        <fembarque><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@ReqShipDate"/></fembarque>
        <faviso><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@SADate"/></faviso>
        <repartidor><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@SCAC"/></repartidor>
        <carrierid><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@SCAC"/></carrierid>   
        <release><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/@ReleaseNo"/></release>
        
        
        <service><xsl:value-of select="ShipmentAdvices/Extn/@ExtnServiceId"/></service>
        
        
        
        <hora><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/SALines/SALine/Extn/@ExtnDateDeliveryApproximate"/></hora>  
        <tentrega><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/SALines/SALine/@FulfillmentType"/></tentrega>
        
        
        
        
        <notas> // Ninguno
            <xsl:for-each select="ShipmentAdvices/ShipmentAdvice/Instructions/Instruction">
                <xsl:value-of select="@InstructionText"/><xsl:text>, </xsl:text>
            </xsl:for-each>
        </notas>
        
        
        
        
        <cobroPorEntrega>
            <xsl:choose>
                <xsl:when test="ShipmentAdvices/ShipmentAdvice/PackListPriceInfo/@TotalCharges">
                    <xsl:text><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/PackListPriceInfo/@TotalCharges"/></xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>0</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </cobroPorEntrega>
        
         
         
        <status>0</status>
        
        
        
        
        
        <tipo>
            <xsl:choose>
                <xsl:when test="ShipmentAdvices/@ReceivingNode">
                    <xsl:text>T</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>O</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </tipo>
        
             
        <cliente>
            <personid><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@PersonID"/></personid>
            <addressLine1><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@AddressLine1"/></addressLine1>
            <addressLine2><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@AddressLine2"/></addressLine2>
            <addressLine3><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@AddressLine3"/></addressLine3>
            <addressLine4><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@AddressLine4"/></addressLine4>
            <addressLine5><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@AddressLine5"/></addressLine5>
            <addressLine6><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@AddressLine6"/></addressLine6>
            
            
            <alternateEmailID><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@AlternateEmailID"/></alternateEmailID>
            <beeper><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@Beeper"/></beeper>
            <city><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@City"/></city>
            <company><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@Company"/></company>
            <country><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@Country"/></country>
            
            <dayFaxNo><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@DayFaxNo"/></dayFaxNo>
            <dayPhone><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@DayPhone"/></dayPhone>
            <department><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@Department"/></department>
            <eMailID><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@EMailID"/></eMailID>  // Campo incorrecto
            
            
            
            <eveningFaxNo><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@EveningFaxNo"/></eveningFaxNo>
            <eveningPhone><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@EveningPhone"/></eveningPhone>
            <firstName><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@FirstName"/></firstName>
            <jobTitle><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@JobTitle"/></jobTitle>
            <lastName><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@LastName"/></lastName>
            <middleName><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@MiddleName"/></middleName>
            <mobilePhone><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@MobilePhone"/></mobilePhone>
            <otherPhone><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@OtherPhone"/></otherPhone>
            
            
            <personID><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@PersonID"/></personID> // Campo no valido
            
            
            <state><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@State"/></state>
            
            <suffix><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@Suffix"/></suffix>
            <title><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@Title"/></title>
            <zipCode><xsl:value-of select="ShipmentAdvices/ShipmentAdvice/ShipTo/@ZipCode"/></zipCode>
        </cliente>
        
        
        <xsl:for-each select="ShipmentAdvices/ShipmentAdvice/SALines/SALine">
            <lineas>
                <producto><xsl:value-of select="Item/@ItemID"/></producto>
<!--                <descripcion><xsl:value-of select="Item/@ItemShortDesc"/></descripcion>-->
                <cup><xsl:value-of select="Item/@UPCCode"/></cup>
                <pedidas><xsl:value-of select="@OrderedQty"/></pedidas>
                <numLinea><xsl:value-of select="@PrimeLineNo"/></numLinea>
                <precioVenta><xsl:value-of select="PackListPriceInfo/@UnitPrice"/></precioVenta>
                <precioLista><xsl:value-of select="PackListPriceInfo/@ListPrice"/></precioLista>
                <cantidad><xsl:value-of select="@OrderedQty"/></cantidad>
                <precioBase><xsl:value-of select="PackListPriceInfo/@UnitPrice"/></precioBase>
                <status>N</status>
                <sucursal><xsl:value-of select="/ShipmentAdvices/@ShipNode"/></sucursal>
                <idOrden><xsl:value-of select="/ShipmentAdvices/ShipmentAdvice/@SalesOrderNo"/></idOrden>
                <precio><xsl:value-of select="Item/@UnitCost"></xsl:value-of></precio>
                <surtidas><xsl:value-of select="PackListPriceInfo/@InvoicedPricingQty"></xsl:value-of></surtidas>
                <productClass><xsl:value-of select="@LineType"></xsl:value-of></productClass> 
                
                <peso>
                    <xsl:choose>
                        <xsl:when test="Item/@ItemWeight">
                            <xsl:text><xsl:value-of select="Item/@ItemWeight"/></xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>0</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </peso>
                <uom>
                    <xsl:choose>
                        <xsl:when test="Item/@ItemWeightUOM">
                            <xsl:text><xsl:value-of select="Item/@ItemWeightUOM"/></xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>KG</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </uom>
                <cargo><xsl:value-of select="PackListPriceInfo/@Charges"></xsl:value-of></cargo>
            </lineas>
        </xsl:for-each>
        
        
        <xsl:for-each select="ShipmentAdvices/Notes/Note">
            <notesOrden>
                <reasonCode><xsl:value-of select="@ReasonCode"/></reasonCode>
                <sequenceNo><xsl:value-of select="@SequenceNo"/></sequenceNo>
                <noteText><xsl:value-of select="@NoteText"/></noteText>
            </notesOrden>
        </xsl:for-each>
        
    </orden>
</xsl:template>
</xsl:transform>