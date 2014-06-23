/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.javascript.gwt2.impl.client.map.feature;

import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService.LogicalOperator;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService.QueryType;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService.SearchLayerType;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.javascript.api.client.map.feature.JsFeature;
import org.geomajas.javascript.api.client.map.feature.JsFeatureArrayCallback;
import org.geomajas.javascript.api.client.map.feature.JsFeatureSearchService;
import org.geomajas.javascript.api.client.map.layer.JsFeaturesSupported;
import org.geomajas.layer.feature.SearchCriterion;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service definition for searching for features. It defines a list of methods that do nothing but presenting different
 * ways of searching features.
 *
 * @author Pieter De Graef
 */
@Export("FeatureSearchService")
@ExportPackage("gm.feature")
public class JsFeatureSearchServiceImpl implements JsFeatureSearchService, Exportable {

	private MapPresenter mapPresenter;

	public JsFeatureSearchServiceImpl() {
	}

	public JsFeatureSearchServiceImpl(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	/**
	 * Search features within a certain layer, using the feature IDs.
	 *
	 * @param layer The features supported layer wherein to search.
	 * @param ids The unique IDs of the feature within the layer.
	 * @param callback Call-back method executed on return (when the feature has been found).
	 */
	public void searchById(final JsFeaturesSupported layer, final String[] ids, final JsFeatureArrayCallback callback) {
		Layer gwtLayer = mapPresenter.getLayersModel().getLayer(layer.getId());
		if (gwtLayer != null && gwtLayer instanceof VectorServerLayer) {
			final VectorServerLayer vLayer = (VectorServerLayer) gwtLayer;
			SearchCriterion[] criteria = new SearchCriterion[ids.length];
			for (int i = 0; i < ids.length; i++) {
				criteria[i] = new SearchCriterion(SearchFeatureRequest.ID_ATTRIBUTE, "=", ids[i]);
			}
			String crs = mapPresenter.getViewPort().getCrs();
			GeomajasServerExtension.getInstance().getServerFeatureService()
					.search(crs, vLayer, criteria, LogicalOperator.OR, ids.length, new FeatureMapFunction() {

						@Override
						public void execute(
								Map<org.geomajas.gwt2.client.map.layer.FeaturesSupported,
								List<org.geomajas.gwt2.client.map.feature.Feature>> featureMap) {
							List<JsFeature> features = new ArrayList<JsFeature>();
							for (org.geomajas.gwt2.client.map.feature.Feature feature :
									featureMap.get(vLayer.getId())) {
								features.add(new JsFeatureImpl(feature, layer));
							}
							callback.execute(new JsFeatureArrayCallback.JsFeatureArrayHolder(features.toArray(new
									JsFeature[features.size()
									])));
						}
					});
		}
	}

	/**
	 * Search all features within a certain layer that intersect a certain bounding box.
	 *
	 * @param layer The features supported layer wherein to search.
	 * @param bbox The bounding box wherein to search.
	 * @param callback Call-back method executed on return (when features have been found).
	 */
	public void searchInBounds(final JsFeaturesSupported layer, Bbox bbox, final JsFeatureArrayCallback callback) {
		Layer gwtLayer = mapPresenter.getLayersModel().getLayer(layer.getId());
		if (gwtLayer != null && gwtLayer instanceof VectorServerLayer) {
			final VectorServerLayer vLayer = (VectorServerLayer) gwtLayer;
			String crs = mapPresenter.getViewPort().getCrs();
			Geometry location = GeometryService.toPolygon(bbox);
			GeomajasServerExtension
					.getInstance()
					.getServerFeatureService()
					.search(mapPresenter, location, 0, QueryType.INTERSECTS, SearchLayerType.SEARCH_ALL_LAYERS, 0,
							new FeatureMapFunction() {

								@Override
								public void execute(
										Map<org.geomajas.gwt2.client.map.layer.FeaturesSupported,
											List<org.geomajas.gwt2.client.map.feature.Feature>> featureMap) {
									List<JsFeature> features = new ArrayList<JsFeature>();
									for (org.geomajas.gwt2.client.map.feature.Feature feature : featureMap.get(
											vLayer.getId())) {
										features.add(new JsFeatureImpl(feature, layer));
									}
									callback.execute(new JsFeatureArrayCallback.JsFeatureArrayHolder(features
											.toArray(new JsFeature[features
													.size()])));
								}
							});
		}
	}
}