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

<!-- substitui uma tag por outra copiando os valores de 
	atributo namesapce e descendentes-->
	<xsl:template match="//element()[(namespace-uri(.)='http://www.example.org/a')and(local-name(.)='bbb')]/element()[(namespace-uri(.)='')and(local-name(.)='ccc')]/element()[(namespace-uri(.)='http://www.example.org/test')and(local-name(.)='eee')]/element()[(namespace-uri(.)='')and(local-name(.)='fff')]">
	<xsl:copy>
	    <xsl:copy-of select="@* [not((namespace-uri()='http://www.example.org/test') and (local-name() ='attr'))]" />
	    <xsl:attribute name="attr1" namespace="http://www.example.org/test">
		<xsl:value-of select="@* [((local-name(.) ='attr')and(namespace-uri(.)='http://www.example.org/test'))]"/>
	    </xsl:attribute>
	    <xsl:for-each select="namespace::*">
			<xsl:copy />
	    </xsl:for-each>
	 <xsl:apply-templates/>
	</xsl:copy>
</xsl:template>

</xsl:stylesheet>