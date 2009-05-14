<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		version="2.0"
>

<xsl:output method="xml" encoding="iso-8859-1" indent="yes"/>
<xsl:strip-space elements="*" />

<xsl:include href="renameaaatozeelement.xsl" />

<!-- para os demais copia sem altera›es para a sa’da -->
<xsl:template match="*">
    <xsl:copy>
        <xsl:copy-of select="@*"/>
        <xsl:apply-templates />
    </xsl:copy>
</xsl:template>

</xsl:stylesheet>