<?xml version = "1.0" encoding = "UTF-8"?>
<xsl:stylesheet version = "1.0" 
xmlns:xsl = "http://www.w3.org/1999/XSL/Transform">
<xsl:variable name="owner" select="/history/@owner"/>
<xsl:template match="*">
<html> 
	<head>
		<title>Chat</title>
		<style type="text/css">
			body {
                margin: 0;
			    padding:0;
            }
			ul{
				list-style: none;
				margin: 0;
				padding: 0;
                
			}

			ul li{
				display:inline-block;
				clear: both;
				padding: 20px;
				border-radius: 30px;
				margin-bottom: 2px;

            }

			.him{
				background: #72c8e1;
				float: left;
			}

			.me{
				float: right;
				color: #fff;
                background: #eee;
			}

			.him + .me{
				border-bottom-right-radius: 5px;
			}

			.me + .me{
				border-top-right-radius: 5px;
				border-bottom-right-radius: 5px;
			}

			.me:last-of-type {
				border-bottom-right-radius: 30px;
			}
            #page{
                padding: 16px;
            }
            #logo{
                padding: 10px;
                background-color: #2D3447;
                text-align: center;
            }
            img{
                width: 100px;
                height: 50px;
            }
            span{
                
                color: #f05189;
                font-size: 18px;
            }
            h6{
                padding: 0;
                margin: 0;
                margin-top: 2px;
                color: #2D3447;
                
            }
            p{
                padding:0;
                margin:0;
                display: inline-block;
                
            
            }
		</style>
    </head>
	<body>
        <div id="logo"><img src="a.png"/></div>
        <div id="page">
        <ul>
			<xsl:for-each select="message">
				<xsl:choose>
					<xsl:when test="fromID = $owner">
						<li class="him">
									<span><xsl:value-of select="from"/>: </span>
									<p style="color:{color};
                                    font-size: {size}px;
                                    font-family: {family};"><xsl:value-of select="content"/></p>
                            <h6><xsl:value-of select="date"/></h6></li>
					</xsl:when>
					<xsl:otherwise>
						<li class="me">
									<span><xsl:value-of select="from"/>: </span>
									<p style="color:{color};
                                    font-size: {size}px;
                                    font-family: {family};"><xsl:value-of select="content"/></p>
                            <h6><xsl:value-of select="date"/></h6></li>
					</xsl:otherwise>
				</xsl:choose>
			  </xsl:for-each>               
                </ul>
        </div>
            </body>
        </html>
         
    </xsl:template>
</xsl:stylesheet>
