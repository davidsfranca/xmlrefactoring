<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		version="2.0"
>

<!-- substitui uma tag por outra copiando os valores de 
	atributo namesapce e descendentes-->
<xsl:template match="author">
	<autor>
	    <xsl:copy-of select="@*"/>
	    <xsl:for-each select="namespace::*">
			<xsl:copy />
		</xsl:for-each>
		<xsl:apply-templates/>
	</autor>
</xsl:template>

</xsl:stylesheet>

