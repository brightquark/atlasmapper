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

package au.gov.aims.atlasmapperserver.layerGenerator;

import au.gov.aims.atlasmapperserver.Errors;
import au.gov.aims.atlasmapperserver.URLCache;
import au.gov.aims.atlasmapperserver.dataSourceConfig.AbstractDataSourceConfig;
import au.gov.aims.atlasmapperserver.jsonWrappers.client.DataSourceWrapper;
import au.gov.aims.atlasmapperserver.jsonWrappers.client.LayerWrapper;
import au.gov.aims.atlasmapperserver.layerConfig.AbstractLayerConfig;
import au.gov.aims.atlasmapperserver.layerConfig.LayerCatalog;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractLayerGenerator<L extends AbstractLayerConfig, D extends AbstractDataSourceConfig> {
	protected long instanceTimestamp = -1;

	protected abstract String getUniqueLayerId(L layer, D dataSourceConfig);

	public DataSourceWrapper generateLayerCatalog(D dataSourceConfig, boolean redownloadPrimaryFiles, boolean redownloadSecondaryFiles) throws IOException, JSONException {
		// startDate: Used to delete old entries in the cache (entries that do not get
		//     access after that date are considered unused and are removed)
		Date startDate = new Date();

		LayerCatalog rawLayerCatalog = this.generateRawLayerCatalog(dataSourceConfig, redownloadPrimaryFiles, redownloadSecondaryFiles);

		List<URLCache.Category> categories = new ArrayList<URLCache.Category>(3);
		categories.add(URLCache.Category.CAPABILITIES_DOCUMENT);
		categories.add(URLCache.Category.MEST_RECORD);
		categories.add(URLCache.Category.BRUTEFORCE_MEST_RECORD);
		URLCache.deleteOldEntries(dataSourceConfig, startDate, categories);

		Errors dataSourceErrors = URLCache.getDataSourceErrors(dataSourceConfig, dataSourceConfig.getConfigManager().getApplicationFolder());

		DataSourceWrapper catalogWrapper = new DataSourceWrapper();
		catalogWrapper.addAllErrors(dataSourceErrors);
		catalogWrapper.addAllErrors(rawLayerCatalog.getErrors());
		for (AbstractLayerConfig layer : rawLayerCatalog.getLayers()) {
			LayerWrapper layerWrapper = new LayerWrapper(layer.toJSonObject());
			catalogWrapper.addLayer(layer.getLayerId(), layerWrapper);
		}

		return catalogWrapper;
	}

	// TODO Maybe return a DataSourceWrapper?
	// Redownload parameters are ignored by most data sources, but some use it (like KML)
	protected abstract LayerCatalog generateRawLayerCatalog(D dataSourceConfig, boolean redownloadPrimaryFiles, boolean redownloadSecondaryFiles);

	// The layer name used to request the layer. Usually, the layerName is
	// the same as the layerId, so this field is let blank. This attribute
	// is only used when there is a duplication of layerId.
	protected void ensureUniqueLayerId(L layer, D dataSourceConfig) {
		String uniqueLayerId =
				dataSourceConfig.getDataSourceId() + "_" +
				this.getUniqueLayerId(layer, dataSourceConfig);

		layer.setLayerName(layer.getLayerId());
		layer.setLayerId(uniqueLayerId);
	}
}
