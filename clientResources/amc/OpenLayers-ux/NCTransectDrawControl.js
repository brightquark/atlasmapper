/*
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
 */

/**
 * @author Greg Coleman
 */
if (typeof(OpenLayers.Control.ux) == 'undefined') {
	OpenLayers.Control.ux = {};
}

OpenLayers.Control.ux.NCTransectDrawControl = OpenLayers.Class(OpenLayers.Control.DrawFeature, {
	renderer: null,
	transect: null,
	map: null,
	time: null,
	ncLayer: null,

	initialize: function() {
		var that = this;

		var plot = function(event) {
			var lineString = "";

			var points = event.feature.geometry.getVertices();
			for (i=0; i<points.length; i++) {
				lineString = lineString + points[i].x + " " + points[i].y + ",";
			}

			var url = that.ncLayer.getFullRequestString (
				{
					REQUEST: "GetTransect",
					TIME: that.time,
					LINESTRING: lineString,
					FORMAT: "image/png",
					CRS: that.ncLayer.projection.toString()
				},
				null
			);
			url = url.replace("LAYERS=", "LAYER=");

			var win = new Ext.Window({
				html: '<img src="' + url + '" />'
			});

			win.show();
		};

		//this.renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
		//this.renderer = (this.renderer) ? [this.renderer] : OpenLayers.Layer.Vector.prototype.renderers;

		//this.transect = new OpenLayers.Layer.Vector("Transect Layer", {
		//	renderers: this.renderer, displayInLayerSwitcher: false
		//});

		this.transect = new OpenLayers.Layer.Vector("Transect Layer", {
			displayInLayerSwitcher: false,
			styleMap: new OpenLayers.StyleMap({
				"default": {
					'strokeColor': "#000000",
					'strokeWidth': 5
				},
				"temporary": {
					'strokeColor': "#FFFFFF",
					'strokeWidth': 5
				}
			})
		});

		this.transect.rendererOptions = { strokeColor: "#000000" };

		var newArguments = [];
		newArguments.push(this.transect, OpenLayers.Handler.Path, {'displayClass': 'olControlDrawFeaturePath'});

		OpenLayers.Control.DrawFeature.prototype.initialize.apply(this, newArguments);
		this.transect.events.register('featureadded', this.transect, plot);
	},

	showTransect: function() {
		this.map.addLayer(this.transect);
	},

	hideTransect: function() {
		this.map.removeLayer(this.transect);
		this.transect.removeAllFeatures();
	}
});
