<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		version="2.0"
>

<xsl:template match="*">
    <xsl:copy>
        <xsl:copy-of select="@*"/>
        <xsl:apply-templates />
    </xsl:copy>
</xsl:template>

<!-- substitui uma tag por outra copiando os valores de 
	atributo namesapce e descendentes-->
	<xsl:template match="//element()[(namespace-uri(.)='http://www.example.org/GolfCountryClub')and(local-name(.)='GolfClub')]">
	<xsl:element name="{if (prefix-from-QName(node-name(.))) then concat(prefix-from-QName(node-name(.)), ':') else ''}GolfClub2" namespace="{namespace-uri(.)}">
	    <xsl:copy-of select="@*"/>
	    <xsl:for-each select="namespace::*">
			<xsl:copy />
		</xsl:for-each>
		<xsl:apply-templates/>
	</xsl:element>
</xsl:template>

</xsl:stylesheet>

