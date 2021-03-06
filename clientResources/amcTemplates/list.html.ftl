<!DOCTYPE html>
<!--
 *  This file is part of AtlasMapper server and clients.
 *
 *  Copyright (C) 2011 Australian Institute of Marine Science
 *
 *  Contact: Gael Lafond <g.lafond@aims.org.au>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->
<html>

<!-- Generated with AtlasMapper version ${version!} -->
<head>
	<title>${clientName!} layers</title>
	<link rel="icon" type="image/png" href="resources/favicon.png?atlasmapperVer=${version}" />
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />

	<link rel="stylesheet" type="text/css" href="resources/css/styles.css?atlasmapperVer=${version}" />
	<!--[if lte IE 6 ]>
		<link rel="stylesheet" type="text/css" href="resources/css/styles-ie6.css?atlasmapperVer=${version}" />
	<![endif]-->

	<!--[if IE]>
		<script type="text/javascript" src="modules/Utils/ECMAScriptPatch.js"></script>
	<![endif]-->

	<style>
		div.layerBlock {
			width: ${layerBoxWidth!202}px;
			height: ${layerBoxHeight!225}px;
			float: left;
			font-size: 0.8em;
			overflow: hidden;
			margin: 0 10px;
		}
		div.layerBlock .image {
			border: 1px solid #000000;
			background-repeat: no-repeat;
		}

		h2 {
			background-color: #CCCCCC;
			text-align: center;
			font-size: 4em;
		}
		h2 span {
			font-size: 0.6em;
		}
	</style>
</head>

<body id="list">
	${listPageHeader!}

	<#if layers??>
		<#list layers?keys as dataSourceName>
			<h2>${dataSourceName} <span>(${layers[dataSourceName]?size} layers)</span></h2>

			<#list layers[dataSourceName] as layer>
				<div class="layerBlock">
					<#if layer["imageUrl"]??>
						<div class="image" style="width:${layer["imageWidth"]!200}px; height:${layer["imageHeight"]!180}px; background-image:url('${layer["baseLayerUrl"]!}');">
							<#if layer["mapUrl"]??>
								<a href="${layer["mapUrl"]!}" target="_blank"><img alt="${layer["title"]!"Untitled"}" src="${layer["imageUrl"]!}" style="border: none" /></a>
							<#else>
								<img alt="${layer["title"]!"Untitled"}" src="${layer["imageUrl"]!}" style="border: none" />
							</#if>
						</div>
					</#if>
					<!-- ${layer["id"]!} -->
					${layer["title"]!"Untitled"}
				</div>
			</#list>

			<!-- Stop the floating -->
			<div style="clear:both"></div>
		</#list>
	</#if>

	${listPageFooter!}
</body>

</html>
