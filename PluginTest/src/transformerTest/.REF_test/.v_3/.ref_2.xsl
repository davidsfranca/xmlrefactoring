<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		version="2.0">
<xsl:output indent="yes"/>
<xsl:strip-space elements="*"/>
 <xsl:variable 
	name="set0" select="//element()[(namespace-uri(.)='http://www.example.org/a')and(local-name(.)='bbb')]/element()[(namespace-uri(.)='')and(local-name(.)='ccc')]/element()[ (namespace-uri(.)='')and(local-name(.)='eee1') or  (namespace-uri(.)='')and(local-name(.)='fff') ]" /> 
<xsl:template match="*">
    <xsl:copy>
        <xsl:copy-of select="@*"/>
        <xsl:apply-templates />
    </xsl:copy>
</xsl:template>
<xsl:template match="//element()[(namespace-uri(.)='http://www.example.org/a')and(local-name(.)='bbb')]/element()[(namespace-uri(.)='')and(local-name(.)='ccc')]/element()[ (namespace-uri(.)='')and(local-name(.)='eee1') or  (namespace-uri(.)='')and(local-name(.)='fff') ]">
				<xsl:if test="$set0[1] = current()">
			<xsl:element name="group" namespace="">
							<xsl:copy-of select="../element()[(namespace-uri(.)='')and(local-name()='eee1')]"/>
							<xsl:copy-of select="../element()[(namespace-uri(.)='')and(local-name()='fff')]"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>