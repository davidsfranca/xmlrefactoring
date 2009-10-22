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

<!-- copia todos os atributos do elemento exceto o schemaVersion e cria um nova atributo de schemaVersion -->
	<xsl:template match="/element()">
		<xsl:copy>
			<xsl:attribute name="schemaVersion">1</xsl:attribute>
	     	<xsl:copy-of select="@* except attribute(schemaVersion)"/>
		    <xsl:for-each select="namespace::*">
				<xsl:copy />
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>