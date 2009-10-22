<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		version="2.0">
<xsl:output indent="yes"/>
<xsl:strip-space elements="*"/>
<xsl:template match="*">
    <xsl:copy>
        <xsl:copy-of select="@*"/>
        <xsl:apply-templates />
    </xsl:copy>
</xsl:template>
<xsl:template match="//element()[(namespace-uri(.)='http://www.example.org/a')and(local-name(.)='bbb')]/element()[(namespace-uri(.)='')and(local-name(.)='ccc')]/element()[ (namespace-uri(.)='')and(local-name(.)='group') ]">
	<xsl:copy-of select="*"/>
</xsl:template>
</xsl:stylesheet>