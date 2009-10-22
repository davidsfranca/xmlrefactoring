<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		version="2.0">

<xsl:template match="*">
    <xsl:copy>
        <xsl:copy-of select="@*"/>
        <xsl:apply-templates />
    </xsl:copy>
</xsl:template>

<xsl:template match="//element()[(namespace-uri(.)='http://www.example.org/a')and(local-name(.)='bbb')]/element()[(namespace-uri(.)='')and(local-name(.)='ccc')]/element()[(namespace-uri(.)='')and(local-name(.)='eee')]">
	<xsl:element name="{if (prefix-from-QName(node-name(.))) then concat(prefix-from-QName(node-name(.)), ':') else ''}eee1" namespace="{namespace-uri(.)}">
	    <xsl:copy-of select="@*"/>
	    <xsl:for-each select="namespace::*">
			<xsl:copy />
		</xsl:for-each>
		<xsl:apply-templates/>
	</xsl:element>

</xsl:template>

</xsl:stylesheet>